package com.playground.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.playground.entity.Instructor;
import com.playground.mapper.JpaRefMapper;
import com.playground.model.InstructorDTO;
import com.playground.model.ResponseDTO;
import com.playground.repo.InstructorDetailsRepo;
import com.playground.repo.InstructorRepo;
import com.sandbox.util.SandboxUtils;

import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@RestController
@RequestMapping("jpa-reference/instructors")
public class InstructorContoller {
	private InstructorRepo instructorRepo;

	private InstructorDetailsRepo instructorDetailsRepo;

	private static Faker f;

	public InstructorContoller(InstructorRepo instructorRepo, InstructorDetailsRepo instructorDetailsRepo) {
		this.instructorRepo = instructorRepo;
		this.instructorDetailsRepo = instructorDetailsRepo;
		f = new Faker();
	}

	@PostMapping("/{testDataSize}")
	@Transactional
	public ResponseDTO addNewInstructors(@PathVariable String testDataSize) {

		if (StringUtils.isNumeric(testDataSize)) {
			List<Instructor> instructorList = instructorRepo
					.saveAll(JpaRefMapper.generateInstuctorEntityList(Integer.valueOf(testDataSize)));

			List<InstructorDTO> instructorDTOList = instructorList.stream()
					.map(JpaRefMapper::buildInstructorDTO)
					.collect(Collectors.toList());

			return JpaRefMapper.buildResponseDTOWithInstructorDTO(instructorDTOList);
		}

		return JpaRefMapper.buildResponseDTOWithInstructorDTO(Collections.emptyList());
	}

	@GetMapping
	public ResponseDTO getAllInstructors() {
		List<Instructor> instructorList = instructorRepo.findAll();

		List<InstructorDTO> instructorDTOList = instructorList.stream()
				.map(JpaRefMapper::buildInstructorDTO)
				.collect(Collectors.toList());

		return JpaRefMapper.buildResponseDTOWithInstructorDTO(instructorDTOList);
	}

	@GetMapping("/{id}")
	public InstructorDTO getInstructorById(@PathVariable String id) {
		if (!StringUtils.isNumeric(id))
			return new InstructorDTO();

		Instructor instructor = instructorRepo.findById(Integer.valueOf(id)).orElse(new Instructor());

		return JpaRefMapper.buildInstructorDTO(instructor);

	}

	@PutMapping("/{id}")
	@Transactional
	public InstructorDTO updateInstructorById(@PathVariable String id) {

		if (!StringUtils.isNumeric(id))

			return new InstructorDTO();

		Instructor origInstructor = instructorRepo.findById(Integer.valueOf(id)).orElse(new Instructor());

		if (ObjectUtils.isEmpty(origInstructor))
			return new InstructorDTO();

		System.out.println("origInstructor=" + SandboxUtils.getPrettyPrintJsonFromObject(origInstructor));

		Instructor updatedInstructor = JpaRefMapper.updateInstructor(origInstructor,
				JpaRefMapper.buildMockInstructorEntity());

		updatedInstructor.setFirstName("Let's Go Falcons");
		updatedInstructor.getCoursesList().forEach(i -> {
			String title = f.book().title().concat("-").concat(f.number().digits(5));
			i.setTitle(title);
		});

		System.out.println("updatedInstructor=" + SandboxUtils.getPrettyPrintJsonFromObject(updatedInstructor));

		Instructor newlySavedInstructor = instructorRepo.save(updatedInstructor);

		return JpaRefMapper.buildInstructorDTO(newlySavedInstructor);

	}

	@GetMapping("/details/{id}")
	public InstructorDTO getInstructorByInstructorDetailsId(@PathVariable String id) {

		if (!StringUtils.isNumeric(id))
			return new InstructorDTO();

		Instructor instructor = instructorRepo.findByInstructorDetailsId(Integer.valueOf(id));

		return JpaRefMapper.buildInstructorDTO(instructor);

	}

	@GetMapping("/courses/{id}")
	public InstructorDTO getInstructorByInstructorId(@PathVariable String id) {

		if (!StringUtils.isNumeric(id))
			return new InstructorDTO();

//		   this logic doesnt use JOIN FETCH
//			Instructor instructor = instructorRepo.findById(Integer.valueOf(id)).orElse(new Instructor());
//			if(ObjectUtils.isEmpty(instructor) || ObjectUtils.isEmpty(instructor.getId())) {
//				return new InstructorDTO();
//			}
//			
//			List<Course> courseList = courseRepo.findByInstructorId(instructor.getId());
//
//			instructor.setCoursesList(courseList);

		Instructor instructor = instructorRepo.findInstructorWithCoursesByInstructorId(Integer.valueOf(id))
				.orElse(new Instructor());

		return JpaRefMapper.buildInstructorDTO(instructor);

	}

	// @DeleteMapping("/details/delete/{id}")
	// @ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public InstructorDTO deleteInstructorDetailsById(@PathVariable String id) {
		if (StringUtils.isNumeric(id)) {
			instructorDetailsRepo.deleteById(Integer.valueOf(id));
			Instructor instructor = instructorRepo.findById(Integer.valueOf(id)).orElse(null);
			if (ObjectUtils.isNotEmpty(instructor)) {
				instructor.setInstructorDetails(null);
				return new InstructorDTO(); // instructorRepo.save(instructor);
			}
		}

		return new InstructorDTO();
	}

	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void deleteInstructorById(@PathVariable String id) {
		if (!StringUtils.isNumeric(id)) {
			return;
		} else {
			Instructor instructor = instructorRepo.findById(Integer.valueOf(id)).orElse(new Instructor());

			if (ObjectUtils.isNotEmpty(instructor)) {
				instructor.getCoursesList().forEach(course -> {
					course.setInstructor(null);
				});
				instructorRepo.deleteById(Integer.valueOf(id));
			}
		}
	}

	@DeleteMapping("/delete/all")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void deleteAllInstructors() {
		List<Instructor> instructorsList = instructorRepo.findAll();
		Optional.ofNullable(instructorsList).orElse(new ArrayList<>()).stream().forEach(instructor -> {
			Optional.ofNullable(instructor.getCoursesList()).orElse(new ArrayList<>()).stream().forEach(course -> {
				course.setInstructor(null);
			});
		});

		instructorRepo.deleteAll();

	}
}

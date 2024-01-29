package com.playground.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.entity.Course;
import com.playground.entity.Student;
import com.playground.mapper.JpaRefMapper;
import com.playground.model.CourseDTO;
import com.playground.model.ResponseDTO;
import com.playground.repo.CourseRepo;
import com.sandbox.util.SandboxUtils;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("jpa-reference/courses")
public class CourseController {
	private CourseRepo courseRepo;

	public CourseController(CourseRepo courseRepo) {
		this.courseRepo = courseRepo;
	}

	@PostMapping("/{testDataSize}")
	@Transactional
	public ResponseDTO addNewCoursesWithReviews(@PathVariable String testDataSize) {

		if (StringUtils.isNumeric(testDataSize) && (1 < Integer.valueOf(testDataSize))) {
			List<Course> coursesList = JpaRefMapper.generateCourseEntityList(Integer.valueOf(testDataSize));

			Set<Student> studentsSet = JpaRefMapper.generateStudentsEntitySet(Integer.valueOf(testDataSize) - 1);

			coursesList.forEach(course -> {
				course.setStudentsList(studentsSet);
			});

			coursesList = courseRepo.saveAll(coursesList);

			List<CourseDTO> coursesDTOList = coursesList.stream()
					.map(JpaRefMapper::buildCourseDTOWithNullCoursesDTOList)
					.collect(Collectors.toList());

			return JpaRefMapper.buildResponseDTOWithCourseDTO(coursesDTOList);
		}

		return JpaRefMapper.buildResponseDTOWithCourseDTO(Collections.emptyList());

	}

	@GetMapping
	public ResponseDTO getAllCourses() {

		List<Course> coursesList = courseRepo.findAll();

		List<CourseDTO> coursesDTOList = coursesList.stream()
				.map(JpaRefMapper::buildCourseDTOWithNullCoursesDTOList)
				.collect(Collectors.toList());

		return JpaRefMapper.buildResponseDTOWithCourseDTO(coursesDTOList);

	}

	@GetMapping("/{id}")
	public CourseDTO getCourseByCourseId(@PathVariable String id) {
		if (!StringUtils.isNumeric(id))
			return new CourseDTO();

		Course course = courseRepo.findById(Integer.valueOf(id)).orElse(new Course());

		return (ObjectUtils.isEmpty(course)) ? new CourseDTO() : JpaRefMapper.buildCourseDTOWithNullCoursesDTOList(course);

	}

	@GetMapping("/reviews/{id}")
	public CourseDTO getCourseWithReviewsByCourseId(@PathVariable String id) {
		if (!StringUtils.isNumeric(id))
			return new CourseDTO();

		Course course = courseRepo.findCourseWithReviewsByCourseId(Integer.valueOf(id)).orElse(new Course());

		return (ObjectUtils.isEmpty(course)) ? new CourseDTO() : JpaRefMapper.buildCourseDTOWithNullCoursesDTOList(course);
	}

	@GetMapping("/students/{id}")
	public CourseDTO getCourseWithStudentsByCourseId(@PathVariable String id) {
		if (!StringUtils.isNumeric(id))
			return new CourseDTO();

		Course course = courseRepo.findCourseWithStudentsByCourseId(Integer.valueOf(id)).orElse(new Course());

		System.out.println(id + " course.getTitle()" + course.getTitle());
		
		return (ObjectUtils.isEmpty(course)) ? new CourseDTO() : JpaRefMapper.buildCourseDTOWithNullCoursesDTOList(course);
	}

	@GetMapping("/students/reviews/{id}")
	public CourseDTO getCourseWithStudentsAndReviewsByCourseId(@PathVariable String id) {
		if (!StringUtils.isNumeric(id))
			return new CourseDTO();

		Course course = courseRepo.findCourseWithStudentsAndReviewsByCourseId(Integer.valueOf(id)).orElse(new Course());

		return (ObjectUtils.isEmpty(course)) ? new CourseDTO() : JpaRefMapper.buildCourseDTOWithNullCoursesDTOList(course);

	}

	@PutMapping("/{id}")
	@Transactional
	public CourseDTO updateCourseById(@PathVariable String id) {

		if (!StringUtils.isNumeric(id))
			return new CourseDTO();

		Course origCourse = courseRepo.findById(Integer.valueOf(id)).orElse(new Course());

		if (ObjectUtils.isEmpty(origCourse))
			return new CourseDTO();

		System.out.println("origCourse=" + SandboxUtils.getPrettyPrintJsonFromObject(origCourse));

		Course updatedCourse = JpaRefMapper.buildUpdatedCourse(origCourse, JpaRefMapper.buildMockCourseEntity());

		System.out.println("updatedCourse=" + SandboxUtils.getPrettyPrintJsonFromObject(updatedCourse));

		Course newlySavedCourse = courseRepo.save(updatedCourse);

		return JpaRefMapper.buildCourseDTOWithNullCoursesDTOList(newlySavedCourse);

	}

	@DeleteMapping("/delete/{id}")
	@Transactional
	public void deleteCourseById(@PathVariable String id) {

		if (StringUtils.isNumeric(id))
			courseRepo.deleteById(Integer.valueOf(id));

	}

	@DeleteMapping("/delete/all")
	@Transactional
	public void deleteAllCourses() {
		courseRepo.deleteAll();
	}
}

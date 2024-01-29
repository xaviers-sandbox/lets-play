package com.playground.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.entity.Student;
import com.playground.mapper.JpaRefMapper;
import com.playground.model.StudentDTO;
import com.playground.repo.StudentRepo;
import com.sandbox.util.SandboxUtils;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("jpa-reference/students")
public class StudentController {
	private StudentRepo studentRepo;

	public StudentController(StudentRepo studentRepo) {
		this.studentRepo = studentRepo;
	}

//	@PostMapping("/{testDataSize}")
//	@Transactional
//	public List<CourseDTO> addNewCoursesWithReviews(@PathVariable String testDataSize) {
//
//		if (StringUtils.isNumeric(testDataSize) && (1 < Integer.valueOf(testDataSize) )) {
//			List<Course> coursesList = JpaRefMapper.generateCourseEntityList(Integer.valueOf(testDataSize));
//			
//			List<Student> studentsList = JpaRefMapper.generateStudentsEntityList(Integer.valueOf(testDataSize) - 1);
//			
//			coursesList.forEach(course -> {
//				course.setStudentsList(studentsList);
//			});
//			
//			coursesList = courseRepo.saveAll(coursesList);
//
//			List<CourseDTO> courseDTOList = coursesList.stream().map(i -> {
//				CourseDTO courseDTO = JpaRefMapper.buildCourseDTO(i);
//				return courseDTO;
//			}).collect(Collectors.toList());
//
//			return courseDTOList;
//		}
//
//		return null;
//
//	}

	@GetMapping
	public List<StudentDTO> getAllStudents() {

		List<Student> studentsList = studentRepo.findAll();

		return studentsList.stream().map(JpaRefMapper::buildStudentDTO).collect(Collectors.toList());

	}

	@GetMapping("/{id}")
	public StudentDTO getStudentById(@PathVariable String id) {

		if (!StringUtils.isNumeric(id)) {

			return new StudentDTO();
		} else {

			Student student = studentRepo.findById(Integer.valueOf(id)).orElse(new Student());

			return (ObjectUtils.isEmpty(student)) ? new StudentDTO() : JpaRefMapper.buildStudentDTO(student);
		}
	}

	@PutMapping("/{id}/courses/{size}")
	@Transactional
	public StudentDTO updateStudentCoursesByIdAndSize(@PathVariable String id, @PathVariable String size) {

		if (!StringUtils.isNumeric(id) || !StringUtils.isNumeric(size))
			return new StudentDTO();

		Student student = studentRepo.findById(Integer.valueOf(id)).orElse(new Student());

		if (ObjectUtils.isEmpty(student)) 
			return new StudentDTO();
			
			System.out.println("origStudent=" + SandboxUtils.getPrettyPrintJsonFromObject(student));

			Student updatedStudent = JpaRefMapper.updateStudentCoursesByIdAndSize(student, Integer.valueOf(size));

			System.out.println("updatedStudent=" + SandboxUtils.getPrettyPrintJsonFromObject(updatedStudent));

			Student newlySavedStudent = studentRepo.save(updatedStudent);

			return JpaRefMapper.buildStudentDTO(newlySavedStudent);
		
	}

	@DeleteMapping("/delete/{id}")
	@Transactional
	public void deleteStudentById(@PathVariable String id) {

		if (StringUtils.isNumeric(id))
			studentRepo.deleteById(Integer.valueOf(id));

	}

	@DeleteMapping("/delete/all")
	@Transactional
	public void deleteAllStudents() {
		studentRepo.deleteAll();
	}
}

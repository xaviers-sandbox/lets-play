package com.playground.controller;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.entity.Course;
import com.playground.mapper.JpaRefMapper;
import com.playground.model.CourseDTO;
import com.playground.repo.CourseRepo;
import com.sandbox.util.SandboxUtils;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("jpa-reference/instructor-details")
public class InstructorDetailsController {
	private CourseRepo courseRepo;

	public InstructorDetailsController(CourseRepo courseRepo) {
		this.courseRepo = courseRepo;
	}

	@GetMapping
	public List<CourseDTO> getAllInstructorDetails() {

		List<Course> coursesList = courseRepo.findAll();

		//return coursesList.stream().map(JpaRefMapper::buildCourseDTO).collect(Collectors.toList());
		return null;

	}

	@GetMapping("/{id}")
	public CourseDTO getInstructorDetailsById(@PathVariable String id) {

		if (!StringUtils.isNumeric(id))
			return new CourseDTO();

		Course origCourse = courseRepo.findById(Integer.valueOf(id)).orElse(new Course());

		//return (ObjectUtils.isEmpty(origCourse)) ? new CourseDTO() : JpaRefMapper.buildCourseDTO(origCourse);
		
		return null;

	}

	@PutMapping("/{id}")
	@Transactional
	public CourseDTO updateInstructorDetailsById(@PathVariable String id) {

		if (!StringUtils.isNumeric(id))
			return new CourseDTO();

		Course origCourse = courseRepo.findById(Integer.valueOf(id)).orElse(new Course());

		if (ObjectUtils.isEmpty(origCourse))
			return new CourseDTO();

		System.out.println("origCourse=" + SandboxUtils.getPrettyPrintJsonFromObject(origCourse));

		Course updatedCourse = JpaRefMapper.buildUpdatedCourse(origCourse, JpaRefMapper.buildMockCourseEntity());

		System.out.println("updatedCourse=" + SandboxUtils.getPrettyPrintJsonFromObject(updatedCourse));

		Course newlySavedCourse = courseRepo.save(updatedCourse);

		//return JpaRefMapper.buildCourseDTO(newlySavedCourse);
		
		return null;
	}
}

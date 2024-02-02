package com.playground.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.playground.entity.Course;
import com.playground.entity.Instructor;
import com.playground.entity.InstructorDetails;
import com.playground.entity.Review;
import com.playground.entity.Student;
import com.playground.model.CourseDTO;
import com.playground.model.CourseResponseDTO;
import com.playground.model.InstructorDTO;
import com.playground.model.InstructorDetailsDTO;
import com.playground.model.InstructorResponseDTO;
import com.playground.model.ResponseDTO;
import com.playground.model.ReviewDTO;
import com.playground.model.StudentDTO;
import com.sandbox.util.SandboxUtils;

import net.datafaker.Faker;

public class JpaRefMapper {
	private static Faker f = new Faker();

	public static List<Instructor> generateInstuctorEntityList(int testDataSize) {
		List<Instructor> instructorList = IntStream.range(0, testDataSize)
				.mapToObj(i -> buildMockInstructorEntity())
				.collect(Collectors.toList());

		SandboxUtils.prettyPrintObjectToJson(instructorList);

		return instructorList;
	}

	public static Instructor buildMockInstructorEntity() {
		InstructorDetails details = buildInstructorDetails();

		Instructor instructor = buildInstructor();
		instructor.setInstructorDetails(details);

		Course course = buildMockCourseEntity();

		Course course2 = buildMockCourseEntity();

		instructor.addCourse(course);

		instructor.addCourse(course2);

		return instructor;
	}

	public static Instructor buildInstructor() {
		return Instructor.builder()
				.firstName(f.artist().name())
				.lastName(f.funnyName().name())
				.email(f.internet().emailAddress())
				.build();
	}

	public static InstructorDetails buildInstructorDetails() {
		InstructorDetails details = InstructorDetails.builder()
				.hobby(f.hobby().activity())
				.youtubeChannel(f.internet().domainName())
				.build();
		return details;
	}

	public static ResponseDTO buildResponseDTOWithInstructorDTO(List<InstructorDTO> instructorDTOList) {
		return InstructorResponseDTO.builder()
				.resultSetSize(instructorDTOList.size())
				.instructorDTOList(instructorDTOList)
				.build();
	}

	public static Instructor updateInstructor(Instructor origInstructor, Instructor updatedInstructor) {
		List<Course> coursesList = Optional.ofNullable(updatedInstructor.getCoursesList())
				.orElse(new ArrayList<>())
				.stream()
				.map(i -> {
					Course c = new Course();
					BeanUtils.copyProperties(i, c);
					return c;
				})
				.collect(Collectors.toList());

		InstructorDetails instructorDetails = new InstructorDetails();

		if (ObjectUtils.isNotEmpty(updatedInstructor.getInstructorDetails())) {

			BeanUtils.copyProperties(updatedInstructor.getInstructorDetails(), instructorDetails);
		}
		instructorDetails.setId(origInstructor.getId());

		return Instructor.builder()
				.id(origInstructor.getId())
				.firstName(updatedInstructor.getFirstName())
				.lastName(updatedInstructor.getLastName())
				.email(updatedInstructor.getEmail())
				.instructorDetails(instructorDetails)
				.coursesList(coursesList)
				.build();
	}

	public static InstructorDTO buildInstructorDTO(Instructor instructor) {
		if (ObjectUtils.isEmpty(instructor))
			return new InstructorDTO();

		List<CourseDTO> courseDTOList = Optional.ofNullable(instructor.getCoursesList())
				.orElse(new ArrayList<>())
				.stream()
				.map(i -> {
					CourseDTO cDTO = new CourseDTO();
					BeanUtils.copyProperties(i, cDTO);

					return cDTO;
				})
				.collect(Collectors.toList());

		InstructorDetailsDTO instructorDetailsDTO = new InstructorDetailsDTO();

		if (ObjectUtils.isNotEmpty(instructor.getInstructorDetails())) {

			BeanUtils.copyProperties(instructor.getInstructorDetails(), instructorDetailsDTO);
		}

		return InstructorDTO.builder()
				.id(instructor.getId())
				.firstName(instructor.getFirstName())
				.lastName(instructor.getLastName())
				.email(instructor.getEmail())
				.instructorDetailsDTO(instructorDetailsDTO)
				.coursesDTOList(courseDTOList)
				.build();
	}

	public static List<Course> generateCourseEntityList(int testDataSize) {
		List<Course> coursesList = IntStream.range(0, testDataSize)
				.mapToObj(i -> buildMockCourseEntity())
				.collect(Collectors.toList());

		SandboxUtils.prettyPrintObjectToJson(coursesList);

		return coursesList;
	}

	public static CourseDTO buildCourseDTOWithNullCoursesDTOList(Course course) {

		List<ReviewDTO> reviewsDTOList = buildReviewsDTOList(course);

		List<StudentDTO> studentsDTOList = buildStudentsDTOListWithNullCoursesDTOList(course);

		return CourseDTO.builder()
				.id(course.getId())
				.title(course.getTitle())
				.reviewsDTOList(reviewsDTOList)
				.studentsDTOList(studentsDTOList)
				.build();
	}

	private static StudentDTO buildStudentDTOWithNullCoursesDTOList(Student student) {
		return StudentDTO.builder()
				.id(student.getId())
				.firstName(student.getFirstName())
				.lastName(student.getLastName())
				.email(student.getEmail())
				.coursesDTOList(null)
				.build();
	}

	public static List<ReviewDTO> buildReviewsDTOList(Course course) {
		if (CollectionUtils.isEmpty(course.getReviewsList()) || course.getReviewsList().size() < 1) {
			return Collections.emptyList();
		}

		return course.getReviewsList()
				.stream()
				.filter(ObjectUtils::isNotEmpty)
				.map(i -> buildReviewDTO(i))
				.collect(Collectors.toList());
	}

	public static List<StudentDTO> buildStudentsDTOListWithoutCourses(Course course) {
		System.out.println("course.getStudentsList()=" + course.getStudentsList().size());

		if (CollectionUtils.isEmpty(course.getStudentsList()) || course.getStudentsList().size() < 1) {
			return Collections.emptyList();
		}

		return course.getStudentsList()
				.stream()
				.filter(ObjectUtils::isNotEmpty)
				.map(i -> buildStudentDTOWithoutCourses(i))
				.collect(Collectors.toList());
	}

//	public static List<CourseDTO> buildCoursesDTOList(Student student) {
//
//		System.out.println("student.getCoursesList()=" + student.getCoursesList().size());
//
//		if (CollectionUtils.isEmpty(student.getCoursesList()) || student.getCoursesList().size() < 1) {
//			return Collections.emptyList();
//		}
//
//		// return Collections.emptyList();
//		return student.getCoursesList()
//				.stream()
//				.filter(ObjectUtils::isNotEmpty)
//				.map(i -> buildCourseDTO(i))
//				.collect(Collectors.toList());
//	}

	public static StudentDTO buildStudentDTOWithoutCourses(Student student) {

		return StudentDTO.builder()
				.id(student.getId())
				.firstName(student.getFirstName())
				.lastName(student.getLastName())
				.email(student.getEmail())
				.coursesDTOList(null)
				.build();
	}

	public static Course buildUpdatedCourse(Course origCourse, Course updatedCourse) {

		return Course.builder()
				.title(updatedCourse.getTitle())
				.id(origCourse.getId())
				.instructor(origCourse.getInstructor())
				.build();
	}

	public static Course buildMockCourseEntity() {

		String title = f.pokemon().name().concat("-").concat(f.number().digits(5));

		return Course.builder().title(title).reviewsList(generateReviewEntityList(3)).build();
	}

	public static List<Review> generateReviewEntityList(int testDataSize) {
		List<Review> reviewsList = IntStream.range(0, testDataSize)
				.mapToObj(i -> buildMockReviewEntity())
				.collect(Collectors.toList());

		SandboxUtils.prettyPrintObjectToJson(reviewsList);

		return reviewsList;
	}

	public static Review buildMockReviewEntity() {

		return Review.builder().comment(f.lorem().sentence(6)).build();
	}

	public static ReviewDTO buildReviewDTO(Review review) {

		return ReviewDTO.builder().id(review.getId()).comment(review.getComment()).build();
	}

	public static Set<Student> generateStudentsEntitySet(int testDataSize) {
		Set<Student> studentsSet = IntStream.range(0, testDataSize)
				.mapToObj(i -> buildMockStudentEntity())
				.collect(Collectors.toSet());

		SandboxUtils.prettyPrintObjectToJson(studentsSet);

		return studentsSet;

	}

	public static Student buildMockStudentEntity() {
		return Student.builder()
				.firstName(f.artist().name())
				.lastName(f.funnyName().name())
				.email(f.internet().emailAddress())
				.build();
	}

	public static Student updateStudentCoursesByIdAndSize(Student origStudent, int size) {
		return Student.builder()
				.id(origStudent.getId())
				.firstName(origStudent.getFirstName())
				.lastName(origStudent.getLastName())
				.email(origStudent.getEmail())
				.coursesList(generateCourseEntityList(size))
				.build();
	}

	public static ResponseDTO buildResponseDtoWithInstructor(List<Instructor> instructorList) {
		return InstructorResponseDTO.builder()
				.resultSetSize(instructorList.size())
				.instructorList(instructorList)
				.build();
	}

	public static ResponseDTO buildResponseDtoWithInstructorDetails(List<InstructorDetails> instructorDetailsList) {
		return InstructorResponseDTO.builder()
				.resultSetSize(instructorDetailsList.size())
				.instructorDetailsList(instructorDetailsList)
				.build();
	}

	public static ResponseDTO buildResponseDTOWithCourseDTO(List<CourseDTO> coursesDTOList) {
		return CourseResponseDTO.builder().resultSetSize(coursesDTOList.size()).coursesDTOList(coursesDTOList).build();
	}

	public static StudentDTO buildStudentDTOWithNullStudentsDTOList(Student student) {
		List<CourseDTO> coursesDTOList = buildCoursesDTOListWithNullStudentsDTOList(student);

		return StudentDTO.builder()
				.id(student.getId())
				.firstName(student.getFirstName())
				.lastName(student.getLastName())
				.coursesDTOList(coursesDTOList)
				.build();
	}

	public static List<CourseDTO> buildCoursesDTOListWithNullStudentsDTOList(Student student) {
		return  Optional.ofNullable(student.getCoursesList())
				.orElse(new ArrayList<>())
				.stream()
				.filter(ObjectUtils::isNotEmpty)
				.map(i -> buildCourseDTOWithNullStudentsDTOList(i))
				.collect(Collectors.toList());
	}

	public static CourseDTO buildCourseDTOWithNullStudentsDTOList(Course course) {
		List<ReviewDTO> reviewsDTOList = Optional.ofNullable(course.getReviewsList())
				.orElse(new ArrayList<>())
				.stream()
				.map(i -> buildReviewDTO(i))
				.collect(Collectors.toList());

		return CourseDTO.builder()
				.id(course.getId())
				.reviewsDTOList(reviewsDTOList)
				.studentsDTOList(null)
				.title(course.getTitle())
				.build();
	}

	public static List<StudentDTO> buildStudentsDTOListWithNullCoursesDTOList(Course course) {
		return Optional.ofNullable(course.getStudentsList())
				.orElse(new HashSet<>())
				.stream()
				.filter(ObjectUtils::isNotEmpty)
				.map(i -> buildStudentDTOWithNullCoursesDTOList(i))
				.collect(Collectors.toList());
	}
}
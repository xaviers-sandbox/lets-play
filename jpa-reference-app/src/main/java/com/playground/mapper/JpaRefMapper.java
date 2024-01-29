package com.playground.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;

import com.playground.entity.Course;
import com.playground.entity.Instructor;
import com.playground.entity.InstructorDetails;
import com.playground.entity.Review;
import com.playground.entity.Student;
import com.playground.model.CourseDTO;
import com.playground.model.InstructorDTO;
import com.playground.model.InstructorDetailsDTO;
import com.playground.model.JpaRefResponseDTO;
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
		return JpaRefResponseDTO.builder()
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

	public static ResponseDTO buildResponseDtoWithInstructor(List<Instructor> instructorList) {
		return JpaRefResponseDTO.builder().resultSetSize(instructorList.size()).instructorList(instructorList).build();
	}

	public static ResponseDTO buildResponseDtoWithInstructorDetails(List<InstructorDetails> instructorDetailsList) {
		return JpaRefResponseDTO.builder()
				.resultSetSize(instructorDetailsList.size())
				.instructorDetailsList(instructorDetailsList)
				.build();
	}

	public static List<Course> generateCourseEntityList(int testDataSize) {
		List<Course> coursesList = IntStream.range(0, testDataSize)
				.mapToObj(i -> buildMockCourseEntity())
				.collect(Collectors.toList());

		SandboxUtils.prettyPrintObjectToJson(coursesList);

		return coursesList;
	}

	public static CourseDTO buildCourseDTO(Course course) {

		List<ReviewDTO> reviewsDTOList = course.getReviewsList()
				.stream()
				.map(i -> buildReviewDTO(i))
				.collect(Collectors.toList());

		return CourseDTO.builder().id(course.getId()).title(course.getTitle()).reviewsDTOList(reviewsDTOList).build();
	}

	public static StudentDTO buildStudentDTO(Student student) {

		List<CourseDTO> courseDTOList = student.getCoursesList()
				.stream()
				.map(i -> buildCourseDTO(i))
				.collect(Collectors.toList());

		return StudentDTO.builder()
				.id(student.getId())
				.firstName(student.getFirstName())
				.lastName(student.getLastName())
				.email(student.getEmail())
				.coursesDTOList(courseDTOList)
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

	public static List<Student> generateStudentsEntityList(int testDataSize) {
		List<Student> studentsList = IntStream.range(0, testDataSize)
				.mapToObj(i -> buildMockStudentEntity())
				.collect(Collectors.toList());

		SandboxUtils.prettyPrintObjectToJson(studentsList);

		return studentsList;

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
}
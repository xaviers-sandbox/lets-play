package com.playground.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorRequestDTO {
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private InstructorDetailsRequestDTO instructorDetails;
	private List<CourseRequestDTO> coursesList;
}

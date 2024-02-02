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
//@JsonInclude(Include.NON_NULL)
public class InstructorDTO {
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private InstructorDetailsDTO instructorDetailsDTO;
	private List<CourseDTO> coursesDTOList;
	
}

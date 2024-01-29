package com.playground.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.playground.entity.Instructor;
import com.playground.entity.InstructorDetails;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class JpaRefResponseDTO extends ResponseDTO {
	private int resultSetSize;
	private List<Instructor> instructorList;
	private List<InstructorDetails> instructorDetailsList;
	private List<InstructorDTO> instructorDTOList;
}

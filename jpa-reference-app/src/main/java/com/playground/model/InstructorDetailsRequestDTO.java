package com.playground.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorDetailsRequestDTO {
	private Integer id;
	private String youtubeChannel;
	private String hobby;
}

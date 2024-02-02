package com.playground.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "course_student")
@Table(name = "course_student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStudent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "course_id")
	private Integer courseId;
	
	@Column(name = "student_id")
	private Integer studentId;
}

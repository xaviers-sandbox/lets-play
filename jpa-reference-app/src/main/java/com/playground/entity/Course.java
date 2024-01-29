package com.playground.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "course")
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "title")
	private String title;

	// do not cascade the deletes or removes
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "instructor_id")
	@JsonBackReference
	private Instructor instructor;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id")
	@JsonBackReference
	//@Fetch(value = FetchMode.SUBSELECT)
	private List<Review> reviewsList;

	// do not cascade the deletes or removes
	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "course_student", 
		joinColumns = @JoinColumn(name = "course_id"), 
		inverseJoinColumns = @JoinColumn(name = "student_id"))
	@JsonBackReference
	//@Fetch(value = FetchMode.SUBSELECT)
	private Set<Student> studentsList;

	public void addReview(Review newReview) {
		if (CollectionUtils.isEmpty(reviewsList)) {
			reviewsList = new ArrayList<Review>();
		}

		reviewsList.add(newReview);
	}

	public void addStudent(Student newStudent) {
		if (CollectionUtils.isEmpty(studentsList)) {
			studentsList = new HashSet<Student>();
		}

		studentsList.add(newStudent);
	}

	// override the toString to deal with the circular toString calls on nested
	// objects
	@Override
	public String toString() {
		return "Course [id=" + id + ", title=" + title + "]";
	}
}

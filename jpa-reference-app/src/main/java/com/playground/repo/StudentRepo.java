package com.playground.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.playground.entity.Student;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
	@Query("select s from student s join fetch s.coursesList c where s.id = :id")
	public Optional<Student> findStudentWithCoursesByStudentId(@Param("id") final int id);
}

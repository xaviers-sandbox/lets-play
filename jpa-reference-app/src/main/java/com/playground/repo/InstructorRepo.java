package com.playground.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.playground.entity.Instructor;

@Repository
public interface InstructorRepo extends JpaRepository<Instructor, Integer> {
	public Instructor findByInstructorDetailsId(final int id);

	@Query("select i from instructor i join fetch i.coursesList c join fetch i.instructorDetails d where i.id = :id")
	public Optional<Instructor> findInstructorWithCoursesByInstructorId(@Param("id") final int id);
}

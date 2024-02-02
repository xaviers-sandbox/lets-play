package com.playground.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.playground.entity.Course;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {
	List<Course> findByInstructorId(final int id);
	
	@Query("select c from course c join fetch c.reviewsList r where c.id = :id")
	public Optional<Course> findCourseWithReviewsByCourseId(@Param("id") final int id);
	
	
	@Query("select c from course c join fetch c.studentsList s where c.id = :id")
	public Optional<Course> findCourseWithStudentsByCourseId(@Param("id") final int id);
	
	@Query("select c from course c join fetch c.reviewsList r join fetch c.studentsList s where c.id = :id")
	public Optional<Course> findCourseWithStudentsAndReviewsByCourseId(@Param("id") final int id);
}

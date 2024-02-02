package com.playground.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.playground.entity.InstructorDetails;

@Repository
public interface InstructorDetailsRepo extends JpaRepository<InstructorDetails, Integer> {

}

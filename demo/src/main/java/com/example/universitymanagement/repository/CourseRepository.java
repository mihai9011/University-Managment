package com.example.universitymanagement.repository;

import org.springframework.stereotype.Repository;

import com.example.universitymanagement.model.Course;

@Repository
public class CourseRepository extends BaseRepo<Course> {
    CourseRepository(){};
}

package com.example.UniversityMan.repository;

import com.example.UniversityMan.model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T extends BaseEntity> {
	T save(T entity);
	List<T> findAll();
	Optional<T> findById(String id);
	void delete(String id);
}

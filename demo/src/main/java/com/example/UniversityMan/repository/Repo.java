package com.example.UniversityMan.repository;

import java.util.List;

import com.example.UniversityMan.model.BaseEntity;

public interface Repo<T extends BaseEntity> {
    T save(T entity);
    List<T> findAll();
    T findById(String id);
    boolean delete(String id);
}
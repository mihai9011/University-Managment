package com.example.universitymanagement.repository;

import java.util.List;

import com.example.universitymanagement.model.BaseEntity;

public interface Repo<T extends BaseEntity> {
    T save(T entity);
    List<T> findAll();
    T findById(String id);
    boolean delete(String id);
}

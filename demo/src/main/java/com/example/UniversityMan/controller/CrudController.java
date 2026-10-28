package com.example.UniversityMan.controller;

import com.example.UniversityMan.model.BaseEntity;
import com.example.UniversityMan.service.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public abstract class CrudController<T extends BaseEntity> {
	private final CrudService<T> service;

	protected CrudController(CrudService<T> service) {
		this.service = service;
	}

	@GetMapping
	public List<T> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public T findById(@PathVariable String id) {
		return service.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public T create(@RequestBody T entity) {
		return service.create(entity);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String id) {
		service.delete(id);
	}
}

package com.example.UniversityMan.repository;

import com.example.UniversityMan.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryRepository<T extends BaseEntity> implements GenericRepository<T> {
	private final Map<String, T> store = new ConcurrentHashMap<>();

	@Override
	public T save(T entity) {
		if (entity.getId() == null) {
			entity.setId(UUID.randomUUID().toString());
		}
		store.put(entity.getId(), entity);
		return entity;
	}

	@Override
	public List<T> findAll() {
		return new ArrayList<>(store.values());
	}

	@Override
	public Optional<T> findById(String id) {
		return Optional.ofNullable(store.get(id));
	}

	@Override
	public void delete(String id) {
		store.remove(id);
	}
}

package com.example.UniversityMan.service;

import com.example.UniversityMan.model.BaseEntity;
import com.example.UniversityMan.repository.GenericRepository;

import java.util.List;
import java.util.NoSuchElementException;

public abstract class CrudService<T extends BaseEntity> {
	protected final GenericRepository<T> repository;

	protected CrudService(GenericRepository<T> repository) {
		this.repository = repository;
	}

	public List<T> findAll() {
		return repository.findAll();
	}

	public T findById(String id) {
		return require(repository, id);
	}

	// erzeugt immer einen neuen Eintrag, eine vom Client gesendete Id wird ignoriert
	public T create(T entity) {
		entity.setId(null);
		return repository.save(entity);
	}

	public void delete(String id) {
		beforeDelete(findById(id));
		repository.delete(id);
	}

	// Hook: Unterklassen pruefen Referenzen und raeumen Listen in anderen Entitaeten auf
	protected void beforeDelete(T entity) {
	}

	protected static <E extends BaseEntity> E require(GenericRepository<E> repository, String id) {
		if (id == null) {
			throw new IllegalArgumentException("Id fehlt");
		}
		return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Nicht gefunden: " + id));
	}

	protected static void ensureEmpty(List<?> list, String what) {
		if (!list.isEmpty()) {
			throw new IllegalStateException("Loeschen nicht moeglich, es existieren noch " + what);
		}
	}
}

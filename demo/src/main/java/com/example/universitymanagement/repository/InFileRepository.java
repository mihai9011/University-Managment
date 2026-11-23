package com.example.universitymanagement.repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.example.universitymanagement.model.BaseEntity;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

public abstract class InFileRepository<T extends BaseEntity> implements Repo<T> {

    private static final String dir = "src/main/resources/data";
    private static final JsonMapper mapper = JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();

    private final Path path;
    private final List<T> entities;
    private final JavaType listType;

    protected InFileRepository(String file, Class<T> type) {
        path = Path.of(dir, file);
        listType = mapper.getTypeFactory().constructCollectionType(List.class, type);

        if (Files.exists(path)) {
            entities = new ArrayList<>(mapper.<List<T>>readValue(path.toFile(), listType));
        } else {
            entities = new ArrayList<>();
        }
    }

    public List<T> findAll() {
        return new ArrayList<>(entities);
    }

    public T findById(String id) {
        for (T entity : entities) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    public T save(T entity) {
        if (entity.getId() == null || entity.getId().isBlank()) {
            throw new IllegalArgumentException("Entity id cannot be null or blank");
        }

        entities.removeIf(e -> e.getId().equals(entity.getId()));
        entities.add(entity);
        write();
        return entity;
    }

    public boolean delete(String id) {
        boolean removed = entities.removeIf(e -> e.getId().equals(id));
        if (removed) {
            write();
        }
        return removed;
    }

    private void write() {
        try {
            Files.createDirectories(path.getParent());
            mapper.writerFor(listType).writeValue(path.toFile(), entities);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write " + path, e);
        }
    }
}

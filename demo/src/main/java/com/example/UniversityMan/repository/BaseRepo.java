package com.example.UniversityMan.repository;

import java.util.ArrayList;
import java.util.List;

import com.example.UniversityMan.model.BaseEntity;

public abstract class BaseRepo<T extends BaseEntity> {

    protected final List<T> list = new ArrayList<>();

    public T save(T entity) {
        // replace if it already exists (acts as update)
        delete(entity.getId());         
        list.add(entity);
        return entity;
    }

    public List<T> findAll() {
        //copy so it cant be modified
        return new ArrayList<>(list);   
    }
    
    public T findById(String id) {
        for(T ent : list){
            if(ent.getId().equals(id)){
                return ent;
            }
        }
        return null;
    }

    public boolean delete(String id) {
        int index = -1;

        for(T ent: list){
            index += 1;

            if(ent.getId().equals(id)){
                list.remove(index);
                return true;
            }
        }

        return false;
    }
}

package com.todolists.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todolists.models.ToDo;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Integer> {

}

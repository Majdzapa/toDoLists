package com.todolists.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todolists.models.SubTask;

public interface SubTaskRepository extends JpaRepository<SubTask, Integer> {

}

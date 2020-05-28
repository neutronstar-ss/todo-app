package com.neutronstar.todoapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neutronstar.todoapp.models.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	List <Todo> findByUserName(String user);

}

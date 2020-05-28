package com.neutronstar.todoapp.services;

import java.util.List;

import com.neutronstar.todoapp.models.Todo;

public interface ITodoService {
	
	List<Todo> getTodosByUser(String user);

}

package com.neutronstar.todoapp.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.neutronstar.todoapp.models.Todo;
import com.neutronstar.todoapp.services.ITodoService;

@Controller
public class TodoController {
	
	@Autowired
	private ITodoService todosService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	@RequestMapping(value = "/list-todos", method = RequestMethod.GET)
	public String showTodos(ModelMap model) {
		String name = getLoggedInUserName(model);
		 model.put("todos", todosService.getTodosByUser(name));
	        // model.put("todos", service.retrieveTodos(name));
	        return "list-todos";
	}
	
	private String getLoggedInUserName(ModelMap model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		return principal.toString();
	}
	
	@RequestMapping(value = "/add-todo", method = RequestMethod.GET)
    public String showAddTodoPage(ModelMap model) {
        model.addAttribute("todo", new Todo());
        return "todo";
    }
	
	@RequestMapping(value = "/delete-todo", method = RequestMethod.GET)
	public String deleteTodo(@RequestParam Long id) {
		todosService.deleteTodo(id);
		// service.deleteTodo(id);
        return "redirect:/list-todos";
	}
	
	@RequestMapping(value = "/update-todo", method = RequestMethod.GET)
    public String showUpdateTodoPage(@RequestParam long id, ModelMap model) {
        Todo todo = todosService.getTodoById(id).get();
        model.put("todo", todo);
        return "todo";
    }
	 
	 @RequestMapping(value = "/add-todo", method = RequestMethod.POST)
	    public String addTodo(ModelMap model, @Valid Todo todo, BindingResult result) {

	        if (result.hasErrors()) {
	            return "todo";
	        }

	        todo.setUserName(getLoggedInUserName(model));
	        todosService.saveTodo(todo);
	        return "redirect:/list-todos";
	    }
	

}

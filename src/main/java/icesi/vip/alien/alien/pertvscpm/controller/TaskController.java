package icesi.vip.alien.alien.pertvscpm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import icesi.vip.alien.alien.pertvscpm.model.Task;
import icesi.vip.alien.alien.pertvscpm.services.TaskService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping(
{ "/tasks" })
@RestController
public class TaskController
{

	@Autowired
	TaskService service;

	@GetMapping
	public List<Task> list()
	{

		return service.list();
	}

	@PostMapping
	public Task addTask(@RequestBody Task task)
	{
		return service.add(task);
	}

	@GetMapping(path =
	{ "/{id}" })
	public Task listById(@PathVariable("id") int id)
	{
		return service.findById(id);
	}

	@PutMapping(path =
	{ "/{id}" })
	public Task update(@RequestBody Task task, @PathVariable("id") int id)
	{
		task.setId(id);
		return service.edit(task);
	}
	
	@GetMapping(path =
		{ "/criticalPathMethod/{startId}/{endId}" })
		public List<Task> criticalPathMethod(@PathVariable("startId") int startId,@PathVariable("endId") int endId)
		{
			List<Task> tasks = service.list();
			Task start =service.findById(startId);
			Task finish =service.findById(endId);
			return service.executeCPM(tasks, start, finish);
		}
}

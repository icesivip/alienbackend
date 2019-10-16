package icesi.vip.alien.alien.pertvscpm.repositories;

import java.util.List;

import icesi.vip.alien.alien.pertvscpm.model.Task;

public interface TaskRepository
{

	public List<Task> findAll();

	public List<Task> loadTasksFromFile();

	public Task findById(int id);

	public Task save(Task task);

	public Task delete(Task task);

}

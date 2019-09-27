package icesi.vip.alien.alien.pertvscpm.repositories;

import java.util.List;

import icesi.vip.alien.alien.pertvscpm.model.Task;

public interface TaskRepository{

	List<Task> findAll();
	
	Task findById(int id);
	
	Task save(Task task);
	
	void delete(Task task);  
	
}

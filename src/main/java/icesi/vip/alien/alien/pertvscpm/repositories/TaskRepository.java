package icesi.vip.alien.alien.pertvscpm.repositories;

import java.util.List;
import java.util.Map;

import icesi.vip.alien.alien.pertvscpm.model.Task;

public interface TaskRepository
{

	List<Task> findAll();

	Task findById(int id);

	Task save(Task task);

	Task delete(Task task);
	
	List<Task> loadTasksFromFile();

	List<Task> loadPertTasksFromFile();

	List<Task> findScenarioById(int id);

	Map<Integer,List<Task>> initScenarios(int numberOfScenarios);

	List<Task> addScenario(int scenarioId, List<Task> scenario);

}

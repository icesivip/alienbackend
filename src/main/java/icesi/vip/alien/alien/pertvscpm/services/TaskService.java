package icesi.vip.alien.alien.pertvscpm.services;

import java.util.List;
import java.util.Map;

import icesi.vip.alien.alien.pertvscpm.model.Task;

/**
 * @author jcampaz
 *
 */
public interface TaskService
{

	/**
	 * Provide the available tasks in the project
	 * 
	 * @return - returns an empty list in case of no tasks created yet
	 */
	List<Task> list();

	/**
	 * Provide a task in the project found by its Id
	 * 
	 * @param id - Identifier of the task
	 * @return - returns null in case of no match with Id
	 */
	Task findById(int id);

	/**
	 * Add a task to the list of tasks in the project
	 * 
	 * @param task
	 * @return
	 */
	Task add(Task task);

	/**
	 * Add a lists of task to the project
	 * 
	 * @param tasks
	 * @return
	 */
	List<Task> buildGraph(List<Task> tasks);

	/**
	 * Loads a sample set of tasks defined in a text file in the static resource
	 * folder <br>
	 * <b>Pre:</b> The selected file's structure match the requirements to upload
	 * the definition of the tasks<br>
	 * <b>Post:</b> Every task already existing and stored is removed and the new
	 * set of tasks is stored in the task repository
	 * 
	 * @param fileUrl
	 * 
	 * @return the list of tasks with the respective predecessors and successors
	 */
	public List<Task> loadSampleTaks(String fileUrl);

	/**
	 * @param task
	 * @return
	 */
	Task edit(Task task);

	/**
	 * @param id - Identifier of the task
	 * @return
	 */
	Task delete(int id);

	/**
	 * Computes the Earliest Start and <b>(ES)</b> time for all the tasks using an
	 * specific task as started point <br>
	 * <b>Pre:</b> The task and transitions are already configured as a PDM<br>
	 * <b>Post:</b> The earliest start for every task in the list of tasks is
	 * updated with the computed values
	 * 
	 * @param tasks - tasks with respective transitions
	 * @param start - the starting task must not have any predecessor
	 * @return the list of tasks with the new earliest times computed
	 */
	List<Task> computeEarliestTimes(List<Task> tasks, Task start);

	/**
	 * Computes the Latest Start <b>(LS)</b> time for all the tasks using an
	 * specific task as started point <br>
	 * <b>Pre:</b> The task and transitions are already configured as a PDM, and
	 * have non null earliest start<br>
	 * <b>Post:</b> The latest start for every task in the list of tasks is updated
	 * with the computed values
	 * 
	 * @param tasks  - tasks with respective transitions
	 * @param finish - the starting task must not have any predecessor
	 * @return the list of tasks with the new latest times computed
	 */
	List<Task> computeLatestTimesAndSlack(List<Task> tasks, Task finish);

	/**
	 * Executes the Critical Path Method <b>(CPM)</b> computes earliest times,
	 * latest times, slacks and if marks as critical the tasks that has 0 slack<br>
	 * <b>Pre:</b> The task and transitions are already configured as a PDM<br>
	 * <b>Post:</b> The earliest start for every task in the list of tasks is
	 * updated with the computed values
	 * 
	 * @param tasks  - tasks with respective transitions
	 * @param start  - the starting task must not have any predecessor
	 * @param finish - the starting task must not have any predecessor
	 * @return the list of tasks with the new earliest times computed
	 */
	List<Task> executeCPM(List<Task> tasks, Task start, Task finish);

	/**
	 * Creates a determined number of scenarios where the duration of a task is
	 * computed using its probability distribution and parameters, to extract a
	 * sample value of the probability<br>
	 * 
	 * @param tasks     the list with the tasks and precedences that will be used to
	 *                  generate every scenario
	 * @param scenarios is the total number of scenarios that will be generated
	 * 
	 * @return return the set of scenarios with the randomly generated durations
	 */
	Map<Integer, List<Task>> generateScenarios(List<Task> tasks, int scenarios);

	List<Task> loadPertSampleTasks(String fileUrl);

	List<Task> executePERTCPM(int scenarioId);

	List<Task> findScenarioByid(int id);

}

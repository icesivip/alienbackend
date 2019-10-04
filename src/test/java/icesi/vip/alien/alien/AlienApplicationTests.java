package icesi.vip.alien.alien;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import icesi.vip.alien.alien.pertvscpm.model.Task;
import icesi.vip.alien.alien.pertvscpm.model.Transition;
import icesi.vip.alien.alien.pertvscpm.services.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlienApplicationTests {

	@Test
	public void contextLoads() {
	}


	@Autowired
	TaskService service;

	List<Task> tasks;
	
	Task start;
	
	Task finish;
	
	Map<Integer, Double>expectedEs;

	Map<Integer, Double> expectedLs;
	
	Map<Integer, Boolean> expectedIsCritical;
	
	@Before
	public void init()
	{
		expectedEs = new HashMap<Integer, Double>(); 
		
		expectedLs = new HashMap<Integer, Double>();
		
		expectedIsCritical= new HashMap<Integer, Boolean>();
		
		start = new Task(1, "START", 0.0);
		expectedEs.put(1, 0.0);
		expectedLs.put(1, 0.0);
		expectedIsCritical.put(1, true);
		
		Task taskA = new Task(2, "A", 2.0);
		expectedEs.put(2, 0.0);
		expectedLs.put(2, 4.0);
		expectedIsCritical.put(2, false);
		
		Task taskB = new Task(3, "B", 6.0);
		expectedEs.put(3, 0.0);
		expectedLs.put(3, 3.0);
		expectedIsCritical.put(3, false);
		
		Task taskC = new Task(4, "C", 4.0);
		expectedEs.put(4, 0.0);
		expectedLs.put(4, 0.0);
		expectedIsCritical.put(4, true);
		
		
		Task taskD = new Task(5, "D", 3.0);
		expectedEs.put(5, 2.0);
		expectedLs.put(5, 6.0);
		expectedIsCritical.put(5, false);
		
		Task taskE = new Task(6, "E", 5.0);
		expectedEs.put(6, 4.0);
		expectedLs.put(6, 4.0);
		expectedIsCritical.put(6, true);
		
		Task taskF = new Task(7, "F", 4.0);
		expectedEs.put(7, 2.0);
		expectedLs.put(7, 7.0);
		expectedIsCritical.put(7, false);
		
		Task taskG = new Task(8, "G", 2.0);
		expectedEs.put(8, 9.0);
		expectedLs.put(8, 9.0);
		expectedIsCritical.put(8, true);
		
		
		finish = new Task(9, "END", 0.0);
		expectedEs.put(9, 11.0);
		expectedLs.put(9, 11.0);
		expectedIsCritical.put(9, true);
		
		Transition edge = new Transition(start, taskA);
		
		start.getSuccessors().add(edge);
		taskA.getPredecessors().add(edge);

		edge = new Transition(start, taskB);
		start.getSuccessors().add(edge);
		taskB.getPredecessors().add(edge);

		edge = new Transition(start, taskC);
		start.getSuccessors().add(edge);
		taskC.getPredecessors().add(edge);

		edge = new Transition(taskA, taskD);
		taskA.getSuccessors().add(edge);
		taskD.getPredecessors().add(edge);

		edge = new Transition(taskA, taskF);
		taskA.getSuccessors().add(edge);
		taskF.getPredecessors().add(edge);

		edge = new Transition(taskB, taskG);
		taskB.getSuccessors().add(edge);
		taskG.getPredecessors().add(edge);

		edge = new Transition(taskC, taskE);
		taskC.getSuccessors().add(edge);
		taskE.getPredecessors().add(edge);

		edge = new Transition(taskD, taskG);
		taskD.getSuccessors().add(edge);
		taskG.getPredecessors().add(edge);

		edge = new Transition(taskE, taskG);
		taskE.getSuccessors().add(edge);
		taskG.getPredecessors().add(edge);

		edge = new Transition(taskG, finish);
		taskG.getSuccessors().add(edge);
		finish.getPredecessors().add(edge);

		edge = new Transition(taskF, finish);
		taskF.getSuccessors().add(edge);
		finish.getPredecessors().add(edge);

		tasks = Arrays.asList(start, taskA, taskB, taskC, taskD, taskE, taskF, taskG, finish);
	}
	
	
	@Test
	public void testEarliestTimes()
	{

		service.computeEarliestTimes(tasks, start);

		tasks.forEach( t-> {
			assertThat(t.getEarliestStart()).isEqualTo(expectedEs.get(t.getId()));
		});

	}
	
	@Test
	public void testLatestTimes()
	{
		service.computeEarliestTimes(tasks, start);
		service.computeLatestTimesAndSlack(tasks, finish);
		tasks.forEach( t-> {
			assertThat(t.getLatestStart()).isEqualTo(expectedLs.get(t.getId()));
		});
	}
	
	@Test
	public void testCPM()
	{
		List<Task> criticalPath =service.executeCPM(tasks, start, finish);
		
		criticalPath.forEach(t->{
			assertThat(t.getIsCritical()).isEqualTo(expectedIsCritical.get(t.getId()));
		});
	}

}

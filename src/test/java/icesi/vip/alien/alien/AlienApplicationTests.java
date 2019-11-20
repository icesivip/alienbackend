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

//	@Test
//	public void contextLoads() {
//	}


	@Autowired
	TaskService service;

	private List<Task> tasks;
	
	private Task start;
	private Task finish;
	private Map<Integer, Double>expectedEs;
	private Map<Integer, Double> expectedLs;
	private Map<Integer, Boolean> expectedIsCritical;
	
	@Before
	public void init()
	{
		initCPM();
			
	}
	
	private void initCPM()
	{
expectedEs = new HashMap<Integer, Double>(); 
		
		expectedLs = new HashMap<Integer, Double>();
		
		expectedIsCritical= new HashMap<Integer, Boolean>();
		
		start = new Task(0, "START", 0.0);
		service.add(start);
		expectedEs.put(0, 0.0);
		expectedLs.put(0, 0.0);
		expectedIsCritical.put(0, true);
		
		Task taskA = new Task(1, "A", 2.0);
		service.add(taskA);
		expectedEs.put(1, 0.0);
		expectedLs.put(1, 4.0);
		expectedIsCritical.put(1, false);
		
		Task taskB = new Task(2, "B", 6.0);
		service.add(taskB);
		expectedEs.put(2, 0.0);
		expectedLs.put(2, 3.0);
		expectedIsCritical.put(2, false);
		
		Task taskC = new Task(3, "C", 4.0);
		service.add(taskC);
		expectedEs.put(3, 0.0);
		expectedLs.put(3, 0.0);
		expectedIsCritical.put(3, true);
		
		
		Task taskD = new Task(4, "D", 3.0);
		service.add(taskD);
		expectedEs.put(4, 2.0);
		expectedLs.put(4, 6.0);
		expectedIsCritical.put(4, false);
		
		Task taskE = new Task(5, "E", 5.0);
		service.add(taskE);
		expectedEs.put(5, 4.0);
		expectedLs.put(5, 4.0);
		expectedIsCritical.put(5, true);
		
		Task taskF = new Task(6, "F", 4.0);
		service.add(taskF);
		expectedEs.put(6, 2.0);
		expectedLs.put(6, 7.0);
		expectedIsCritical.put(6, false);
		
		Task taskG = new Task(7, "G", 2.0);
		service.add(taskG);
		expectedEs.put(7, 9.0);
		expectedLs.put(7, 9.0);
		expectedIsCritical.put(7, true);
		
		
		finish = new Task(8, "END", 0.0);
		service.add(finish);
		expectedEs.put(8, 11.0);
		expectedLs.put(8, 11.0);
		expectedIsCritical.put(8, true);
		
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
	public void testCPM()
	{
		List<Task> criticalPath =service.executeCPM(tasks, start, finish);
		
		criticalPath.forEach(t->{
			assertThat(t.getEarliestStart()).isEqualTo(expectedEs.get(t.getId()));
			assertThat(t.getLatestStart()).isEqualTo(expectedLs.get(t.getId()));
			assertThat(t.getIsCritical()).isEqualTo(expectedIsCritical.get(t.getId()));
		});
	}

}

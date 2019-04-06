package com.wyc.drools;

import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DroolsApplicationTests {

	@Autowired
	private KieSession kieSession;

	@Autowired
	private KieServices kieServices;

	@Test
	public void testHelloWorld() {
		kieSession.fireAllRules();
	}

	@Test
	public void testChangeUserName(){
		User user = new User();
		user.setAge(18);
		user.setName("张三");
		kieSession.insert(user);
		kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("user"));
    	System.err.println("更改后的姓名：" + user.getName());
	}

	@Test
	public void testCustomerFilter(){
		kieSession.fireAllRules(new CustomerFilter());
	}

	@Test
	public void testContains(){
		User user = new User();
		user.setName("张三");
		//按照rule中的参数顺序依次insert
		kieSession.insert("张三");
		kieSession.insert(user);
		kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("contains"));
	}

	@Test
	public void testMemberOf(){
		List<String> names = Stream.of("张三","李四").collect(Collectors.toList());
		User user = new User();
		user.setName("张三");
		kieSession.insert(names);
		kieSession.insert(user);
		kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("memberOf"));
	}

	@Test
	public void testMatches(){
		User user = new User();
		user.setName("张三");
		kieSession.insert(user);
		kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("matches"));
	}

	@Test
	public void testBirthDay(){
		User user = new User();
		user.setName("张三");
		user.setBirthDate(new Date());
		kieSession.insert(user);
		kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("birthDay"));
	}

	@Test
	public void testBatchExecute() throws ParseException {
		KieCommands kieCommands = kieServices.getCommands();
		User user1 = new User();
		user1.setName("张三");
		user1.setBirthDate(new Date());

		User user2 = new User();
		user2.setName("李四");
		user2.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01"));
		List<Command> commands = Stream.of(kieCommands.newInsert(user1,"张三",true,null),kieCommands.newInsert(user2,"李四",true,null)).collect(Collectors.toList());
		ExecutionResults executionResults = kieSession.execute(kieCommands.newBatchExecution(commands));
    	Assert.assertEquals(user1,executionResults.getValue("张三"));
    	Assert.assertEquals(user2,executionResults.getValue("李四"));
	}

}

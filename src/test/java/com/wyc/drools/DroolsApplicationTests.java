package com.wyc.drools;

import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DroolsApplicationTests {

	@Autowired
	private KieSession kieSession;

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

}

package com.wyc.drools;

import com.wyc.drools.stateful.Fire;
import com.wyc.drools.stateful.Room;
import com.wyc.drools.stateful.Sprinkler;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: wyc
 * @date: 2019/4/6
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatefulTest {

    @Autowired
    private KieSession kieSession;


    @Test
    public void testStateful() {
        List<String> names = Arrays.asList("kitchen","bedroom","office","livingroom");
        Map<String,Room> map = Maps.newHashMap(new String(),new Room());
        names.forEach(name -> {
            Room room = new Room();
            room.setName(name);
            map.put(name,room);
            kieSession.insert(room);
            Sprinkler sprinkler = new Sprinkler(room);
            kieSession.insert(sprinkler);

        });

        //加入火警，触发喷水器和告警
        Fire kitchenFire = new Fire(map.get("kitchen"));
        Fire bedroomFire = new Fire(map.get("bedroom"));
        FactHandle kitchenHandle = kieSession.insert(kitchenFire);
        FactHandle bedroomHandle = kieSession.insert(bedroomFire);

        //删除火警
        kieSession.delete(kitchenHandle);
        kieSession.delete(bedroomHandle);

        kieSession.fireAllRules();

    }
}



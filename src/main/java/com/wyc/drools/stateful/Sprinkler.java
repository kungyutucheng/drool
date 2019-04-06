package com.wyc.drools.stateful;

import lombok.Data;

/**
 * @author: wyc
 * @date: 2019/4/6
 */
@Data
public class Sprinkler {

    private boolean on;

    private Room room;

    public Sprinkler(Room room ) {
        this.room = room;
    }

    public Sprinkler(boolean on) {
        this.on = on;
    }
}

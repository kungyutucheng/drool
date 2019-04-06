package com.wyc.drools.stateful;

import lombok.Data;

/**
 * @author: wyc
 * @date: 2019/4/6
 */
@Data
public class Fire {

    private Room room;

    public Fire(Room room) {
        this.room = room;
    }
}

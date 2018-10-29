package com.alexbros.opidlubnyi.allfootball;

public enum EventStatusEnum {
    STATUS_RUNNING(1<<0)
    , STATUS_FINISHED(1<<1)
    , STATUS_UPCOMING(1<<2)
    , STATUS_CANCELLED(1<<3)

    , STATUS_HALF_TIME(1<<4)
    , STATUS_POSTPONED(1<<5)
    , STATUS_DELAYED(1<<6)
    , STATUS_FIRST_HALF(1<<7)
    , STATUS_SECOND_HALF(1<<8)
    , STATUS_BREAK(1<<9)
    , STATUS_EXTRA_TIME(1<<10)
    , STATUS_PENALTIES(1<<11)
    , STATUS_INTERRUPTED(1<<12)
    , STATUS_SUSPENDED(1<<13)
    , STATUS_AFTER_EXTRA_TIME(1<<14)
    , STATUS_AFTER_PENALTIES(1<<15)
    , STATUS_ABANDONED(1<<16);

    private int flag;

    EventStatusEnum(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }
}

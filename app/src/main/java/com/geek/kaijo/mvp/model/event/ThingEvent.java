package com.geek.kaijo.mvp.model.event;

public class ThingEvent {
    private int eventType;
    private int position;

    public ThingEvent(int eventType, int position) {
        this.eventType = eventType;
        this.position = position;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getEventType() {
        return eventType;
    }

    public int getPosition() {
        return position;
    }
}

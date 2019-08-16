package com.example.activeledgersdk.event;

import com.here.oksse.ServerSentEvent;


public class Event {

    private ServerSentEvent sse;
    private String id;
    private String event;
    private String message;

    public Event(ServerSentEvent sse, String id, String event, String message) {
        this.sse = sse;
        this.id = id;
        this.event = event;
        this.message = message;
    }

    public ServerSentEvent getSse() {
        return sse;
    }

    public String getId() {
        return id;
    }

    public String getEvent() {
        return event;
    }

    public String getMessage() {
        return message;
    }


}

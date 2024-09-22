package com.restaurantsystem.common.messages;

import java.util.Date;
import java.util.UUID;

public class Message<T> {
    private String type;
    private String id = UUID.randomUUID().toString();
    private Date time = new Date();
    private T data;

    private String traceId = UUID.randomUUID().toString();

    public Message(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public Message(String type, T data, String traceId) {
        this.type = type;
        this.data = data;
        this.traceId = traceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

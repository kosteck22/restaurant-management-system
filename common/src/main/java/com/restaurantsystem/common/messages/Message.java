package com.restaurantsystem.common.event;

import java.util.Date;
import java.util.UUID;

public class Message<T> {
    private String type;
    private String id = UUID.randomUUID().toString();
    private String source = "Default";
    private Date time = new Date();
    private T data;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

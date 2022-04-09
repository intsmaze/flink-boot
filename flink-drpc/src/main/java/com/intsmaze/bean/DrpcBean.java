package com.intsmaze.bean;

public class DrpcBean {

    private String data;

    private String uuid;

    public DrpcBean() {
    }

    public DrpcBean(String data, String uuid) {
        this.data = data;
        this.uuid = uuid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

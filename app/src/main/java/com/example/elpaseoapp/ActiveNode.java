package com.example.elpaseoapp;

import java.util.Date;

public class ActiveNode {

    private int id;
    private Nodo node;
    private Date day;
    private String dateTimeFrom;
    private String dateTimeTo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Nodo getNode() {
        return node;
    }

    public void setNode(Nodo node) {
        this.node = node;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getDateTimeFrom() {
        return dateTimeFrom;
    }

    public void setDateTimeFrom(String dateTimeFrom) {
        this.dateTimeFrom = dateTimeFrom;
    }

    public String getDateTimeTo() {
        return dateTimeTo;
    }

    public void setDateTimeTo(String dateTimeTo) {
        this.dateTimeTo = dateTimeTo;
    }

}

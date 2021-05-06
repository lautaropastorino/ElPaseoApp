package com.example.elpaseoapp;

import java.util.List;

public class GeneralActiveResponse {
    private int id;
    private Usuario user;
    private List<ActiveNode> activeNodes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public List<ActiveNode> getActiveNodes() {
        return activeNodes;
    }

    public void setActiveNodes(List<ActiveNode> activeNodes) {
        this.activeNodes = activeNodes;
    }

}

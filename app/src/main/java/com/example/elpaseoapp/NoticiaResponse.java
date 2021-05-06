package com.example.elpaseoapp;

import java.util.ArrayList;

public class NoticiaResponse {
    private int totalElements;
    private ArrayList<Noticia> page;

    public int getTotalElements() {
        return totalElements;
    }

    public ArrayList<Noticia> getPage() {
        return page;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public void setPage(ArrayList<Noticia> page) {
        this.page = page;
    }
}

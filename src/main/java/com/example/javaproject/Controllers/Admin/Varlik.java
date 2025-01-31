package com.example.javaproject.Controllers.Admin;

abstract class Varlik {
    protected int id;
    protected String ad;

    public Varlik(int id, String ad) {
        this.id = id;
        this.ad = ad;
    }

    public int getId() {
        return this.id;
    }

    public String getAd() {
        return this.ad;
    }

    public String toString() {
        return "ID: " + this.id + ", Ad: " + this.ad;
    }
}

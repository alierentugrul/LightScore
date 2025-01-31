package com.example.javaproject.Controllers.Admin;

public class Oyuncu extends Varlik {

    public Oyuncu(int id, String name) {
        super(id, name);
    }

    public String toString() {
        return super.toString(); // Üst sınıftaki toString metodunu kullanıyoruz
    }
}
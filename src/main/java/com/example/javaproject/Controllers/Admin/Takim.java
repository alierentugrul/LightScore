package com.example.javaproject.Controllers.Admin;

import java.util.ArrayList;
import java.util.List;

public class Takim extends Varlik implements Puanli {
    private List<Oyuncu> oyuncular = new ArrayList();
    private int toplamPuan = 0;

    public Takim(int id, String ad) {
        super(id, ad);
    }

    public List<Oyuncu> getOyuncular() {
        return this.oyuncular;
    }


    public void puanYenile(int puan) {
        this.toplamPuan = puan;
    }

    public String toString() {
        String var10000 = super.toString();
        return var10000 + ", Puan: " + this.toplamPuan;
    }
}
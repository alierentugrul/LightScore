
package com.example.javaproject.Controllers.Admin;

public class PuanDurumu {
    private String takimAdi;
    private int oynananMac;
    private int galibiyet;
    private int beraberlik;
    private int maglubiyet;
    private int atilanGol;
    private int yenilenGol;

    // Constructor
    public PuanDurumu(String takimAdi) {
        this.takimAdi = takimAdi;
    }

    // Getter ve Setter metodları
    public String getTakimAdi() {
        return takimAdi;
    }


    public int getOynananMac() {
        return oynananMac;
    }

    public void setOynananMac(int oynananMac) {
        this.oynananMac = oynananMac;
    }

    public int getGalibiyet() {
        return galibiyet;
    }

    public void setGalibiyet(int galibiyet) {
        this.galibiyet = galibiyet;
    }

    public int getBeraberlik() {
        return beraberlik;
    }

    public void setBeraberlik(int beraberlik) {
        this.beraberlik = beraberlik;
    }

    public int getMaglubiyet() {
        return maglubiyet;
    }

    public void setMaglubiyet(int maglubiyet) {
        this.maglubiyet = maglubiyet;
    }

    public int getAtilanGol() {
        return atilanGol;
    }

    public void setAtilanGol(int atilanGol) {
        this.atilanGol = atilanGol;
    }

    public int getYenilenGol() {
        return yenilenGol;
    }

    public void setYenilenGol(int yenilenGol) {
        this.yenilenGol = yenilenGol;
    }

    public int getAveraj() {
        return atilanGol - yenilenGol; // Averaj hesaplaması
    }

    public int getPuan() {
        return galibiyet * 3 + beraberlik; // Puan hesaplaması
    }
}
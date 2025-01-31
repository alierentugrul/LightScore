package com.example.javaproject.Controllers.Admin;

import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;

public class PuanDurumuHesapla {

    public static List<PuanDurumu> puanDurumuHesapla() {
        // JSON veya mevcut bir kaynaktan Maçları yükleme bölümü
        List<Match> maclar = new ArrayList<>();
        try (FileReader reader = new FileReader("maclar.json")) { // JSON dosyasını oku
            Gson gson = new Gson();
            maclar = gson.fromJson(reader, new TypeToken<List<Match>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Puan durumu tutmak için bir harita
        Map<String, PuanDurumu> takimSonuclari = new HashMap<>();

        // Tüm maçlar için skorları kontrol et ve işlemleri gerçekleştir
        for (Match mac : maclar) {
            // 1. Eksik skorlara karşı kontrol yap
            if (mac.getEvSahibiSkor() == null || mac.getDeplasmanSkor() == null) {
                System.err.println("Eksik skor tespit edildi: Maç ID: " + mac.getId());
                continue; // Skor eksikse bu maçı işlememeye devam et
            }

            // 2. Skorları al
            int evSahibiSkor = mac.getEvSahibiSkor();
            int deplasmanSkor = mac.getDeplasmanSkor();

            String evSahibi = mac.getEvSahibi();
            String deplasman = mac.getDeplasman();

            // Skorları puan durumu tablosuna işle
            PuanDurumu evSahibiDurumu = takimSonuclari.getOrDefault(evSahibi, new PuanDurumu(evSahibi));
            PuanDurumu deplasmanDurumu = takimSonuclari.getOrDefault(deplasman, new PuanDurumu(deplasman));

            evSahibiDurumu.setOynananMac(evSahibiDurumu.getOynananMac() + 1);
            deplasmanDurumu.setOynananMac(deplasmanDurumu.getOynananMac() + 1);

            evSahibiDurumu.setAtilanGol(evSahibiDurumu.getAtilanGol() + evSahibiSkor);
            deplasmanDurumu.setAtilanGol(deplasmanDurumu.getAtilanGol() + deplasmanSkor);

            evSahibiDurumu.setYenilenGol(evSahibiDurumu.getYenilenGol() + deplasmanSkor);
            deplasmanDurumu.setYenilenGol(deplasmanDurumu.getYenilenGol() + evSahibiSkor);

            // Galibiyet, beraberlik ve mağlubiyet durumları
            if (evSahibiSkor > deplasmanSkor) {
                evSahibiDurumu.setGalibiyet(evSahibiDurumu.getGalibiyet() + 1);
                deplasmanDurumu.setMaglubiyet(deplasmanDurumu.getMaglubiyet() + 1);
            } else if (evSahibiSkor < deplasmanSkor) {
                deplasmanDurumu.setGalibiyet(deplasmanDurumu.getGalibiyet() + 1);
                evSahibiDurumu.setMaglubiyet(evSahibiDurumu.getMaglubiyet() + 1);
            } else {
                evSahibiDurumu.setBeraberlik(evSahibiDurumu.getBeraberlik() + 1);
                deplasmanDurumu.setBeraberlik(deplasmanDurumu.getBeraberlik() + 1);
            }

            // Güncel sonucu haritaya geri ekle
            takimSonuclari.put(evSahibi, evSahibiDurumu);
            takimSonuclari.put(deplasman, deplasmanDurumu);
        }

        // Puan durumu listesini sırala ve return et
        List<PuanDurumu> puanDurumuListesi = new ArrayList<>(takimSonuclari.values());
        puanDurumuListesi.sort(Comparator.comparingInt(PuanDurumu::getPuan).reversed()
                .thenComparingInt(PuanDurumu::getAveraj).reversed()
                .thenComparing(PuanDurumu::getTakimAdi));

        return puanDurumuListesi;
    }
}
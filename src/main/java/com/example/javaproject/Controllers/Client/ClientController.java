package com.example.javaproject.Controllers.Client;

import com.example.javaproject.Controllers.Admin.*;
import com.google.gson.Gson;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClientController {

    @FXML
    private TableView<Oyuncu> oyuncularTable; // Oyuncu tablosu
    @FXML
    private TableColumn<Oyuncu, Integer> oyuncuIdColumn; // Oyuncu ID sütunu
    @FXML
    private TableColumn<Oyuncu, String> oyuncuAdiColumn; // Oyuncu Adı sütunu
    @FXML
    private TableView<Match> maclarTable;
    @FXML
    private TableColumn<Match, String> tarihColumn;
    @FXML
    private TableColumn<Match, String> evSahibiColumn;
    @FXML
    private TableColumn<Match, String> deplasmanColumn;
    @FXML
    private TableColumn<Match, String> skorColumn;
    @FXML
    private TableView<PuanDurumu> puanDurumuTable;
    @FXML
    private TableColumn<PuanDurumu, String> takimAdiColumn;
    @FXML
    private TableColumn<PuanDurumu, String> takimAdiColumn2;
    @FXML
    private TableColumn<PuanDurumu, Integer> oynananMacColumn;
    @FXML
    private TableColumn<PuanDurumu, Integer> galibiyetColumn;
    @FXML
    private TableColumn<PuanDurumu, Integer> beraberlikColumn;
    @FXML
    private TableColumn<PuanDurumu, Integer> maglubiyetColumn;
    @FXML
    private TableColumn<PuanDurumu, Integer> atilanGolColumn;
    @FXML
    private TableColumn<PuanDurumu, Integer> yenilenGolColumn;
    @FXML
    private TableColumn<PuanDurumu, Integer> averajColumn;
    @FXML
    private TableColumn<PuanDurumu, Integer> puanColumn;
    @FXML
    private AnchorPane acilis_ekran;
    @FXML
    private AnchorPane puan_durumu_ekran;
    @FXML
    private AnchorPane maclar_ekran;
    @FXML
    private AnchorPane oyuncular_ekran;


    private List<AnchorPane> ekranlar; // Tüm ekranların listesini buraya ekleyeceğiz


    @FXML
    private void initialize() {



        ekranlar = new ArrayList<>();
        ekranlar.add(acilis_ekran);
        ekranlar.add(puan_durumu_ekran);
        ekranlar.add(maclar_ekran);
        ekranlar.add(oyuncular_ekran);

        takimAdiColumn.setCellValueFactory(new PropertyValueFactory<>("takimAdi"));
        oynananMacColumn.setCellValueFactory(new PropertyValueFactory<>("oynananMac"));
        galibiyetColumn.setCellValueFactory(new PropertyValueFactory<>("galibiyet"));
        beraberlikColumn.setCellValueFactory(new PropertyValueFactory<>("beraberlik"));
        maglubiyetColumn.setCellValueFactory(new PropertyValueFactory<>("maglubiyet"));
        atilanGolColumn.setCellValueFactory(new PropertyValueFactory<>("atilanGol"));
        yenilenGolColumn.setCellValueFactory(new PropertyValueFactory<>("yenilenGol"));
        averajColumn.setCellValueFactory(new PropertyValueFactory<>("averaj"));
        puanColumn.setCellValueFactory(new PropertyValueFactory<>("puan"));

        // Puan durumunu hesapla ve tabloya ekle
        List<PuanDurumu> puanDurumuListesi = PuanDurumuHesapla.puanDurumuHesapla();
        ObservableList<PuanDurumu> observablePuanDurumuListesi = FXCollections.observableArrayList(puanDurumuListesi);
        puanDurumuTable.setItems(observablePuanDurumuListesi);

        // Tablo sütunlarını bağlama
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Maç ID (örnek olması için)
        evSahibiColumn.setCellValueFactory(new PropertyValueFactory<>("evSahibi"));
        deplasmanColumn.setCellValueFactory(new PropertyValueFactory<>("deplasman"));
        skorColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEvSahibiSkor() + " - " + cellData.getValue().getDeplasmanSkor())
        );

        // Başlangıçta tabloyu yükle
        tabloyuGuncelle2();

        // Oyuncu tablosu sütunlarını bağlama
        oyuncuIdColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Oyuncunun ID'si
        oyuncuAdiColumn.setCellValueFactory(new PropertyValueFactory<>("ad"));// Oyuncunun adı
        takimAdiColumn2.setCellValueFactory(cellData -> {
            // Oyuncunun hangi takıma ait olduğunu sütuna tanımlama
            String takimAdi = takimlar.stream()
                    .filter(t -> t.getOyuncular().contains(cellData.getValue()))
                    .map(Takim::getAd)
                    .findFirst()
                    .orElse("Bilinmiyor");
            return new SimpleStringProperty(takimAdi);
        });

        // Oyuncuları tabloya yükle
        tabloyuGuncelleOyuncular();

    }

    @FXML
    private void tabloyuGuncelleOyuncular() {
        // Tüm takımlardaki oyuncuları bir listeye dahil et
        List<Oyuncu> oyuncularListesi = takimlar.stream()
                .flatMap(t -> t.getOyuncular().stream()) // Takımlardan oyuncuları al
                .collect(Collectors.toList());

        // Oyuncu listesi ObservableList'e dönüştürülerek tabloya eklenir
        ObservableList<Oyuncu> observableOyuncular = FXCollections.observableArrayList(oyuncularListesi);
        oyuncularTable.setItems(observableOyuncular);
    }

    // Maçları JSON'dan okur
    private List<Match> maclariOku() {
        List<Match> maclar = new ArrayList<>();
        try (FileReader reader = new FileReader("maclar.json")) {
            Gson gson = new Gson();
            Match[] mevcutMaclar = gson.fromJson(reader, Match[].class);
            if (mevcutMaclar != null) {
                maclar = new ArrayList<>(Arrays.asList(mevcutMaclar));
            }
        } catch (Exception e) {
            System.err.println("maclar.json dosyası okunurken hata oluştu.");
        }
        return maclar;
    }


    @FXML
    private void tabloyuGuncelle2() {
        try {
            // JSON'dan maç bilgilerini al
            List<Match> tumMaclar = maclariOku(); // Tüm maçları JSON'dan oku

            // Oynanan maçları filtrele (skoru girilmiş olanlar)
            List<Match> oynananMaclar = tumMaclar.stream()
                    .filter(mac -> mac.getEvSahibiSkor() != null && mac.getDeplasmanSkor() != null)
                    .collect(Collectors.toList());

            // Filtrelenen maçları tabloya ekle
            ObservableList<Match> observableOynananMaclar = FXCollections.observableArrayList(oynananMaclar);
            maclarTable.setItems(observableOynananMaclar);

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Hata", "Maçlar yüklenirken bir hata oluştu.");
        }
    }



    @FXML
    private void tabloyuGuncelle() {
        // Puan durumunu tekrar hesapla
        List<PuanDurumu> puanDurumuListesi = PuanDurumuHesapla.puanDurumuHesapla();

        // ObservableList'e dönüştür
        ObservableList<PuanDurumu> observablePuanDurumuListesi = FXCollections.observableArrayList(puanDurumuListesi);

        // Tabloya ekle
        puanDurumuTable.setItems(observablePuanDurumuListesi);
    }

    private void ekranDegistir(AnchorPane yeniEkran) {
        // Tüm ekranları kapat (gizle)
        for (AnchorPane ekran : ekranlar) {
            ekran.setVisible(false);
        }

        // Yeni ekranı görünür yap
        yeniEkran.setVisible(true);
    }

    @FXML
    private void openPuanDurumuEkran() {
        ekranDegistir(puan_durumu_ekran); // Puan Durumu ekranını görüntüle
        tabloyuGuncelle(); // Tabloyu güncelle
    }
    @FXML
    private void openMaclarEkran() {
        ekranDegistir(maclar_ekran);
        tabloyuGuncelle();
    }
    @FXML
    private void openOyuncularEkran() { ekranDegistir(oyuncular_ekran);tabloyuGuncelleOyuncular();}


    private static List<Takim> takimlar = new ArrayList<>();



    // Constructor: JSON dosyasını ilk başta yüklemek için
    public ClientController() {
        try {
            // JSON dosyasını yükleme
            Gson gson = new Gson();
            try (FileReader reader = new FileReader("takimlar.json")) {
                Takim[] mevcutTakimlar = gson.fromJson(reader, Takim[].class);

                if (mevcutTakimlar != null) {
                    takimlar = new ArrayList<>(Arrays.asList(mevcutTakimlar));
                }
            }
        } catch (IOException e) {
            System.out.println("JSON dosyası yüklenirken hata oluştu. Yeni bir liste başlatılacak.");
            takimlar = new ArrayList<>();
        }
    }



    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}









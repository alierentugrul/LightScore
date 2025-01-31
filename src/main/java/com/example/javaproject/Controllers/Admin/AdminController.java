package com.example.javaproject.Controllers.Admin;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController {

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
    private TextField takim_ekle_txtfld;
    @FXML
    private ComboBox<String> takim_sec_box;
    @FXML
    private TextField oyuncu_adi_txtfld;
    @FXML
    private ComboBox<String> ev_sahibi_box; // Ev sahibi takım ComboBox
    @FXML
    private ComboBox<String> deplasman_box; // Deplasman takım ComboBox
    @FXML
    private TextField ev_sahibi_skor_txtfld;
    @FXML
    private TextField deplasman_skor_txtfld;
    @FXML
    private AnchorPane acilis_ekran;
    @FXML
    private AnchorPane takim_ekle_ekran;
    @FXML
    private AnchorPane oyuncu_ekle_ekran;
    @FXML
    private AnchorPane mac_ekle_ekran;
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
        ekranlar.add(takim_ekle_ekran);
        ekranlar.add(oyuncu_ekle_ekran);
        ekranlar.add(mac_ekle_ekran);
        ekranlar.add(puan_durumu_ekran);
        ekranlar.add(maclar_ekran);
        ekranlar.add(oyuncular_ekran);

        // Takımları ev_sahibi_box ve deplasman_box ComboBox'larına yükle
if (takimlar == null || takimlar.isEmpty()) {
}


ObservableList<String> takimListesi = FXCollections.observableArrayList();


for (Takim takim : takimlar) {
    takimListesi.add(takim.getAd());
}
ev_sahibi_box.setItems(takimListesi);
deplasman_box.setItems(takimListesi);
takim_sec_box.setItems(takimListesi);

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
    private void openTakimEkleEkran() {
        ekranDegistir(takim_ekle_ekran); // Takım Ekle ekranına geç
    }
    @FXML
    private void openOyuncuEkleEkran() { ekranDegistir(oyuncu_ekle_ekran);}
    @FXML
    private void openMacEkleEkran() { ekranDegistir(mac_ekle_ekran);}
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
    public AdminController() {
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

    public void takimEkle() {
        // TextField'den kullanıcı girişini alıyoruz.
        String name = takim_ekle_txtfld.getText();

        // Boş isim kontrolü
        if (name == null || name.trim().isEmpty()) {
            // Hata mesajını bir alert ile göstermek.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText("Geçersiz Takım İsmi");
            alert.setContentText("Lütfen geçerli bir takım ismi giriniz!");
            alert.showAndWait();
            return; // İşleme devam etme, kullanıcıdan yeni bir giriş bekleniyor.
        }

        // Aynı isimde bir takım olup olmadığını kontrol etmek.
        if (takimlar.stream().anyMatch((takimx) -> takimx.getAd().equalsIgnoreCase(name))) {
            // Hata mesajını bir alert ile göstermek.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Takım Eklenemedi");
            alert.setContentText("Bu ad ile bir takım zaten var!");
            alert.showAndWait();
        } else {
            // Yeni takım oluşturup listeye ekliyoruz.
            Takim takim = new Takim(takimlar.size() + 1, name);
            takimlar.add(takim);

            // JSON dosyasına yazma
            if (jsonGuncelle()) {
                // Başarı mesajını bir alert ile göstermek.
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Başarılı");
                alert.setHeaderText("Takım Eklendi");
                alert.setContentText("Yeni takım başarıyla eklendi.");
                alert.showAndWait();

                // TextField'i temizlemek (isteğe bağlı).
                takim_ekle_txtfld.clear();

                // **YENİ LİSTEYİ ComboBox'A EKLEME ADIMI**
                takim_sec_box.getItems().add(name); // Yeni eklenen takımı ComboBox'a ekleyin
            }
        }
    }



    @FXML
    private void macEkle() {
        // Ev sahibi ve deplasman takımı seçimlerini al
        String evSahibi = ev_sahibi_box.getValue();
        String deplasman = deplasman_box.getValue();

        // Skor girişlerini kontrol et
        String evSahibiSkorStr = ev_sahibi_skor_txtfld.getText();
        String deplasmanSkorStr = deplasman_skor_txtfld.getText();

        Integer evSahibiSkor;
        Integer deplasmanSkor;

        try {
            evSahibiSkor = Integer.parseInt(evSahibiSkorStr.trim());
            deplasmanSkor = Integer.parseInt(deplasmanSkorStr.trim());

            // Negatif skora karşı kontrol
            if (evSahibiSkor < 0 || deplasmanSkor < 0) {
                showWarningDialog("Hatalı Skor", "Skorlar negatif olamaz. Lütfen geçerli bir değer giriniz.");
                return;
            }
        } catch (NumberFormatException e) {
            showWarningDialog("Geçersiz Skor", "Lütfen geçerli bir sayı giriniz.");
            return;
        }

        // Takımların seçilip seçilmediğini kontrol et
        if (evSahibi == null || deplasman == null) {
            showWarningDialog("Geçersiz Seçim", "Lütfen takımları seçiniz.");
            return;
        }

        // Aynı takım seçimine karşı kontrol
        if (evSahibi.equals(deplasman)) {
            showWarningDialog("Geçersiz Seçim", "Ev sahibi ve deplasman takımları farklı olmalıdır.");
            return;
        }

        try {
            // Mevcut maçları JSON'dan oku
            List<Match> maclar = new ArrayList<>();
            Gson gson = new Gson();
            try (FileReader reader = new FileReader("maclar.json")) {
                Match[] mevcutMaclar = gson.fromJson(reader, Match[].class);
                if (mevcutMaclar != null) {
                    maclar = new ArrayList<>(Arrays.asList(mevcutMaclar));
                }
            } catch (IOException e) {
                System.out.println("maclar.json oluşturuluyor.");
            }

            // Yeni maçı oluştur ve listeye ekle
            int yeniMacId = maclar.size() + 1;
            Match yeniMac = new Match(yeniMacId, evSahibi, deplasman, evSahibiSkor, deplasmanSkor);
            maclar.add(yeniMac);

            // Listeyi JSON dosyasına yaz
            try (FileWriter writer = new FileWriter("maclar.json")) {
                gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(maclar, writer);
            }

            // Başarı mesajını göster
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Başarılı");
            alert.setContentText("Maç başarıyla kaydedildi!");
            alert.showAndWait();

            // Formu temizle
            ev_sahibi_box.setValue(null);
            deplasman_box.setValue(null);
            ev_sahibi_skor_txtfld.clear();
            deplasman_skor_txtfld.clear();

            // **Tabloyu Güncelle**
            tabloyuGuncelle2(); // Burada çağrıldığından, tablo güncellenir

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Hata", "Maç kaydedilirken bir sorun oluştu.");
        }
    }

    // İlgili yardım metotları
    private void showWarningDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void oyuncuEkle() {
        // ComboBox'tan seçilen takım ismini alıyoruz
        String secilenTakim = takim_sec_box.getValue();
        String oyuncuAdi = oyuncu_adi_txtfld.getText();

        // Hata kontrolleri: Takım ve oyuncu adı doğrulama
        if (secilenTakim == null || secilenTakim.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText("Takım Seçilmedi");
            alert.setContentText("Lütfen bir takım seçiniz!");
            alert.showAndWait();
            return;
        }

        if (oyuncuAdi == null || oyuncuAdi.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText("Geçersiz Oyuncu Adı");
            alert.setContentText("Lütfen geçerli bir oyuncu adı giriniz!");
            alert.showAndWait();
            return;
        }

        // Seçilen takımı liste üzerinden buluyoruz
        Takim takim = takimlar.stream()
                .filter(t -> t.getAd().equalsIgnoreCase(secilenTakim)) // Takım ismine göre filtreliyoruz
                .findFirst()
                .orElse(null);

        if (takim != null) {
            // Oyuncu nesnesi oluştur ve takıma ekle
            int yeniOyuncuId = takim.getOyuncular().size() + 1;
            Oyuncu yeniOyuncu = new Oyuncu(yeniOyuncuId, oyuncuAdi); // Oyuncu nesnesi oluşturma
            takim.getOyuncular().add(yeniOyuncu); // Takımın oyuncular listesine ekleme

            // JSON'u güncelle
            if (jsonGuncelle()) {
                // Başarılı mesajını alert ile göster
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Başarılı");
                alert.setHeaderText("Oyuncu Eklendi");
                alert.setContentText(secilenTakim + " takımına yeni oyuncu eklendi.");
                alert.showAndWait();

                // Oyuncu adı alanını temizle
                oyuncu_adi_txtfld.clear();
            }
        } else {
            // Eğer takım bulunamazsa hata mesajı göster
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Takım Bulunamadı");
            alert.setContentText("Seçilen takım bulunamadı!");
            alert.showAndWait();
        }
    }




        private boolean jsonGuncelle() {
        try {
            // JSON dosyasını yazma
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter("takimlar.json")) {
                gson.toJson(takimlar, writer);
            }
            return true;
        } catch (IOException e) {
            // JSON dosyası yazarken hata oluşursa kullanıcıya uyarı göster.
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Dosya Hatası");
            alert.setContentText("JSON dosyasına yazılırken hata oluştu!");
            alert.showAndWait();
            return false;
        }}

    }









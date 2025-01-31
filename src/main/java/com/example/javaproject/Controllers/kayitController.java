package com.example.javaproject.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.io.File;

public class kayitController {
    @FXML
    private TextField email_fld;
    @FXML
    private AnchorPane loginForm;
    @FXML
    private PasswordField password_add;
    @FXML
    private PasswordField password_add_check;
    @FXML
    private PasswordField password_fld;
    @FXML
    private AnchorPane registerForm;
    @FXML
    private CheckBox show_password;
    @FXML
    private TextField show_password_fld;
    @FXML
    private TextField username_add_fld;
    @FXML
    private TextField username_fld;


    @FXML
    private final File file = new File("users.csv");

    @FXML
    private void onRegisterLinkClick() {
        // registerForm'u görünür yap
        registerForm.setVisible(true);
        // loginForm'u gizle
        loginForm.setVisible(false);
    }

    @FXML
    private void onLoginLinkClick() {
        loginForm.setVisible(true);
        registerForm.setVisible(false);
    }

    @FXML
    private void registerUser() {
        // 1. Kullanıcı bilgilerini al
        String username = username_add_fld.getText().trim();
        String email = email_fld.getText().trim();
        String password = password_add.getText().trim();
        String confirmPassword = password_add_check.getText().trim();

        // 2. Basit doğrulama
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Hata", "Tüm alanları doldurmalısınız.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Hata", "Şifreler uyuşmuyor.");
            return;
        }

        // 3. Kullanıcı adının daha önce kaydedilip kaydedilmediğini kontrol et
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length > 0) {
                    String existingUsername = userData[0];
                    if (existingUsername.equals(username)) {
                        showAlert("Hata", "Bu kullanıcı adı zaten mevcut. Lütfen farklı bir kullanıcı adı seçin.");
                        return;
                    }
                }
            }
        } catch (IOException e) {
            showAlert("Hata", "Dosya okunurken bir hata oluştu.");
            return;
        }

        // 4. Kullanıcı bilgilerini dosyaya yazdır
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(username + "," + email + "," + password);
            writer.newLine();
            showAlert("Başarılı", "Kayıt başarıyla tamamlandı!");
            clearRegisterForm(); // Formu temizle
        } catch (IOException e) {
            showAlert("Hata", "Dosyaya yazılamadı.");
        }
    }
    @FXML
    private void loginUser() {
        // 1. Kullanıcı bilgilerinin formdan alınması
        String username = username_fld.getText().trim();
        String password = password_fld.getText().trim();

        // 2. Alan boş kontrolü
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Hata", "Kullanıcı adı ve şifre giriniz.");
            return;
        }

        // 3. Kullanıcı doğrulama
        boolean loginSuccessful = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    String savedUsername = userData[0];
                    String savedPassword = userData[2];

                    // Kullanıcı adı ve şifre eşleşiyor mu?
                    if (savedUsername.equals(username) && savedPassword.equals(password)) {
                        loginSuccessful = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            showAlert("Hata", "Dosya okunurken bir hata oluştu.");
            return;
        }

        // 4. Sonuç
        if (loginSuccessful) {
            showAlert("Başarılı", "Giriş başarılı!");
            clearLoginForm(); // Giriş formunu temizle

            if ("admin".equals(username) && "1234".equals(password)) { // Admin girişi
                loadForm("Admin.fxml", "Admin Menüsü");
            } else { // Müşteri (Client) girişi
                loadForm("Client.fxml", "Müşteri Menüsü");
            }

        } else {
            showAlert("Hata", "Kullanıcı adı veya şifre hatalı.");
        }
    }

    private void loadForm(String fxmlFile, String title) {
        try {
            // FXML dosyasını yüklerken tam yolu doğru belirtin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Page/" + fxmlFile));
            Parent root = loader.load();

            // Yeni sahne (Stage) oluştur
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            // Giriş penceresini kapat
            Stage currentStage = (Stage) username_fld.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FXML dosyası yüklenemedi: " + fxmlFile);
        }
    }

    public void showPassword() {
        if (show_password.isSelected()) {
            show_password_fld.setText(password_fld.getText());
            show_password_fld.setVisible(true);
            password_fld.setVisible(false);
        } else {
            password_fld.setText(show_password_fld.getText());
            show_password_fld.setVisible(false);
            password_fld.setVisible(true);
        }
    }

    // Kaydolma formunu temizleyen yardımcı metot
    @FXML
    private void clearRegisterForm() {
        username_add_fld.clear();
        email_fld.clear();
        password_add.clear();
        password_add_check.clear();
    }

    // Giriş formunu temizleyen yardımcı metot
    @FXML
    private void clearLoginForm() {
        username_fld.clear();
        password_fld.clear();
    }

    // Kullanıcıya uyarı gösteren yardımcı metot
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}

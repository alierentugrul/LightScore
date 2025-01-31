package com.example.javaproject.Controllers.Admin;

public class Match {
    private int id; // Maç ID'si
    private String evSahibi;
    private String deplasman;
    private Integer evSahibiSkor;
    private Integer deplasmanSkor;

    // Constructor
    public Match(int id, String evSahibi, String deplasman, int evSahibiSkor, int deplasmanSkor) {
        this.id = id;
        this.evSahibi = evSahibi;
        this.deplasman = deplasman;
        this.evSahibiSkor = evSahibiSkor;
        this.deplasmanSkor = deplasmanSkor;
    }

    // Getter ve Setter metotları
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvSahibi() {
        return evSahibi;
    }


    public String getDeplasman() {
        return deplasman;
    }


    public Integer getEvSahibiSkor() {
        return evSahibiSkor;
    }


    public Integer getDeplasmanSkor() {
        return deplasmanSkor;
    }

}
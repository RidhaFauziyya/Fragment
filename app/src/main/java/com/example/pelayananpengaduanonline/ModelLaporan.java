package com.example.pelayananpengaduanonline;

public class ModelLaporan {
    private String judul, isi, tanggal, lokasi, username, key;

    public ModelLaporan() {
    }

    public ModelLaporan(String judul, String isi, String tanggal, String lokasi, String name) {
        this.judul = judul;
        this.isi = isi;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
        this.username = name;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

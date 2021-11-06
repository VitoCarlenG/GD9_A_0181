package com.example.gd9_a_0181.models;

import com.google.gson.annotations.SerializedName;

public class Mahasiswa {

    private Long id;
    private String nama;
    private String npm;

    @SerializedName("jenis_kelamin")
    private String jenisKelamin;

    private String fakultas;
    private String prodi;

    public Mahasiswa(String nama, String npm, String jenisKelamin, String fakultas, String prodi) {
        this.nama = nama;
        this.npm = npm;
        this.jenisKelamin = jenisKelamin;
        this.fakultas = fakultas;
        this.prodi = prodi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }
}

package com.example.tesutbk.model;

public class ReqSoal{
    private String soal;
    private String jenissoal;
    private String status;

    public ReqSoal(){

    }

    public ReqSoal(String soal, String jenissoal, String status) {
        this.soal = soal;
        this.jenissoal = jenissoal;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSoal() {
        return soal;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public String getJenissoal() {
        return jenissoal;
    }

    public void setJenissoal(String jenissoal) {
        this.jenissoal = jenissoal;
    }
}

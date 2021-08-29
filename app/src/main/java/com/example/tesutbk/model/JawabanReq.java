package com.example.tesutbk.model;

public class JawabanReq {
    private String jawaban;
    private int nilai;
    private String kett;


    public  JawabanReq(){

    }

    public JawabanReq(String jawaban, int nilai, String kett) {
        this.jawaban = jawaban;
        this.nilai = nilai;
        this.kett = kett;
    }

    public String getKett() {
        return kett;
    }

    public void setKett(String kett) {
        this.kett = kett;
    }

    public int getNilai() {
        return nilai;
    }

    public void setNilai(int nilai) {
        this.nilai = nilai;
    }



    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
    }
}

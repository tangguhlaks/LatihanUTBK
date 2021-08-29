package com.example.tesutbk.model;

public class ReqSubSoal {
    private String no;
    private String pertanyaan;
    private String a;
    private String b;
    private String c;
    private String d;
    private String benar;

    public ReqSubSoal(){

    }
    public ReqSubSoal(String no, String pertanyaan, String a, String b, String c, String d, String benar) {
        this.no = no;
        this.pertanyaan = pertanyaan;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.benar = benar;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getBenar() {
        return benar;
    }

    public void setBenar(String benar) {
        this.benar = benar;
    }
}

package com.example.chatapp;

public class DataCall {
    private String id;
    private String sodienthoai,noidung,ketqua;

    public DataCall(String id, String sodienthoai, String noidung, String ketqua) {
        this.id = id;
        this.sodienthoai = sodienthoai;
        this.noidung = noidung;
        this.ketqua = ketqua;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getKetqua() {
        return ketqua;
    }

    public void setKetqua(String ketqua) {
        this.ketqua = ketqua;
    }
}

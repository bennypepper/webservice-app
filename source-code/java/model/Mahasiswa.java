package com.example.uas_webservice.model;

import com.google.gson.annotations.SerializedName;

public class Mahasiswa {

    @SerializedName("id")
    private String id;

    @SerializedName("NIM")
    private String NIM;

    @SerializedName("Nama")
    private String Nama;

    @SerializedName("Jurusan")
    private String Jurusan;

    @SerializedName("Alamat")
    private String Alamat;

    @SerializedName("foto")
    private String foto;

    public String getId()      { return id; }
    public String getNIM()     { return NIM; }
    public String getNama()    { return Nama; }
    public String getJurusan() { return Jurusan; }
    public String getAlamat()  { return Alamat; }
    public String getFoto()    { return foto; }

    public void setId(String id)           { this.id = id; }
    public void setNIM(String NIM)         { this.NIM = NIM; }
    public void setNama(String Nama)       { this.Nama = Nama; }
    public void setJurusan(String Jurusan) { this.Jurusan = Jurusan; }
    public void setAlamat(String Alamat)   { this.Alamat = Alamat; }
    public void setFoto(String foto)       { this.foto = foto; }
}

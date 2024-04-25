package com.example.regapp.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Mahasiswa implements Serializable {
    private int id;
    private String name;
    private String tglLahir;
    private String gender;

    private String address;

    public Mahasiswa(int id, String name, String tglLahir, String gender, String address) {
        this.id = id;
        this.name = name;
        this.tglLahir = tglLahir;
        this.gender = gender;
        this.address = address;
    }

    public Mahasiswa(String name, String tglLahir, String gender, String address) {
        this.name = name;
        this.tglLahir = tglLahir;
        this.gender = gender;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%3d %s", getId(), getName());
    }
}

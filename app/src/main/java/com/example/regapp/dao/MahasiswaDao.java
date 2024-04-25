package com.example.regapp.dao;

import com.example.regapp.model.Mahasiswa;

import java.util.List;

public interface MahasiswaDao {
    void insert(Mahasiswa mahasiswa);

    void update(int i, Mahasiswa mahasiswa);

    void delete(int i);

    Mahasiswa getMahasiswaById(int id);

    List<Mahasiswa> getAllMahasiswa();
}

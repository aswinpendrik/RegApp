package com.example.regapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.regapp.adapter.MahasiswaAdapter;
import com.example.regapp.model.Mahasiswa;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String DATA_MAHASISWA = "data-mahasiswa";
    private List<Mahasiswa> mList = new ArrayList<>();
    private MahasiswaAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.main_toolbar);

        //buat object mAdapter dengan parameter list mahasiswa dan context
        mAdapter = new MahasiswaAdapter(mList, this);

        if(getListFromSharedPreferences()!=null)
            mList.addAll(getListFromSharedPreferences());

        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(this::itemOnClick);
        listView.setOnItemLongClickListener(this::itemOnLongClick);


        //floating action button fab untuk tambah mahasiswa
        findViewById(R.id.fab).setOnClickListener(this::addMahasiswa);
    }

    private void itemOnClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, AddAndViewActivity.class);
        intent.putExtra(MahasiswaAdapter.MODE, MahasiswaAdapter.VIEW_MODE);
        intent.putExtra(MahasiswaAdapter.MAHASISWA, (Mahasiswa) mAdapter.getItem(i));
        startActivity(intent);
    }

    private void addMahasiswa(View view) {
        //tampilkan form mahasiswa dalam mode edit
        Intent intent = new Intent(this, AddAndViewActivity.class);
        intent.putExtra(MahasiswaAdapter.MODE, MahasiswaAdapter.ADD_MODE);
        resultLauncher.launch(intent);
    }

    private boolean itemOnLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        CharSequence[] items = {"Update", "Delete"};
        int[] checked = {-1};
        new AlertDialog.Builder(this)
                .setTitle("Your Action")
                .setSingleChoiceItems(items, checked[0], (dialogInterface, i1) -> checked[0] = i1)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", (dialogInterface, i1) -> {
                    switch (checked[0]) {
                        //Tampilkan Form Edit Mahasiswa
                        case 0:
                            Intent intent = new Intent(this, AddAndViewActivity.class);
                            intent.putExtra(MahasiswaAdapter.MODE, MahasiswaAdapter.EDIT_MODE);
                            intent.putExtra(MahasiswaAdapter.MAHASISWA, (Mahasiswa) mAdapter.getItem(i));
                            intent.putExtra(MahasiswaAdapter.POSITION, i);
                            resultLauncher.launch(intent);
                            break;
                        //Delete Mahasiswa
                        case 1: //delete
                            new AlertDialog.Builder(this)
                                    .setTitle("Confirm")
                                    .setMessage("Delete " + ((Mahasiswa) mAdapter.getItem(i)).toString() + "?")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("Yes", (dialogInterface1, i2) -> {
                                        mAdapter.delete(i);
                                        mAdapter.notifyDataSetChanged();
                                    }).show();
                            break;
                    }
                }).show();
        return true;
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            if (result.getData().getIntExtra(MahasiswaAdapter.MODE, -1) == MahasiswaAdapter.ADD_MODE) {
                //Insert Mahasiswa
                mAdapter.insert((Mahasiswa) result.getData().getSerializableExtra(MahasiswaAdapter.MAHASISWA));
                mAdapter.notifyDataSetChanged();
            } else if (result.getData().getIntExtra(MahasiswaAdapter.MODE, -1) == MahasiswaAdapter.EDIT_MODE) {
                //Update Mahasiswa
                int pos = result.getData().getIntExtra(MahasiswaAdapter.POSITION, -1);
                mAdapter.update(pos, (Mahasiswa) result.getData().getSerializableExtra(MahasiswaAdapter.MAHASISWA));
                mAdapter.notifyDataSetChanged();
            }
        }
    });

    //tempel menu ke actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //aksi mi_logout, mi_about dan mi_exit
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //ketika user klik menu logout
        if (item.getItemId() == R.id.mi_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm to Logout")
                    .setMessage("Logout from app?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        SharedPreferences sp = getSharedPreferences(LoginActivity.DATA_USER, MODE_PRIVATE);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("loginStatus", false);
                        editor.apply();

                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .show();
        }
        //ketika user klik menu about
        else if (item.getItemId() == R.id.mi_about) {
            new AlertDialog.Builder(this)
                    .setTitle("Tentang Aplikasi")
                    .setMessage(R.string.pesan_about)
                    .setPositiveButton("OK", null).show();
        }
        //ketika user klik exit
        else if (item.getItemId() == R.id.mi_exit) {
            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi")
                    .setMessage("Tutup Aplikasi?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //simpan list kedalam shared preferences
        saveListToSharedPreferences(mList);
    }

    //method ambil list mahasiswa dari Shared Preferences
    public List<Mahasiswa> getListFromSharedPreferences() {
        SharedPreferences sp = getSharedPreferences(DATA_MAHASISWA, MODE_PRIVATE);
        String json = sp.getString(MahasiswaAdapter.MAHASISWAS, null);
        Type type = new TypeToken<ArrayList<Mahasiswa>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    //method menyimpan list mahasiswa ke Shared Preferences
    public void saveListToSharedPreferences(List<Mahasiswa> ls) {
        SharedPreferences sp = getSharedPreferences(DATA_MAHASISWA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String json = new Gson().toJson(ls); //convert ArrayList to String
        editor.putString(MahasiswaAdapter.MAHASISWAS, json).apply();
    }

}
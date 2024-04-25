package com.example.regapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.regapp.adapter.MahasiswaAdapter;
import com.example.regapp.model.Mahasiswa;
import com.google.android.material.textfield.TextInputEditText;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddAndViewActivity extends AppCompatActivity {

    private TextInputEditText tieID, tieName, tieTglLahir, tieGender, tieAddress;
    private Button btClear, btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_view);

        tieID = findViewById(R.id.tie_id);
        tieName = findViewById(R.id.tie_name);
        tieTglLahir = findViewById(R.id.tie_tgl_lahir);
        tieTglLahir.setOnClickListener(this::showDatePickerDialog);

        tieGender = findViewById(R.id.tie_gender);
        tieAddress = findViewById(R.id.tie_address);

        btClear = findViewById(R.id.bt_clear);
        btSave = findViewById(R.id.bt_save);

        btClear.setOnClickListener(this::clearForm);
        btSave.setOnClickListener(this::saveForm);
    }

    private void showDatePickerDialog(View view) {
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            mCalendar.set(year, month, dayOfMonth);
            tieTglLahir.setText(sdf.format(mCalendar.getTime()));
        },
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void saveForm(View view) {
        //TODO simpan data dari form
        if (Objects.requireNonNull(tieID.getText()).toString().isEmpty() || Objects.requireNonNull(tieName.getText()).toString().isEmpty()) {
            Toast.makeText(this, "ID atau Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        Mahasiswa mahasiswa = null;
        mahasiswa = new Mahasiswa(
                Integer.parseInt(tieID.getText().toString()),
                tieName.getText().toString(),
                tieTglLahir.getText().toString(),
                tieGender.getText().toString(),
                tieAddress.getText().toString()
        );

        assert getIntent().getExtras() != null;

        if (getIntent().getIntExtra(MahasiswaAdapter.MODE, -1) == MahasiswaAdapter.ADD_MODE) {
            Intent intent = new Intent();
            intent.putExtra(MahasiswaAdapter.MODE, MahasiswaAdapter.ADD_MODE);
            intent.putExtra(MahasiswaAdapter.MAHASISWA, mahasiswa);
            setResult(RESULT_OK, intent);

        } else if (getIntent().getIntExtra(MahasiswaAdapter.MODE, -1) == MahasiswaAdapter.EDIT_MODE) {
            int pos = getIntent().getIntExtra(MahasiswaAdapter.POSITION, -1);

            Intent intent = new Intent();
            intent.putExtra(MahasiswaAdapter.MODE, MahasiswaAdapter.EDIT_MODE);
            intent.putExtra(MahasiswaAdapter.MAHASISWA, mahasiswa);
            intent.putExtra(MahasiswaAdapter.POSITION, pos);
            setResult(RESULT_OK, intent);
        }

        finish();
    }

    private void clearForm(View view) {
        //TODO bersihkan form
        tieID.setText("");
        tieName.setText("");
        tieTglLahir.setText("");
        tieGender.setText("");
        tieAddress.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        assert getIntent().getExtras() != null;
        Mahasiswa mahasiswa = (Mahasiswa) getIntent().getSerializableExtra(MahasiswaAdapter.MAHASISWA);

        assert getSupportActionBar() != null;

        if (getIntent().getIntExtra(MahasiswaAdapter.MODE, -1) == MahasiswaAdapter.ADD_MODE) {
            getSupportActionBar().setTitle("Tambah Mahasiswa");
            tieTglLahir.setFocusable(false);
        } else if (getIntent().getIntExtra(MahasiswaAdapter.MODE, -1) == MahasiswaAdapter.VIEW_MODE) {
            getSupportActionBar().setTitle("Detail Mahasiswa");
            assert mahasiswa != null;

            tieID.setText(String.valueOf(mahasiswa.getId()));
            tieName.setText(mahasiswa.getName());
            tieTglLahir.setText(mahasiswa.getTglLahir());
            tieGender.setText(mahasiswa.getGender());
            tieAddress.setText(mahasiswa.getAddress());

            tieID.setFocusable(false);
            tieName.setFocusable(false);
            tieTglLahir.setEnabled(false);
            tieGender.setFocusable(false);
            tieAddress.setFocusable(false);

            btClear.setVisibility(View.GONE);
            btSave.setVisibility(View.GONE);
        } else if (getIntent().getIntExtra(MahasiswaAdapter.MODE, -1) == MahasiswaAdapter.EDIT_MODE) {
            getSupportActionBar().setTitle("Edit Mahasiswa");
            assert mahasiswa != null;

            tieID.setText(String.valueOf(mahasiswa.getId()));
            tieName.setText(mahasiswa.getName());
            tieTglLahir.setText(mahasiswa.getTglLahir());
            tieGender.setText(mahasiswa.getGender());
            tieAddress.setText(mahasiswa.getAddress());

            tieID.setFocusable(false);

            btClear.setVisibility(View.GONE);
        }
    }
}
package com.example.regapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    public static final String DATA_USER = "user";
    private TextInputEditText tieUsername, tiePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.login_toolbar);

        tieUsername = findViewById(R.id.tie_username);
        tiePassword = findViewById(R.id.tie_password);


        findViewById(R.id.bt_register).setOnClickListener(this::registerUser);

        findViewById(R.id.bt_login).setOnClickListener(v -> {
            if (Objects.requireNonNull(tieUsername.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(tiePassword.getText()).toString().isEmpty()) {

                Toast.makeText(this, "Username and password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            //check data di shared preferences
            SharedPreferences sp = getSharedPreferences(LoginActivity.DATA_USER, MODE_PRIVATE);

            String username = sp.getString("username", null);
            String password = sp.getString("password", null);

            //check username dan password
            if (tieUsername.getText().toString().equals(username) && tiePassword.getText().toString().equals(password)) {

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("loginStatus", true);
                editor.apply(); //untuk memastikan perubahan data

                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else
                Toast.makeText(this, "Username or password tidak cocok!", Toast.LENGTH_SHORT).show();

        });
    }

    private void registerUser(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //periksa apakah file SharedPreferences eksis,
        //jika eksis artinya user telah direkam ke shared preferences
        //dan tinggal login
        if (preferenceFileExist(LoginActivity.DATA_USER)) {
            //sembunyikan tv_info dan bt_register
            findViewById(R.id.tv_info).setVisibility(View.GONE);
            findViewById(R.id.bt_register).setVisibility(View.GONE);
        }

        SharedPreferences sp = getSharedPreferences(LoginActivity.DATA_USER, MODE_PRIVATE);
        if (sp.getBoolean("loginStatus", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    //method menguji jika file shared preferences exists
    public boolean preferenceFileExist(String fileName) {
        File f = new File(getApplicationContext().getApplicationInfo().dataDir +
                "/shared_prefs/" + fileName + ".xml");
        return f.exists();
    }
}
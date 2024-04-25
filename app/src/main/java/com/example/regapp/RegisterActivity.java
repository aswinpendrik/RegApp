package com.example.regapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText tieRegUsername, tieRegPassword, tieRegConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //periksa jika support action bar
        assert getSupportActionBar() != null;
        //beri judul action bar
        getSupportActionBar().setTitle("Register for New User");

        tieRegUsername = findViewById(R.id.tie_reg_username);
        tieRegPassword = findViewById(R.id.tie_reg_password);
        tieRegConfirmPassword = findViewById(R.id.tie_reg_confirm_password);

        findViewById(R.id.bt_register).setOnClickListener(v -> {
            if (Objects.requireNonNull(tieRegUsername.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(tieRegPassword.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(tieRegConfirmPassword.getText()).toString().isEmpty()) {

                Toast.makeText(this, "username and password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!tieRegPassword.getText().toString()
                    .equals(tieRegConfirmPassword.getText().toString())) {

                Toast.makeText(this, "Password and password confirm not equal!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sp = getSharedPreferences(LoginActivity.DATA_USER, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", tieRegUsername.getText().toString());
            editor.putString("password", tieRegPassword.getText().toString());
            editor.putBoolean("loginStatus", false);
            editor.apply();

            //close Register activity
            finish();
        });
    }
}
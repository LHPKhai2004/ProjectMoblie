package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.LoginRequest;
import com.example.apphoctienganh.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private TextView tvRegister, tvForgotPassword;
    private Button btnLogin;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "UserPrefs", KEY_USERNAME = "username", KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.editTextTaiKhoan);
        etPassword = findViewById(R.id.editTextMatKhau);
        tvRegister = findViewById(R.id.textView_register);
        btnLogin = findViewById(R.id.buttonDangNhap);
        tvForgotPassword = findViewById(R.id.textView_forgotPassword);
    }

    private void setupListeners() {
        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
        btnLogin.setOnClickListener(v -> loginUser());
        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên người dùng");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }
        if (password.length() < 8) {
            etPassword.setError("Mật khẩu phải có ít nhất 8 ký tự");
            return;
        }
        btnLogin.setEnabled(false);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.loginUser(new LoginRequest(username, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    prefs.edit().putString(KEY_USERNAME, username).putString(KEY_TOKEN, response.body().getData().getToken()).apply();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, LayoutActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, response.body() != null ? response.body().getMessage() : "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
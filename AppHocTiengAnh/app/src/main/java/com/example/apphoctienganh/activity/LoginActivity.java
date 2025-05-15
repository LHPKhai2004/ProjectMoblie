package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.LoginRequest;
import com.example.apphoctienganh.model.LoginResponse;
import com.example.apphoctienganh.model.ForgetPasswordRequest;
import com.example.apphoctienganh.model.ApiResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editPassword;
    private TextView textViewRegister;
    private Button btnLogin;
    private TextView textViewForgotPassword;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        mapping();
        setupListeners();
    }

    private void mapping() {
        editUsername = findViewById(R.id.editTextTaiKhoan);
        editPassword = findViewById(R.id.editTextMatKhau);
        textViewRegister = findViewById(R.id.textView_register);
        btnLogin = findViewById(R.id.buttonDangNhap);
        textViewForgotPassword = findViewById(R.id.textView_forgotPassword);
    }

    private void setupListeners() {
        // Navigate to SignUpActivity
        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        // Handle login button click
        btnLogin.setOnClickListener(v -> loginUser());

        // Show forgot password dialog
        textViewForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void loginUser() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Validate inputs
        if (username.isEmpty()) {
            editUsername.setError("Vui lòng nhập tên người dùng");
            return;
        }
        if (password.isEmpty()) {
            editPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }
        if (password.length() < 8) {
            editPassword.setError("Mật khẩu phải có ít nhất 8 ký tự");
            return;
        }

        btnLogin.setEnabled(false); // Prevent multiple clicks

        LoginRequest loginRequest = new LoginRequest(username, password);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isResult()) {
                        // Save username and token to SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_USERNAME, username);
                        editor.putString(KEY_TOKEN, loginResponse.getData().getToken());
                        editor.apply();

                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, LayoutActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage() != null ? loginResponse.getMessage() : "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Lỗi server (HTTP " + response.code() + ")";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += ": " + response.errorBody().string();
                        } catch (IOException e) {
                            errorMessage += ": Không thể đọc nội dung lỗi";
                        }
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_forgot, null);
        builder.setView(dialogView);

        EditText emailEditText = dialogView.findViewById(R.id.editTextEmail);
        Button resetButton = dialogView.findViewById(R.id.buttonResetPassword);

        resetButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                emailEditText.setError("Vui lòng nhập email");
                return;
            }
            if (!isValidEmail(email)) {
                emailEditText.setError("Email không đúng định dạng");
                return;
            }

            resetButton.setEnabled(false); // Prevent multiple clicks

            ForgetPasswordRequest request = new ForgetPasswordRequest(email);
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<ApiResponse> call = apiService.forgetPassword(request);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    resetButton.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse.isResult()) {
                            Toast.makeText(LoginActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            // Close the dialog
                            ((AlertDialog) v.getTag()).dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, apiResponse.getMessage() != null ? apiResponse.getMessage() : "Yêu cầu thất bại", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String errorMessage = "Lỗi server (HTTP " + response.code() + ")";
                        if (response.errorBody() != null) {
                            try {
                                errorMessage += ": " + response.errorBody().string();
                            } catch (IOException e) {
                                errorMessage += ": Không thể đọc nội dung lỗi";
                            }
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    resetButton.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // Store dialog reference for resetButton to dismiss if needed
        resetButton.setTag(alertDialog);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
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
import com.example.apphoctienganh.model.OtpRequest;
import com.example.apphoctienganh.model.OtpResponse;
import com.example.apphoctienganh.model.ResetPasswordRequest;
import com.example.apphoctienganh.model.ApiResponse;
import java.io.IOException;
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
        tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
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

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_forgot_email, null);
        builder.setView(dialogView);
        EditText etEmail = dialogView.findViewById(R.id.editTextEmail);
        Button btnSendOtp = dialogView.findViewById(R.id.buttonSendOtp);
        btnSendOtp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etEmail.setError("Vui lòng nhập email");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Email không đúng định dạng");
                return;
            }
            btnSendOtp.setEnabled(false);
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.forgetPassword(new ForgetPasswordRequest(email)).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    btnSendOtp.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                        Toast.makeText(LoginActivity.this, "OTP đã được gửi", Toast.LENGTH_LONG).show();
                        ((AlertDialog) v.getTag()).dismiss();
                        showOtpVerificationDialog(email);
                    } else {
                        Toast.makeText(LoginActivity.this, response.body() != null ? response.body().getMessage() : "Yêu cầu thất bại", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    btnSendOtp.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
        btnSendOtp.setTag(dialog);
    }

    private void showOtpVerificationDialog(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_verify_otp, null);
        builder.setView(dialogView);
        EditText etOtp = dialogView.findViewById(R.id.editTextOtp);
        Button btnVerify = dialogView.findViewById(R.id.buttonVerifyOtp);
        btnVerify.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            if (otp.isEmpty()) {
                etOtp.setError("Vui lòng nhập mã OTP");
                return;
            }
            btnVerify.setEnabled(false);
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.verifyOtp(new OtpRequest(email, otp)).enqueue(new Callback<OtpResponse>() {
                @Override
                public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                    btnVerify.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                        Toast.makeText(LoginActivity.this, "Xác minh OTP thành công", Toast.LENGTH_LONG).show();
                        ((AlertDialog) v.getTag()).dismiss();
                        showResetPasswordDialog(email);
                    } else {
                        Toast.makeText(LoginActivity.this, response.body() != null ? response.body().getMessage() : "Xác minh thất bại", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<OtpResponse> call, Throwable t) {
                    btnVerify.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
        btnVerify.setTag(dialog);
    }

    private void showResetPasswordDialog(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_reset_password, null);
        builder.setView(dialogView);
        EditText etNewPassword = dialogView.findViewById(R.id.editTextNewPassword);
        EditText etConfirmPassword = dialogView.findViewById(R.id.editTextConfirmPassword);
        Button btnReset = dialogView.findViewById(R.id.buttonResetPassword);
        btnReset.setOnClickListener(v -> {
            String password = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            if (password.isEmpty()) {
                etNewPassword.setError("Vui lòng nhập mật khẩu mới");
                return;
            }
            if (password.length() < 8) {
                etNewPassword.setError("Mật khẩu phải có ít nhất 8 ký tự");
                return;
            }
            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Mật khẩu không khớp");
                return;
            }
            btnReset.setEnabled(false);
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.resetPassword(null, new ResetPasswordRequest(email, password)).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    btnReset.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                        Toast.makeText(LoginActivity.this, "Đặt lại mật khẩu thành công", Toast.LENGTH_LONG).show();
                        ((AlertDialog) v.getTag()).dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body() != null ? response.body().getMessage() : "Đặt lại mật khẩu thất bại", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    btnReset.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
        btnReset.setTag(dialog);
    }
}
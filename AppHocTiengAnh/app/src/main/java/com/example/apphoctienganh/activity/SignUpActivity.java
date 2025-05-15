package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.OtpRequest;
import com.example.apphoctienganh.model.OtpResponse;
import com.example.apphoctienganh.model.RegisterRequest;
import com.example.apphoctienganh.model.RegisterResponse;
import java.io.IOException;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etPassword2, etOtp;
    private Button btnSignUp, btnVerifyOtp;
    private LinearLayout llOtp;
    private ProgressBar progressBar;
    private TextView tvLogin;
    private ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etPassword2 = findViewById(R.id.editTextPasswordAgain);
        btnSignUp = findViewById(R.id.buttonSignUp);
        llOtp = findViewById(R.id.otpLayout);
        etOtp = findViewById(R.id.editTextOtp);
        btnVerifyOtp = findViewById(R.id.buttonVerifyOtp);
        progressBar = findViewById(R.id.progressBar);
        tvLogin = findViewById(R.id.textView_login);
        llOtp.setVisibility(View.GONE);
    }

    private void setupListeners() {
        btnSignUp.setOnClickListener(v -> registerUser());
        btnVerifyOtp.setOnClickListener(v -> verifyOtp());
        tvLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private boolean validateInputs() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();
        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên người dùng");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không đúng định dạng");
            return false;
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            etPassword.setError("Mật khẩu cần ít nhất 8 ký tự, gồm chữ hoa, thường, số và ký tự đặc biệt");
            return false;
        }
        if (!password.equals(password2)) {
            etPassword2.setError("Mật khẩu không khớp");
            return false;
        }
        return true;
    }

    private void registerUser() {
        if (!validateInputs()) return;
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        btnSignUp.setEnabled(false);
        apiService.registerUser(new RegisterRequest(username, email, password)).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnSignUp.setEnabled(true);
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công. Vui lòng nhập OTP.", Toast.LENGTH_LONG).show();
                    llOtp.setVisibility(View.VISIBLE);
                } else {
                    handleRegistrationError(response.body());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSignUp.setEnabled(true);
                Toast.makeText(SignUpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verifyOtp() {
        String email = etEmail.getText().toString().trim();
        String otp = etOtp.getText().toString().trim();
        if (otp.isEmpty()) {
            etOtp.setError("Vui lòng nhập mã OTP");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        btnVerifyOtp.setEnabled(false);
        apiService.verifyOtp(new OtpRequest(email, otp)).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnVerifyOtp.setEnabled(true);
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    Toast.makeText(SignUpActivity.this, "Xác minh OTP thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                } else {
                    etOtp.setError("Mã OTP không hợp lệ");
                    Toast.makeText(SignUpActivity.this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnVerifyOtp.setEnabled(true);
                Toast.makeText(SignUpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleRegistrationError(RegisterResponse response) {
        if (response == null) {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = response.getCode();
        if ("ERROR-ACCOUNT-002".equals(code)) {
            etUsername.setError("Tên người dùng đã tồn tại");
            Toast.makeText(this, "Tên người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
        } else if ("VALIDATION_ERROR".equals(code)) {
            StringBuilder errorMsg = new StringBuilder("Lỗi: ");
            Map<String, String> errors = response.getData();
            if (errors != null) {
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    errorMsg.append(entry.getKey()).append(": ").append(entry.getValue()).append("; ");
                    if (entry.getKey().contains("username")) etUsername.setError(entry.getValue());
                    else if (entry.getKey().contains("email")) etEmail.setError(entry.getValue());
                    else if (entry.getKey().contains("password")) etPassword.setError(entry.getValue());
                }
            }
            Toast.makeText(this, errorMsg.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
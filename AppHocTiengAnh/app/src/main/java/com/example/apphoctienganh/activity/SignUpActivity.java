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
    private EditText username, email, password, password2, otpInput;
    private Button signUp, verifyOtp;
    private LinearLayout otpLayout;
    private ProgressBar progressBar;
    private TextView loginTextView;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Retrofit
        apiService = RetrofitClient.getClient().create(ApiService.class);

        mapping();
        setupListeners();
    }

    private void mapping() {
        username = findViewById(R.id.editTextUsername);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        password2 = findViewById(R.id.editTextPasswordAgain);
        signUp = findViewById(R.id.buttonSignUp);
        otpLayout = findViewById(R.id.otpLayout);
        otpInput = findViewById(R.id.editTextOtp);
        verifyOtp = findViewById(R.id.buttonVerifyOtp);
        progressBar = findViewById(R.id.progressBar);
        loginTextView = findViewById(R.id.textView_login);
        otpLayout.setVisibility(View.GONE);
    }

    private void setupListeners() {
        signUp.setOnClickListener(v -> {
            if (!validateInputs()) {
                return;
            }
            registerUser();
        });

        verifyOtp.setOnClickListener(v -> verifyOtp());

        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateInputs() {
        String usernameStr = username.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String password2Str = password2.getText().toString().trim();

        if (usernameStr.isEmpty()) {
            username.setError("Vui lòng nhập tên người dùng");
            return false;
        }
        if (!isValidEmail(emailStr)) {
            email.setError("Email không đúng định dạng");
            return false;
        }
        if (!isValidPassword(passwordStr)) {
            password.setError("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, thường, số và ký tự đặc biệt");
            return false;
        }
        if (!passwordStr.equals(password2Str)) {
            password2.setError("Mật khẩu không khớp");
            return false;
        }
        return true;
    }

    private void registerUser() {
        String usernameStr = username.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        signUp.setEnabled(false);

        RegisterRequest request = new RegisterRequest(usernameStr, emailStr, passwordStr);
        Call<RegisterResponse> call = apiService.registerUser(request);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressBar.setVisibility(View.GONE);
                signUp.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse.isResult()) {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công. Vui lòng nhập OTP được gửi đến email.", Toast.LENGTH_LONG).show();
                        otpLayout.setVisibility(View.VISIBLE);
                    } else {
                        handleRegistrationError(registerResponse);
                    }
                } else {
                    handleServerError(response, "Đăng ký thất bại");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                signUp.setEnabled(true);
                handleNetworkError(t, "Lỗi kết nối khi đăng ký");
            }
        });
    }

    private void verifyOtp() {
        String emailStr = email.getText().toString().trim();
        String otpStr = otpInput.getText().toString().trim();

        if (otpStr.isEmpty()) {
            otpInput.setError("Vui lòng nhập mã OTP");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        verifyOtp.setEnabled(false);

        OtpRequest request = new OtpRequest(emailStr, otpStr);
        Call<OtpResponse> call = apiService.verifyOtp(request);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                progressBar.setVisibility(View.GONE);
                verifyOtp.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    OtpResponse otpResponse = response.body();
                    if (otpResponse.isResult()) {
                        Toast.makeText(SignUpActivity.this, "Xác minh OTP thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        otpInput.setError("Mã OTP không hợp lệ");
                        Toast.makeText(SignUpActivity.this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleServerError(response, "Xác minh OTP thất bại");
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                verifyOtp.setEnabled(true);
                handleNetworkError(t, "Lỗi kết nối khi xác minh OTP");
            }
        });
    }

    private void handleRegistrationError(RegisterResponse response) {
        String code = response.getCode();
        if ("ERROR-ACCOUNT-002".equals(code)) {
            username.setError("Tên người dùng đã tồn tại");
            Toast.makeText(this, "Tên người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
        } else if ("VALIDATION_ERROR".equals(code)) {
            StringBuilder errorMsg = new StringBuilder("Lỗi: ");
            Map<String, String> errors = response.getData();
            if (errors != null) {
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    errorMsg.append(entry.getKey()).append(": ").append(entry.getValue()).append("; ");
                    // Set error to corresponding field
                    if (entry.getKey().contains("username")) {
                        username.setError(entry.getValue());
                    } else if (entry.getKey().contains("email")) {
                        email.setError(entry.getValue());
                    } else if (entry.getKey().contains("password")) {
                        password.setError(entry.getValue());
                    }
                }
            }
            Toast.makeText(this, errorMsg.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleServerError(Response<?> response, String defaultMessage) {
        String errorMessage = defaultMessage + " (HTTP " + response.code() + ")";
        if (response.errorBody() != null) {
            try {
                errorMessage += ": " + response.errorBody().string();
            } catch (IOException e) {
                errorMessage += ": Không thể đọc nội dung lỗi";
            }
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        android.util.Log.e("SignUpActivity", errorMessage);
    }

    private void handleNetworkError(Throwable t, String defaultMessage) {
        String errorMessage = defaultMessage + ": " + t.getMessage();
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        android.util.Log.e("SignUpActivity", errorMessage);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }
}
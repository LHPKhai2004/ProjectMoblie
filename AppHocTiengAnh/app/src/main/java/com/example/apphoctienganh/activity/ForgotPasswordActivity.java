package com.example.apphoctienganh.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.ForgetPasswordRequest;
import com.example.apphoctienganh.model.OtpRequest;
import com.example.apphoctienganh.model.OtpResponse;
import com.example.apphoctienganh.model.ResetPasswordRequest;
import com.example.apphoctienganh.model.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showForgotPasswordDialog();
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
                        Toast.makeText(ForgotPasswordActivity.this, "OTP đã được gửi", Toast.LENGTH_LONG).show();
                        ((AlertDialog) v.getTag()).dismiss();
                        showOtpVerificationDialog(email);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, response.body() != null ? response.body().getMessage() : "Yêu cầu thất bại", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    btnSendOtp.setEnabled(true);
                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.cancel();
            finish();
        });
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
                        Toast.makeText(ForgotPasswordActivity.this, "Xác minh OTP thành công", Toast.LENGTH_LONG).show();
                        ((AlertDialog) v.getTag()).dismiss();
                        showResetPasswordDialog(email);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, response.body() != null ? response.body().getMessage() : "Xác minh thất bại", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<OtpResponse> call, Throwable t) {
                    btnVerify.setEnabled(true);
                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.cancel();
            finish();
        });
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
                        Toast.makeText(ForgotPasswordActivity.this, "Đặt lại mật khẩu thành công", Toast.LENGTH_LONG).show();
                        ((AlertDialog) v.getTag()).dismiss();
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, response.body() != null ? response.body().getMessage() : "Đặt lại mật khẩu thất bại", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    btnReset.setEnabled(true);
                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.cancel();
            finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        btnReset.setTag(dialog);
    }
}
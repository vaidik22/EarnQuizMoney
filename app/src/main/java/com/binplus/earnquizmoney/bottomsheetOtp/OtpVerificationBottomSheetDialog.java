package com.binplus.earnquizmoney.bottomsheetOtp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.binplus.earnquizmoney.R;
import com.chaos.view.PinView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Timer;
import java.util.TimerTask;

public class OtpVerificationBottomSheetDialog extends BottomSheetDialogFragment {
    TextView tvOtpTitle;
    PinView pinView;
    TextView tvOtpTimer;
    Button btnSubmit;
    Long timeOutSeconds = 60L;
    ImageView iv_close;
    private Timer timer;
    private boolean isAttached = false;
    private boolean isOtpVerified = false;
    private String generatedOtp;
    private OtpVerificationListener otpVerificationListener;
    private ResendOtpListener resendOtpListener;
    private boolean isMobileOtp;

    public void setOtpVerificationListener(OtpVerificationListener listener) {
        this.otpVerificationListener = listener;
    }

    public void setResendOtpListener(ResendOtpListener listener) {
        this.resendOtpListener = listener;
    }

    public void setOtp(int otp, boolean isMobile) {
        this.generatedOtp = String.valueOf(otp);
        this.isMobileOtp = isMobile;
        if (pinView != null) {
            pinView.setText(generatedOtp);
            pinView.requestFocus();
            pinView.setCursorVisible(true);
            pinView.setSelection(generatedOtp.length());
            pinView.setError(null);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pinView != null) {
                        pinView.setText(generatedOtp);
                        pinView.requestFocus();
                        pinView.setCursorVisible(true);
                        pinView.setSelection(generatedOtp.length());
                        pinView.setError(null);

                    }
                }
            }, 5000);
        }
    }

    public String getGeneratedOtp() {
        return generatedOtp;
    }

    private void verifyOtp() {
        String otpInput = pinView.getText().toString();
        if (otpInput.equals(generatedOtp)) {
            isOtpVerified = true;
            if (otpVerificationListener != null) {
                otpVerificationListener.onOtpVerified(isMobileOtp, generatedOtp);
            }
            dismiss();
        } else {
            pinView.setError("Invalid OTP");
        }
    }

    public interface OtpVerificationListener {
        void onOtpVerified(boolean isMobileOtp, String otp);
    }

    public interface ResendOtpListener {
        void onResendOtp();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_verification_bottom_sheet, container, false);
        initView(view);
        allClick();
        startResendTimer();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        isAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
        if (timer != null) {
            timer.cancel();
        }
    }

    private void allClick() {
        btnSubmit.setOnClickListener(v -> verifyOtp());

        iv_close.setOnClickListener(v -> dismiss());

        tvOtpTimer.setOnClickListener(v -> {
            if (timeOutSeconds == 0 && resendOtpListener != null) {
                timeOutSeconds = 60L;
                resendOtpListener.onResendOtp();
                startResendTimer();
            }
        });
    }

    private void initView(View view) {
        tvOtpTitle = view.findViewById(R.id.tv_otp_title);
        pinView = view.findViewById(R.id.et_otp);
        tvOtpTimer = view.findViewById(R.id.tv_otp_timer);
        btnSubmit = view.findViewById(R.id.btn_submit);
        iv_close = view.findViewById(R.id.iv_close);
    }

    private void startResendTimer() {
        if (tvOtpTimer != null) {
            tvOtpTimer.setEnabled(false);
        }
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (isAttached && timeOutSeconds > 0) {
                    getActivity().runOnUiThread(() -> {
                        timeOutSeconds--;
                        if (tvOtpTimer != null) {
                            tvOtpTimer.setText("Time remaining: " + String.format("00:%02d", timeOutSeconds));
                            tvOtpTimer.setTextColor(Color.RED);
                            if (timeOutSeconds <= 0) {
                                timer.cancel();
                                tvOtpTimer.setEnabled(true);
                                tvOtpTimer.setText("Resend OTP");
                                tvOtpTimer.setTextColor(Color.BLACK);
                            }
                        }
                    });
                } else {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    public boolean isOtpVerified() {
        return isOtpVerified;
    }
}

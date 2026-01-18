package github.stecjr.srms.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import github.stecjr.srms.R;
import github.stecjr.srms.util.Toast;

public class OtpVerification extends BottomSheetDialogFragment {
    private final String otp;
    private final Runnable onOtpVerified;

    public OtpVerification(String otp, Runnable onOtpVerified) {
        this.otp = otp;
        this.onOtpVerified = onOtpVerified;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp_verification, container, false);
        EditText otpVerificationInput = view.findViewById(R.id.otpVerificationInput);
        AppCompatButton otpVerificationVerifyButton = view.findViewById(R.id.otpVerificationVerifyButton);

        otpVerificationVerifyButton.setOnClickListener(v -> {
            String enteredOtp = otpVerificationInput.getText().toString();
            if (enteredOtp.equals(otp)) {
                if (onOtpVerified != null) onOtpVerified.run();
                dismiss();
            } else Toast.authenticationError(getContext(), "Incorrect OTP");
        });
        return view;
    }
}

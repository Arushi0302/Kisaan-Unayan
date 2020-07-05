package com.example.sih.Authentication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaos.view.PinView;
import com.example.sih.MainActivity_newbee;
import com.example.sih.MainActivity_registered;
import com.example.sih.R;
import com.example.sih.ui.Details.DetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Signin extends AppCompatActivity implements View.OnClickListener {

    private PinView pinView;
    private Button next;
    private TextView topText, textU;
    private EditText userName, userPhone;
    private ConstraintLayout first, second;

    String phoneNumber, otp;
    private String verificationCode;

    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser firebaseUser;
    DatabaseReference myRef;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //check if user is null
        if(firebaseUser != null){
            Intent intent = new Intent(Signin.this, MainActivity_registered.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();

        StartFirebaseLogin();

        topText = findViewById(R.id.topText);
        pinView = findViewById(R.id.pinView);
        next = findViewById(R.id.button);
        userName = findViewById(R.id.username);
        userPhone = findViewById(R.id.userPhone);
        first = findViewById(R.id.first_step);
        second = findViewById(R.id.secondStep);
        textU = findViewById(R.id.textView_noti);
        first.setVisibility(View.VISIBLE);

        next.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (next.getText().equals("Let's go!")) {
            String name = userName.getText().toString();
            String phone = userPhone.getText().toString();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {

                phoneNumber = userPhone.getText().toString();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        Signin.this,        // Activity (for callback binding)
                        mCallback);                      // OnVerificationStateChangedCallbacks

                next.setText("Verify");
                first.setVisibility(View.GONE);
                second.setVisibility(View.VISIBLE);
                topText.setText("I Still don't trust you.\nTell me something that only two of us know.");
            } else {
                Toast.makeText(Signin.this, "Please enter the details", Toast.LENGTH_SHORT).show();
            }
        } else if (next.getText().equals("Verify")) {
            otp = pinView.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
            SigninWithPhone(credential);
        }

    }


    private void SigninWithPhone(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            myRef = database.getReference().child("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("uid", userid);
                            hashMap.put("username", userName.getText().toString());
                            hashMap.put("Mobile Number", userPhone.getText().toString());

                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pinView.setLineColor(Color.GREEN);
                                        textU.setText("OTP Verified");
                                        textU.setTextColor(Color.GREEN);
                                        startActivity(new Intent(Signin.this, MainActivity_newbee.class));
                                        finish();
                                    } else {
                                        pinView.setLineColor(Color.RED);
                                        textU.setText("X Incorrect OTP");
                                        textU.setTextColor(Color.RED);
                                        Toast.makeText(Signin.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void StartFirebaseLogin() {

        mAuth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(Signin.this, "verification completed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Signin.this, "verification failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(Signin.this, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };
    }
}

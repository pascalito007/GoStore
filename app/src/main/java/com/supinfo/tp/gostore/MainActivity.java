package com.supinfo.tp.gostore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.supinfo.tp.gostore.activities.LoginActivity;
import com.supinfo.tp.gostore.activities.QrcodeHomeActivity;
import com.supinfo.tp.gostore.data.model.UserInfo;

import java.util.function.Function;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.supinfo.tp.gostore.Utils.Constants.USER_INFO_KEY;

public class MainActivity extends AppCompatActivity {

    private static final int MAIN_ACTIVITY_RESULT = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_full_name)
    TextInputEditText tvFullName;
    @BindView(R.id.tv_phone)
    TextInputEditText tvPhone;
    @BindView(R.id.tv_email)
    TextInputEditText tvEmail;
    @BindView(R.id.tv_address)
    TextInputEditText tvAddress;
    @BindView(R.id.tv_username)
    TextInputEditText tvUserName;
    @BindView(R.id.tv_password)
    TextInputEditText tvPassword;
    private UserInfo userInfo;

    DatabaseReference ref;
    private FirebaseAuth mAuth;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new Gson();
        userInfo = gson.fromJson(preferences.getString(USER_INFO_KEY, null), UserInfo.class);
        Log.d("userinfo:", userInfo + "");
        if (userInfo != null && userInfo.getCardHolder() != null) {
            Intent intent = new Intent(MainActivity.this, QrcodeHomeActivity.class);
            startActivity(intent);
        } else if (preferences.getString(USER_INFO_KEY, null) != null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void next(View view) {
        userInfo = getUserInfo();
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Inscription en cours...");
        pDialog.setCancelable(false);
        createUserInFirebase(pDialog);
        pDialog.show();

    }

    private void createUserInFirebase(SweetAlertDialog pDialog) {
        pDialog.setOnShowListener(dialog -> {
            mAuth.createUserWithEmailAndPassword(this.userInfo.getEmail(), this.userInfo.getPassword())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                this.userInfo.setPassword(null);
                                String email = user.getEmail().replace(".", ",");
                                this.userInfo.setEmail(email);
                                ref.child("users").child(email).setValue(this.userInfo);
                                pDialog.dismiss();
                                SweetAlertDialog dial = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Inscription réussie")
                                        .setContentText("Veuillez confirmer l'email envoyé");
                                dial.setOnShowListener(dialog12 -> user.sendEmailVerification());
                                dial.setConfirmClickListener(dialog1 -> {
                                    Gson gson = new Gson();
                                    SharedPreferences.Editor editor = preferences.edit();
                                    String userInfo = gson.toJson(this.userInfo);
                                    editor.putString(USER_INFO_KEY, userInfo);
                                    editor.apply();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    dial.dismissWithAnimation();

                                });
                                dial.show();
                            }
                        } else {
                            pDialog.dismiss();
                        }
                    })
                    .addOnCanceledListener(() -> {
                        pDialog.dismiss();
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Une erreur s'est produite")
                                .setContentText("Inscription annulée")
                                .show();
                    })
                    .addOnFailureListener(e -> {
                        Log.d("error:", e.getMessage());
                        pDialog.dismiss();
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Une erreur s'est produite")
                                .setContentText(e.getMessage())
                                .show();
                    });
        });
    }

    private UserInfo getUserInfo() {
        userInfo = new UserInfo();
        Function<TextInputEditText, String> getInputValue = input -> input.getText().toString();

        if (!TextUtils.isEmpty(tvAddress.getText()))
            userInfo.setAddress(getInputValue.apply(tvAddress));
        if (!TextUtils.isEmpty(tvEmail.getText()))
            userInfo.setEmail(getInputValue.apply(tvEmail));
        if (!TextUtils.isEmpty(tvFullName.getText()))
            userInfo.setFullName(getInputValue.apply(tvFullName));
        if (!TextUtils.isEmpty(tvPhone.getText()))
            userInfo.setPhone(getInputValue.apply(tvPhone));
        if (!TextUtils.isEmpty(tvUserName.getText()))
            userInfo.setUserName(tvUserName.getText().toString());
        if (!TextUtils.isEmpty(tvPassword.getText()))
            userInfo.setPassword(tvPassword.getText().toString());
        return userInfo;
    }
}

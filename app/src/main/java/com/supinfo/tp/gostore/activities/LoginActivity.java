package com.supinfo.tp.gostore.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.supinfo.tp.gostore.Utils.Constants;
import com.supinfo.tp.gostore.R;
import com.supinfo.tp.gostore.data.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {


    UserInfo userInfo;
    SharedPreferences preferences;
    FirebaseAuth auth;
    @BindView(R.id.tv_login_email)
    TextInputEditText tvLoginEmail;
    @BindView(R.id.tv_login_password)
    TextInputEditText tvLoginPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        Gson gson = new Gson();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userInfo = gson.fromJson(preferences.getString(Constants.USER_INFO_KEY, null), UserInfo.class);


    }

    public void connect(View view) {
        auth.signInWithEmailAndPassword(this.tvLoginEmail.getText().toString(), this.tvLoginPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (userInfo != null && userInfo.getCardHolder() == null) {
                            Intent intent = new Intent(LoginActivity.this, CheckOutActivity.class);
                            startActivity(intent);
                        } else if (userInfo != null && userInfo.getCardHolder() != null) {
                            Intent intent = new Intent(LoginActivity.this, QrcodeHomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}

package com.supinfo.tp.gostore.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.supinfo.tp.gostore.Utils.Constants;
import com.supinfo.tp.gostore.MainActivity;
import com.supinfo.tp.gostore.R;
import com.supinfo.tp.gostore.data.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrcodeHomeActivity extends AppCompatActivity {
    SharedPreferences preferences;
    UserInfo userInfo;
    @BindView(R.id.qrcode)
    ImageView imageViewQrCode;
    @BindView(R.id.welcome)
    TextView tvWelcome;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_home);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        userInfo = gson.fromJson(preferences.getString(Constants.USER_INFO_KEY, null), UserInfo.class);
        tvWelcome.setText("Bienvenu \n" + userInfo.getFullName().toUpperCase());
        userInfo = gson.fromJson(preferences.getString(Constants.USER_INFO_KEY, null), UserInfo.class);
        Log.d(this.getClass().getName(), userInfo.toString());
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(gson.toJson(userInfo), BarcodeFormat.QR_CODE, 800, 800);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_account) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.USER_INFO_KEY, null);
            editor.apply();
            auth.signOut();
            Intent intent = new Intent(QrcodeHomeActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_logout) {
            auth.signOut();
            Intent intent = new Intent(QrcodeHomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.supinfo.tp.gostore;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrcodeHomeActivity extends AppCompatActivity {
    SharedPreferences preferences;
    CardInfo cardInfo;
    @BindView(R.id.qrcode)
    ImageView imageViewQrCode;
    @BindView(R.id.welcome)
    TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_home);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson=new Gson();
        cardInfo =gson.fromJson(preferences.getString(Constants.CARD_INFO_KEY, null), CardInfo.class);
        tvWelcome.setText("Bienvenu \n"+cardInfo.getCardHolder().toUpperCase());
        cardInfo =gson.fromJson(preferences.getString(Constants.CARD_INFO_KEY, null), CardInfo.class);
        Log.d(this.getClass().getName(),cardInfo.toString());
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(gson.toJson(cardInfo), BarcodeFormat.QR_CODE, 800, 800);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

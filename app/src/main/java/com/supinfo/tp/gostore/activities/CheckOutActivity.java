package com.supinfo.tp.gostore.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.supinfo.tp.gostore.Utils.Constants;
import com.supinfo.tp.gostore.R;
import com.supinfo.tp.gostore.Utils.CreditCardExpiryTextWatcher;
import com.supinfo.tp.gostore.Utils.CreditCardFormattingTextWatcher;
import com.supinfo.tp.gostore.Utils.CreditCardUtils;
import com.supinfo.tp.gostore.Utils.FontTypeChange;
import com.supinfo.tp.gostore.data.model.UserInfo;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.supinfo.tp.gostore.Utils.Constants.USER_INFO_KEY;
import static com.supinfo.tp.gostore.Utils.CreditCardUtils.AMEX;
import static com.supinfo.tp.gostore.Utils.CreditCardUtils.DISCOVER;
import static com.supinfo.tp.gostore.Utils.CreditCardUtils.MASTERCARD;
import static com.supinfo.tp.gostore.Utils.CreditCardUtils.NONE;
import static com.supinfo.tp.gostore.Utils.CreditCardUtils.VISA;

public class CheckOutActivity extends AppCompatActivity {

    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.flip)
    EasyFlipView flipView;

    private SharedPreferences preferences;
    private static final int CHECKOUT_ACTIVITY_CODE = 111;
    private UserInfo userInfo;


    @BindView(R.id.et_number)
    TextInputEditText et_number;
    @BindView(R.id.et_name)
    TextInputEditText et_name;
    @BindView(R.id.et_validity)
    TextInputEditText et_validity;


    @BindView(R.id.tv_card_number)
    TextView tvNumber;
    @BindView(R.id.tv_member_name)
    TextView tvName;
    @BindView(R.id.tv_validity)
    TextView tvValidity;
    @BindView(R.id.ivType)
    ImageView ivType;
    @BindView(R.id.tv_cvv)
    TextView tv_cvv;

    FontTypeChange fontTypeChange;

    @BindView(R.id.securedCode)
    TextInputEditText securedCode;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        ButterKnife.bind(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ref= FirebaseDatabase.getInstance().getReference();
        //userInfo = (UserInfo) getIntent().getSerializableExtra(Constants.USER_INFO_KEY);
        Gson gson=new Gson();
        this.userInfo = gson.fromJson(preferences.getString(Constants.USER_INFO_KEY, null), UserInfo.class);
        setCardInputData();
    }

    private void setCardInputData() {
        securedCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                flipView.flipTheView();
            } else {
                flipView.flipTheView(false);
            }
        });

        fontTypeChange = new FontTypeChange(this);
        et_number.setTypeface(fontTypeChange.get_fontface(3));
        et_name.setTypeface(fontTypeChange.get_fontface(3));


        securedCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (tv_cvv != null) {
                    if (TextUtils.isEmpty(editable.toString().trim()))
                        tv_cvv.setText("XXX");
                    else
                        tv_cvv.setText(editable.toString());

                } else
                    Log.d(TAG, "afterTextChanged: cvv null");

            }
        });
        et_validity.addTextChangedListener(new CreditCardExpiryTextWatcher(et_validity, tvValidity));

        et_number.addTextChangedListener(new CreditCardFormattingTextWatcher(et_number, tvNumber, ivType, type -> {
            Log.d("Card", "setCardType: " + type);

            setCardType(type);
        }));

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (tvName != null) {
                    if (TextUtils.isEmpty(editable.toString().trim()))
                        tvName.setText("CARD HOLDER");
                    else
                        tvName.setText(editable.toString());

                }

            }
        });

        if (!TextUtils.isEmpty(securedCode.getText()))
            tv_cvv.setText(securedCode.getText().toString().trim());
    }

    private static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    public void onBackPressed() {
        hidKeyBoardOrQuit();
    }

    private void hidKeyBoardOrQuit() {
        int heightDiff = root.getRootView().getHeight() - root.getHeight();
        if (heightDiff > dpToPx(CheckOutActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
            final InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                if (this.getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                }
            }
        } else {
            super.onBackPressed();
        }
    }


    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } else {
            activity.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    //useful for hiding the soft-keyboard is:
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void checkEntries() {
        if (TextUtils.isEmpty(et_name.getText())) {
            Toast.makeText(CheckOutActivity.this, "Enter Valid Name", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(et_number.getText()) || !CreditCardUtils.isValid(et_number.getText().toString().replace(" ", ""))) {
            Toast.makeText(CheckOutActivity.this, "Enter Valid card number", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(et_validity.getText()) || !CreditCardUtils.isValidDate(et_validity.getText().toString())) {
            Toast.makeText(CheckOutActivity.this, "Enter correct validity", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(securedCode.getText()) || securedCode.getText().toString().length() < 3) {
            Toast.makeText(CheckOutActivity.this, "Enter valid security number", Toast.LENGTH_SHORT).show();
            return;
        } else
            Toast.makeText(CheckOutActivity.this, "Your card is added", Toast.LENGTH_SHORT).show();


    }

    private void setCardType(int type) {
        switch (type) {
            case VISA:
                ivType.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visa));
                break;
            case MASTERCARD:
                ivType.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_mastercard));
                break;
            case AMEX:
                ivType.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_amex));
                break;
            case DISCOVER:
                ivType.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_discover));
                break;
            case NONE:
                ivType.setImageResource(android.R.color.transparent);
                break;

        }
    }

    public void validate(View view) {
        checkEntries();

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        userInfo.setCardHolder(et_name.getText().toString());
        userInfo.setCardNumber(et_number.getText().toString());
        userInfo.setExpireDate(et_validity.getText().toString());
        userInfo.setSecretCode(securedCode.getText().toString());
        String cardInfoJson = gson.toJson(userInfo);
        editor.putString(USER_INFO_KEY, cardInfoJson);
        editor.apply();
        ref.child("subscriptions").child(this.userInfo.getEmail().replace(".", ",")).setValue(this.userInfo);
        Intent intent = new Intent(this, QrcodeHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

package com.supinfo.tp.gostore.CCFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.supinfo.tp.gostore.CardFrontFragment;
import com.supinfo.tp.gostore.CheckOutActivity;
import com.supinfo.tp.gostore.R;
import com.supinfo.tp.gostore.Utils.CreditCardEditText;
import com.supinfo.tp.gostore.Utils.CreditCardExpiryTextWatcher;
import com.supinfo.tp.gostore.Utils.CreditCardFormattingTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CCNumberFragment extends Fragment {


    @BindView(R.id.et_number)
    CreditCardEditText et_number;
    TextView tv_number;

    @BindView(R.id.et_name)
    CreditCardEditText et_name;
    TextView tv_Name;

    @BindView(R.id.et_validity)
    CreditCardEditText et_validity;
    TextView tv_validity;

    CheckOutActivity activity;
    CardFrontFragment cardFrontFragment;

    public CCNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ccnumber, container, false);
        ButterKnife.bind(this, view);

        activity = (CheckOutActivity) getActivity();
        cardFrontFragment = activity.cardFrontFragment;

        tv_number = cardFrontFragment.getNumber();

        //Do your stuff
        setFrontCardData();

        return view;
    }

    private void setFrontCardData() {
        et_number.addTextChangedListener(new CreditCardFormattingTextWatcher(et_number, tv_number,cardFrontFragment.getCardType(), type -> {
            Log.d("Card", "setCardType: "+type);

            cardFrontFragment.setCardType(type);
        }));

        et_number.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if(activity!=null)
                {
                    activity.nextClick();
                    return true;
                }

            }
            return false;
        });

        et_number.setOnBackButtonListener(() -> {
            if(activity!=null)
                activity.onBackPressed();
        });


        // Name Holder
        tv_Name = cardFrontFragment.getName();

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(tv_Name!=null)
                {
                    if (TextUtils.isEmpty(editable.toString().trim()))
                        tv_Name.setText("CARD HOLDER");
                    else
                        tv_Name.setText(editable.toString());

                }

            }
        });

        et_name.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if(activity!=null)
                {
                    activity.nextClick();
                    return true;
                }

            }
            return false;
        });


        et_name.setOnBackButtonListener(() -> {
            if(activity!=null)
                activity.onBackPressed();
        });

        //Validity date
        tv_validity = cardFrontFragment.getValidity();
        et_validity.addTextChangedListener(new CreditCardExpiryTextWatcher(et_validity, tv_validity));

        et_validity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (activity != null) {
                    activity.nextClick();
                    return true;
                }

            }
            return false;
        });

        et_validity.setOnBackButtonListener(() -> {
            if(activity!=null)
                activity.onBackPressed();
        });
    }

    public String getCardNumber()
    {
        if(et_number!=null)
            return et_number.getText().toString().trim();

        return null;
    }

    public String getName()
    {
        if(et_name!=null)
            return et_name.getText().toString().trim();

        return null;
    }
    public String getValidity()
    {
        if(et_validity!=null)
            return et_validity.getText().toString().trim();

        return null;
    }



}

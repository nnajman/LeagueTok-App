package com.example.leaguetok.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Listeners {

    public static void addListenerToRequiredEditText(TextInputLayout layout, TextInputEditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(layout, s);
            }
        });

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateEditText(layout, ((EditText)v).getText());
                }
            }
        });
    }

    private static void validateEditText(TextInputLayout layout, Editable s) {
        if (TextUtils.isEmpty(s)) {
            Log.d("TAG", "field is empty");
            layout.setError("Field cannot be empty");
        } else {
            layout.setError(null);
        }
    }
}

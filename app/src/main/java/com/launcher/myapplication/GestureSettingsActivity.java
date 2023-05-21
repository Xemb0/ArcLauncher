package com.launcher.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GestureSettingsActivity extends AppCompatActivity {
    private TextView tvSelectedOption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestures_setting);

        /* gesture options */
        Button doubletap = findViewById(R.id.double_tap_option);
        doubletap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gesturepopup();
            }
        });
    }

    private  void Gesturepopup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.radio_buttons_popup, null);
        builder.setView(dialogView);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radio_options);
        Button btnCancel = dialogView.findViewById(R.id.btn_cencle);

        AlertDialog dialog = builder.create();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle the selection change
                RadioButton selectedRadioButton = dialogView.findViewById(checkedId);
                String selectedOption = selectedRadioButton.getText().toString();
                tvSelectedOption.setText(selectedOption);
                tvSelectedOption.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}

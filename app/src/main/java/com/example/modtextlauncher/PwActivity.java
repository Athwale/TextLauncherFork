package com.example.modtextlauncher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class PwActivity extends android.app.Activity implements CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pw_activity);

        // todo kill on sleep.
        // todo kill on home button

        ToggleButton tg1 = findViewById(R.id.toggleButton1);
        tg1.setOnCheckedChangeListener(this);
        ToggleButton tg2 = findViewById(R.id.toggleButton2);
        tg2.setOnCheckedChangeListener(this);
        ToggleButton tg3 = findViewById(R.id.toggleButton3);
        tg3.setOnCheckedChangeListener(this);
        ToggleButton tg4 = findViewById(R.id.toggleButton4);
        tg4.setOnCheckedChangeListener(this);
        ToggleButton tg5 = findViewById(R.id.toggleButton5);
        tg5.setOnCheckedChangeListener(this);
        ToggleButton tg6 = findViewById(R.id.toggleButton6);
        tg6.setOnCheckedChangeListener(this);
        ToggleButton tg7 = findViewById(R.id.toggleButton7);
        tg7.setOnCheckedChangeListener(this);
        ToggleButton tg8 = findViewById(R.id.toggleButton8);
        tg8.setOnCheckedChangeListener(this);
        ToggleButton tg9 = findViewById(R.id.toggleButton9);
        tg9.setOnCheckedChangeListener(this);
        ToggleButton tgOk = findViewById(R.id.toggleButtonOk);
        tgOk.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.d("PPPP", "B");

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        // todo back button must be cancel.
        // todo check password.
        // todo gamify lighting and turning buttons off.
        finish();
    }
}
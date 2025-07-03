package com.example.modtextlauncher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class PwActivity extends android.app.Activity implements CompoundButton.OnCheckedChangeListener {

    private ToggleButton tg1 = null;
    private ToggleButton tg2 = null;
    private ToggleButton tg3 = null;
    private ToggleButton tg4 = null;
    private ToggleButton tg5 = null;
    private ToggleButton tg6 = null;
    private ToggleButton tg7 = null;
    private ToggleButton tg8 = null;
    private ToggleButton tg9 = null;
    private ToggleButton tgOk = null;
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pw_activity);

        // todo kill on sleep.
        // todo kill on home button

        this.tg1 = findViewById(R.id.toggleButton1);
        this.tg1.setOnCheckedChangeListener(this);
        this.tg2 = findViewById(R.id.toggleButton2);
        this.tg2.setOnCheckedChangeListener(this);
        this.tg3 = findViewById(R.id.toggleButton3);
        this.tg3.setOnCheckedChangeListener(this);
        this.tg4 = findViewById(R.id.toggleButton4);
        this.tg4.setOnCheckedChangeListener(this);
        this.tg5 = findViewById(R.id.toggleButton5);
        this.tg5.setOnCheckedChangeListener(this);
        this.tg6 = findViewById(R.id.toggleButton6);
        this.tg6.setOnCheckedChangeListener(this);
        this.tg7 = findViewById(R.id.toggleButton7);
        this.tg7.setOnCheckedChangeListener(this);
        this.tg8 = findViewById(R.id.toggleButton8);
        this.tg8.setOnCheckedChangeListener(this);
        this.tg9 = findViewById(R.id.toggleButton9);
        this.tg9.setOnCheckedChangeListener(this);
        this.tgOk = findViewById(R.id.toggleButtonOk);
        this.tgOk.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean b) {
        String name = getResources().getResourceEntryName(button.getId());

        switch(name) {
            case "toggleButton1":
                construct_password(1);
                this.tg2.setChecked(true);
                this.tg4.setChecked(true);
                this.tg3.setChecked(!this.tg3.isChecked());
                break;
            case "toggleButton2":
                construct_password(2);
                this.tg1.setChecked(!this.tg1.isChecked());
                this.tg4.setChecked(false);
                this.tg5.setChecked(!this.tg5.isChecked());
                break;
            case "toggleButton3":
                construct_password(3);
                this.tg1.setChecked(!this.tg1.isChecked());
                this.tg5.setChecked(false);
                this.tg6.setChecked(!this.tg5.isChecked());
                break;
            case "toggleButton4":
                construct_password(4);
                this.tg1.setChecked(true);
                this.tg5.setChecked(!this.tg5.isChecked());
                this.tg7.setChecked(true);
                break;
            case "toggleButton5":
                construct_password(5);
                this.tg2.setChecked(!this.tg2.isChecked());
                this.tg4.setChecked(!this.tg4.isChecked());
                this.tg6.setChecked(!this.tg6.isChecked());
                this.tg8.setChecked(!this.tg8.isChecked());
                break;
            case "toggleButton6":
                construct_password(6);
                this.tg3.setChecked(!this.tg3.isChecked());
                this.tg5.setChecked(false);
                this.tg8.setChecked(true);
                this.tg9.setChecked(!this.tg9.isChecked());
                break;
            case "toggleButton7":
                construct_password(7);
                this.tg5.setChecked(!this.tg5.isChecked());
                this.tg4.setChecked(true);
                this.tg8.setChecked(!this.tg8.isChecked());
                break;
            case "toggleButton8":
                construct_password(8);
                this.tg9.setChecked(true);
                this.tg6.setChecked(!this.tg6.isChecked());
                this.tg6.setChecked(!this.tg6.isChecked());
                break;
            case "toggleButton9":
                construct_password(9);
                this.tg6.setChecked(!this.tg6.isChecked());
                this.tg5.setChecked(!this.tg5.isChecked());
                this.tg8.setChecked(!this.tg8.isChecked());
                break;
            case "toggleButtonOk":
                check_password();
                break;
            default:
                // This should actually never run.
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;
        }
    }

    private void construct_password(int part) {
        this.result += String.valueOf(part);
        if (this.result.length() > 800) {
            this.result = String.valueOf(part);
        }
    }

    private void check_password() {
        if (this.result.length() < 100 || this.result.length() > 800) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }

        // TODO have hash and parts in assets in git ignored files.
        Log.d("PPPP", "Check: " + this.result);
    }
}
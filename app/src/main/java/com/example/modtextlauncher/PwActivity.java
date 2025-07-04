package com.example.modtextlauncher;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


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
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pw_activity);

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
        ToggleButton tgOk = findViewById(R.id.toggleButtonOk);
        tgOk.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.fail();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // Hide in multitasking view.
        super.onWindowFocusChanged(hasFocus);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean b) {
        String name = getResources().getResourceEntryName(button.getId());

        switch(name) {
            case "toggleButton1":
                collect_input(1);
                this.tg2.setChecked(true);
                this.tg4.setChecked(true);
                this.tg3.setChecked(!this.tg3.isChecked());
                break;
            case "toggleButton2":
                collect_input(2);
                this.tg1.setChecked(!this.tg1.isChecked());
                this.tg4.setChecked(false);
                this.tg5.setChecked(!this.tg5.isChecked());
                break;
            case "toggleButton3":
                collect_input(3);
                this.tg1.setChecked(!this.tg1.isChecked());
                this.tg5.setChecked(false);
                this.tg6.setChecked(!this.tg5.isChecked());
                break;
            case "toggleButton4":
                collect_input(4);
                this.tg1.setChecked(true);
                this.tg5.setChecked(!this.tg5.isChecked());
                this.tg7.setChecked(true);
                break;
            case "toggleButton5":
                collect_input(5);
                this.tg2.setChecked(!this.tg2.isChecked());
                this.tg4.setChecked(!this.tg4.isChecked());
                this.tg6.setChecked(!this.tg6.isChecked());
                this.tg8.setChecked(!this.tg8.isChecked());
                break;
            case "toggleButton6":
                collect_input(6);
                this.tg3.setChecked(!this.tg3.isChecked());
                this.tg5.setChecked(false);
                this.tg8.setChecked(true);
                this.tg9.setChecked(!this.tg9.isChecked());
                break;
            case "toggleButton7":
                collect_input(7);
                this.tg5.setChecked(!this.tg5.isChecked());
                this.tg4.setChecked(true);
                this.tg8.setChecked(!this.tg8.isChecked());
                break;
            case "toggleButton8":
                collect_input(8);
                this.tg9.setChecked(true);
                this.tg6.setChecked(!this.tg6.isChecked());
                this.tg6.setChecked(!this.tg6.isChecked());
                break;
            case "toggleButton9":
                collect_input(9);
                this.tg6.setChecked(!this.tg6.isChecked());
                this.tg5.setChecked(!this.tg5.isChecked());
                this.tg8.setChecked(!this.tg8.isChecked());
                break;
            case "toggleButtonOk":
                check_access();
                break;
            default:
                // This should actually never run.
                this.fail();
                break;
        }
    }

    private void collect_input(int part) {
        this.result += String.valueOf(part);
        if (this.result.length() > 800) {
            this.result = String.valueOf(part);
        }
    }

    private void fail() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void success() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private String getHash(String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            this.fail();
        }
        digest.reset();
        byte[] hash = digest.digest(input.getBytes());

        return String.format("%0" + (hash.length*2) + "X", new BigInteger(1, hash));
    }

    private void check_access() {
        if (this.result.length() < 100 || this.result.length() > 800) {
            this.fail();
        }
        BufferedReader reader;
        Map<String, String> pair_map = new HashMap<>();

        try {
            final InputStream file = getAssets().open("pwfile.ptx");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split(":");
                pair_map.put(split[0], split[1]);
                line = reader.readLine();
            }
            file.close();
        } catch(Exception e) {
            this.fail();
        }

        StringBuilder translated = new StringBuilder();
        for (char c : this.result.toCharArray()) {
            translated.append(pair_map.get(String.valueOf(c)));
        }

        try {
            String hash = this.getHash(translated.toString());
            final InputStream file = getAssets().open("hash.ptx");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            file.close();

            if (hash.equals(line)) {
                this.success();
            } else {
                this.fail();
            }
        } catch(Exception e) {
            this.fail();
        }
    }
}
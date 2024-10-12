package com.example.modtextlauncher;

import android.content.Intent;

public final class LauncherActivity extends android.app.Activity {
    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(this, Activity.class));
        finish();
    }
}

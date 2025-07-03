package com.example.modtextlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.ACTION_PACKAGE_ADDED;
import static android.content.Intent.ACTION_PACKAGE_REMOVED;
import static android.content.Intent.ACTION_PACKAGE_REPLACED;
import static android.content.Intent.CATEGORY_LAUNCHER;


public final class Activity extends android.app.Activity implements
        Comparator<Model>,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener {

    private final Adapter adapter = new Adapter();
    private BroadcastReceiver broadcastReceiver;
    private static final String PW_PREF_NAME = "PasswdSetRunOnce";
    private static final int A_CODE = 29836;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        update();

        SharedPreferences prefs = getSharedPreferences(PW_PREF_NAME, MODE_PRIVATE);
        boolean is_pw_set = prefs.getBoolean("pwset", false);

        if (!is_pw_set) {
            try {
                Intent intent = new Intent();
                // Start by setting the password once.
                intent.setClassName("com.android.settings",
                        "com.android.settings.password.ScreenLockSuggestionActivity");
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            // Save the fact that password was set.
            SharedPreferences.Editor editor = getSharedPreferences(PW_PREF_NAME, MODE_PRIVATE).edit();
            editor.putBoolean("pwset", true);
            editor.apply();
        }

        ListView list = findViewById(R.id.list);

        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PACKAGE_ADDED);
        intentFilter.addAction(ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                update();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public int compare(Model lhs, Model rhs) {
        return lhs.label.compareToIgnoreCase(rhs.label);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        // In the case it is not the secret, reset the counter.
        this.counter = 0;
        String package_name = adapter.getItem(index).packageName;
        try {
            startActivity(getPackageManager().getLaunchIntentForPackage(package_name));
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == A_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                Log.d("PPPP", "OK");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("PPPP", "CANCEL");
            }
        }
    }

    public void pw_dialog() {
        Intent intent = new Intent(this, PwActivity.class);
        startActivityForResult(intent, A_CODE);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long id) {
        String package_name = adapter.getItem(index).packageName;
        try {
            if (Objects.equals(package_name, "com.android.documentsui")) {
                this.counter++;
                if (this.counter >= 2) {
                    this.pw_dialog();
                    this.counter = 0;
                    return true;
                    }
                }

            // In the case it is not the secret, reset the counter.
            this.counter = 0;
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", package_name, null));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, package_name, Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private void update() {
        //Log.d("TLINFO", resolveInfo.activityInfo.packageName);
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(ACTION_MAIN, null);
        intent.addCategory(CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(intent, 0);
        ArrayList<Model> models = new ArrayList<>();
        long id = 0;
        for (ResolveInfo resolveInfo : availableActivities) {
            if ("com.example.modtextlauncher".equalsIgnoreCase(resolveInfo.activityInfo.packageName)) {
                continue;
            }

            if ("com.android.settings".equalsIgnoreCase(resolveInfo.activityInfo.packageName)) {
                models.add(new Model(++id, "Settings",
                        resolveInfo.activityInfo.packageName
                ));
                continue;
            }

            models.add(new Model(++id, resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.activityInfo.packageName
            ));
        }
        models.sort(this);
        adapter.update(models);
    }
}

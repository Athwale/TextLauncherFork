package com.example.modtextlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    private boolean checkSystemWritePermission() {
        boolean retVal = Settings.System.canWrite(this);
        if (retVal) {
            return retVal;
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            startActivity(intent);
        }
        return retVal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        update();

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
        try {
            if (adapter.getItem(index).packageName.equalsIgnoreCase("rotationSwitch")) {
                if (this.checkSystemWritePermission()) {
                    Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                    int orientation = this.getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Settings.System.putInt(getContentResolver(), Settings.System.USER_ROTATION, Surface.ROTATION_90);
                    } else {
                        Settings.System.putInt(getContentResolver(), Settings.System.USER_ROTATION, Surface.ROTATION_0);
                    }
                }
            } else if (adapter.getItem(index).packageName.equalsIgnoreCase("wetaoSettings")) {
                Intent intent = new Intent();
                intent.setClassName("com.android.settings",
                        "com.android.settings.WetaoSettings");
                startActivity(intent);
            } else {
                startActivity(getPackageManager().getLaunchIntentForPackage(adapter.getItem(index).packageName));
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long id) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", adapter.getItem(index).packageName, null));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, adapter.getItem(index).packageName, Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private void update() {
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(ACTION_MAIN, null);
        intent.addCategory(CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(intent, 0);
        ArrayList<Model> models = new ArrayList<>();
        long id = 0;
        for (ResolveInfo resolveInfo : availableActivities) {
            if ("com.example.modtextlauncher".equalsIgnoreCase(resolveInfo.activityInfo.packageName))
                continue;
            models.add(new Model(++id, resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.activityInfo.packageName
            ));
        }

        // Add settings.
        models.add(new Model(++id, "Settings", "wetaoSettings"));

        // Add screen rotation button.
        models.add(new Model(++id, "Switch orientation", "rotationSwitch"));

        models.sort(this);
        adapter.update(models);
    }
}

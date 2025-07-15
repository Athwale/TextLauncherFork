package com.example.modtextlauncher;

import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.CATEGORY_LAUNCHER;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public final class SecondMenu extends android.app.Activity implements
        Comparator<Model>,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener {

    private final Adapter adapter = new Adapter();
    private final ArrayList<String> ignore_list=new ArrayList<>();
    private String start_code = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read activity start code.
        BufferedReader reader;
        try {
            final InputStream file = getAssets().open("start_code.ptx");
            reader = new BufferedReader(new InputStreamReader(file));
            this.start_code = reader.readLine();
            file.close();
        } catch (Exception e) {
            finish();
        }

        String code = getIntent().getStringExtra("start_code");
        if (!Objects.equals(code, this.start_code)) {
            finish();
        }

        setContentView(R.layout.activity);

        try {
            final InputStream file = getAssets().open("ignored.ptx");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while (line != null) {
                this.ignore_list.add(line);
                line = reader.readLine();
            }
            file.close();
        } catch(Exception e) {
            Toast.makeText(this, "Reading ignore list failed",
                    Toast.LENGTH_LONG).show();
        }

        update();
        ListView list = findViewById(R.id.list);

        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
    }

    private void end() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.end();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // Hide in multitasking view.
        super.onWindowFocusChanged(hasFocus);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public int compare(Model lhs, Model rhs) {
        return lhs.label.compareToIgnoreCase(rhs.label);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        if (Objects.equals(adapter.getItem(index).packageName, "Empty")) {
            Toast.makeText(this, "List is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("name", adapter.getItem(index).packageName);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long id) {
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

            if (this.ignore_list.contains(resolveInfo.activityInfo.packageName)) {
                models.add(new Model(++id, resolveInfo.loadLabel(packageManager).toString(),
                        resolveInfo.activityInfo.packageName
                ));
            }
        }
        models.sort(this);
        if (models.isEmpty()) {
            models.add(new Model(++id, "Empty", "Empty"));
        }
        adapter.update(models);
    }
}

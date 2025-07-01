package com.example.modtextlauncher;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;


public final class SecondMenu extends android.app.Activity implements
        Comparator<Model>,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener {

    // TODO require password dialog.

    private final Adapter adapter = new Adapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

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

    @Override
    public int compare(Model lhs, Model rhs) {
        return lhs.label.compareToIgnoreCase(rhs.label);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        try {
            startActivity(getPackageManager().getLaunchIntentForPackage(adapter.getItem(index).packageName));
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long id) {
        return true;
    }

    private void update() {
        ArrayList<Model> models = new ArrayList<>();
        long id = 0;
        models.add(new Model(++id, "Settings", "test"));

        models.sort(this);
        adapter.update(models);
    }
}

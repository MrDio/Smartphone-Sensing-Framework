package edu.example.ssf.mma.userInterface;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import edu.example.ssf.mma.data.Lap;
import edu.example.ssf.mma.data.LapListAdapter;

public class LapListActivity extends ListActivity {

    private ArrayList<Lap> data;
    private LapListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiateData();
        adapter = new LapListAdapter(this, data);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    private void initiateData(){
        data = new ArrayList();

        Lap lap = new Lap(0, 14.231, 0.2);
        data.add(lap);

        lap = new Lap(1, 12.769, 0.6);
        data.add(lap);

        lap = new Lap(2, 11.711, 0.84);
        data.add(lap);

        lap = new Lap(3, 10.899, 1);
        data.add(lap);

        lap = new Lap(4, 11.201, 0.92);
        data.add(lap);

        lap = new Lap(5, 16.631, 0.1);
        data.add(lap);
    }
}

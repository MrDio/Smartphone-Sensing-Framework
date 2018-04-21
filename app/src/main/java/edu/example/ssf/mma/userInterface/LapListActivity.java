package edu.example.ssf.mma.userInterface;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import edu.example.ssf.mma.data.CsvFileReader;
import edu.example.ssf.mma.data.Lap;
import edu.example.ssf.mma.data.LapListAdapter;
import edu.example.ssf.mma.data.PerformanceAnalyzer;
import edu.example.ssf.mma.data.Section;
import edu.example.ssf.mma.data.SectionIdentifier;

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
        data = CsvFileReader.readFile();
        CsvFileReader.closeFile();
        SectionIdentifier.initialize(data);
        SectionIdentifier.smoothCurves();
        SectionIdentifier.applySavitzkyGolay();
        data = SectionIdentifier.createSections();
        PerformanceAnalyzer.initialize(data);
        PerformanceAnalyzer.calculatePerformanceIndicator();

    }
}

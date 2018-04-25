package edu.example.ssf.mma.userInterface;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.example.ssf.mma.R;
import edu.example.ssf.mma.data.CsvFileReader;
import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.data.DataModification;
import edu.example.ssf.mma.model.Lap;
import edu.example.ssf.mma.data.LapListAdapter;
import edu.example.ssf.mma.dataAnalyzation.PerformanceAnalyzer;
import edu.example.ssf.mma.dataAnalyzation.SectionIdentifier;

public class LapListActivity extends ListActivity {

    private ArrayList<Lap> data;
    private LapListAdapter adapter;
    public static ProgressDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        data = new ArrayList<>();
        adapter = new LapListAdapter(this, data);
        new DataAnalyzerTask().execute();

        this.setListAdapter(adapter);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        if(data.get(position).isValid() && !data.get(position).isFastestLap()){
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("sections", data.get(position).getSections());
            startActivity(intent);
        }

    }

    private void initiateData(){
        data = CsvFileReader.readFile();
        CsvFileReader.closeFile();

        if(data.size() < 2){
            Toast.makeText(this, "Sorry, not enough data to generate a report", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            DataModification.smoothCurves(data);
            DataModification.applySavitzkyGolay(data);
            PerformanceAnalyzer.fitThreshold(data);
            PerformanceAnalyzer.calculatePerformanceIndicator(data);
        }




    }

    public class DataAnalyzerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            initiateData();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            adapter.refreshData(data);
        }
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LapListActivity.this, "Analyzing Data", "Please wait");
        }
    }
}

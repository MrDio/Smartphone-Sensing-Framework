package edu.example.ssf.mma.userInterface;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import edu.example.ssf.mma.data.CsvFileReader;
import edu.example.ssf.mma.data.DataModification;
import edu.example.ssf.mma.model.Lap;
import edu.example.ssf.mma.data.LapListAdapter;
import edu.example.ssf.mma.dataAnalyzation.PerformanceAnalyzer;
import edu.example.ssf.mma.dataAnalyzation.SectionIdentifier;

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

        DataModification.smoothCurves(data);
        DataModification.applySavitzkyGolay(data);

        data = SectionIdentifier.createSections(data);
        data = SectionIdentifier.classifySections(data);
        data = SectionIdentifier.invalidateLaps(data);

        System.out.println("<<<<AFTER MERGING>>>>>");
        for(Lap lap : data){
            System.out.println("Lap "+lap.getNumber()+" Sections: "+lap.getSections().size());
            for(int i = 0; i < lap.getSections().size(); i++){
                System.out.println("Section: Start: "+lap.getSections().get(i).getStart().getTimeStamp()+" | End: "+lap.getSections().get(i).getEnd().getTimeStamp()+" | Type: "+lap.getSections().get(i).getType());
            }
        }

        PerformanceAnalyzer.calculatePerformanceIndicator(data);

    }
}

package edu.example.ssf.mma.userInterface;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import edu.example.ssf.mma.model.Lap;
import edu.example.ssf.mma.model.Section;
import edu.example.ssf.mma.data.SectionListAdapter;
import edu.example.ssf.mma.model.SectionType;

public class ReportActivity extends ListActivity {

    private ArrayList<Section> data;
    private SectionListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initiateData();
        adapter = new SectionListAdapter(this, data);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {

    }

    private void initiateData(){
        data = (ArrayList<Section>)getIntent().getSerializableExtra("sections");
    }
}

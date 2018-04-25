package edu.example.ssf.mma.userInterface;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import edu.example.ssf.mma.config.ConfigApp;
import edu.example.ssf.mma.data.SessionListAdapter;

public class SessionListActivity extends ListActivity {

    private ArrayList<String> data;
    private SessionListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        data = new ArrayList<>();
        loadSessions();

        adapter = new SessionListAdapter(this, data);
        this.setListAdapter(adapter);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){

            ConfigApp.currentLapFile = "S_" + data.get(position) + ".csv";
            Intent intent = new Intent(this, LapListActivity.class);
            startActivity(intent);
    }

    private void loadSessions(){
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ ConfigApp.targetStorageDir);
        File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if(s.startsWith("S_") && s.endsWith(".csv")){
                    return true;
                }
                else{
                    return false;
                }

            }
        });
        for (File file : files) {
            String fileNameWithoutExtension = file.getName().substring(2, file.getName().indexOf('.'));
            data.add(fileNameWithoutExtension);
        }
    }
}

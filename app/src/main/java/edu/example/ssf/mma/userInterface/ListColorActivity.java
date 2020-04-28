package edu.example.ssf.mma.userInterface;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.example.ssf.mma.R;

public class ListColorActivity extends ListActivity {

    private String path = "config.txt";
    private static final int READ_REQUEST_CODE = 42;
    List<String[]> colorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_vision_list_colors);

        FileReader fis = null;
        List<String[]> actLine = new ArrayList<>();
        try {
            fis = new FileReader(Environment.getExternalStorageDirectory() + "/ColorData/" + path);
            BufferedReader br = new BufferedReader(fis);
            String text;

            while ((text = br.readLine()) != null) {
                actLine.add(text.split(","));
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        List names = new ArrayList();
        for (String[] current : actLine) {
            names.add(current[0]);
        }
        colorList = actLine;

        // Put the data into the listView
        // use arrayAdapter to insert colors
        ArrayAdapter adapter = new ArrayAdapter(ListColorActivity.this,
                android.R.layout.simple_list_item_2, android.R.id.text1, names) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text2.setText("Color Example");
                String[] rgb = colorList.get(position);

                int r = (int) Math.round(Double.parseDouble(rgb[2]));
                int g = (int) Math.round(Double.parseDouble(rgb[3]));
                int b = (int) Math.round(Double.parseDouble(rgb[4]));
                String hex = String.format("#%02x%02x%02x", r, g, b);
                text2.setBackgroundColor(Color.parseColor(hex));
                text2.setTextColor(Color.parseColor(hex));
                return view;
            }

            ;
        };

        setListAdapter(adapter);
        if (colorList.size() == 0) {
            String failedMessage = "Color List is empty";
            Toast.makeText(this, failedMessage, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String[] rgb = colorList.get(position);
        // back to machineVision with values
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", rgb);
        setResult(ListColorActivity.RESULT_OK, returnIntent);
        finish();
    }
}

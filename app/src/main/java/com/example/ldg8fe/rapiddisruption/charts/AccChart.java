
package com.example.ldg8fe.rapiddisruption.charts;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;

import com.example.ldg8fe.rapiddisruption.R;
import com.example.ldg8fe.rapiddisruption.data.CurrentTickData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by LDG8FE on 11.01.2018.
 */

import android.app.Activity;


public class AccChart extends Activity {

    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_chart);
        // we get graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setScrollable(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 100; i++) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
    }

    // add random data to graph
    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, CurrentTickData.accVecA), true, 1000);
    }


}

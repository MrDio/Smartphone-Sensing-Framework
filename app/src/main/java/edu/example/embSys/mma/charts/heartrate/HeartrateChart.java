/*
 *  This file is part of the Multimodal Mobility Analyser(MMA), based
 *  on the Smartphone Sensing Framework (SSF)

    MMA (also SSF) is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MMA (also SSF) is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU v3 General Public License for more details.

    Released under GNU v3
    
    You should have received a copy of the GNU General Public License
    along with MMA.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.example.embSys.mma.charts.heartrate;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import edu.example.embSys.mma.charts.IChart;
import edu.example.embSys.mma.charts.converter.ChartDataConverter;
import edu.example.embSys.mma.charts.samples.AbstractDemoChart;
import edu.example.embSys.mma.data.CurrentTickData;

// TODO: Auto-generated Javadoc
/**
 * This class contains the Gyroscope Chart, which is a derived implementation from AverageTemperatureChart @see charts/samples .
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */
public class HeartrateChart extends AbstractDemoChart implements IChart{
  
	/** The name of the lines. */
    String[] titles = new String[] {"Rate"};
    
    /** Different color for each line. */
    int[] colors = new int[] {Color.RED};
    
    /** Different pointstyles for each line. */
    PointStyle[] styles = new PointStyle[] {PointStyle.CIRCLE};
    
    /** This is the x-axis-data-pool for visualisation. */
    List<double[]> x = new ArrayList<double[]>();
    
    /** These buffers are used to collect the data during several execution calls. */
    List<Double> x_buff = new ArrayList<Double>();
    
    /** The acc rate_buff. */
    List<Double> rate_buffer = new ArrayList<Double>();

    /** This is the y-axis-data-pool for visualisation. */
    List<double[]> values = new ArrayList<double[]>();
    
    /** The init. */
    private boolean init=true;

	
	/**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "Heartrate Chart";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "This chart visualises the heartrate from the user";
  }

  /**
   * Executes the chart intent. With calling this function, the chart is visualised
   * 
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context) {
	  
	if (init) {
		initChart();
		init=false;
	}  
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "Heartrate of User", "Tick [1]", "Rate [bpm]", 0.5, 20, -5, 190, 
        Color.LTGRAY, Color.LTGRAY);
    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.RIGHT);
    renderer.setZoomButtonsVisible(true);

    XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
    Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer,
        this.getName());
    return intent;
  }

	/**
	 * Initialisation of the chart-data.
	 */
	@Override
	public void initChart() {
	  /**
	   * Four elements are added to the list because we need four data-pools for the lines 
	   */
	   x.add(new double[] { 1 });
	  
	   /**
	    * Four elements are added to the list because we need four data-pools for the lines 
	   */
	   values.add(new double[] {1});

	}
	
	/**
	 * This method executes an update of the chart.
	 */
	@Override
	public void updateChartData() {
		/**
		 * Check if statemachine is runnig. if not, put some sample data into the chart
		 */
		if (init) {
			initChart();
			init=false;
		}
		
		/**
		 * Fetch and convert the data from current tick data pool CurrentTickData
		 */
		x_buff.add(Double.valueOf(CurrentTickData.curTick));
		rate_buffer.add(Double.valueOf(CurrentTickData.rotationX));


	    
	    /**
	     * The added data pools are refreshed in this section
	     */
	    // Update x-axis
	    x.set(0, ChartDataConverter.listToArrayDouble(x_buff));


	    /**
	     * The added data pools are refreshed in this section
	     */
	    // Update y-axis
	    values.set(0,(ChartDataConverter.listToArrayDouble(rate_buffer)));

	 }

}

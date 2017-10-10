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

package edu.example.embSys.mma.charts.acc;

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
 * This class contains the Acceleration Chart, which is a derived implementation from AverageTemperatureChart @see charts/samples .
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */
public class AccChart extends AbstractDemoChart implements IChart{
  
	/** The name of the lines. */
    String[] titles = new String[] { "AccX", "AccY", "AccZ", "Acc Vector a"};
    
    /** Different color for each line. */
    int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW};
    
    /** Different pointstyles for each line. */
    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND, PointStyle.TRIANGLE, PointStyle.SQUARE};
    
    /** This is the x-axis-data-pool for visualisation. */
    List<double[]> x = new ArrayList<double[]>();
    
    /** These buffers are used to collect the data during several execution calls. */
    List<Double> x_buff = new ArrayList<Double>();
    
    /** The acc x_buff. */
    List<Double> accX_buff = new ArrayList<Double>();
    
    /** The acc y_buff. */
    List<Double> accY_buff = new ArrayList<Double>();
    
    /** The acc z_buff. */
    List<Double> accZ_buff = new ArrayList<Double>();
    
    /** The acc vec a_buff. */
    List<Double> accVecA_buff = new ArrayList<Double>();
    
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
    return "Acceleration Chart";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "This chart visualises the accelerations in directions X, Y and Z";
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
    setChartSettings(renderer, "Acceleration in X, Y, Z", "Tick [1]", "Acceleration [m/s^2]", 0.5, 12.5, -10, 40,
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
	   x.add(new double[] { 1 });
	   x.add(new double[] { 1 });
	   x.add(new double[] { 1 });
	  
	   /**
	    * Four elements are added to the list because we need four data-pools for the lines 
	   */
	   values.add(new double[] {1});
	   values.add(new double[] {1});
	   values.add(new double[] {1});
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
		accX_buff.add(Double.valueOf(CurrentTickData.accX));
	    accY_buff.add(Double.valueOf(CurrentTickData.accY));
	    accZ_buff.add(Double.valueOf(CurrentTickData.accZ));
	    accVecA_buff.add(Double.valueOf(CurrentTickData.accVecA));
	    
	    /**
	     * The added data pools are refreshed in this section
	     */
	    // Update x-axis
	    x.set(0, ChartDataConverter.listToArrayDouble(x_buff));
	    x.set(1, ChartDataConverter.listToArrayDouble(x_buff));
	    x.set(2, ChartDataConverter.listToArrayDouble(x_buff));
	    x.set(3, ChartDataConverter.listToArrayDouble(x_buff));

	    /**
	     * The added data pools are refreshed in this section
	     */
	    // Update y-axis
	    values.set(0,(ChartDataConverter.listToArrayDouble(accX_buff)));
	    values.set(1,(ChartDataConverter.listToArrayDouble(accY_buff)));
	    values.set(2,(ChartDataConverter.listToArrayDouble(accZ_buff)));
	    values.set(3,(ChartDataConverter.listToArrayDouble(accVecA_buff)));
	 }

}

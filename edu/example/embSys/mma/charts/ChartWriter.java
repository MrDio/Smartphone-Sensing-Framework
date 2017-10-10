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
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MMA.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.example.embSys.mma.charts;

/**
 * This class contains and handles the charts. Further charts can be found in the folder charts/samples
 * 
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

import edu.example.embSys.mma.charts.GPSMap.GPSMap;
import edu.example.embSys.mma.charts.acc.AccChart;
import edu.example.embSys.mma.charts.gyro.GyroChart;
import edu.example.embSys.mma.charts.heartrate.HeartrateChart;

// TODO: Auto-generated Javadoc
/**
 * The Class ChartWriter.
 */
public class ChartWriter {

	/**
	 * Implemented chart classes can be instantiated here. The array mCharts is visualised as list
	 * in the draw state on the app UI. 
	 */
	private IChart[] charts = new IChart[] { 
			
			  new AccChart(),
			  new GyroChart(),
			  new HeartrateChart(),
			  new GPSMap()
/*		      new AverageCubicTemperatureChart(),
		      new SalesStackedBarChart(),
		      new SalesBarChart(),
		      new TrigonometricFunctionsChart(),
		      new ScatterChart(),
		      new SalesComparisonChart(),
		      new ProjectStatusChart(),
		      new SalesGrowthChart(),
		      new BudgetPieChart(),
		      new BudgetDoughnutChart(),
		      new ProjectStatusBubbleChart(),
		      new TemperatureChart(),
		      new WeightDialChart(),
		      new SensorValuesChart(),
		      new CombinedTemperatureChart(),
		      new MultipleTemperatureChart() */
			  };


	/**
	 * Getter for charts.
	 *
	 * @return An array of instantiated charts
	 */
	public IChart[] getAllCharts() {
		return this.charts;
	}
	
	/**
	 * This method should call all update methods of the implemented charts
	 * In this version, just the Acceleration and Gyroscope Chart is implemented.
	 */
	public void updateAllCharts(){
		for (int i = 0; i < charts.length; i++) {
			charts[i].updateChartData();
		}
	}
	
	
}

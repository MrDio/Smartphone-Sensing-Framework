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
 * This is the interface to access the chart modules
 * 
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

import edu.example.embSys.mma.charts.samples.IDemoChart;

// TODO: Auto-generated Javadoc
/**
 * The Interface IChart.
 */
public interface IChart extends IDemoChart{

	
	/**
	 * Initialisation of the chart-data.
	 */
	public void initChart();
	
	/**
	 * This method executes an update of the chart.
	 */
	public void updateChartData();
	
}

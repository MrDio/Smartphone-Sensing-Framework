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

package edu.example.embSys.mma.charts.GPSMap;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import edu.example.embSys.mma.charts.IChart;
import edu.example.embSys.mma.charts.samples.AbstractDemoChart;
import edu.example.embSys.mma.data.CurrentTickData;
import edu.example.embSys.mma.userInterface.ggmap.ActivityGGMap;

// TODO: Auto-generated Javadoc
/**
 * This class contains the Google Map View.
 * 
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */
public class GPSMap extends AbstractDemoChart implements IChart {

	/** This ist ++ */
	ArrayList<LatLng> points;

	/** The init. */
	private boolean init = true;

	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "GPS Map";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "This chart visualises GPS-coordinates as a polyline in GoogleMaps";
	}

	private void addLatLng(Double lat, Double lng) {

		this.points.add(new LatLng(lat, lng));
	}

	/**
	 * Executes the chart intent. With calling this function, the chart is
	 * visualised
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context) {

		if (init) {
			initChart();
			init = false;
		}
		Intent intent = new Intent(context, ActivityGGMap.class);
		return intent;
	}

	/**
	 * Initialisation of the Map-Data.
	 */
	@Override
	public void initChart() {
		/**
		 * New Array of points is initialised
		 */
		points = new ArrayList<LatLng>();

	}

	/**
	 * This method executes an update of the chart.
	 */
	@Override
	public void updateChartData() {
		/**
		 * Check if statemachine is runnig. if not, put some sample data into
		 * the chart
		 */
		if (init) {
			initChart();
			init = false;
		}

		LatLng tmp = new LatLng(CurrentTickData.GPSlat, CurrentTickData.GPSlon);
		if (points.size() != 0) {
			// compare the new point with last point from array
			if (!tmp.equals(points.get(points.size() - 1))) {
				addLatLng(CurrentTickData.GPSlat, CurrentTickData.GPSlon);
				/**
				 * Fetch and convert the data from current tick data pool
				 * CurrentTickData
				 */
				if (ActivityGGMap.getCurrentInstance() != null) {
					ActivityGGMap.getCurrentInstance()
							.setPointList(this.points);
				}
			}
		} else {
			addLatLng(CurrentTickData.GPSlat, CurrentTickData.GPSlon);
		}
	}
}

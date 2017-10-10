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
package edu.example.embSys.mma.userInterface.ggmap;
/**
 * Activity class, which implements the Google Map View
 * @author Jian Wu and Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.1
 */
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.example.embSys.mma.R;

public class ActivityGGMap extends Activity {

	private GoogleMap ggMap;
	private ArrayList<LatLng> pointList;
	
	private static ActivityGGMap instance;
	
	private TextView tvState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		instance = this;
		
		setContentView(R.layout.activity_google_map);

		ggMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		
		tvState = (TextView) findViewById(R.id.tvStatus);


		updateMap();

	}

	public void setPointList(ArrayList<LatLng> pl) {
		
		this.pointList = pl;
		
		/*
		 * show GPS-Coordinates in the TextView
		 */
		if (pointList.size() != 0) {
			int lastIdx = pointList.size()-1;
			LatLng lastPoint = pointList.get(lastIdx);
			
			tvState.setText(lastPoint.latitude + "; " + lastPoint.longitude);
		}
		
		updateMap();
	}

	private void updateMap() {
		
		if (ggMap != null) {
			
			ggMap.clear();

			if (pointList != null) {
				PolylineOptions trace = new PolylineOptions().geodesic(false)
						.width(20).color(0xFF179C7D);
				LatLngBounds.Builder bound = new LatLngBounds.Builder();

				for (LatLng pt : pointList) {
					trace.add(pt);
					bound.include(pt);
				}

				ggMap.addPolyline(trace);
				CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(
						bound.build(), 700, 450, 10);
				ggMap.animateCamera(cu);
			}
		}
		
	}
	
	public static ActivityGGMap getCurrentInstance() {
		return instance;
	}

}

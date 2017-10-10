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
package edu.example.embSys.mma.userInterface.drawState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import edu.example.embSys.mma.R;
import edu.example.embSys.mma.charts.samples.IDemoChart;

// TODO: Auto-generated Javadoc
/**
 * Fragment class, which allows the user to select a saved csv file and send it
 * via E-Mail.
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.1
 */

public class DrawStateUIListFragmentActivitiy extends ListFragment { 

	/** Declaration of the attribute for the String that is used as the TAG, when working with LOGs. String is "CapturedStateUIActivity" */
	private final String TAG = "DrawStateUIActivity";
	
	/** The m charts. */
	private IDemoChart[] mCharts = null;
	
	/**
	 * Instantiates a new draw state ui list fragment.
	 *
	 * @param mCharts the m charts
	 */
	public DrawStateUIListFragmentActivitiy(IDemoChart[] mCharts) {
		this.mCharts=mCharts;
	}
		  
  		/** The m menu text. */
  		private String[] mMenuText;

		  /** The m menu summary. */
  		private String[] mMenuSummary;
		  
		  
		  /**
  		 *  Called when the activity is first created.
  		 *
  		 * @param savedInstanceState the saved instance state
  		 */
		  @Override
		  public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    int length = mCharts.length;
		    mMenuText = new String[length];
		    mMenuSummary = new String[length];
		    
		    for (int i = 0; i < length; i++) {
		      mMenuText[i] = mCharts[i].getName();
		      mMenuSummary[i] = mCharts[i].getDesc();
		    }
		    setListAdapter(new SimpleAdapter(getActivity(), getListValues(), android.R.layout.simple_list_item_2,
		        new String[] { IDemoChart.NAME, IDemoChart.DESC }, new int[] { android.R.id.text1,
		            android.R.id.text2 }));
		  }

		  /**
  		 * Gets the list values.
  		 *
  		 * @return the list values
  		 */
  		private List<Map<String, String>> getListValues() {
		    List<Map<String, String>> values = new ArrayList<Map<String, String>>();
		    int length = mMenuText.length;
		    for (int i = 0; i < length; i++) {
		      Map<String, String> v = new HashMap<String, String>();
		      v.put(IDemoChart.NAME, mMenuText[i]);
		      v.put(IDemoChart.DESC, mMenuSummary[i]);
		      values.add(v);
		    }
		    return values;
		  }

		  /* (non-Javadoc)
  		 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
  		 */
  		@Override
		  public void onListItemClick(ListView l, View v, int position, long id) {
		    super.onListItemClick(l, v, position, id);
		    Intent intent = null;
			Log.i(TAG, "DrawStateUIActivity::onListItemClick position:"+position);
			
			intent = mCharts[position].execute(getActivity());
		    startActivity(intent);
		  }
		  
		  /* (non-Javadoc)
  		 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
  		 */
  		@Override
		  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		    Bundle savedInstanceState) {
		   return inflater.inflate(R.layout.draw_state, container, false);
		  }
}



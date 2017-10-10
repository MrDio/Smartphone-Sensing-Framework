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
package edu.example.embSys.mma.userInterface.capturedState;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import edu.example.embSys.mma.R;

import edu.example.embSys.mma.config.ConfigApp;

// TODO: Auto-generated Javadoc
/**
 * Fragment class, which allows the user to select a saved csv file and send it
 * via E-Mail.
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class CapturedStateUIActivity extends Fragment { 

	/** Declaration of the attribute for the String that is used as the TAG, when working with LOGs. String is "CapturedStateUIActivity" */
	private final String TAG = "CapturedStateUIActivity";
	
	/**  Declaration of the attribute for the view, that is inflated in onCreateView(). */
	private View view;
	
	/**  Declaration of the attribute for the spinner, that is used to display the saved files. */
	private Spinner spinner;
	
	
	/**
	 * Creates the Layout for the the Fragment and sets the Listener and onClick
	 * Method for the Mail and and Refresh Button.
	 * 
	 * @param inflater
	 *            the inflater object is used to inflate any views in the fragment.
	 * @param container
	 *            if not null, this is the parent view that the fragment's UI should be attached to.
	 * @param savedInstanceState
	 *            if not null, the fragment is being relaunched from a previous state.
	 * @return the view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.i(TAG, "View of Capture_Fragment created");

		view = inflater.inflate(R.layout.capture_state, container, false);
		spinner = (Spinner) view.findViewById(R.id.spinner1);
		getFilenames();

		Button tmpMail = (Button) view.findViewById(R.id.Mail);
		Button tmpRefresh = (Button) view.findViewById(R.id.Refresh);

		/**
		 * sets the onClickListener for the Mail Button
		 */
		tmpMail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// click calls handle method for mail button
				handleMailButton();
			}
		});

		/**
		 * sets the OnCLickListener for the Refresh Button
		 */
		tmpRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// click calls handle method for refresh button
				getFilenames();
			}
		});

		return view;
	}

	
	
	/**
	 * reads the saved Filenames and saves them in a ArrayList and attaches them
	 * to the spinner.
	 * 
	 * @return the filenames
	 */
	private void getFilenames() {
		// TODO Auto-generated method stub

		File files = new File(Environment.getExternalStorageDirectory(),
				ConfigApp.targetStorageDir);
		if (files.isDirectory()) {
			String[] filenames = files.list();
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < filenames.length; i++) {
				Log.d("Filename", filenames[i]);
				list.add(filenames[i]);
			}
			ArrayAdapter<String> filenameAdapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_checked,
					list);
			spinner.setAdapter(filenameAdapter);

		}
	}

	
	/**
	 * attaches the csv file selected by the user to the E-Mail, and handles the Mail-Intent.
	 */
	private void handleMailButton() {
		String selectedFile = spinner.getSelectedItem().toString();
		String FILE = Environment.getExternalStorageDirectory() + File.separator + ConfigApp.targetStorageDir;
		
		Log.i(TAG,"Selected File is " + selectedFile);
		Log.d(TAG,"Stored in " + FILE + "/");
		
		Log.i(TAG, "mailButton clicked");
		
		String[] recipients = new String[]{ConfigApp.emailAddress};
		Intent emailIntent = new Intent (Intent.ACTION_SEND);
		emailIntent.setType("application/csv");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
		String temp_path = FILE + "/" + selectedFile;
		File F = new File(temp_path);
		Log.d(TAG,"Path is " + temp_path);
		Uri u1 = Uri.fromFile(F);
		emailIntent.putExtra(Intent.EXTRA_STREAM, u1);
		
        startActivity(Intent.createChooser(emailIntent, "Send File via"));
        try {
			this.finalize();  
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}//onClick
}

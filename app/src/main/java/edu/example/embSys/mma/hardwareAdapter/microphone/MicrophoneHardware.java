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
package edu.example.embSys.mma.hardwareAdapter.microphone;

import java.io.File;
import java.io.IOException;

import edu.example.embSys.mma.hardwareAdapter.IMicrophone;

import android.media.MediaRecorder;
import android.os.Environment;

// TODO: Auto-generated Javadoc
/**
 * Initialising the microphone of the Smartphone and get the data and record the audio .
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class MicrophoneHardware implements IMicrophone{
	
	/** Reference to the media recorder. */
	private MediaRecorder mediaRecorder = null;
	
	/** setting the variable for responisble for recording to false. */
	private Boolean isRecording = false;
	
	/** REference to the audiofile. */ 
	private File audiofile = null;
	
	
	/**
	 * 	create the new media recorder in order to record the audio.
	 */
	public MicrophoneHardware() {
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		initMicrophone();
	}
	
	/**
	 * get the max Amplitude.
	 *
	 * @return 0 if not recording
	 * maximal amplitude if recording in integer type
	 */
	@Override
	public Integer getMaxAmplitude() {
		if (!isRecording) {
			return 0;
		}
		return this.mediaRecorder.getMaxAmplitude();
	}

	/**
	 * Instantiate the microphone and create a new sound-file,  in order to record the audio into a file and save it on the smartphone.
	 */
	@Override
	public void initMicrophone() {
		if (audiofile == null) 
		{
		   File sampleDir = Environment.getExternalStorageDirectory();
		   try {
			 audiofile = File.createTempFile("ibm", ".3gp", sampleDir);
			} catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
			}
		}
		   mediaRecorder.setOutputFile(audiofile.getAbsolutePath());
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaRecorder.start();
		this.isRecording=true;
	}
}

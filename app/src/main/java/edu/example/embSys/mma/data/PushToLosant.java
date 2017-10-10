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

package edu.example.embSys.mma.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import android.util.Log;

// See https://github.com/Losant/example-android-phone-tracker/blob/master/app/src/main/java/io/getstructure/phonetracker/LocationService.java
// See https://docs.losant.com/rest-api/device/


public class PushToLosant {

	public void pushtoLosant(){
	    try {
            // The API endpoint - /application/:appId/device/:deviceId
        //    URL url = new URL(
        //        "https://api.getstructure.io/" +
        //        "applications/56919b1a9d206d0100c54152/" +
        //        "devices/56919b3c9d206d0100c54153/state");

            URL url = new URL("https://api.losant.com/applications/58beb82e2bb36b0001a21b4d/devices/58d1a3b23cc88e00018ee10a/state");
            
            // Setup the Https request.
            HttpsURLConnection urlConnection =
                (HttpsURLConnection) url.openConnection();

            JSONObject auth = new JSONObject();
            auth.put("deviceId", "Your_device_ID");
            auth.put("key", "Your_key");
            auth.put("secret", "Your_secret_key");
            
            urlConnection.addRequestProperty(
                "Content-Type", "application/json");
            urlConnection.addRequestProperty(
                "Authorization", auth.toString());

            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setChunkedStreamingMode(0);

            // StreamWriter used to send state information to API.
            OutputStreamWriter out =
                new OutputStreamWriter(urlConnection.getOutputStream());


            // Build the JSON payload.

            JSONObject state = new JSONObject();
            state.put("State", CurrentTickData.actState);

            JSONObject data = new JSONObject();
            data.put("Timestamp", CurrentTickData.curTimestamp);
            data.put("GPSlat", CurrentTickData.GPSlat);
            data.put("GPSlon", CurrentTickData.GPSlon);        

            state.put("data", data);

            String result = state.toString();
            System.out.println(result);

            out.write(result);
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while((result = in.readLine()) != null) {
                System.out.println(result);
            }

            in.close();
            urlConnection.disconnect();
        }
        catch(Exception e) {
            Log.e("Error", e.toString());
        }
    }
}
	    
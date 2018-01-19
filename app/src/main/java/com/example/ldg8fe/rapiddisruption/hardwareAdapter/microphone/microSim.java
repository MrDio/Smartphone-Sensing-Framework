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
package com.example.ldg8fe.rapiddisruption.hardwareAdapter.microphone;


// TODO: Auto-generated Javadoc

import android.content.Context;
import android.widget.TextView;

import com.example.ldg8fe.rapiddisruption.data.CsvFileReader;
import com.example.ldg8fe.rapiddisruption.hardwareAdapter.IMicrophone;

/**
 * Simulated microphone with data input from a saved CSV file .
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class microSim implements IMicrophone {

    @Override
    public Double getMaxAmplitude() {
        return CsvFileReader.getMaxAmplitude();
    }


    @Override
    public void initMicrophone(Context context) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void micUI(int v1, TextView... tvs) {
        tvs[0].setText("Max Amplitude : " + String.format("%.2f", v1));
    }
}

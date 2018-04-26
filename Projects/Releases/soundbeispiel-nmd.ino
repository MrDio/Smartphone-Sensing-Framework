// This #include statement was automatically added by the Particle IDE.

/*
 *  This file is a sample application, based
 *  on the IoT Prototyping Framework (IoTPF)

    This application and IoTPF is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    IoTPF is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU v3 General Public License for more details.

    Released under GNU v3

    You should have received a copy of the GNU General Public License
    along with IoTPF.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This is a sample demonstrates particle photon with losant.
 * 
 * Needed components
 * Mic MAX4466 (MAX)
 * Particle Photon (PHO)
 * SD-Card (SD)
 * 9DOF LSM9DS1 (LSM)
 * 
 * Wiring
 * see assignment description of lecture 
 * 
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */ 

// This #include statement was automatically added by the Particle IDE.
#include <SparkFunLSM9DS1.h>

// This #include statement was automatically added by the Particle IDE.
#include <SdFat.h>

#include "math.h"

LSM9DS1 imu;

#define LSM9DS1_M	0x1E // Would be 0x1C if SDO_M is LOW
#define LSM9DS1_AG	0x6B // Would be 0x6A if SDO_AG is LOW

#define PRINT_CALCULATED
#define PRINT_SPEED 250 // 250 ms between prints
#define DECLINATION -8.58 // Declination (degrees) in Boulder, CO.


// Pick an SPI configuration.
// See SPI configuration section below (comments are for photon).
#define SPI_CONFIGURATION 0
#define MICROPHONE_PIN A0
#define BUTTON_PIN D6
#define LED_PIN D7
#define AUDIO_BUFFER_MAX 8192 //255
#define TICK_TIMER 1 // in milliseconds
// -> 10kHz
//------------------------------------------------------------------------------
// Setup SPI configuration.
#if SPI_CONFIGURATION == 0
// Primary SPI with DMA
// SCK => A3, MISO => A4, MOSI => A5, SS => A2 (default)
SdFat sd;
const uint8_t chipSelect = SS;
#elif SPI_CONFIGURATION == 1
// Secondary SPI with DMA
// SCK => D4, MISO => D3, MOSI => D2, SS => D1
SdFat sd(1);
const uint8_t chipSelect = D1;
#elif SPI_CONFIGURATION == 2
// Primary SPI with Arduino SPI library style byte I/O.
// SCK => A3, MISO => A4, MOSI => A5, SS => A2 (default)
SdFatLibSpi sd;
const uint8_t chipSelect = SS;
#elif SPI_CONFIGURATION == 3
// Software SPI.  Use any digital pins.
// MISO => D5, MOSI => D6, SCK => D7, SS => D0
SdFatSoftSpi<D5, D6, D7> sd;
const uint8_t chipSelect = D0;
#endif  // SPI_CONFIGURATION

//------------------------------------------------------------------------------
struct DataTick{
    uint16_t cnt;
    String timestamp;
    uint16_t amplitude;
    
    uint16_t accx;
    uint16_t accy;
    uint16_t accz;
    
    uint16_t magx;
    uint16_t magy;
    uint16_t magz;
    
    uint16_t gyrx;
    uint16_t gyry;
    uint16_t gyrz;
    
    uint16_t temp;
};

//
bool debug = true;

//
File myFile;
int idx = 0;
uint16_t cnt_gbl = 0;

DataTick buffer;
String csv_str = "";

/* My own variables */
uint16_t myAmplitude[AUDIO_BUFFER_MAX];
//String myTime[AUDIO_BUFFER_MAX];
String myTime[2];

void setup() {
  
  Serial.begin(115200);
    
  // Wait for USB Serial 
   pinMode(BUTTON_PIN, INPUT_PULLUP);
   digitalWrite(BUTTON_PIN, HIGH);
   pinMode(LED_PIN, OUTPUT);
   pinMode(MICROPHONE_PIN, INPUT);
   pinMode(BUTTON_PIN, INPUT);

    if(debug){
        Particle.publish("[Debug] Setup start", 1);        
    }
    
    //Setup SD-Card
    if (!sd.begin(chipSelect, SPI_HALF_SPEED)) {
        if(debug){
            Particle.publish("[Debug] CS wiring problem", 0);        
        }
        sd.initErrorHalt();
    }
    
    if (!myFile.open("MessungenToni.csv", O_RDWR | O_CREAT )) {
        if(debug){
            Particle.publish("[Debug] Opening Messung.csv for write failed", 0);        
        }
        sd.errorHalt("opening Messung.csv for write failed");
    }else{
        myFile.println("Amplitude 1kHz ; Time start; Time end");
        if(debug){
            Particle.publish("[Debug] SD Card ready", 0);        
        }
    }


    
    // Before initializing the IMU, there are a few settings
    // we may need to adjust. Use the settings struct to set
    // the device's communication mode and addresses:
    imu.settings.device.commInterface = IMU_MODE_I2C;
    imu.settings.device.mAddress = LSM9DS1_M;
    imu.settings.device.agAddress = LSM9DS1_AG;

    // Try to initialise and warn if we couldn't detect the chip
    if (!imu.begin())
    {
        if(debug){
            Particle.publish("[Debug] Oops ... unable to initialize the LSM9DS0. Check your wiring!", 0);        
        }
        while (1);
    }
    if(debug){
        Particle.publish("[Debug] LSM9DS1 ready", 1);        
    }
  
    if(debug){
        Particle.publish("[Debug] Setup done...", 1);        
    }
    
    myTime[0] = "null";
}

typedef enum STATE{START, NOISE_DETECTED, MOTION_DETECTED, NOISE_MOTION_DETECTED} STATE_TYPE;

STATE_TYPE actState = START;
STATE_TYPE nextState = START;

bool noiseDetection(){
    return (buffer.amplitude > 2340 || buffer.amplitude < 1500);
}

bool movementDetection(){
    return (buffer.gyrx > 1300 || buffer.gyrx< 1000);
}



void transition(){
    switch(actState){
        case START:{
        
            if(noiseDetection() && !movementDetection())
            {
                if(debug){
                    Particle.publish("[Debug] State-Machine START - Event: Noise Detected", 1);        
                }
                nextState = NOISE_DETECTED;
            }
            if(noiseDetection() && movementDetection())
            {
                if(debug){
                    Particle.publish("[Debug] State-Machine START - Event: Noise and Movement Detected", 1);        
                }
                nextState = NOISE_MOTION_DETECTED;
            }
            if(!noiseDetection() && movementDetection()){
                if(debug){
                        Particle.publish("[Debug] State-Machine START - Event: Movement Detected", 1);        
                    }
                    nextState = MOTION_DETECTED;
                }
        }break;
        case NOISE_DETECTED:{
        
            if(!noiseDetection() && !movementDetection())
            {
                if(debug){
                    Particle.publish("[Debug] State-Machine NOISE_DETECTED - Event: No Noise and Movement Detected", 1);        
                }
                nextState = START;
            }
            if(noiseDetection() && movementDetection())
            {
                if(debug){
                    Particle.publish("[Debug] State-Machine NOISE_DETECTED - Event: Noise and Movement Detected", 1);        
                }
                nextState = NOISE_MOTION_DETECTED;
            }
            if(!noiseDetection() && movementDetection()){
                if(debug){
                        Particle.publish("[Debug] State-Machine NOISE_DETECTED - Event: Movement Detected", 1);        
                    }
                    nextState = MOTION_DETECTED;
                }
        }break;
        case MOTION_DETECTED:{
        
            if(!noiseDetection() && !movementDetection())
            {
                if(debug){
                    Particle.publish("[Debug] State-Machine MOVEMENT_DETECTED - Event: No Noise and Movement Detected", 1);        
                }
                nextState = START;
            }
            if(noiseDetection() && movementDetection())
            {
                if(debug){
                    Particle.publish("[Debug] State-Machine MOVEMENT_DETECTED - Event: Noise and Movement Detected", 1);        
                }
                nextState = NOISE_MOTION_DETECTED;
            }
            if(noiseDetection() && !movementDetection()){
                if(debug){
                        Particle.publish("[Debug] State-Machine MOVEMENT_DETECTED - Event: Noise Detected", 1);        
                    }
                    nextState = NOISE_DETECTED;
                }
        }break;
        case NOISE_MOTION_DETECTED:{
        
            if(!noiseDetection() && !movementDetection())
            {
                if(debug){
                    Particle.publish("[Debug] State-Machine NOISE_MOVEMENT_DETECTED - Event: No Noise and Movement Detected", 1);        
                }
                nextState = START;
            }
            if(!noiseDetection() && movementDetection())
            {
                if(debug){
                    Particle.publish("[Debug] State-Machine NOISE_MOVEMENT_DETECTED - Event: Movement Detected", 1);        
                }
                nextState = MOTION_DETECTED;
            }
            if(noiseDetection() && !movementDetection()){
                if(debug){
                        Particle.publish("[Debug] State-Machine NOISE_MOVEMENT_DETECTED - Event: Noise Detected", 1);        
                    }
                    nextState = NOISE_DETECTED;
                }
        }break;
        default:{
             if(debug){
                Particle.publish("[Debug] State-Machine transition DEFAULT", 1);        
            }
            nextState = START;
        }
        actState=nextState;
    }   
}

void dispatch(){
    switch(actState){
        case START:{
            if(debug){
                Particle.publish("[Debug] State-Machine START", 1);        
            }
            transition();
        }break;
        case NOISE_DETECTED:{
            if(debug){
                Particle.publish("[Debug] State-Machine NOISE_DETECTED", 1);        
            }
            transition();
        }break;
        case MOTION_DETECTED:{
            if(debug){
                Particle.publish("[Debug] State-Machine MOTION_DETECTED", 1);        
            }
            transition();
        }break;
        case NOISE_MOTION_DETECTED:{
            if(debug){
                Particle.publish("[Debug] State-Machine NOISE_MOTION_DETECTED", 1);        
            }
            transition();
        }break;
        default:{
            if(debug){
                Particle.publish("[Debug] State-Machine DEFAULT", 1);        
            }
            transition();    
        };
    }
}


void loop() {

    // Heartbeat
    digitalWrite(LED_PIN, HIGH);

   /* imu.readGyro();
    imu.readAccel();
    imu.readMag();
    imu.readTemp(); */
  
    /*buffer.amplitude=analogRead(MICROPHONE_PIN);
    buffer.timestamp=String(Time.now());
    buffer.cnt=++cnt_gbl;

    buffer.accx=imu.ax;
    buffer.accy=imu.ay;
    buffer.accz=imu.az;
    
    buffer.magx=imu.mx;
    buffer.magy=imu.my;
    buffer.magz=imu.mz;
    
    buffer.gyrx=imu.gx;
    buffer.gyry=imu.gy;
    buffer.gyrz=imu.gz;*/
    
// With the calc* method you can calculate the physical value out of the digital. With this calc you lose precision.
//    buffer.accx=imu.calcAccel(imu.ax);
//    buffer.accy=imu.calcAccel(imu.ay);
//    buffer.accz=imu.calcAccel(imu.az);
    
//    buffer.magx=imu.calcMag(imu.mx);
//    buffer.magy=imu.calcMag(imu.my);
//    buffer.magz=imu.calcMag(imu.mz);
    
//    buffer.gyrx=imu.calcGyro(imu.gx);
//    buffer.gyry=imu.calcGyro(imu.gy);
//    buffer.gyrz=imu.calcGyro(imu.gz);
 
//    dispatch();
    if(myTime[0] == "null"){
        myTime[0] = String(Time.now());
    }
    
    myAmplitude[idx] = analogRead(MICROPHONE_PIN);

    idx++;
    
    if(AUDIO_BUFFER_MAX == idx){
        myTime[1] = String(Time.now());
        for(int i = 0; i < AUDIO_BUFFER_MAX; i++){
            if(i == 0){
                
                myFile.println( String(myAmplitude[i]) + ";" + myTime[0] + ";" + myTime[1] );
            }
            else{
                myFile.println( String(myAmplitude[i]) );
            }
        }
        
        if(myFile.isOpen()){
            myFile.close();
            if(debug){
                Particle.publish("[Debug] File writing done.", 1);        
            }
        digitalWrite(LED_PIN, LOW);
        idx = 0;
        myTime[0] = "null";
        delay(10000);
        }
    }
    /*
    if(AUDIO_BUFFER_MAX > idx){
        myFile.println(
        String(buffer.cnt) + ";" +
        buffer.timestamp + ";" +
        String(buffer.amplitude) + ";" + 
        String(buffer.accx) + ";" + 
        String(buffer.accy) + ";" + 
        String(buffer.accz) + ";" + 
        String(buffer.magx) + ";" +
        String(buffer.magy) + ";" + 
        String(buffer.magz) + ";" + 
        String(buffer.gyrx) + ";" + 
        String(buffer.gyry) + ";" + 
        String(buffer.gyrz) + ";" + 
        String(buffer.temp) + ";" +
        String(actState));

        idx++;
        
    }else{
        if(myFile.isOpen()){
            myFile.close();
            if(debug){
                Particle.publish("[Debug] File writing done.", 1);        
            }
            digitalWrite(LED_PIN, LOW);
            delay(10000);
        }
    }*/
    
    // Heartbeat
    //digitalWrite(LED_PIN, LOW);
    // Tick
    delay(TICK_TIMER);
}
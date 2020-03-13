# ABC-Buddy 1.0
ABC-Buddy was developed by Kristin Findeis, Matthias Gebert, Christian Holder & Büsra Yagbasan as part of the Embedded Systems Engineering course.

## Idea
The basic idea is simple, by repeated movements of the mobile phone the spelling of the individual letters is to be internalised. In this way children should learn the writting of the alphabet in a fun and playful way.


## Features of the Application
The application has two different modes:

- Learn-Mode: In this mode, the user can learn a specific letter by selecting it. The user is then shown a video for drawing the letter. After the user has drawn the letter, the application evaluates the recording and displays to the user whether he has written the letter correctly or not.
- Recognize-Mode: In this mode the user can write any letter in the air. The application compares the record with all letters and shows the user which letter he has written.

## State Machine
The application consists of three states. The default-state is the idle-state. If the user starts the recording, the application changes to the recording-state. After 4 seconds, the application switches to the analysis-state. In this state the mathematical calculation are done. Finally, the application prints the result and changes to the idle-state.

## Implementation
### Recording

When the application is put into recording mode, the user has one second to prepare for the movement. After one second a signal indicates that the movement can be started and from this moment on the recording will take place for four seconds. When the four seconds have elapsed, another signal appears indicating the end of the recording phase. The actual recording of the combined acceleration vector is done using the implementation of the Smartphone Sensing Framework. Ten data values are collected per second, whereby one data value is a so-called tick. For the analysis the data of the individual axes are collected in order to calculate the combined vector with which the Mean Squared Error is later calculated.

### Analysis

As already mentioned, the Mean Squared Error method was used for the analysis. In this procedure, as explained in a, the deviation of an input data set from the data sets stored for the characters is calculated. The input data set in this case is the acceleration captured during the recording phase. While the comparison data sets are optimal accelerations of motion sequences created by the developers. Each character has 40 comparison data sets, with ten data sets from each developer.

If the learning function is selected, the calculated value belonging to the character selected is compared with a threshold value. This threshold value was created individually for each character by trial and error tests. The letter was correctly drawn into the air if the calculated value is smaller than the threshold.

package com.lscarp4.lscarpl4assessments.StudentFiles;

public interface TaskUploadCompleted {
    // Define data you like to return from AysncTask
     void onTaskComplete(String result, String apiRequestName, boolean statusFlag, String fileName);
}

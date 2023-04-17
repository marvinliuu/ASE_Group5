package com.example.testdisasterevent.algorithms;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import android.content.Context;


import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.ml.Ambulance;


public class ResearchAllocation {
    // Load the TFLite model from assets folder
    public int getAllocation (Context context, String[][] input, int modelType) {
        try {
            Interpreter tflite = new Interpreter(loadModelFile(context, modelType), new Interpreter.Options());
            // Run inference
            double[][] input1 = new double[1][2];
            input1[0][0] = 1.0f;
            input1[0][1] = 2.0f;

            String[][] output = new String[1][1];
            tflite.run(input1, output);
            // Get inference result
            return Integer.parseInt(output[0][0]);
        } catch (IOException ex) {
            // Handle the exception
            return 0;
        }
    }

    // Helper function to load model file from assets folder
    private MappedByteBuffer loadModelFile(Context context, int modelType) throws IOException {
        AssetFileDescriptor fileDescriptor;
        if (modelType == 1) fileDescriptor = context.getAssets().openFd("Ambulance.tflite");
        else if (modelType == 2) fileDescriptor = context.getAssets().openFd("Police.tflite");
        else if (modelType == 3) fileDescriptor = context.getAssets().openFd("Trunk.tflite");
        else fileDescriptor = context.getAssets().openFd("Bus.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}

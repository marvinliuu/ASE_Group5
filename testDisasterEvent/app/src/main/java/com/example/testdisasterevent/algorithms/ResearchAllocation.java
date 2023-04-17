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
    public double getResource(Context context, double[][] input) {
        try {
            Interpreter tflite = new Interpreter(loadModelFile(context), new Interpreter.Options());
//            // Prepare input data
//            double[][] input = new double[1][2];
//            input[0][0] = 1.0f;
//            input[0][1] = 2.0f;

// Run inference
            double[][] output = new double[1][1];
            tflite.run(input, output);

// Get inference result
            return output[0][0];
//            double result = output[0][0];
//            return result;
        } catch (IOException ex) {
            // Handle the exception
            return 0;
        }
    }

    // Helper function to load model file from assets folder
    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("Ambulance.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}

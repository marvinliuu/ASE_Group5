package com.example.testdisasterevent.algorithms;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class DownsampleImage {
    public DownsampleImage() {


    }




    public Bitmap DownsampleImage(Uri imageUri, Context context) {


        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width / 3;
        int newHeight = height / 3;

        float ratioX = (float)  width/newWidth;
        float ratioY = (float) height/newHeight;

        Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                // Calculate the corresponding pixel position in the original image
                int srcx = (int) (i * ratioX);
                int srcy = (int) (j * ratioY);

                int x = (int)Math.floor(srcx);
                int y = (int)Math.floor(srcy);

                // Calculate the fractional parts of the pixel position
                float u = srcx - x;
                float v = srcy - y;


                // Interpolate the color value using Bilinear interpolation
                int c00 = bitmap.getPixel(x, y);
                int c01 = bitmap.getPixel(x + 1, y);
                int c10 = bitmap.getPixel(x,y + 1);
                int c11 = bitmap.getPixel(x + 1,y + 1);


                float w00 = (1 - u) * (1 - v);
                float w01 = u * (1 - v);
                float w10 = (1 - u) * v;
                float w11 = u * v;

                int color = (int) (w00 * c00 + w01 * c01 + w10 * c10 + w11 * c11);

                // Set the interpolated color as the color of the corresponding pixel in the new image

                newBitmap.setPixel(i, j, color);

            }
        }


        return newBitmap;
    }

}

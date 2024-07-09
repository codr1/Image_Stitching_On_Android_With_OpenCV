package com.example.opencvandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.media.MediaScannerConnection;
import android.net.Uri;



import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.io.IOException;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenCVLoader.initDebug();
    }


    public void stitchHorizontal(View v){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap im1 = BitmapFactory.decodeResource(getResources(), R.drawable.img1, options);
        Bitmap im2 = BitmapFactory.decodeResource(getResources(), R.drawable.img2, options);
        Bitmap im3 = BitmapFactory.decodeResource(getResources(), R.drawable.img3, options);
        Bitmap im4 = BitmapFactory.decodeResource(getResources(), R.drawable.img4, options);
        Bitmap im5 = BitmapFactory.decodeResource(getResources(), R.drawable.img5, options);
        Bitmap im6 = BitmapFactory.decodeResource(getResources(), R.drawable.img6, options);

        Mat img1 = new Mat();
        Mat img2 = new Mat();
        Mat img3 = new Mat();
        Mat img4 = new Mat();
        Mat img5 = new Mat();
        Mat img6 = new Mat();

        Utils.bitmapToMat(im1, img1);
        Utils.bitmapToMat(im2, img2);
        Utils.bitmapToMat(im3, img3);
        Utils.bitmapToMat(im4, img4);
        Utils.bitmapToMat(im5, img5);
        Utils.bitmapToMat(im6, img6);


        Bitmap imgBitmap = stitchImagesHorizontal( Arrays.asList(img1, img2, img3, img4, img5, img6));
        ImageView imageView = findViewById(R.id.opencvImg);
        imageView.setImageBitmap(imgBitmap);
        saveBitmap(imgBitmap, "stitch_horizontal");


        /*
        Mat img = null;

        try {
            img = Utils.loadResource(getApplicationContext(), R.drawable.test);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);

        Mat img_result = img.clone();
        Imgproc.Canny(img, img_result, 80, 90);
        Bitmap img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_result, img_bitmap);
        ImageView imageView = findViewById(R.id.img);
        imageView.setImageBitmap(img_bitmap);

        */

    }

    Bitmap stitchImagesHorizontal(List<Mat> src) {
        Mat dst = new Mat();
        Core.hconcat(src, dst);
        Bitmap imgBitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, imgBitmap);

        return imgBitmap;
    }

    void saveBitmap(Bitmap imgBitmap, String fileNameOpening){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();
        String fileName = fileNameOpening + "_" + formatter.format(now) + ".jpg";

        FileOutputStream outStream;
        try{
            // Get a public path on the device storage for saving the file. Note that the word external does not mean the file is saved in the SD card. It is still saved in the internal storage.
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            // Creates a directory for saving the image.
            File saveDir =  new File(path + "/HeartBeat/");

            // If the directory is not created, create it.
            if(!saveDir.exists())
                saveDir.mkdirs();

            // Create the image file within the directory.
            File fileDir =  new File(saveDir, fileName); // Creates the file.

            // Write into the image file by the BitMap content.
            outStream = new FileOutputStream(fileDir);
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            MediaScannerConnection.scanFile(this.getApplicationContext(),
                    new String[] { fileDir.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });

            // Close the output stream.
            outStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
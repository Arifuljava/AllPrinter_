package com.messas.cpclprintersdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static android.content.ContentValues.TAG;

import es.dmoral.toasty.Toasty;
public class FreshCPClActivity extends AppCompatActivity {
    Uri imageuri;
    int flag = 0;
    BluetoothSocket m5ocket;
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice device;
    ImageView imageposit;

    Button printimageA;
    Bitmap bitmapdataMe;
    ImageView bitmapcalling;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh_c_p_cl);
        imageposit = findViewById(R.id.imageposit);
        printimageA = findViewById(R.id.printimageA);
        bitmapcalling=findViewById(R.id.bitmapcalling);




        printimageA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BlueMac = "FB:7F:9B:F2:20:B7";
                mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                mBluetoothAdapter = mBluetoothManager.getAdapter();
                final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);
                ///Toasty.info(getApplicationContext(),"Please active bluetooth"+mBluetoothAdapter.isEnabled(),Toasty.LENGTH_SHORT,true).show();
                if (!mBluetoothAdapter.isEnabled()) {
                    Toasty.info(getApplicationContext(), "Please active bluetooth", Toasty.LENGTH_SHORT, true).show();
                    android.app.AlertDialog.Builder mybuilder = new android.app.AlertDialog.Builder(FreshCPClActivity.this);
                    mybuilder.setTitle("Confirmation")
                            .setMessage("Do you want to active bluetooth");
                    mybuilder.setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Right Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (ActivityCompat.checkSelfPermission(FreshCPClActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                mBluetoothAdapter.enable();
                                Toasty.info(getApplicationContext(), "Bluetooth is active now.", Toasty.LENGTH_SHORT, true).show();
                            } else {
                                mBluetoothAdapter.enable();
                                Toasty.info(getApplicationContext(), "Bluetooth is active now.", Toasty.LENGTH_SHORT, true).show();
                            }

                        }
                    }).create().show();

                    return;
                } else {
                    printImage1();
                }


            }
        });

    }

    /*========================================================================================================
        ==================================PRINT SECTION===========================================================
         */
    int bitmapHeight = 1080;
    OutputStream os = null;


    CountDownTimer countDownTimer,countDownTimer1;

    private  byte[]  BitmapToRGBbyteA(Bitmap bitmapOrg) {
        ArrayList<Byte> Gray_ArrayList;
        Gray_ArrayList =new ArrayList<Byte>();
        int height = 1080;
        if(bitmapOrg.getHeight()>height)
        {
            height=1080;
        }
        else
        {
            height=bitmapOrg.getHeight();
        }
        int width = 384;
        int R = 0, B = 0, G = 0;
        //int pixles;
        int []pixels = new int[width * height];
        int x = 0, y = 0;
        Byte[] Gray_Send;
        //bitSet = new BitSet();
        try {

            bitmapOrg.getPixels(pixels, 0, width, 0, 0, width, height);
            int alpha = 0xFF << 24;
            //int []i_G=new int[7];
            int []i_G=new int[13];
            int Send_Gray=0x00;
            int StartInt=0;
            char  StartWords=' ';

            int k=0;
            int Send_i=0;
            int mathFlag=0;
            for(int i = 0; i < height; i++)
            {

                k=0;
                Send_i=0;
                for (int j = 0; j <width; j++)
                {
                    int grey = pixels[width * i + j];
                    int red = ((grey & 0x00FF0000) >> 16);
                    int green = ((grey & 0x0000FF00) >> 8);
                    int blue = (grey & 0x000000FF);
                    grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);


//==================================
                    if(grey>128)
                    {
                        //bufImage[j]=0x00;
                        mathFlag=0;

                    }
                    else
                    {
                        //bufImage[j]=0x01;
                        mathFlag=1;
                    }
                    k++;
                    if(k==1)
                    {
                        Send_i=0;
                        Send_i=Send_i+128*mathFlag;//mathFlag|0x80
                    }
                    else if(k==2)
                    {
                        Send_i=Send_i+64*mathFlag;//mathFlag|0x40
                    }
                    else if(k==3)
                    {
                        Send_i=Send_i+32*mathFlag;//mathFlag|0x20
                    }
                    else if(k==4)
                    {
                        Send_i=Send_i+16*mathFlag;//mathFlag|0x10
                    }
                    else if(k==5)
                    {
                        Send_i=Send_i+8*mathFlag;//mathFlag|0x08
                    }
                    else if(k==6)
                    {
                        Send_i=Send_i+4*mathFlag;//mathFlag|0x04
                    }
                    else if(k==7)
                    {
                        Send_i=Send_i+2*mathFlag;//mathFlag|0x02
                    }
                    else if(k==8)
                    {
                        Send_i=Send_i+1*mathFlag;//mathFlag|0x01
                        Gray_ArrayList.add((byte)Send_i);

                        Send_i=0;
                        k=0;
                    }

                }
                int aBc=0;

            }

            byte[] sss=new byte[Gray_ArrayList.size()];
            Gray_Send=new Byte[Gray_ArrayList.size()];
            Gray_ArrayList.toArray(Gray_Send);
            for(int xx=0;xx<Gray_Send.length;xx++){
                sss[xx]=Gray_Send[xx];
            }
            return  sss;
        } catch (Exception e) {

        }
        return null;
    }

    Uri bitmapUri;
    Bitmap mainimageBitmap;

    int PICK=12;
    boolean request=false;


    private void printImage1() {
      //  final Bitmap bitmap = bitmapdataMe;
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.testing);
        float scax=384f /bitmap.getWidth();
        float scaly= 384f / bitmap.getHeight();
        Log.e("dolon",""+bitmap);
        Log.e("zzz",""+bitmap.getWidth());
        Log.e("zzz",""+bitmap.getHeight());
        Matrix matrix=new Matrix();
        matrix.postScale(scax,scaly);
        Bitmap resize= Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        Log.e("Ariful9",""+scax);
        //final Bitmap bitmap = bitmapdataMe;

        final byte[] bitmapGetByte = BitmapToRGBbyteA(resize);//convertBitmapToRGBBytes (resize);
        Log.e("Ariful4",""+bitmapGetByte);
        String BlueMac = "FB:7F:9B:F2:20:B7";
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);
        ///

        ////
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /// Toast.makeText(AssenTaskDounwActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(FreshCPClActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        m5ocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                        m5ocket.connect();

                        os = m5ocket.getOutputStream();

                        if(resize.getHeight()>bitmapHeight)
                        {
                            bitmapHeight=1080;
                        }
                        else
                        {
                            bitmapHeight=resize.getHeight();
                        }
                        Log.e("Ariful1",""+resize.getWidth());
                        Log.e("Ariful2",""+resize.getHeight());
                        Log.e("Ariful3",""+bitmap);
                        Random random=new Random();
                        int sendingnumber=random.nextInt(10);
                        int mimisecond=sendingnumber*1000;


                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // write your code here
                                countDownTimer =new CountDownTimer(10000,1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                        double seconddd=millisUntilFinished/1000;
                                        printimageA.setText("Sending Data : "+seconddd+" S");



                                    }

                                    @Override
                                    public void onFinish() {
                                        try {
                                            String t_line1 = "! 0 200 200 "+bitmapHeight+" 1 \r\n";//bitmap.getHeight()
                                            String t_line2 = "pw "+384+"\r\n";
                                            String t_line3 = "DENSITY 12\r\n";
                                            String t_line4 = "SPEED 3\r\n";
                                            String t_line5 = "CG "+384/8+" "+bitmapHeight+" 0 0 ";
                                            String t_line6 ="PR 0\r\n";
                                            String t_line7= "FORM\r\n";
                                            String t_line8 = "PRINT\r\n";
                                            String t_line9 = "\r\n";
                                            os.write(t_line1.getBytes());
                                            os.write(t_line2.getBytes());
                                            os.write(t_line3.getBytes());
                                            os .write(t_line4.getBytes());
                                            os .write(t_line5.getBytes());

                                            os.write(bitmapGetByte);
                                            os .write(t_line9.getBytes());
                                            os .write(t_line6.getBytes());
                                            os.write(t_line7.getBytes());
                                            os.write(t_line8.getBytes());
                                            Log.e("Ariful5","PrintCommand");
                                        }catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e("Ariful6",""+e.getMessage());
                                        }
                                        countDownTimer1=new CountDownTimer(2000,1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                long second=  (millisUntilFinished/1000);
                                                int mysecond=Integer.parseInt(String.valueOf(second));



                                            }

                                            @Override
                                            public void onFinish() {

                                                printimageA.setText("Print Out");
                                                try {
                                                    os.flush();
                                                    os.flush();
                                                    m5ocket.close();
                                                    Log.e("Ariful7","Go to print");
                                                }catch (Exception e) {
                                                    e.printStackTrace();
                                                    Log.e("Ariful8",""+e.getMessage());
                                                }

                                                Toasty.success(getApplicationContext(),"Data Sending Complete",Toasty.LENGTH_SHORT,true).show();
                                                return;
                                            }
                                        }.start();
                                        countDownTimer1.start();


                                    }
                                };
                                countDownTimer.start();
                            }
                        });
                    }
                    else {
                        m5ocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                        m5ocket.connect();

                        os = m5ocket.getOutputStream();

                        if(bitmap.getHeight()>bitmapHeight)
                        {
                            bitmapHeight=1080;
                        }
                        else
                        {
                            bitmapHeight=bitmap.getHeight();
                        }
                        Log.e("Ariful1",""+bitmap.getWidth());
                        Random random=new Random();
                        int sendingnumber=random.nextInt(10);
                        int mimisecond=sendingnumber*1000;


                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // write your code here
                                countDownTimer =new CountDownTimer(10000,1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                        double seconddd=millisUntilFinished/1000;
                                        printimageA.setText("Sending Data : "+seconddd+" S");



                                    }

                                    @Override
                                    public void onFinish() {
                                        try {
                                            String t_line1 = "! 0 200 200 "+bitmapHeight+" 1 \r\n";//bitmap.getHeight()
                                            String t_line2 = "pw "+384+"\r\n";
                                            String t_line3 = "DENSITY 12\r\n";
                                            String t_line4 = "SPEED 3\r\n";
                                            String t_line5 = "CG "+384/8+" "+bitmapHeight+" 0 0 ";
                                            String t_line6 ="PR 0\r\n";
                                            String t_line7= "FORM\r\n";
                                            String t_line8 = "PRINT\r\n";
                                            String t_line9 = "\r\n";
                                            os.write(t_line1.getBytes());
                                            os.write(t_line2.getBytes());
                                            os.write(t_line3.getBytes());
                                            os .write(t_line4.getBytes());
                                            os .write(t_line5.getBytes());

                                            os.write(bitmapGetByte);
                                            os .write(t_line9.getBytes());
                                            os .write(t_line6.getBytes());
                                            os.write(t_line7.getBytes());
                                            os.write(t_line8.getBytes());
                                        }catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        countDownTimer1=new CountDownTimer(2000,1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                long second=  (millisUntilFinished/1000);
                                                int mysecond=Integer.parseInt(String.valueOf(second));



                                            }

                                            @Override
                                            public void onFinish() {

                                                printimageA.setText("Print Out");
                                                try {
                                                    os.flush();
                                                    os.flush();
                                                    m5ocket.close();
                                                }catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                Toasty.success(getApplicationContext(),"Data Sending Complete",Toasty.LENGTH_SHORT,true).show();
                                                return;
                                            }
                                        }.start();
                                        countDownTimer1.start();


                                    }
                                };
                                countDownTimer.start();
                            }
                        });
                    }




                } catch (IOException e) {
                    Log.e("Error", ""+e.getMessage());

                }
            }
        });
        thread.start();
    }
    private void pddrintImage1() {

        String BlueMac = "FB:7F:9B:F2:20:B7";
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);
        ///

        ////
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /// Toast.makeText(AssenTaskDounwActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(FreshCPClActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        m5ocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                        m5ocket.connect();

                        os = m5ocket.getOutputStream();






                    }
                    else {

                    }




                } catch (IOException e) {
                    Log.e("Error", ""+e.getMessage());

                }
            }
        });
        thread.start();
    }
}
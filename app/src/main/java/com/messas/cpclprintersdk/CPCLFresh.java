package com.messas.cpclprintersdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class CPCLFresh extends AppCompatActivity {
    EditText quantityProductPage;
    SeekBar seekBar;
TextView progressbarsechk;
TextView connectedornot;
    private BluetoothHelper bluetoothHelper;
    String geeet;

    /////bitmap data
    Uri imageuri;
    int flag = 0;
    BluetoothSocket m5ocket;
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice device;
    ImageView imageposit;

    Button printimageA;
    Bitmap bitmapdataMe;
    TextView printtimer;
    String printer_detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_p_c_l_fresh);
        ScrollView scrollView = findViewById(R.id.scrollView);

// Perform smooth scrolling to a specific position within the ScrollView
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                // Replace the X and Y coordinates with the desired scroll position
                scrollView.smoothScrollTo(0, 500);
            }
        });
        printtimer=findViewById(R.id.printtimer);
        quantityProductPage=findViewById(R.id.quantityProductPage);
        progressbarsechk=findViewById(R.id.progressbarsechk);
        connectedornot=findViewById(R.id.connectedornot);
        seekBar=findViewById(R.id.seekBar);
        ImageView closedialouge=findViewById(R.id.closedialouge);
        closedialouge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressbarsechk=findViewById(R.id.progressbarsechk);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                progressbarsechk.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
             //   Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });
        //check connected or not
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean value11 = isBluetoothConnected(CPCLFresh.this);

     /*
        if(value11){
            connectedornot.setText("Connected");
            connectedornot.setTextColor(Color.GREEN);
        }
        else{
            connectedornot.setText("Not Connected");
            connectedornot.setTextColor(Color.RED);
        }
      */
        bluetoothHelper = new BluetoothHelper();

        // Check if Bluetooth is enabled
        if (!bluetoothHelper.isBluetoothEnabled()) {
            connectedornot.setText("Disable");
            connectedornot.setTextColor(Color.RED);
        }

        // Check if Bluetooth is connected
        if (bluetoothHelper.isBluetoothConnected(this)) {
            connectedornot.setText("Connected");
            connectedornot.setTextColor(Color.GREEN);
        } else {
            connectedornot.setText("Not Connected");
            connectedornot.setTextColor(Color.RED);
        }
        RelativeLayout relagoo=findViewById(R.id.relagoo);
        relagoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Findlocation.class));
            }
        });

        //getdata
       try {
           geeet=getIntent().getStringExtra("geeet");
           if (TextUtils.isEmpty(geeet)|| geeet.equals(null))
           {
               geeet="FB:7F:9B:F2:20:B7";
           }
           else
           {
               geeet=geeet;
           }
       }catch (Exception e){
           e.printStackTrace();
       }
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(geeet);

       RelativeLayout printcommand=findViewById(R.id.printcommand);
        printcommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BlueMac = "FB:7F:9B:F2:20:B7";

                Toast.makeText(CPCLFresh.this, "gggg", Toast.LENGTH_SHORT).show();
                mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                mBluetoothAdapter = mBluetoothManager.getAdapter();
                final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);
                ///Toasty.info(getApplicationContext(),"Please active bluetooth"+mBluetoothAdapter.isEnabled(),Toasty.LENGTH_SHORT,true).show();
                if (!mBluetoothAdapter.isEnabled()) {
                    Toasty.info(getApplicationContext(), "Please active bluetooth", Toasty.LENGTH_SHORT, true).show();
                    android.app.AlertDialog.Builder mybuilder = new android.app.AlertDialog.Builder(CPCLFresh.this);
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
                            if (ActivityCompat.checkSelfPermission(CPCLFresh.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
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
    private String findPrinterIP(String printerMACAddress) {
        // Perform network discovery or use a network scanning library to find the printer IP address based on MAC address
        // This is just a sample implementation, you may need to customize it based on your network scanning approach

        // Assuming you have the printer IP address
        return "192.168.1.100";
    }
    public String  PrinterModeDetector(String printerMACAddress)
    {
        String printerIP = findPrinterIP(printerMACAddress);
        if (printerIP == null) {
            // Printer not found
            return "Printer not found";
        }
        // Establish a connection with the printer
        try (Socket socket = new Socket(printerIP, 9100)) {
            // Send CPCL command to check CPCL mode
            String cpclCommand = "! U1 getvar \"allcv\"\r\n";
            socket.getOutputStream().write(cpclCommand.getBytes());

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();
            if (response.contains("! U1 ")) {
                // Printer is in CPCL mode
                return "CPCL mode";
            }

            // Send ESC/POS command to check ESC mode
            byte[] escCommand = {0x1B, 0x76};
            socket.getOutputStream().write(escCommand);

            // Read the response
            response = reader.readLine();
            if (response != null) {
                // Printer is in ESC mode
                return "ESC mode";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Unable to determine printer mode
        return "Unknown";


    }
    public void decrement(View view) {
        int value = Integer.parseInt(quantityProductPage.getText().toString());
        if (value==1) {
            Toast.makeText(this, "It is the lowest value.Print Copy value is not decrement now.", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value-1;
            quantityProductPage.setText(""+value);
        }
    }

    public void increment(View view) {
        int value = Integer.parseInt(quantityProductPage.getText().toString());
        if (value==99) {
            Toast.makeText(this, "It is the highest value. Print Copy value is not increment now.", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value+1;
            quantityProductPage.setText(""+value);
        }
    }
    public static boolean isBluetoothConnected(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false; // Bluetooth is not available or not enabled
        }

        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);//Toast.makeText(context, ""+bluetoothManager.getConnectedDevices(BluetoothProfile.GATT), Toast.LENGTH_SHORT).show();

        if (bluetoothManager != null) {
            for (BluetoothDevice device : bluetoothManager.getConnectedDevices(BluetoothProfile.GATT)) {

                if (device.getType() == BluetoothDevice.DEVICE_TYPE_LE || device.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                    return true; // At least one Bluetooth device is connected
                }
            }
        }

        return false; // No Bluetooth devices connected
    }
    public static boolean isSocketConnected(Socket socket) {
        if (socket == null) {
            return false;
        }

        return socket.isConnected() && !socket.isClosed();
    }

    public static boolean isSocketConnected(String host, int port) {
        try {
            InetAddress address = InetAddress.getByName(host);
            Socket socket = new Socket(address, port);
            boolean isConnected = isSocketConnected(socket);
            socket.close();
            return isConnected;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //print section
    Uri bitmapUri;
    Bitmap mainimageBitmap;

    int PICK=12;
    boolean request=false;
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
    int bitmapHeight = 1080;
    OutputStream os = null;

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
                    if (ActivityCompat.checkSelfPermission(CPCLFresh.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
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

for (int i=1;i<=Integer.parseInt(quantityProductPage.getText().toString());i++){
    new Handler(Looper.getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
            // write your code here
            countDownTimer =new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    double seconddd=millisUntilFinished/1000;
                    printtimer.setText("Sending Data : "+seconddd+" S");



                }

                @Override
                public void onFinish() {
                    try {
                        String t_line1 = "! 0 200 200 "+bitmapHeight+" 1 \r\n";//bitmap.getHeight()
                        String t_line2 = "pw "+384+"\r\n";
                        String t_line3 = "DENSITY 12\r\n";
                        String t_line4 = "SPEED 9\r\n";
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

                            printtimer.setText("Print Out");
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

                    }
                    else {

                    }




                } catch (IOException e) {
                   // Toast.makeText(CPCLFresh.this, "Try Again. Bluetooth Connection Problem.", Toast.LENGTH_SHORT).show();
                   // printtimer.setText("Try Again. Bluetooth Connection Problem.");
                    Log.e("Error : ",""+e.getMessage());

                }
            }
        });
        thread.start();
    }
}
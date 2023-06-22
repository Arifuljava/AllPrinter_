package com.messas.cpclprintersdk;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ShowAllPairdDevices extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private ListView deviceListView;
    private DeviceListAdapter  deviceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_paird_devices);
        deviceListView = findViewById(R.id.deviceListView);

        // Get the default Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if Bluetooth is supported on the device
        if (bluetoothAdapter == null) {
            // Bluetooth is not supported
            return;
        }

        // Check if Bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled, request user to enable it
            // You can use startActivityForResult to handle the result
            bluetoothAdapter.enable();
        }

        // Get a list of paired devices
        //Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        List<BluetoothDevice> deviceList = new ArrayList<>(pairedDevices);

        // Create an ArrayAdapter and set it as the ListView adapter
        deviceListAdapter = new DeviceListAdapter(deviceList);
        deviceListView.setAdapter(deviceListAdapter);
    }
    private class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

        public DeviceListAdapter(List<BluetoothDevice> devices) {
            super(ShowAllPairdDevices.this, R.layout.showalllisted, devices);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = getLayoutInflater();
                itemView = inflater.inflate(R.layout.showalllisted, parent, false);
            }

            BluetoothDevice device = getItem(position);
            if (device != null) {
                TextView nameTextView = itemView.findViewById(R.id.listedd);
                CardView carditem=itemView.findViewById(R.id.carditem);
                String BlueMac = "FB:7F:9B:F2:20:B7";
                mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                mBluetoothAdapter = mBluetoothManager.getAdapter();
                final BluetoothDevice device22 = mBluetoothAdapter.getRemoteDevice(BlueMac);



                carditem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ShowAllPairdDevices.this, ""+device.getAddress(), Toast.LENGTH_SHORT).show();
                        String BlueMac = "FB:7F:9B:F2:20:B7";
                        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                        mBluetoothAdapter = mBluetoothManager.getAdapter();
                        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);
                        Intent intent=new Intent(getApplicationContext(),CPCLFresh.class);
                        intent.putExtra("geeet",""+device.getAddress());
                        startActivity(intent);
                        ///

                        ////
                      /*
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    /// Toast.makeText(AssenTaskDounwActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                    if (ActivityCompat.checkSelfPermission(ShowAllPairdDevices.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                        m5ocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                                        m5ocket.connect();
                                        Log.d("Connect","Conncted");









                                    }
                                    else {

                                    }




                                } catch (IOException e) {
                                    Log.e("Error", ""+e.getMessage());
                                    Toast.makeText(ShowAllPairdDevices.this, "Try Again. Bluetooth Connection Problem.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        thread.start();
                       */
                    }
                });


                nameTextView.setText(device.getName()+"\n"+device.getAddress());


            }

            return itemView;
        }
    }
    BluetoothSocket m5ocket;
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice device;
}

package com.messas.cpclprintersdk;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class SecondList extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private ListView deviceListView;
    private DeviceListAdapter deviceListAdapter;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 1;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceListAdapter.addDevice(device);
                deviceListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_list);
        // Check if the permission is already granted
        if (isFineLocationPermissionGranted()) {
            // Permission is granted, you can proceed with your logic
            // ...
        } else {
            // Permission is not granted, request it from the user
            requestFineLocationPermission();
        }

        deviceListView = findViewById(R.id.deviceListView);

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

        // Create a custom ArrayAdapter and set it as the ListView adapter
        deviceListAdapter = new DeviceListAdapter(this);
        deviceListView.setAdapter(deviceListAdapter);

        // Register the BroadcastReceiver to receive Bluetooth discovery events
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);


        // Start device discovery
        bluetoothAdapter.startDiscovery();
        boolean aaa = bluetoothAdapter.isDiscovering();
        Toast.makeText(SecondList.this, ""+aaa, Toast.LENGTH_SHORT).show();
    }
    private boolean isFineLocationPermissionGranted() {
        // Check if the permission is granted
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestFineLocationPermission() {
        // Request the permission from the user
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_FINE_LOCATION_PERMISSION) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with your logic
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
                // ...
            } else {
                // Permission denied, handle the situation accordingly (e.g., show an explanation or disable functionality)
                // ...
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop device discovery and unregister the BroadcastReceiver
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(receiver);
    }

    private class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

        public DeviceListAdapter(Context context) {
            super(context, R.layout.list_item_device);
        }

        public void addDevice(BluetoothDevice device) {
            // Add the device to the adapter
            if (!getDevices().contains(device)) {
                add(device);
            }
        }

        public List<BluetoothDevice> getDevices() {
            // Get the list of devices
            List<BluetoothDevice> devices = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                devices.add(getItem(i));
            }
            return devices;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.list_item_device, parent, false);
            }

            BluetoothDevice device = getItem(position);
            if (device != null) {
                TextView nameTextView = view.findViewById(R.id.deviceNameTextView);
                TextView addressTextView = view.findViewById(R.id.deviceAddressTextView);

                nameTextView.setText(device.getName());
                addressTextView.setText(device.getAddress());
             Toast.makeText(SecondList.this, ""+device.getAddress(), Toast.LENGTH_SHORT).show();
            }
            else
            {
              //  Toast.makeText(SecondList.this, "ffff", Toast.LENGTH_SHORT).show();
            }

            return view;
        }
    }
}
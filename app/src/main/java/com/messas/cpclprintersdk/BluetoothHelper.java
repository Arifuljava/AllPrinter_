package com.messas.cpclprintersdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

public class BluetoothHelper {

    private BluetoothAdapter bluetoothAdapter;

    public BluetoothHelper() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public boolean isBluetoothConnected(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false; // Bluetooth is disabled
        }

        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            return false;
        }

        for (BluetoothDevice device : bluetoothManager.getConnectedDevices(BluetoothProfile.GATT)) {
            if (device.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC ||
                    device.getType() == BluetoothDevice.DEVICE_TYPE_LE ||
                    device.getType() == BluetoothDevice.DEVICE_TYPE_DUAL) {
                return true; // Bluetooth is connected
            }
        }

        return false; // No connected Bluetooth devices
    }
}
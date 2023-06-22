package com.messas.cpclprintersdk;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {
    private LayoutInflater inflater;
    private int resourceId;

    public BluetoothDeviceAdapter(Context context, int resourceId, List<BluetoothDevice> devices) {
        super(context, resourceId, devices);
        inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(resourceId, parent, false);
        }

        BluetoothDevice device = getItem(position);
        TextView deviceNameTextView = view.findViewById(R.id.deviceNameTextView);
        TextView deviceAddressTextView = view.findViewById(R.id.deviceAddressTextView);

        deviceNameTextView.setText(device.getName());
        deviceAddressTextView.setText(device.getAddress());

        return view;
    }
}

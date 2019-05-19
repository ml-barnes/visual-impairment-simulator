package com.op.barnm7.vis;

import android.Manifest;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

public class SyncActivity extends AppCompatActivity implements MyAdapter.RecyclerViewClickListener {

    public final static int BLUETOOTH_REQUEST = 12;
    public BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver b1;
    private BroadcastReceiver b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final ArrayList<CustomDevice> devs = new ArrayList<>();
        final ArrayList<BluetoothDevice> btdevs = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.mRecyerID);
        final MyAdapter ca = new MyAdapter(devs, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ca);

        b1 = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                     //Create a new device item
                    //Log.d("TEST", device.getName());
                    if (device.getName() != null && device.getBondState() == BluetoothDevice.BOND_NONE)
                    {
                        if (!btdevs.contains(device))
                        {
                            btdevs.add(device);
                            devs.add(new CustomDevice(device));
                            ca.notifyDataSetChanged();
                        }
                    }
                }
            }
        };

        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions
        (
            this,
            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
        );

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(b1, filter);
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void recyclerViewListClicked(CustomDevice device) {

//        Log.d("TEST_DEVICE", String.valueOf(device.getBtd()));
//
//        long l = 12345678910L;
//        long l2 = 12345678910L;

        mBluetoothAdapter.cancelDiscovery();

        boolean bonded = device.getBtd().createBond();
        Log.d("TEST BOND", String.valueOf(bonded));

//        ConnectThread ct = new ConnectThread(device.getBtd());
//        ct.run();

        b2 = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();

                ProgressBar pb = findViewById(R.id.progressBar);
                if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
                {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    final int bondState = device.getBondState();
                    Log.d("TEST B", String.valueOf(bondState));

                    switch (bondState)
                    {
                        case BluetoothDevice.BOND_BONDING:
                            pb.setVisibility(View.VISIBLE);
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(SyncActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                            setResultCode(RESULT_OK);
                            finish();
                            break;
                        case BluetoothDevice.BOND_NONE:
                            pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(SyncActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            break;

                    }

                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(b2, filter);


    }


}

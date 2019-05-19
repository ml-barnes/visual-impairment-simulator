package com.op.barnm7.vis;

import android.bluetooth.BluetoothDevice;

public class CustomDevice {

    private BluetoothDevice btd;
    private String name;

    public CustomDevice(BluetoothDevice btd) {
        this.btd = btd;
        name = btd.getName();

    }

    public CustomDevice() {

        name = "TEST";

    }

    @Override
    public String toString()
    {
        return name;
    }

    public BluetoothDevice getBtd() {
        return btd;
    }

    public void setBtd(BluetoothDevice btd) {
        this.btd = btd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

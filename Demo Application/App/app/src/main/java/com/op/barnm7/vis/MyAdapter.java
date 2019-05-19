package com.op.barnm7.vis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    // All classes using this adapter must implement this interface
    public interface RecyclerViewClickListener
    {
        void recyclerViewListClicked(CustomDevice device);
    }

    private final ArrayList<CustomDevice> devices;
    private final RecyclerViewClickListener listener;

    public MyAdapter(ArrayList<CustomDevice> devices, RecyclerViewClickListener listener) {

        this.devices = devices;
        this.listener = listener;
    }

    //INITIALIE VH
    @Override
    public MyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout,parent,false);
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    //BIND DATA
    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {
        holder.bind(devices.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        TextView nameTxt;

        MyHolder(View itemView)
        {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.custom_tv);

        }
        public void bind(final CustomDevice device, final RecyclerViewClickListener listener)
        {
            nameTxt.setText(device.getName());
            nameTxt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v)
                {
                    listener.recyclerViewListClicked(device);
                }
            });


        }
    }
}
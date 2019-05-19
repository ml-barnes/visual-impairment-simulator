package com.op.barnm7.vis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Filter extends FrameLayout
{
    private LayoutInflater mInflater;
    private ImageView i;

    public Filter(Context context) {
        super(context);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.over, this);
        i = findViewById(R.id.imageView);
    }

    public void changeImage(int resID)
    {
        i.setImageResource(resID);
    }

    public void hideText()
    {
        Button b2 = findViewById(R.id.button2);
        b2.setVisibility(INVISIBLE);
        Button b3 = findViewById(R.id.button3);
        b3.setVisibility(INVISIBLE);
    }

    public void showText()
    {
        Button b2 = findViewById(R.id.button2);
        b2.setVisibility(VISIBLE);
        Button b3 = findViewById(R.id.button3);
        b3.setVisibility(VISIBLE);
    }


    public void changeText( String impairmentName)
    {
        Button b2 = findViewById(R.id.button2);
        b2.setText(impairmentName);
        Button b3 = findViewById(R.id.button3);
        b3.setText(impairmentName);
        b2.bringToFront();
        b3.bringToFront();
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public ImageView getI() {
        return i;
    }

    public void setI(ImageView i) {
        this.i = i;
    }
}

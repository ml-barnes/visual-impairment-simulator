package com.op.barnm7.vis;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class CustomPanoramaView extends VrPanoramaView {

    private Context c;

    public CustomPanoramaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        c = context;
    }

    @Override
    public void setDisplayMode(int newDisplayMode) {
        super.setDisplayMode(newDisplayMode);
    }

    @Override
    public void resumeRendering() {
        super.resumeRendering();
    }
}


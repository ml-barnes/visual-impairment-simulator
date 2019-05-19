package com.op.barnm7.vis;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;

import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;


public class Blurred extends Impairment
{

    public Blurred(PanoViewData pvd)
    {
        super(pvd);
        imageNames = new String[] {"b1.jpg", "b2.jpg", "b3.jpg", "b4.jpg"};
    }

    @Override
    public void run()
    {
        // After button has been press the first time
        if (count == 1)
        {
            wm.addView(filter, params);
            filter.changeText("Cataracts");
            filter.showText();
        }
        loadPanoImage(imageNames[count]);
        incrementIndex();
    }
}

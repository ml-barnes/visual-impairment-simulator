package com.op.barnm7.vis;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Vibrator;
import android.view.WindowManager;

public class ImpairmentFilter extends Impairment {

    protected String impairmentName;
    protected String backgroundImageName;


    public ImpairmentFilter(PanoViewData pvd, ImpairmentData id)
    {
        super(pvd);
        this.imageNames = id.getImageNames();
        this.backgroundImageName = id.getBackgroundImageName();
        this.impairmentName = id.getImpairmentName();
    }

    @Override
    protected void run()
    {
        if (!loaded)
        {
            //Load image - This will trigger on load success event which will reload text when image has loaded
            loadPanoImage(backgroundImageName);
        }
        else
        {
            int resID = res.getIdentifier(imageNames[count], "drawable", "com.op.barnm7.vis");
            filter.changeImage(resID);
            incrementIndex();
        }
    }

    @Override
    protected void setUp()
    {
        wm.addView(filter, params);
        filter.changeText(impairmentName);
        loaded = true;
    }

}

package com.op.barnm7.vis;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class Impairment
{
    protected int count;
    protected boolean finished;
    protected CustomPanoramaView panoWidgetView;
    protected ImageLoaderTask backgroundImageLoaderTask;
    protected AssetManager am;
    protected Filter filter;
    protected WindowManager wm;
    protected WindowManager.LayoutParams params;
    protected String[] imageNames;
    protected Resources res;
    protected boolean loaded;

    public Impairment(PanoViewData pvd)
    {
        this.panoWidgetView = pvd.getPanoWidgetView();
        this.backgroundImageLoaderTask = pvd.getBackgroundImageLoaderTask();
        this.am = pvd.getAm();
        this.filter = pvd.getFilter();
        this.wm = pvd.getWm();
        this.params = pvd.getParams();
        this.res = pvd.getRes();

        count = 0;
        finished = false;
        loaded = false;
    }

    public void init()
    {
        count = 0;
        finished = false;
        loaded = false;
    }

    public void incrementIndex()
    {
        // At last image finish
        if (count == imageNames.length - 1)
        {
            finished = true;
        }
        else
        {
            count++;
        }
    }

    public synchronized void loadPanoImage(String imageName)
    {
        ImageLoaderTask task = backgroundImageLoaderTask;

        if (task != null && !task.isCancelled())
        {
            // Cancel any task from a previous loading.
            task.cancel(true);
        }

        // pass in the name of the image to load from assets.
        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
        viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;

        // create the task passing the widget view and call execute to start.
        task = new ImageLoaderTask(panoWidgetView, viewOptions, imageName);
        task.execute(am);
        backgroundImageLoaderTask = task;
    }

    protected void removeFilter()
    {
        wm.removeView(filter);
        int resID = res.getIdentifier("nofilter", "drawable", "com.op.barnm7.vis");
        filter.changeImage(resID);
    }

    protected void run()
    {
    }

    protected void setUp()
    {

    }
}

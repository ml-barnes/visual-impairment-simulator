package com.op.barnm7.vis;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Vibrator;
import android.view.WindowManager;

public class PanoViewData {

    private CustomPanoramaView panoWidgetView;
    private ImageLoaderTask backgroundImageLoaderTask;
    private AssetManager am;
    private Filter filter;
    private WindowManager wm;
    private WindowManager.LayoutParams params;
    private Resources res;


    public PanoViewData(AssetManager am, CustomPanoramaView panoWidgetView, ImageLoaderTask backgroundImageLoaderTask,
                        Filter filter, WindowManager wm, WindowManager.LayoutParams params, Resources res)
    {
        this.panoWidgetView = panoWidgetView;
        this.backgroundImageLoaderTask = backgroundImageLoaderTask;
        this.am = am;
        this.filter = filter;
        this.wm = wm;
        this.params = params;
        this.res = res;
    }

    public CustomPanoramaView getPanoWidgetView() {
        return panoWidgetView;
    }

    public void setPanoWidgetView(CustomPanoramaView panoWidgetView) {
        this.panoWidgetView = panoWidgetView;
    }

    public ImageLoaderTask getBackgroundImageLoaderTask() {
        return backgroundImageLoaderTask;
    }

    public void setBackgroundImageLoaderTask(ImageLoaderTask backgroundImageLoaderTask) {
        this.backgroundImageLoaderTask = backgroundImageLoaderTask;
    }

    public AssetManager getAm() {
        return am;
    }

    public void setAm(AssetManager am) {
        this.am = am;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public WindowManager getWm() {
        return wm;
    }

    public void setWm(WindowManager wm) {
        this.wm = wm;
    }

    public WindowManager.LayoutParams getParams() {
        return params;
    }

    public void setParams(WindowManager.LayoutParams params) {
        this.params = params;
    }

    public Resources getRes() {
        return res;
    }

    public void setRes(Resources res) {
        this.res = res;
    }
}

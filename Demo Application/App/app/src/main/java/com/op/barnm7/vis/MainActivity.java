package com.op.barnm7.vis;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import java.util.ArrayList;
import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.controller.Controller;
import com.google.vr.sdk.controller.Controller.ConnectionStates;
import com.google.vr.sdk.controller.ControllerManager;
import com.google.vr.sdk.controller.ControllerManager.ApiStatus;

public class MainActivity extends GvrActivity {

    public final static int VIBRATE_TIME = 400;
    private CustomPanoramaView panoWidgetView;
    private ImageLoaderTask backgroundImageLoaderTask;
    private int imageIndex;
    private Button button;
    private boolean triggered;
    private WindowManager wm;
    private WindowManager.LayoutParams params;
    private Filter filter;
    private Impairment currentImpairment;
    private int impairmentCount;
    private int narrowCount;
    private ArrayList<Impairment> impairments;
    private Vibrator v;
    private AssetManager a;
    private Resources r;
    private PanoViewData pvd;
    private SparseArray<ImpairmentData> dataMap = new SparseArray<>();
    //Handler for controller
    private Handler uiHandler = new Handler();

    //Controller objects
    private ControllerManager controllerManager;
    private Controller controller;
    private int controllerState = ConnectionStates.DISCONNECTED;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.App);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runApp();
//
        EventListener listener = new EventListener();
        controllerManager = new ControllerManager(MainActivity.this, listener);
        controller = controllerManager.getController();
        controller.setEventListener(listener);


        AndroidCompat.setVrModeEnabled(this, true);

        controllerManager.start();





        // Set up controller

    }

    public void runApp()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setUpOverlay();
        impairmentCount = 0;
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        filter = new Filter(this);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        panoWidgetView = findViewById(R.id.pano_view);
        panoWidgetView.setTransitionViewEnabled(false);
        panoWidgetView.setEventListener(new PanoramaEventHandler());
        panoWidgetView.setFullscreenButtonEnabled(false);

        //cl.setForeground(d);
        //panoWidgetView.setForeground(d);
        dataMap = createImpairmentData();
        pvd = new PanoViewData(getAssets(), panoWidgetView,
                backgroundImageLoaderTask, filter, wm, params, getResources());
        currentImpairment = new Blurred(pvd);
        currentImpairment.run();
    }

    protected class PanoramaEventHandler extends VrPanoramaEventListener
    {
        @Override
        public void onClick()
        {
            //v.vibrate(2000);//VibrationEffect.createWaveform(new long[] {VIBRATE_TIME}, 0));
            //runImpairment();
        }
        @Override
        public void onLoadSuccess()
        {
            if (!currentImpairment.loaded)
            {
                currentImpairment.setUp();
            }
        }

        @Override
        public void onDisplayModeChanged(int displayMode)
        {
            if (displayMode == 1)
            {
                filter.hideText();
                onDestroy();
                finishAffinity();
            }
        }
    }

    public SparseArray<ImpairmentData> createImpairmentData()
    {
        dataMap = new SparseArray<ImpairmentData>();

        dataMap.put(0, new ImpairmentData(new String[]{ "dr1", "dr2", "dr3", "dr4"},
                "Diabetic Retinopathy", "eden.jpg"));

        dataMap.put(1, new ImpairmentData(new String[]{ "dry_amd_mild3", "dry_amd_mild2", "dry_amd_mild1", "dry_amd"},
                "AMD", "hub.jpg"));

        dataMap.put(2, new ImpairmentData(new String[]{ "narrowblur", "narrowblur2", "narrowblur4", "narrowblur6", "narrowblur7"},
                "Glaucoma", "poly.jpg"));

        dataMap.put(3, new ImpairmentData(new String[]{ "hh1", "hh2", "hh3", "h", "h2", "h3"},
                "Hemianopia", "sun.jpg"));

        return dataMap;

    }

    public void runImpairment()
    {
        if (currentImpairment.finished)
        {
            currentImpairment.removeFilter();

            if (impairmentCount == dataMap.size())
            {
               currentImpairment = new Blurred(pvd);
               impairmentCount = 0;
            }
            else
            {
                currentImpairment = new ImpairmentFilter(pvd, dataMap.get(impairmentCount));
                impairmentCount++;
            }
        }
        currentImpairment.run();
    }



    @Override
    public void onPause() {
        tryRemoveFilter();
        panoWidgetView.pauseRendering();
        super.onPause();
    }

    @Override
    public void onResume() {
        panoWidgetView.resumeRendering();
        currentImpairment.setUp();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // Destroy the widget and free memory.
        tryRemoveFilter();
        panoWidgetView.pauseRendering();
        panoWidgetView.shutdown();
        super.onDestroy();
    }

    public void tryRemoveFilter()
    {
        try
        {
            wm.removeView(filter);
        }
        catch (RuntimeException e)
        {

        }
    }

    public void setUpOverlay()
    {
        final DisplayMetrics metrics = new DisplayMetrics();
        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION);


//        if (android.os.Build.VERSION.SDK_INT < 26)
//        {
//            params = new WindowManager.LayoutParams
//            (
//                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
//            );
//        }

        params.format = PixelFormat.TRANSLUCENT;

        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    private class EventListener extends Controller.EventListener implements ControllerManager.EventListener, Runnable {

        // The status of the overall controller API. This is primarily used for error handling since
        // it rarely changes.
        private String apiStatus;
        private Boolean clicked = false;

        // The state of a specific Controller connection.
        private int controllerState = ConnectionStates.DISCONNECTED;

        @Override
        public void onApiStatusChanged(int state) {
            apiStatus = ApiStatus.toString(state);
            uiHandler.post(this);
        }

        @Override
        public void onConnectionStateChanged(int state) {
            controllerState = state;
            uiHandler.post(this);
        }

        @Override
        public void onRecentered() {
            // In a real GVR application, this would have implicitly called recenterHeadTracker().
            // Most apps don't care about this, but apps that want to implement custom behavior when a
            // recentering occurs should use this callback.
        }

        @Override
        public void onUpdate() {
            uiHandler.post(this);
        }

        // Update the various TextViews in the UI thread.
        @Override
        public void run() {

            controller.update();






//            if (controller.clickButtonState)
//            {
//                v.vibrate(2000);//VibrationEffect.createWaveform(new long[] {VIBRATE_TIME}, 0));
//                runImpairment();
//
//            }


            if (controller.clickButtonState)
            {

                if (!clicked)
                {
                    v.vibrate(200);//VibrationEffect.createWaveform(new long[] {VIBRATE_TIME}, 0));
                    runImpairment();
                }
                clicked = true;
            }
            else
            {
                clicked = false;
            }
        }
    }
}


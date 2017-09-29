package com.ford.openxc.webcam.webcam;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;

import com.socks.library.KLog;

public class WebcamService extends Service {
    private final static String TAG = "WebcamManager";

    //usb摄像头设备
    public final static String VIDEO = "/dev/video0";

    private String video = VIDEO;

    private final IBinder mBinder = new WebcamBinder();
    private IWebcam mWebcam;

    public class WebcamBinder extends Binder {
        public WebcamService getService() {
            return WebcamService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.w(TAG, "Service onStartCommand");
        video = intent.getStringExtra("video");

        KLog.w(TAG, "video: " + video);

        // TODO: 16-3-22 修改video设备号
        mWebcam = new NativeWebcam(video);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        KLog.w(TAG, "Service onCreate");
        super.onCreate();

        // TODO: 16-3-22 修改video设备号
        mWebcam = new NativeWebcam(VIDEO);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.w(TAG, "Service being destroyed");
        mWebcam.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        KLog.w(TAG, "Service binding in response to " + intent);
        return mBinder;
    }

    public Bitmap getFrame() {
/*        if (mWebcam == null) {
            return null;
        }*/

        if (!mWebcam.isAttached()) {
            stopSelf();
        }
        return mWebcam.getFrame();
    }
}

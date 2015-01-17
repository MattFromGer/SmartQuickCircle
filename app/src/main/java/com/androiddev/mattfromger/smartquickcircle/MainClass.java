/**
 * Created by MattFromGer on 11/01/15.
 */

package com.androiddev.mattfromger.smartquickcircle;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class MainClass implements IXposedHookLoadPackage {

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        //XposedBridge.log("Loaded app: " + lpparam.packageName);
        //if (!lpparam.packageName.equals("com.android.keyguard"))
        if (!lpparam.packageName.equals("com.android.keyguard"))
            return;

        XposedBridge.log("We are in QuickCircle!!");
        findAndHookMethod("com.lge.lockscreen.LgeQuickCoverOverlayWindow", lpparam.classLoader, "sendSmartLightingMsg", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("SCC kicks in!! " + param.args[0].toString());
                Context mContext = (Context) getObjectField(param.thisObject, "mContext");

                //check if QA is installed
                PackageManager myPM = mContext.getPackageManager();
                if (isPackageInstalled("com.yavst.quickapps", mContext)) {
                    //check if music is playing
                    AudioManager myAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    if (myAM.isMusicActive()) {
                        //start activity
                        XposedBridge.log("SCC playing music detected!!");
                        Intent myIntent = new Intent("com.lge.quickcover");
                        ComponentName myComponent = new ComponentName("com.yoavst.quickapps", "com.yoavst.quickapps.music.CMusicActivity");
                        //ComponentName myComponent = new ComponentName("com.lge.music", "com.lge.music.MusicQuickCircle");
                        myIntent.setComponent(myComponent);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(myIntent);
                    }
                }
            }
        });
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager myPM = context.getPackageManager();
        try {
            myPM.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}

/**
 * Created by MattFromGer on 11/01/15.
 */

package com.androiddev.mattfromger.smartquickcircle;

import android.util.Log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import android.graphics.Color;
import android.widget.TextView;

public class MainClass implements IXposedHookLoadPackage {

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        XposedBridge.log("SmartQuickCircle app working!");

        findAndHookMethod("com.android.keyguard.KeyguardViewManager", lpparam.classLoader, "showQuickCoverWindow", new XC_MethodHook() {
        //findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("lloolololololololol");
                Log.v("com.androiddev.mattfromger.smartquickcircle","lololololololololololol");
            }
        });
    }
}

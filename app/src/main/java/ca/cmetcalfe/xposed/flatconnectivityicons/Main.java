package ca.cmetcalfe.xposed.flatconnectivityicons;

import android.content.res.XModuleResources;
import android.os.Build;

import java.util.HashMap;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

public class Main implements IXposedHookZygoteInit, IXposedHookInitPackageResources {
    private static String MODULE_PATH = null;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals("com.android.systemui"))
            return;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN ||
                Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            XposedBridge.log("The FlatConnectivityIcons Xposed module will only work on " +
                    "Jelly Bean and KitKat - exiting now");
            return;
        }

        // Replace the stock connectivity icons with our own versions
        HashMap<String, Integer> icons = new HashMap<String, Integer>();
        // Mobile
        icons.put("stat_sys_signal_0",       R.drawable.stat_sys_signal_0);
        icons.put("stat_sys_signal_0_fully", R.drawable.stat_sys_signal_0); //sic
        icons.put("stat_sys_signal_1",       R.drawable.stat_sys_signal_1);
        icons.put("stat_sys_signal_1_fully", R.drawable.stat_sys_signal_1_fully);
        icons.put("stat_sys_signal_2",       R.drawable.stat_sys_signal_2);
        icons.put("stat_sys_signal_2_fully", R.drawable.stat_sys_signal_2_fully);
        icons.put("stat_sys_signal_3",       R.drawable.stat_sys_signal_3);
        icons.put("stat_sys_signal_3_fully", R.drawable.stat_sys_signal_3_fully);
        icons.put("stat_sys_signal_4",       R.drawable.stat_sys_signal_4);
        icons.put("stat_sys_signal_4_fully", R.drawable.stat_sys_signal_4_fully);
        icons.put("stat_sys_signal_null",    R.drawable.stat_sys_signal_null);
        // WiFi
        icons.put("stat_sys_wifi_signal_0",       R.drawable.stat_sys_wifi_signal_0);
        icons.put("stat_sys_wifi_signal_1",       R.drawable.stat_sys_wifi_signal_1);
        icons.put("stat_sys_wifi_signal_1_fully", R.drawable.stat_sys_wifi_signal_1_fully);
        icons.put("stat_sys_wifi_signal_2",       R.drawable.stat_sys_wifi_signal_2);
        icons.put("stat_sys_wifi_signal_2_fully", R.drawable.stat_sys_wifi_signal_2_fully);
        icons.put("stat_sys_wifi_signal_3",       R.drawable.stat_sys_wifi_signal_3);
        icons.put("stat_sys_wifi_signal_3_fully", R.drawable.stat_sys_wifi_signal_3_fully);
        icons.put("stat_sys_wifi_signal_4",       R.drawable.stat_sys_wifi_signal_4);
        icons.put("stat_sys_wifi_signal_4_fully", R.drawable.stat_sys_wifi_signal_4_fully);
        icons.put("stat_sys_wifi_signal_null",    R.drawable.stat_sys_wifi_signal_null);

        XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        for (String key : icons.keySet()) {
            try {
                resparam.res.setReplacement("com.android.systemui", "drawable", key,
                        modRes.fwd(icons.get(key)));
            } catch (Throwable t) {
                // Drawable not found - ignore it
            }
        }
    }
}
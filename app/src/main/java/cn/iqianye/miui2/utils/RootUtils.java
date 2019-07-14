package cn.iqianye.miui2.utils;

import com.jaredrummler.android.shell.Shell;
/**
 * Utils for root action
 */
public class RootUtils {

    private RootUtils() {

    }

    /**
     * 重启SystemUI
     */
    public static void restartSystemUI() {
        killAll("com.android.systemui");
    }

    /**
     * killall <process name>
     *
     * @param processName process name
     */
    private static void killAll(String processName) {
        String cmd = String.format("killall %s", processName);
        Shell.SU.run(cmd);
    }

    /**
     * 重启
     */
    public static void reboot() {
        Shell.SU.run("reboot");
    }

    /**
     * 软重启
     */
    public static void softReboot() {
        Shell.SU.run("setprop ctl.restart surfaceflinger; setprop ctl.restart zygote");
    }

}

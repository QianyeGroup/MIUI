package cn.iqianye.miui.utils;

import com.jaredrummler.android.shell.Shell;
/**
 * Utils for root action
 */
public class RootUtils {

    private RootUtils() {

    }

    /**
     * Restart SystemUI
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
     * Reboot
     */
    public static void reboot() {
        Shell.SU.run("reboot");
    }

    /**
     * Soft Reboot
     */
    public static void softReboot() {
        Shell.SU.run("setprop ctl.restart surfaceflinger; setprop ctl.restart zygote");
    }

}

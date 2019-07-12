package cn.iqianye.miui.utils;
import com.jaredrummler.android.shell.Shell;
import android.os.Build;
import com.stericson.RootTools.RootTools;
import android.app.Activity;
import cn.iqianye.miui.MainActivity;
import android.widget.Toast;
import android.content.Context;

public class OtherUtils
{
    public static boolean isMIUI()
    {
        String s = Shell.SH.run("getprop ro.miui.ui.version.code").toString();
        if (s == "")
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    public static Boolean checkRoot()
    {
        if (RootTools.isRootAvailable())
        {
            if (!RootTools.isAccessGiven())
            {
                return false;
            }
        }
        else
        {
           return false;
        }
        return true;
    }
    public static boolean magiskCheck()
    {
        if (RootTools.exists("/data/adb/magisk", true))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

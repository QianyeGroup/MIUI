package cn.iqianye.miui.utils;
import com.jaredrummler.android.shell.Shell;

public class OtherUtils
{
    public static boolean isMIUI()
    {
        String s = Shell.SH.run("getprop ro.miui.ui.version.code").toString();
        if(s == "")
        {
            return false;
        }else
        {
            return true;
        }
        
    }
}

package cn.iqianye.miui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.iqianye.miui.utils.AssetsUtils;
import cn.iqianye.miui.utils.OtherUtils;
import cn.iqianye.miui.utils.RootUtils;
import cn.iqianye.miui.utils.XmlUtils;
import cn.iqianye.miui.utils.ZipUtils;
import com.jaredrummler.android.shell.Shell;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!OtherUtils.checkRoot()) // 检测ROOT
        {
            Toast.makeText(this, "获取ROOT权限失败，请检查是否已给予本软件ROOT权限！", Toast.LENGTH_LONG).show();
            finish();
        }
        if (!OtherUtils.isMIUI()) // 检测MIUI
        {
            Toast.makeText(this, "您使用的系统非MIUI，不能使用本软件！", Toast.LENGTH_LONG).show();
            finish();
        }
        TextView t = findViewById(R.id.device_TextView);
        t.setText(Build.BRAND + " " + Build.MODEL);
        RadioButton magisk = findViewById(R.id.magiskMode_radioButton);
        RadioButton system = findViewById(R.id.systemMode_radioButton);
        if (OtherUtils.magiskCheck()) // 检测Magisk
        {
            magisk.setChecked(true);
            AssetsUtils.copyFolderFromAssetsToSD(this, "Z-MiuiStatusBar", getExternalCacheDir().getAbsolutePath() + "/Z-MiuiStatusBar");
        }
        else
        {
            system.setChecked(true);
            magisk.setEnabled(false);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.reboot:
                AlertDialog.Builder b1 = new AlertDialog.Builder(this);
                b1.setTitle("确认");
                b1.setMessage("是否确认重启？");
                b1.setPositiveButton("是", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface d, int i)
                        {
                            RootUtils.reboot(); // 重启
                        }
                    });
                b1.setNegativeButton("否", null);
                b1.show();
                break;
            case R.id.soft_Reboot:
                AlertDialog.Builder b2 = new AlertDialog.Builder(this);
                b2.setTitle("确认");
                b2.setMessage("是否确认软重启？"); 
                b2.setPositiveButton("是", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface d, int i)
                        {
                            RootUtils.softReboot(); // 软重启
                        }
                    });
                b2.setNegativeButton("否", null);
                b2.show();
                break;
            case R.id.restart_SystemUI:
                AlertDialog.Builder b3 = new AlertDialog.Builder(this);
                b3.setTitle("确认");
                b3.setMessage("是否确认重启SystemUI？"); 
                b3.setPositiveButton("是", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface d, int i)
                        {
                            RootUtils.restartSystemUI(); // 重启SystemUI
                        }
                    });
                b3.setNegativeButton("否", null);
                b3.show();   
                break;
            case R.id.join_Group:
                joinQQGroup("POi0sGneG6tKk4sDLTHDevyaIFWH6C4a");
                break;
            case R.id.feedback:
                Intent data=new Intent(Intent.ACTION_SENDTO);  
                data.setData(Uri.parse("mailto:service@iqianye.cn"));  
                data.putExtra(Intent.EXTRA_SUBJECT, "【意见反馈】修改MIUI状态栏高度APP");  
                data.putExtra(Intent.EXTRA_TEXT, 
                              "设备型号：" 
                              + Build.BRAND + " " 
                              + Build.MODEL + " (" 
                              + Build.DEVICE + ")<br>"
                              + "系统版本："
                              + Shell.SH.run("getprop ro.miui.ui.version.name").toString() + " "
                              + Shell.SH.run("getprop ro.build.version.incremental").toString() + "<br>"
                              + "问题描述：<br><br>"
                              + "联系方式：<br>"

                              );  
                startActivity(data);
                break;
            default:
        }
        return true;
    }
    public void change_onClick(View view)
    {
        RadioButton system = findViewById(R.id.systemMode_radioButton);
        RadioButton magisk = findViewById(R.id.magiskMode_radioButton);
        EditText height = findViewById(R.id.height_editText);
        String path = getExternalCacheDir().getAbsolutePath();
        String filename = path + "/theme_values.xml";
        if (height.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "高度不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            XmlUtils.xmlSave(filename, height.getText().toString() + "dp");
            try
            {
                ZipUtils.zip(path + "/framework-res", "", filename);
            }
            catch (IOException e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (system.isChecked())
        {
            Shell.SU.run("cp -r " + path + "/framework-res /system/media/theme/default/");
            Shell.SU.run("chmod 644 /system/media/theme/default/framework-res");
            Toast.makeText(this, "修改成功，请点击右上角重启SystemUI查看效果", Toast.LENGTH_LONG).show();
        }
        else if (magisk.isChecked())
        {
            Shell.SU.run("cp -r " + path + "/framework-res " + path + "/Z-MiuiStatusBar/system/media/theme/default");
            Shell.SU.run("cp -r " + path + "/Z-MiuiStatusBar /data/adb/modules");
            Shell.SU.run("chmod 644 /data/adb/modules/Z-MiuiStatusBar/system/media/theme/default/framework-res");
            Shell.SU.run("chmod 644 /data/adb/modules/Z-MiuiStatusBar/module.prop");
            Toast.makeText(this, "修改成功，请点击右上角重启查看效果", Toast.LENGTH_LONG).show();
        }
    }
    public void delete_onClick(View view)
    {
        RadioButton system = findViewById(R.id.systemMode_radioButton);
        RadioButton magisk = findViewById(R.id.magiskMode_radioButton);
        if (system.isChecked())
        {
            Shell.SU.run("rm -rf /system/media/theme/default/framework-res");
            Toast.makeText(this, "还原成功，请点击右上角重启SystemUI查看效果", Toast.LENGTH_LONG).show();
        }
        else if (magisk.isChecked())
        {
            Shell.SU.run("rm -rf /data/adb/modules/Z-MiuiStatusBar");
            Toast.makeText(this, "还原成功，请点击右上角重启查看效果", Toast.LENGTH_LONG).show();
        }
    }
    public void superPower_onClick(View view)
    {
        Intent intent = new Intent();
        intent.setClassName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.ExtremePowerEntryActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void androidPower_onClick(View view)
    {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.fuelgauge.PowerUsageSummary");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void systemMode_onClick(View view)
    {
        RadioButton system = findViewById(R.id.systemMode_radioButton);
        RadioButton magisk = findViewById(R.id.magiskMode_radioButton);
        if (system.isChecked())
        {
            magisk.setChecked(false);
        }
    }
    public void magiskMode_onClick(View view)
    {
        RadioButton system = findViewById(R.id.systemMode_radioButton);
        RadioButton magisk = findViewById(R.id.magiskMode_radioButton);
        if (magisk.isChecked())
        {
            system.setChecked(false);
        }
    }

    /****************
     *
     * 发起添加群流程。群号：真心极客 - Redmi K20/Pro |(691802087) 的 key 为： POi0sGneG6tKk4sDLTHDevyaIFWH6C4a
     * 调用 joinQQGroup(POi0sGneG6tKk4sDLTHDevyaIFWH6C4a) 即可发起手Q客户端申请加群 真心极客 - Redmi K20/Pro |(691802087)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key)
    {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try
        {
            startActivity(intent);
            return true;
        }
        catch (Exception e)
        {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(this, "未安装手机QQ，或版本不支持。请安装或升级手机QQ！", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}

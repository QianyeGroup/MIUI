package cn.iqianye.miui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import cn.iqianye.miui.utils.RootUtils;
import cn.iqianye.miui.utils.XmlUtils;
import com.stericson.RootTools.RootTools;
import cn.iqianye.miui.utils.ZipUtil;
import java.io.IOException;
import cn.iqianye.miui.utils.AssetsUtils;
import com.jaredrummler.android.shell.Shell;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkRoot(); // 检测ROOT
        RadioButton magisk = findViewById(R.id.magiskMode_radioButton);
        RadioButton system = findViewById(R.id.systemMode_radioButton);
        if (magiskCheck()) // 检测Magisk
        {
            magisk.setChecked(true);
            AssetsUtils.copyFolderFromAssetsToSD(this, "Z-MiuiStatusBar",getExternalCacheDir().getAbsolutePath() + "/Z-MiuiStatusBar");
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
                checkRoot();
                RootUtils.reboot(); // 重启
                break;
            case R.id.soft_Reboot:
                checkRoot();
                RootUtils.softReboot(); // 软重启
                break;
            case R.id.restart_SystemUI:
                checkRoot();
                RootUtils.restartSystemUI(); // 重启SystemUI
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
        }else
        {
            XmlUtils.xmlSave(filename, height.getText().toString() + "dp");
            try
            {
                ZipUtil.zip(path + "/framework-res", "", filename);
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
            Toast.makeText(this, "修改成功，清点击右上角重启System查看效果", Toast.LENGTH_LONG).show();
        }
        else if (magisk.isChecked())
        {
            Shell.SU.run("cp -r " + path + "/framework-res " + path + "/Z-MiuiStatusBar/system/media/theme/default");
            Shell.SU.run("cp -r " + path + "/Z-MiuiStatusBar /data/adb/modules");
            Shell.SU.run("chmod 644 /data/adb/modules/Z-MiuiStatusBar/system/media/theme/default/framework-res");
            Shell.SU.run("chmod 644 /data/adb/modules/Z-MiuiStatusBar/module.prop");
            Toast.makeText(this, "修改成功，清点击右上角重启查看效果", Toast.LENGTH_LONG).show();
        }
    }
    public void delete_onClick(View view)
    {
        RadioButton system = findViewById(R.id.systemMode_radioButton);
        RadioButton magisk = findViewById(R.id.magiskMode_radioButton);
        if (system.isChecked())
        {
            Shell.SU.run("rm -rf /system/media/theme/default/framework-res");
            Toast.makeText(this, "还原成功，清点击右上角重启System查看效果", Toast.LENGTH_LONG).show();
        }
        else if (magisk.isChecked())
        {
            Shell.SU.run("rm -rf /data/adb/modules/Z-MiuiStatusBar");
            Toast.makeText(this, "还原成功，清点击右上角重启查看效果", Toast.LENGTH_LONG).show();
        }
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
    private void checkRoot()
    {
        if (RootTools.isRootAvailable())
        {
            if (!RootTools.isAccessGiven())
            {
                Toast.makeText(this, "ROOT权限获取失败，请检查！", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else
        {
            Toast.makeText(this, "您的设备没有ROOT，无法使用！", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private boolean magiskCheck()
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

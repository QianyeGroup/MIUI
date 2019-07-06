package cn.iqianye.miui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import com.stericson.RootTools.RootTools;
import com.stericson.RootShell.execution.Command;
import android.view.Menu;
import cn.iqianye.miui.utils.RootUtils;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkRoot();
        magiskCheck();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.reboot:
                checkRoot();
                RootUtils.reboot();
                break;
            case R.id.soft_Reboot:
                checkRoot();
                RootUtils.softReboot();
                break;
            case R.id.restart_SystemUI:
                checkRoot();
                RootUtils.restartSystemUI();
                break;
            default:
        }
        return true;
    }
    public void change_onClick(View view){
        Toast t = Toast.makeText(this, "APP制作的，暂时无法修改！", Toast.LENGTH_LONG);
        t.show();
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
                Toast t = Toast.makeText(this, "ROOT权限获取失败，请检查！", Toast.LENGTH_LONG);
                t.show();
                finish();
            }
        }
        else
        {
            Toast t = Toast.makeText(this, "您的设备没有ROOT，无法使用！", Toast.LENGTH_LONG);
            t.show();
            finish();
        }
    }
    private void magiskCheck()
    {
        if (RootTools.exists("/data/adb/magisk", true))
        {
            RadioButton r = findViewById(R.id.magiskMode_radioButton);
            r.setChecked(true);
        }
        else
        {
            RadioButton rr = findViewById(R.id.systemMode_radioButton);
            rr.setChecked(true);
        }
    }
}

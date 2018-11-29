package tx.a316.com.tx_teacher.activites;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import com.gyf.barlibrary.ImmersionBar;

public class welcomeActivity extends AppCompatActivity {
    //本地保存
    private SharedPreferences pref;
    private ImmersionBar immersionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this);
        immersionBar.init();
        MyApplication.finishAllActivities();
        //开线程,延迟跳转
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(TextUtils.isEmpty(pref.getString("t_tid",""))){
                    intent=new Intent(welcomeActivity.this,LoginActivity.class);
                }else {

                    MyApplication.setT_tid(pref.getString("t_tid",""));
                    MyApplication.setToken(pref.getString("token",""));

                    intent=new Intent(welcomeActivity.this,MainActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 1500);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }
}

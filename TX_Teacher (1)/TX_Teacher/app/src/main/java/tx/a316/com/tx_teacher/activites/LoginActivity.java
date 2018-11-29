package tx.a316.com.tx_teacher.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.Utils.MathTools;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.services.serviceImp.UserServiceImp;

public class LoginActivity extends BaseActivity {
     //绑定登录页面控件
    @BindView(R.id.login_s_gonghao_et)
    EditText loginSGonghaoEt;
    @BindView(R.id.login_s_pwd_et)
    EditText loginSPwdEt;
    @BindView(R.id.login_button)
    Button loginButton;
    //本地保存
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //与活动绑定
        ButterKnife.bind(this);

        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#0086F1");
        immersionBar.init();
        //获得本地存储控件
        pref= PreferenceManager.getDefaultSharedPreferences(getApplication());
        //获得写入权利
        editor=pref.edit();
    }

    @OnClick(R.id.login_button)
    public void onViewClicked() {
        showProgress();
        boolean flag=false;
        if(TextUtils.isEmpty(loginSGonghaoEt.getText().toString())){
            flag=true;
        }
        if(TextUtils.isEmpty(loginSPwdEt.getText().toString())){
            flag=true;
        }
        //如果账号或者密码没填，提示为空
        if (flag){
            Toast.makeText(this,"请填写工号密码后登录",Toast.LENGTH_SHORT).show();
        }
        //向后台提交输入的信息进行验证
        else{
            //进行用户验证
            UserServiceImp userServiceImp=new UserServiceImp(this);
            //密码md5加密
            String pwd= MathTools.getMd5(loginSPwdEt.getText().toString());
            String userId=this.loginSGonghaoEt.getText().toString();
           userServiceImp.loginValidate(userId,pwd);
        }
    }
 //网络访问用回调函数
    public void loginCallBack(String t_tid,String token){
        //登录成功
      this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
              Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
          }
      });
        //本地保存数据
        MyApplication.setT_tid(t_tid);
        MyApplication.setToken(token);
        editor.putString("t_tid",t_tid);
        editor.putString("token",token);
        //提交
        editor.apply();
        //环信密码永远为工号
        String username=this.loginSGonghaoEt.getText().toString();

        //注册环信
        try {
            EMClient.getInstance().createAccount(username,username);
        }catch (final HyphenateException e){

        }
        //登录到环信服务器
        EMClient.getInstance().login(username,username, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("main", "登录聊天服务器成功！");
                    }
                });

            }

            @Override
            public void onError(int code, String error) {
                Log.d("main", "登录聊天服务器失败！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
        //登录成功跳转至 主页面
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }
}

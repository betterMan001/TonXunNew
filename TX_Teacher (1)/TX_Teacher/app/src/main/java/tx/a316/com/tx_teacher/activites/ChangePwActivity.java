package tx.a316.com.tx_teacher.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.services.serviceImp.ChangePWServiceImp;

public class ChangePwActivity extends BaseActivity {
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    @BindView(R.id.changepw_old_et)
    EditText changepwOldEt;
    @BindView(R.id.changepw_new_et)
    EditText changepwNewEt;
    @BindView(R.id.changepw_new2_et)
    EditText changepwNew2Et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        ButterKnife.bind(this);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#00A7FF");
        immersionBar.init();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }

    @OnClick({R.id.changepw_back_ll, R.id.changepw_confirm_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //退回到上一个界面
            case R.id.changepw_back_ll:
                finish();
                break;
                //向服务器端提出修改密码请求
            case R.id.changepw_confirm_ll:
                changePassword();
                break;
        }
    }
    private void changePassword(){
        //1.三个输入框必须都不为空
        if(TextUtils.isEmpty(changepwOldEt.getText().toString())){
            Toast.makeText(this,"存在输入框为空！",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(changepwNewEt.getText().toString())){
            Toast.makeText(this,"存在输入框为空！",Toast.LENGTH_LONG).show();
        }
        else  if(TextUtils.isEmpty(changepwNew2Et.getText().toString())){
            Toast.makeText(this,"存在输入框为空！",Toast.LENGTH_LONG).show();
        }
        else{
              if(changepwNewEt.getText().toString().equals(changepwNew2Et.getText().toString())){
                  new ChangePWServiceImp(this).changePassword(MyApplication.getT_tid(),changepwOldEt.getText().toString(),changepwNewEt.getText().toString());
              }
              else Toast.makeText(this,"两次新密码输入不一致",Toast.LENGTH_LONG).show();
        }

    }

}

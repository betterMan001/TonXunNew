package tx.a316.com.tx_teacher.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tx.a316.com.tx_teacher.Models.StudentModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.services.serviceImp.PushServiceImp;

public class PushActivity extends BaseActivity {
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    //放回上一层界面按钮
    @BindView(R.id.push_back_ll)
    ImageView pushBackLl;
    //发布推送按钮
    @BindView(R.id.main_push_ib)
    ImageButton mainPushIb;
    //推送详情
    @BindView(R.id.push_detail_et)
    EditText pushDetailEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        ButterKnife.bind(this);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#00A7FF");
        immersionBar.init();
    }


    private void pushMessage() {
        //获得老师id
        String t_id = MyApplication.getT_tid();
        //获得学生列表,后台请求测试版
        String students = "";
        /*遍历测试*/
        List<StudentModel> stuList=MainActivity.stuList;
        int length=stuList.size();
        for(int i=0;i<length;i++){
            //吧被选中的学生打包到String
            if(MainActivity.booleanArray.get(i)==true){
                students+=(stuList.get(i).getId())+",";
            }
        }
        MainActivity.booleanArray=new SparseBooleanArray();
        String pushDetail = pushDetailEt.getText().toString();
        //发送推送
        new PushServiceImp(this).PushMessages(t_id, students, pushDetail);
    }

    //网络请求发送成功回调
    public void pushCallBack() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PushActivity.this, "推送发送成功！", Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }
   //相关控件的点击事件
    @OnClick({R.id.push_back_ll, R.id.main_push_ib, R.id.push_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回上一个界面
            case R.id.push_back_ll:
                finish();
                break;
                //推送消息
            case R.id.main_push_ib:
                pushMessage();
                break;
                //跳转至选择学生界面
            case R.id.push_rl:
                Intent intent=new Intent(PushActivity.this,SelectActivity.class);
                this.startActivity(intent);
                break;
        }
    }
}

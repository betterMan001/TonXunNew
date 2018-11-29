package tx.a316.com.tx_teacher.activites;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tx.a316.com.tx_teacher.Adapters.MultiSelectAdapter;
import tx.a316.com.tx_teacher.Models.StudentModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.services.serviceImp.StudentServiceImp;

public class SelectActivity extends BaseActivity {
    //沉浸式状态栏
    public ImmersionBar immersionBar;

    //学生列表RecycleView
    @BindView(R.id.select_rv)
    RecyclerView selectRv;
    @BindView(R.id.main_selectAll_tv)
    TextView mainSelectAllTv;
    private MultiSelectAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#00A7FF");
        immersionBar.init();
        //加载需要选择的RecycleView
        makeData();
    }

    private void initView() {
        //初始化学生列表RecycleView
        selectRv.setLayoutManager(new LinearLayoutManager(this));
         adapter=new MultiSelectAdapter(this, MainActivity.stuList);
        selectRv.setAdapter(adapter);
    }

    //从后台获取学生列表
    private void makeData() {
        MainActivity.stuList = new ArrayList<StudentModel>();
        new StudentServiceImp(this).getStuList(MyApplication.getT_tid());
    }

    //请求网络后回调，初始化视图
    public void callBack() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initView();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }

    //点击结束活动返回
    @OnClick({R.id.select_back_ll})
    public void back_Click() {
        finish();
        //复原多选默认值
        MainActivity.isCheckedAll=false;
    }

    @OnClick(R.id.main_selectAll_tv)
    public void selectAllClick() {
        MainActivity.isCheckedAll=true;
        //设置全选刷新视图
        int length=MainActivity.stuList.size();
        for(int i=0;i<length;i++)
        adapter.notifyItemChanged(i);
    }
}

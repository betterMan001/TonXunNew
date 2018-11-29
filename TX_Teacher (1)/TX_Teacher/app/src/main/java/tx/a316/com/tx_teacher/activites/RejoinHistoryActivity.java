package tx.a316.com.tx_teacher.activites;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tx.a316.com.tx_teacher.Adapters.RejoinHistoryDetailAdapter;
import tx.a316.com.tx_teacher.Models.RejoinDetailModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.services.serviceImp.RejonHistoryServiceImp;

public class RejoinHistoryActivity extends BaseActivity {
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    //所需要的二级界面的数据
    public static List<RejoinDetailModel> rejoinDetaillist;
    //供详细界面使用标记位置
    public static int pos = 0;
    @BindView(R.id.rejonDetail_rv)
    RecyclerView rejonDetailRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejoin_history);
        ButterKnife.bind(this);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#3d644a");
        immersionBar.init();
        makeData();
    }

    private void makeData() {
        //初始化数组并且从服务器请求数据
        rejoinDetaillist = new ArrayList<>();
        new RejonHistoryServiceImp(this).getRejoinDetailList(MyApplication.getT_tid(), RejoinHistoryListActivity.title);
    }

    //回调更新视图
    public void callBack() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initView();
            }
        });

    }

    public void initView() {
        //初始化RecyleView
     this.rejonDetailRv.setLayoutManager(new LinearLayoutManager(this));
     this.rejonDetailRv.setAdapter(new RejoinHistoryDetailAdapter(this,rejoinDetaillist));
    }

    @OnClick(R.id.rejoinDetail_back_ll)
    public void onViewClicked() {
        finish();
    }

}

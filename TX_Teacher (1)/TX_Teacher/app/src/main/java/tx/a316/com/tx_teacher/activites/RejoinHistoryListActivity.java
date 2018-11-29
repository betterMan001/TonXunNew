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
import tx.a316.com.tx_teacher.Adapters.RejoinHistoryAdapter;
import tx.a316.com.tx_teacher.Models.RejoinListModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.services.serviceImp.RejonHistoryServiceImp;

public class RejoinHistoryListActivity extends BaseActivity {
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    public List<RejoinListModel> rejonsList;
    //答辩标题供2级界面使用
    public static String title = "";
    @BindView(R.id.rejonList_rv)
    RecyclerView rejonListRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejoin_history_list);
        ButterKnife.bind(this);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#3d644a");
        immersionBar.init();
        //向服务器获取数据
        makeData();
    }

    private void makeData() {
        rejonsList = new ArrayList<>();
        new RejonHistoryServiceImp(this).getRejoinList(MyApplication.getT_tid());
    }

    private void initView() {
        //初始化RecycleView
        rejonListRv.setLayoutManager(new LinearLayoutManager(this));
        rejonListRv.setAdapter(new RejoinHistoryAdapter(this, rejonsList));
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }
    //回调函数，返回网络请求的数据初始化视图
    public void callBack() {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initView();
            }
        });
    }

    @OnClick(R.id.rejoinList_back_ll)
    public void onViewClicked() {
        finish();
    }
}

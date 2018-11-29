package tx.a316.com.tx_teacher.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tx.a316.com.tx_teacher.Models.RejoinDetailModel;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.widget.LineDividerTextView;

public class RejoinHistoryDetailActivity extends AppCompatActivity {

    @BindView(R.id.rejoinDatil_tv)
    LineDividerTextView rejoinDatilTv;
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    @BindView(R.id.rejoinDatil_title)
    TextView rejoinDatilTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejoin_history_detail);
        ButterKnife.bind(this);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#3d644a");
        immersionBar.init();
        initView();
    }

    private void initView() {
        RejoinDetailModel model = RejoinHistoryActivity.rejoinDetaillist.get(RejoinHistoryActivity.pos);
        //1.1还原问题序列
        String[] quList = (model.getDetail().split("#"));
        int length = quList.length;
        //1.2还原回答序列
        String[] reList = (model.getReviewDetail().split("#"));
        String finalResult = "";
        //生成回答的格式
        for (int i = 0; i < length; i++) {
            String qu = quList[i];
            String re = reList[i];
            String result = qu + "\n" + "回答:" + re + "\n\n";
            finalResult += result;
        }
        //显示到控件页面
        rejoinDatilTv.setText(finalResult);
        rejoinDatilTitle.setText(RejoinHistoryListActivity.title);
    }

    @OnClick(R.id.rejoin_back_ll)
    public void onViewClicked() {
        finish();
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }

}

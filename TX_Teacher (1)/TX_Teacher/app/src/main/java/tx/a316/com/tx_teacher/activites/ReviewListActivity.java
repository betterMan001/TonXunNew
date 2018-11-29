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
import tx.a316.com.tx_teacher.Adapters.NoteAdapter;
import tx.a316.com.tx_teacher.Models.NoteModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.services.serviceImp.NoteServiceImp;

public class ReviewListActivity extends BaseActivity {
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    public static List<NoteModel> notesList;
    //周记列表
    @BindView(R.id.note_rv)
    RecyclerView noteRv;
  //被选中的周记模型
    public  static NoteModel noteModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        ButterKnife.bind(this);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#79695C");
        immersionBar.init();
        //获取周记记录
        makeData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }

    //请求周记数据
    public void makeData() {
        //初始化noteList
        notesList = new ArrayList<NoteModel>();
        new NoteServiceImp(this).getNoteList(MyApplication.getT_tid());
    }

    //initView
    public void initView() {
       noteRv.setLayoutManager(new LinearLayoutManager(this));
       noteRv.setAdapter(new NoteAdapter(this,notesList));
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

    @OnClick(R.id.reviewlist_back_ll)
    public void onViewClicked() {
        finish();
    }
}

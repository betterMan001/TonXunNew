package tx.a316.com.tx_teacher.activites;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Adapters.MyPagerAdapter;
import tx.a316.com.tx_teacher.Adapters.PerMessageAdapter;
import tx.a316.com.tx_teacher.Adapters.SystemAdapter;
import tx.a316.com.tx_teacher.Models.MessageModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.services.serviceImp.MessageServiceImp;

public class MessageActivity extends BaseActivity {
    //View
    private View viewPager_person;
    private View viewPager_system;
    //控件
    private TabLayout message_tl;
    private ViewPager message_vp;
    //list
    private List<String> titleList;//title标题列表
    private List<View> viewList;//ViewPage列表
    //RecycleVoew
    private RecyclerView vp_person_rv;
    private RecyclerView vp_system_rv;
    //page
    private int person_page = 0;
    private int system_page = 0;
    //消息推送的数据列表
    List<MessageModel> sysList;
    List<MessageModel> perList;
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#00A7FF");
        immersionBar.init();
         makeData();

    }

    private void initView() {
        //init 控件
        message_tl = findViewById(R.id.message_tl);
        message_vp = findViewById(R.id.message_vp);
        //init View
        viewPager_person = getLayoutInflater().from(this).inflate(R.layout.viewpager_personal, null);
        viewPager_system = getLayoutInflater().from(this).inflate(R.layout.viewpager_system, null);
        /*init recView*/
        //2.1个人消息
        vp_person_rv = viewPager_person.findViewById(R.id.vp_person_rv);
        vp_person_rv.setLayoutManager(new LinearLayoutManager(this));
        vp_person_rv.setAdapter(new PerMessageAdapter(this, perList));
        //2.2系统消息
        vp_system_rv = viewPager_system.findViewById(R.id.sys_rv);
        vp_system_rv.setLayoutManager(new LinearLayoutManager(this));
        SystemAdapter systemAdapter = new SystemAdapter(this, sysList);
        vp_system_rv.setAdapter(systemAdapter);
        //init 列表
        titleList = new ArrayList<String>();
        viewList = new ArrayList<View>();
        titleList.add("个人通知");
        titleList.add("学校通知");
        viewList.add(viewPager_person);
        viewList.add(viewPager_system);
        //setting tab模式
        message_tl.setTabMode(TabLayout.MODE_FIXED);
        message_tl.addTab(message_tl.newTab().setText(titleList.get(0)));
        message_tl.addTab(message_tl.newTab().setText(titleList.get(1)));
        //viewPager适配
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(viewList, titleList);
        message_vp.setAdapter(myPagerAdapter);
        //tabLayout联动viewPage
        message_tl.setupWithViewPager(message_vp);
    }

    private void makeData() {
        sysList = new ArrayList<MessageModel>();
        perList = new ArrayList<MessageModel>();
        MessageServiceImp messageServiceImp=new MessageServiceImp(this);
        messageServiceImp.getMessages(MyApplication.getT_tid());
    }
    @OnClick(R.id.message_back_ll)
    public void backClicked() {
        //结束活动
        finish();
    }
    //回调函数
    public void messageCallBack(List<MessageModel> list) {
        for (MessageModel messageModel : list) {
            if (messageModel.getType() == 2) {
                sysList.add(messageModel);
            } else
                perList.add(messageModel);
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initView();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }
}

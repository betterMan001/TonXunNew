package tx.a316.com.tx_teacher.activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anlia.photofactory.factory.PhotoFactory;
import com.gyf.barlibrary.ImmersionBar;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Fragments.ContactListFragment;
import tx.a316.com.tx_teacher.Fragments.MineFragment;
import tx.a316.com.tx_teacher.Fragments.WorkFragment;
import tx.a316.com.tx_teacher.Models.StudentModel;
import tx.a316.com.tx_teacher.Models.TeacherModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
//TagAlia相关属性
import tx.a316.com.tx_teacher.Utils.JPushAliasAndTags.TagAliasOperatorHelper;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.services.serviceImp.TeacherInformationServiceImp;

import static tx.a316.com.tx_teacher.Utils.JPushAliasAndTags.TagAliasOperatorHelper.TagAliasBean;
import static tx.a316.com.tx_teacher.Utils.JPushAliasAndTags.TagAliasOperatorHelper.sequence;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.main_fl)
    FrameLayout mainFl;
    @BindView(R.id.tab_message_ib)
    ImageButton tabMessageIb;
    @BindView(R.id.tab_message_tv)
    TextView tabMessageTv;
    @BindView(R.id.tab_message_ll)
    LinearLayout tabMessageLl;
    @BindView(R.id.tab_work_ib)
    ImageButton tabWorkIb;
    @BindView(R.id.tab_work_tv)
    TextView tabWorkTv;
    @BindView(R.id.tab_work_ll)
    LinearLayout tabWorkLl;
    @BindView(R.id.tab_mine_ib)
    ImageButton tabMineIb;
    @BindView(R.id.tab_mine_tv)
    TextView tabMineTv;
    @BindView(R.id.tab_mine_ll)
    LinearLayout tabMineLl;
    //三个工作区
//    private EaseContactListFragment messageFragment;
//   private MessageFragment messageFragment;
    private ContactListFragment messageFragment;
    private WorkFragment workFragment;
    private MineFragment mineFragment;
    private Long temptime=0L;
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    //老师个人信息
    public static TeacherModel mineModel=new TeacherModel();
    //指导学生列表
    public static List<StudentModel> stuList;
    //学生选中判断
    public static SparseBooleanArray booleanArray;
    //环信用户列表
    List<String> friendlist;
//全选判断变量
    public static Boolean isCheckedAll=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //状态栏沉浸效果
        immersionBar = ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#00A7FF");
        immersionBar.init();
        //初始化监听事件
        initEvent();
        //默认加载第二个Fragment
        setSelect(1);
        //设置极光推送别名
        onTagAliasAction();
//获得好友列表
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //拿到好友列表
                     friendlist = EMClient.getInstance().contactManager().getAllContactsFromServer();

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void initEvent() {
        //用实现接口的方式监听
        tabMessageLl.setOnClickListener(this);
        tabWorkLl.setOnClickListener(this);
        tabMineLl.setOnClickListener(this);
    }

    private void setSelect(int i) {
        //开启Fragment管理
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hidenFragment(ft);
        switch (i) {
            //如果Fragment未创建则add，否则直接显示隐藏的Fragment
            case 0:
                if (messageFragment == null) {
                    messageFragment = new ContactListFragment();
                        messageFragment.setContactsMap(getContacts());
                        //设置监听点击item跳转到聊天界面
                        messageFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
                            @Override
                            public void onListItemClicked(EaseUser user) {
                                startActivity(new Intent(MainActivity.this, EChatActivity.class).putExtra("username", user.getUsername()));
                            }
                        });
                    ft.add(R.id.main_fl, messageFragment);
                } else {
                    ft.show(messageFragment);
                }
                break;
            case 1:
                if (workFragment == null) {
                    workFragment = new WorkFragment();
                    ft.add(R.id.main_fl, workFragment);
                } else {
                    ft.show(workFragment);
                }
                break;
            case 2:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    ft.add(R.id.main_fl, mineFragment);
                } else {
                    ft.show(mineFragment);
                }
                break;
        }
        ft.commit();
    }

    //隐藏所有Fragment
    private void hidenFragment(FragmentTransaction tf) {
        if (messageFragment != null)
            tf.hide(messageFragment);
        if (workFragment != null)
            tf.hide(workFragment);
        if (mineFragment != null)
            tf.hide(mineFragment);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getAction() == KeyEvent.ACTION_DOWN))
        {
            if(System.currentTimeMillis() - temptime >1500) // 2s内再次选择back键有效
            {
                Toast.makeText(this, "请再按一次返回退出", Toast.LENGTH_LONG).show();
                temptime = System.currentTimeMillis();
            }
            else {
                finish();
                System.exit(0); //凡是非零都表示异常退出!0表示正常退出!
            }

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {
        resetImgs();
        switch (v.getId()) {
            case R.id.tab_message_ll:
                tabMessageIb.setImageResource(R.drawable.liaotian_press);
                tabMessageTv.setTextColor(Color.parseColor("#4090ff"));
                setSelect(0);
                break;
            case R.id.tab_work_ll:
                tabWorkIb.setImageResource(R.drawable.work_press);
                tabWorkTv.setText("");
                tabWorkTv.setTextSize(0.0f);
                setSelect(1);
                break;
            case R.id.tab_mine_ll:
                tabMineIb.setImageResource(R.drawable.mine_press);
                tabMineTv.setTextColor(Color.parseColor("#4090ff"));
                setSelect(2);
                break;
            default:break;
        }
    }
    //重置图片效果
    private void resetImgs() {
        //重置icon图标
        tabMessageIb.setImageResource(R.drawable.liaotian_normal);
        tabWorkIb.setImageResource(R.drawable.work_normal);
        tabMineIb.setImageResource(R.drawable.mine_normal);

        //重置文字颜色
        tabMessageTv.setTextColor(Color.parseColor("#272727"));
        tabWorkTv.setTextColor(Color.parseColor("#272727"));
        tabMineTv.setTextColor(Color.parseColor("#272727"));
        //重置文字内容
        tabWorkTv.setText("工作");
        tabWorkTv.setTextSize(12.0f);
    }
    public void onTagAliasAction() {
        String alias = null;
        int action = -1;
        boolean isAliasAction = false;
        //用户id
        alias =MyApplication.getT_tid();
        isAliasAction = true;
        //覆盖式设置Alias
        action = TagAliasOperatorHelper.ACTION_SET;
        TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        sequence++;
        tagAliasBean.alias = alias;
        tagAliasBean.isAliasAction = isAliasAction;
        //设置别名和处理设置别名失败请求
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(),sequence,tagAliasBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }
    //获取联系人
    private Map<String, EaseUser> getContacts(){
        Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
        //异常处理
           int length=(friendlist!=null)?friendlist.size():0;
            for(int i = 0; i <length; i++){
                EaseUser user = new EaseUser(friendlist.get(i));
                contacts.put(friendlist.get(i), user); }
            return contacts;
    }

}

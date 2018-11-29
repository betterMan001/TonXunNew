package tx.a316.com.tx_teacher;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    //上下文环境
    private static Context context;
    //教师登录相关信息
    private static String t_tid="";
    private static String token="";
    //本地保存数据
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static List<Activity> activityList=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();
        //环信初始化
        EaseUI.getInstance().init(this,initOptions());
//        EMClient.getInstance().setDebugMode(true);
        //极光推送
        JPushInterface.setDebugMode(true);
        //讯飞语音输入
       SpeechUtility.createUtility(context, SpeechConstant.APPID +"=5bd57fbf");
        if(pref.getBoolean("notify",true)){
            JPushInterface.init(this);
        }
    }
    public static Context getContext() {
        return context;
    }
    //getter和setter

    public static String getT_tid() {
        return t_tid;
    }

    public static void setT_tid(String t_tid) {
        MyApplication.t_tid = t_tid;
    }

    public static String getToken() {
        return token;
    }
    public static void setToken(String token) {
        MyApplication.token = token;
    }
    //添加活动
    public static void addActivity(Activity activity){
        activityList.add(activity);
    }
    //关闭所有活动
    public static void finishAllActivities(){
        for(Activity activity:activityList){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
    private EMOptions initOptions() {

        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);
        return   options;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }
}

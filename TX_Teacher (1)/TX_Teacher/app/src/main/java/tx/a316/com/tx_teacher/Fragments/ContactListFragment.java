package tx.a316.com.tx_teacher.Fragments;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.othershe.nicedialog.NiceDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Models.AddInformation;
import tx.a316.com.tx_teacher.Models.telPeople;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.MyJSONUtil;
import tx.a316.com.tx_teacher.Utils.Net;

public class ContactListFragment extends EaseContactListFragment {
    List<String> list;//保存好友的
    private telPeople telman;//联系人
    //被选中的用户
    EaseUser user;
    private boolean isDelete=false;
    private boolean first=true;
    private List<telPeople> telPeopleList=new ArrayList<>();
    private List<String> nameList;
   private int pos=0;
   //内存缓存中的联系人键值对集
    public static Map<String,EaseUser> contacts=new HashMap<>();
    final NiceDialog progressDialog=NiceDialog.init();
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==12){
                //收到邀请后执行
                AddInformation.message="5";
                titleBar.setLeftImageResource(R.drawable.message4);
                String username=(String)msg.obj;//邀请者的id
                AddInformation.userNumber=username;
                AddInformation.panduan=true;
                Log.i("zjc","请求者的id："+username);
            }
            else if(msg.what==0x13){
                //用来显示好友列表
                list= (List<String>) msg.obj;
                telPeopleList=new ArrayList<>();
                for(String l:list) {
                    telman = new telPeople(l,l);
                    telPeopleList.add(telman);
                }
                //从自己服务器获得别名并且刷新好友列表
                getContacts();
            }
            else if(msg.what==0x14){
                //接受方同意后
                telman=new telPeople(AddInformation.userNumber,AddInformation.userNumber);
                telPeopleList.add(telman);
                AddInformation.jujue="2";
                //从自己服务器获得别名并且刷新好友列表
               updateContact();
            }
            else if(msg.what==0x15){
                //发送方被同意后更新数据
                //从自己服务器获得别名并且刷新好友列表
                updateContact();
            }
            else if(msg.what==0x16){
                titleBar.setLeftImageResource(R.drawable.message4);
                //被拒后后的消息提醒
                AddInformation.jujue="1";
            }
            else if(msg.what==0x17){
                //删除了好友刷新数据
                //从自己服务器获得别名并且刷新好友列表
                updateContact();
            }
        }
    };
    @Override
    protected void initView() {
        super.initView();
        //
        EMClient.getInstance().contactManager().setContactListener(new MyContentListener());
        registerForContextMenu(listView);
        //设置用户信息提供者
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                String userID=MyApplication.getT_tid();
                if(username.equals(userID)){
                    EaseUser user=new EaseUser(userID);
                    int pos=telPeopleList.size()-1;
                    user.setNickname(telPeopleList.get(pos).getName());
                    user.setAvatar(telPeopleList.get(pos).getPicture_url());
                    return user;
                }
                return ContactListFragment.contacts.get(username);
            }
        });
        //下拉刷新控件
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateContact();
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    //长按显示菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //获取选中项的位置
         pos=((AdapterView.AdapterContextMenuInfo)menuInfo).position;
         user = (EaseUser) listView.getItemAtPosition(pos);
        getActivity().getMenuInflater().inflate(R.menu.delete,menu);
    }
//菜单项的点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.contact_delete){
            //1.执行删除选中的联系人操作
            deleteContact();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    //
    @Override
    protected void setUpView() {
        super.setUpView();
        //添加图片
        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setLeftImageResource(R.drawable.message3);
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NetUtils.hasDataConnection(getActivity());
            }
        });
        titleBar.getLeftLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //好友邀请消息
                decide();
            }
        });

        // 添加好友框
        titleBar.getRightLayout().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addFriend();
            }
        });
        //获取好友列表
        updateContact();
    }
    //添加好友方法
    void addFriend(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        final AlertDialog dialog=builder.show();
        final EditText editText=view.findViewById(R.id.telNumber);//输入被加者的号码
        final EditText editText2=view.findViewById(R.id.why);//申请理由
        view.findViewById(R.id.telSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editText.getText().toString())){
                    try {
                        AddInformation.userNumber=editText.getText().toString();
                        AddInformation.userName=" ";
                        AddInformation.whyy=editText2.getText().toString();
                        //添加好友
                        EMClient.getInstance().contactManager().addContact(AddInformation.userNumber, AddInformation.whyy);
                        Log.i("zjc","被加者的id"+AddInformation.userNumber);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(getActivity(),"不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
        view.findViewById(R.id.canel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }
    //判断状态
    void decide(){
        titleBar.setLeftImageResource(R.drawable.message3);
        //添加好友被拒绝
        if(AddInformation.jujue.equals("1")){
            AddInformation.jujue="0";
            refuge();
            //收到申请好友的请求
        } else if (AddInformation.message=="5"){
            AddInformation.message="0";
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.panduan, null);
            builder.setView(view);
            final AlertDialog dialog = builder.show();
            final Button tongyi = view.findViewById(R.id.tongyi);
            final Button butongyi = view.findViewById(R.id.butongyi);
            final TextView askreson = view.findViewById(R.id.askreson);
            final TextView askpeopled=view.findViewById(R.id.askpeopled);
            askpeopled.setText(AddInformation.userNumber);
            askreson.setText(AddInformation.whyy.toString());
            tongyi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.i("zjc", AddInformation.userNumber);
                        AddInformation.panduan = false;
                        EMClient.getInstance().contactManager().acceptInvitation(AddInformation.userNumber);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
            butongyi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AddInformation.panduan = false;
                        EMClient.getInstance().contactManager().declineInvitation(AddInformation.userNumber);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
            //收到同意的通知
        } else if(AddInformation.jujue.equals("2")){
           refuge();
            AddInformation.jujue="0";
        }
    }

    //收到加好友弹出提示对话框
    void refuge(){
        titleBar.setLeftImageResource(R.drawable.message4);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.refuge,null);
        if(AddInformation.jujue.equals("2"))
            ((TextView)view.findViewById(R.id.refuge_text)).setText("对方已同意你的好友请求");
        builder.setView(view);
        final AlertDialog dialog=builder.show();
    }
//监听联系人的变动
    private class MyContentListener implements EMContactListener {

        @Override
        public void onContactAdded(String s) {
            Log.i("zjc","增加了联系人"+s);

            mhandler.sendEmptyMessage(0x14);
        }
        @Override
        public void onContactDeleted(String s) {
            Log.i("zjc","删除了联系人");

            mhandler.sendEmptyMessage(0x17);
        }

        @Override
        public void onContactInvited(String s, String s1) {
            Log.i("zjc","收到"+s+"的好友邀请。"+"理由:"+s1);
            AddInformation.whyy=s1;
            Message message=new Message();
            message.obj=s;
            message.what=12;
            mhandler.sendMessage(message);
        }
        @Override
        public void onFriendRequestAccepted(String s) {
            Log.i("zjc","你被同意了"+s);

            mhandler.sendEmptyMessage(0x14);
        }
        @Override
        public void onFriendRequestDeclined(String s) {
            Log.i("zjc","你被拒绝了");
            AddInformation.jujue="1";
            mhandler.sendEmptyMessage(0x16);
        }
    }

    private void getContacts(){
             contacts=new HashMap<>();
            int length=(telPeopleList!=null)?telPeopleList.size():0;
    //拼接id作为网络访问的参数
            String names="";
            for(int i=0;i<length;i++){
                names+=telPeopleList.get(i).getSid();
                names+=",";
            }
            names+= MyApplication.getT_tid();
            //从网络端获取别名
            contactService(names);

        }

    private void contactService(String names){

        String URL= Net.getUserName;
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("id",names);
        RequestBody fromBody=builder.build();
        MyHttpUtil.sendOkhttpPostRequest(URL, fromBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),Net.fail,Toast.LENGTH_LONG).show();
                    }
                });
                Log.d("getContactName","访问服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    response.close();
                    JSONArray jsonArray=jsonObject.getJSONArray("getUserName");
                    int  length=jsonArray.length();
                    //重置telpepleList
                    telPeopleList=new ArrayList<>();
                    for(int i=0;i<length;i++){
                        telPeople people= MyJSONUtil.toObject(String.valueOf(jsonArray.get(i)),telPeople.class);
                        telPeopleList.add(people);
                    }
                  //装载ContactMap
                    makeMap(length);
                    //更新视图
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //生成联系人页面所需要的键值对集合
    private void makeMap(int length){
    //生成键值对集合
    for(int i=0;i<length-1;i++){
        EaseUser user=new EaseUser(telPeopleList.get(i).getSid());
        //设置别名
        user.setNickname(telPeopleList.get(i).getName());
        user.setAvatar(telPeopleList.get(i).getPicture_url());
        contacts.put(telPeopleList.get(i).getSid(),user);
    }
    setContactsMap(contacts);
}
    private void updateContact(){
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //拿到好友列表
                    List<String> list = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Log.i("zjc", "你的好友个数：" + list.size());
                    Message message=new Message();
                    message.obj=list;
                    message.what=0x13;
                    mhandler.sendMessage(message);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //删除好友
    public void deleteContact() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.从列表中删除目标好友
                    List<telPeople>list=new ArrayList<>();
                    int length=telPeopleList.size();
                    for(telPeople temp:telPeopleList){
                        if(temp.getSid().equals(user.getUsername()))
                            continue;
                        else list.add(temp);
                    }
                    telPeopleList=list;
                    isDelete=true;
                    makeMap(length-1);
                    //2.从环信删除联系人
                    EMClient.getInstance().contactManager().deleteContact(user.getUsername());
                  if(getActivity()==null)
                      return;
                    //3.刷新页面
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                            //toast提示
                            Toast.makeText(getActivity(),"删除"+user.getUsername()+"成功",Toast.LENGTH_LONG).show();

                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    if(getActivity()==null)
                        return;
                    //刷新页面
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //toast提示
                            Toast.makeText(getActivity(),"删除"+user.getUsername()+"失败",Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

}

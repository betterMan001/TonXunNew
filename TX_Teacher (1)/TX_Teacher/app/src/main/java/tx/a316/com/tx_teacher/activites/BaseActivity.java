package tx.a316.com.tx_teacher.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.othershe.nicedialog.NiceDialog;

import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.View.BaseView;

public class BaseActivity extends AppCompatActivity implements BaseView{
    final NiceDialog progressDialog=NiceDialog.init();
    private IntentFilter intentFilter;
    private NetworkChangeReceive networkChangeReceive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //添加至活动列表

        if(this instanceof LoginActivity==false){
            MyApplication.addActivity(this);
        }
        //加载对话框初始化//R.layout.loading_layout
        progressDialog.setLayoutId(R.layout.loading_layout)
                .setWidth(100)
                .setHeight(100)
                .setDimAmount(0.5f)
                .setOutCancel(true);
        //
        //初始化广播接收器
        if(this instanceof MainActivity){
            intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            networkChangeReceive = new NetworkChangeReceive();
            registerReceiver(networkChangeReceive,intentFilter);
        }
    }

    @Override
    public void showProgress() {
        progressDialog.show(this.getSupportFragmentManager());
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this instanceof MainActivity){
            unregisterReceiver(networkChangeReceive);
        }
    }

    //用于在回调函数中显示Toast
    public  void subThreadToast(final String message){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.getContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }
    class NetworkChangeReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isAvailable()){
                // Toast.makeText(context,"网络已恢复连接！",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"网络已断开",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

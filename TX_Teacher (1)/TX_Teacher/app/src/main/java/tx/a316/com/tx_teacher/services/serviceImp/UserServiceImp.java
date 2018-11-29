package tx.a316.com.tx_teacher.services.serviceImp;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.Utils.MathTools;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.LoginActivity;
import tx.a316.com.tx_teacher.services.service.UserService;

public class UserServiceImp implements UserService {
    //回调类
    private LoginActivity loginActivity;
    public UserServiceImp(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }
    @Override
    public void loginValidate(String username, String password) {
        //
        String URL= Net.TLOGIN+"?t_tid="+username+"&t_pwd="+password;
        Log.d("URL",URL);
        //从后台请求登录验证
        MyHttpUtil.sendOkhttpGetRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //访问网络失败则，隐藏加载动画，弹出提示的Toast
                loginActivity.hideProgress();
                loginActivity.subThreadToast(Net.fail);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String result=response.body().string();
                    response.close();
                    //接受返回的jsonObject
                    JSONObject jsonObject= new JSONObject(result);
               JSONObject jsonObject1=jsonObject.getJSONObject("tFirstLogin");
                try{
                    //返回空则捕捉异常
                    String t_tid=jsonObject1.getString("tid");
                    String token=jsonObject1.getString("token");
                    Log.d("t_tid",t_tid);
                    Log.d("token",token);
                    loginActivity.hideProgress();
                    //执行回调
                    loginActivity.loginCallBack(t_tid,token);
                }catch (org.json.JSONException j){
                    loginActivity.subThreadToast("工号密码错误");
                }
            }
             catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}

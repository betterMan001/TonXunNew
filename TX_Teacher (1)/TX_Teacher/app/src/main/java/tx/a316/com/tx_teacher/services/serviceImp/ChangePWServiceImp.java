package tx.a316.com.tx_teacher.services.serviceImp;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Utils.MathTools;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.ChangePwActivity;
import tx.a316.com.tx_teacher.services.service.ChangePwService;

public class ChangePWServiceImp implements ChangePwService {
    private ChangePwActivity changePwActivity;

    public ChangePWServiceImp(ChangePwActivity changePwActivity) {
        this.changePwActivity = changePwActivity;
    }

    @Override
    public void changePassword(String t_tid,String old_pwd,String new_pwd) {
         String URL= Net.tchangePwd;
        Log.d("URL",URL);
        //修改密码采用post传输
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("t_tid",t_tid);
        builder.add("old_pwd", MathTools.getMd5(old_pwd));
        builder.add("new_pwd",MathTools.getMd5(new_pwd));
        RequestBody formBody = builder.build();
        changePwActivity.showProgress();
        //修改密码使用post请求
        MyHttpUtil.sendOkhttpPostRequest(URL, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                changePwActivity.hideProgress();
                changePwActivity.subThreadToast(Net.fail);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    response.close();
                    String result=jsonObject.getString("result");
                    //服务器响应做一个反馈
                    if(result.equals("1")){
                        changePwActivity.subThreadToast("修改成功！");
                    }
                    else
                        changePwActivity.subThreadToast("旧密码输入错误");
                    changePwActivity.hideProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}

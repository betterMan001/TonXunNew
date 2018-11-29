package tx.a316.com.tx_teacher.services.serviceImp;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Models.MessageModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.MessageActivity;
import tx.a316.com.tx_teacher.services.service.MessageService;

public class MessageServiceImp implements MessageService {

    private MessageActivity messageActivity;
    private List<MessageModel> list;

    public MessageServiceImp(MessageActivity messageActivity) {
        this.messageActivity = messageActivity;
        list=new ArrayList<MessageModel>();
    }
    @Override
    public void getMessages(String t_tid) {
               String URL = Net.tJpush + "?t_tid=" + MyApplication.getT_tid();
               Log.d("URL", URL);
               messageActivity.showProgress();
               MyHttpUtil.sendOkhttpGetRequest(URL, new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {
                       messageActivity.hideProgress();
                       messageActivity.subThreadToast(Net.fail);
                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       messageActivity.hideProgress();
                       //从后台获得数据，按类型分成个人和系统
                       try {
                           String result=response.body().string();
                           //获取json对象
                           JSONObject jsonObject = new JSONObject(result);

                           response.close();
                           //拿出目标的array
                           JSONArray jsonArray1 = jsonObject.getJSONArray("jpushList");
                           int length = jsonArray1.length();
                           Gson gson = new Gson();
                           //处理
                           for (int i = 0; i < length; i++) {
                               MessageModel messageModel = gson.fromJson(String.valueOf(jsonArray1.get(i)), MessageModel.class);
                               list.add(messageModel);
                           }
                           messageActivity.messageCallBack(list);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }


                   }
               });
    }
}

package tx.a316.com.tx_teacher.services.serviceImp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Models.RejoinDetailModel;
import tx.a316.com.tx_teacher.Models.RejoinListModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.MyJSONUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.RejoinHistoryActivity;
import tx.a316.com.tx_teacher.activites.RejoinHistoryListActivity;
import tx.a316.com.tx_teacher.services.service.RejonHistoryService;

public class RejonHistoryServiceImp implements RejonHistoryService {
    //这里有两个Activity属性，分别给两个Activity做回调使用
    private RejoinHistoryListActivity activity;
    public RejonHistoryServiceImp(RejoinHistoryListActivity activity) {
        this.activity = activity;
    }

    private RejoinHistoryActivity activity1;

    public RejonHistoryServiceImp(RejoinHistoryActivity activity1) {
        this.activity1 = activity1;
    }
//获得历史答辩列表数据
    @Override
    public void getRejoinList(String t_tid) {
        String URL= Net.tRejoinListXWD+"?t_tid="+t_tid;
        Log.d("URL",URL);
        activity.showProgress();
        MyHttpUtil.sendOkhttpGetRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.hideProgress();
                activity.subThreadToast(Net.fail);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                response.close();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray=jsonObject.getJSONArray("tRejoinList");
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++){
                        RejoinListModel model= MyJSONUtil.toObject(String.valueOf(jsonArray.get(i)),RejoinListModel.class);
                        activity.rejonsList.add(model);
                    }
                    activity.hideProgress();
                    activity.callBack();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
//获得答辩详情数据
    @Override
    public void getRejoinDetailList(String t_tid, String r_title) {
       String URL=Net.tRejoinDetailXWD;
       Log.d("URL",URL);
       //发起网络请求,出现加载动画
        activity1.showProgress();
        MediaType FORM_CONTENT_TYPE
                = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        String temp="t_tid="+t_tid+"&r_title="+r_title;
        Log.d("parms",temp);
        RequestBody body=RequestBody.create(FORM_CONTENT_TYPE,temp);
       MyHttpUtil.sendOkhttpPostRequest(URL,body, new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               activity1.hideProgress();
               activity1.subThreadToast(Net.fail);
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               String result=response.body().string();
               response.close();
               try {
                   JSONObject jsonObject=new JSONObject(result);
                   JSONArray jsonArray=jsonObject.getJSONArray("tRejoinDetail");
                   int length=jsonArray.length();
                   for(int i=0;i<length;i++){
                       RejoinDetailModel model= MyJSONUtil.toObject(String.valueOf(jsonArray.get(i)),RejoinDetailModel.class);
                       activity1.rejoinDetaillist.add(model);
                   }
                   activity1.hideProgress();
                   //回调更新视图
                   activity1.callBack();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });
    }
}

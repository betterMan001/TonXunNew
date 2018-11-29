package tx.a316.com.tx_teacher.services.serviceImp;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Models.NoteModel;
import tx.a316.com.tx_teacher.Models.StudentModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.MyJSONUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.PushActivity;
import tx.a316.com.tx_teacher.activites.ReviewListActivity;
import tx.a316.com.tx_teacher.services.service.NoteService;

public class NoteServiceImp implements NoteService {
    private ReviewListActivity activity;

    public NoteServiceImp(ReviewListActivity activity) {
        this.activity = activity;
    }

    @Override
    public void getNoteList(String t_tid) {
      String URL= Net.tChartlist+"?t_tid="+ MyApplication.getT_tid()+"&page=0"+"&count=10000";
        Log.d("URLl",URL);
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
                    JSONArray jsonArray=jsonObject.getJSONArray("tChartlist");
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++){
                        NoteModel model= MyJSONUtil.toObject(String.valueOf(jsonArray.get(i)),NoteModel.class);
                        activity.notesList.add(model);
                    }
                    activity.hideProgress();
                    activity.callBack();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}

package tx.a316.com.tx_teacher.services.serviceImp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Fragments.MineFragment;
import tx.a316.com.tx_teacher.Models.TeacherModel;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.BaseActivity;
import tx.a316.com.tx_teacher.activites.MainActivity;
import tx.a316.com.tx_teacher.services.service.TeacherInformationService;

public class TeacherInformationServiceImp implements TeacherInformationService {
    MineFragment mineFragment;
    public TeacherInformationServiceImp() {
    }

    public TeacherInformationServiceImp(MineFragment mineFragment) {
        this.mineFragment = mineFragment;
    }

    @Override
    public void getTeacherInfo(String t_tid) {
        String URL= Net.getTeacher+"?t_tid="+t_tid;
        Log.d("URL",URL);
        MyHttpUtil.sendOkhttpGetRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    JSONObject jsonObject1=jsonObject.getJSONObject("getTeacher");
                    MainActivity.mineModel=new TeacherModel();
                    MainActivity.mineModel.setPicture_url(jsonObject1.getString("picture_url"));
                    MainActivity.mineModel.setName(jsonObject1.getString("name"));
                    MainActivity.mineModel.setTid(jsonObject1.getString("tid"));
                    mineFragment.callBack();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

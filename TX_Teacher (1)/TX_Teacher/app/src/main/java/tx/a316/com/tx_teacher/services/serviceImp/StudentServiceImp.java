package tx.a316.com.tx_teacher.services.serviceImp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Models.StudentModel;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.MyJSONUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.LoginActivity;
import tx.a316.com.tx_teacher.activites.MainActivity;
import tx.a316.com.tx_teacher.activites.PushActivity;
import tx.a316.com.tx_teacher.activites.SelectActivity;
import tx.a316.com.tx_teacher.services.service.StudentService;

public class StudentServiceImp implements StudentService {

    private SelectActivity selectActivity;

    public StudentServiceImp(SelectActivity selectActivity){
        this.selectActivity = selectActivity;
    }

    @Override
    public void getStuList(String t_tid) {

        String URL= Net.studentList+"?t_tid="+ MyApplication.getT_tid()+"&page=0"+"&count=10000";
        Log.d("URL",URL);
        //加载
        selectActivity.showProgress();
        //发起网络请求
        MyHttpUtil.sendOkhttpGetRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //加载消失
                selectActivity.hideProgress();
                selectActivity.subThreadToast("访问网络失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    selectActivity.hideProgress();
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    response.close();
                    JSONArray jsonArray=jsonObject.getJSONArray("studentList");
                    int  length=jsonArray.length();
                    //将json串转化为pojo对象
                    for(int i=0;i<length;i++){
                        StudentModel studentModel= MyJSONUtil.toObject(String.valueOf(jsonArray.get(i)),StudentModel.class);
                        MainActivity.stuList.add(studentModel);
                    }
                    selectActivity.callBack();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}

package tx.a316.com.tx_teacher.services.serviceImp;

import android.os.Handler;
import android.os.Message;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Models.Mapinformation;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.MapActivity;
import tx.a316.com.tx_teacher.services.service.MapService;

public class MapServiceImp implements MapService {
    private MapActivity mapActivity;

    private List<Mapinformation> list=new ArrayList<>();
    public MapServiceImp(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x33){
                mapActivity.fail();
            }else if(msg.what==0x34){
                mapActivity.succ(list);
            }
        }
    };


    @Override
    public void getStudentsLocation(String t_tid) {
        String url= Net.studentAddress+"?t_tid="+t_tid;
        Log.i("zjc","获取地理的网址是:"+url);
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zjc","网络请求失败");
                handler.sendEmptyMessage(0x33);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody=response.body().string();
                try {
                    JSONObject jsonObject=new JSONObject(responseBody);
                    JSONArray jsonArray=jsonObject.getJSONArray("address");
                    Gson gson=new Gson();
                    list=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        Mapinformation m= gson.fromJson(String .valueOf(jsonArray.get(i)),Mapinformation.class);
                        list.add(m);
                    }
                    handler.sendEmptyMessage(0x34);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}

package tx.a316.com.tx_teacher.Utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MyHttpUtil {

    //post
public static void sendOkhttpPostRequest(String address, RequestBody requestBody ,Callback callback){
    OkHttpClient client=new OkHttpClient();
    //配置request 开启建立工厂选择url+传输方式+建立
    Request request=new Request.Builder().url(address).post(requestBody).build();
     //调用call.enqueue，将call加入调度队列，然后等待任务执行完成，我们在Callback中即可得到结果
    client.newCall(request).enqueue(callback);

}
//get
    public static void sendOkhttpGetRequest(String address,Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}

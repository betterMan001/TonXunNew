package tx.a316.com.tx_teacher.services.serviceImp;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.PushActivity;
import tx.a316.com.tx_teacher.services.service.PushService;

public class PushServiceImp implements PushService {
   private PushActivity pushActivity;

    public PushServiceImp(PushActivity pushActivity) {
        this.pushActivity = pushActivity;
    }

    @Override
    public void PushMessages(String t_tid, String students, final String pushDetail) {
        String URL= Net.addJpush;
        //由于推送的内容长度不定，所以采用post请求
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("tid",t_tid);
        builder.add("message",pushDetail);
        builder.add("students",students);
        RequestBody formBody = builder.build();
        //发起网络请求开始加载
        pushActivity.showProgress();
        MyHttpUtil.sendOkhttpPostRequest(URL, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pushActivity.hideProgress();
                pushActivity.subThreadToast(Net.fail);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               pushActivity.hideProgress();
               //推送成功发送，Toast提示
               pushActivity.pushCallBack();
            }
        });
    }
}

package tx.a316.com.tx_teacher.services.serviceImp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.ReplyActivity;
import tx.a316.com.tx_teacher.services.service.SendReplyService;

public class SendReplyServiceImp implements SendReplyService {
private ReplyActivity replyActivity;

    public SendReplyServiceImp(ReplyActivity replyActivity) {
        this.replyActivity = replyActivity;
    }

    @Override
    public void sendReply(String s_sid, String rejoin_Title, String rejoin_Detail) {
        String URL= Net.createNewRejoin;
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("t_tid", MyApplication.getT_tid());
        builder.add("s_sid",s_sid);
        builder.add("rejoin_Title",rejoin_Title);
        builder.add("rejoin_Detail",rejoin_Detail);
        RequestBody formBody = builder.build();
        //加载
        replyActivity.showProgress();
        //发起网络请求
        MyHttpUtil.sendOkhttpPostRequest(URL, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                replyActivity.hideProgress();
                replyActivity.subThreadToast(Net.fail);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                replyActivity.hideProgress();
                replyActivity.subThreadToast("成功发起答辩!");
            }
        });
    }
}

package tx.a316.com.tx_teacher.Fragments;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;

public class ChatFragment extends EaseChatFragment {
    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void sendTextMessage(String content) {
        super.sendTextMessage(content);
//           PushMessages(MyApplication.getT_tid(),toChatUsername,"收到来自"+MyApplication.getT_tid()+"的一条文字消息");
    }
    public void PushMessages(String t_tid, String students, final String pushDetail) {
        String URL= Net.addJpush;
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("tid",t_tid);
        builder.add("message",pushDetail);
        builder.add("students",students);
        RequestBody formBody = builder.build();
        //发起网络请求开始加载

        MyHttpUtil.sendOkhttpPostRequest(URL, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }
}

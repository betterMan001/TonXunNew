package tx.a316.com.tx_teacher.services.serviceImp;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.ReviewActivity;
import tx.a316.com.tx_teacher.services.service.ReviewService;

public class ReviewServiceImp implements ReviewService {
    ReviewActivity reviewActivity;

    public ReviewServiceImp(ReviewActivity reviewActivity) {
        this.reviewActivity = reviewActivity;
    }

    @Override
    public void sentReview(String c_cid, String detail) {
        String URL= Net.readChart+"?c_cid="+c_cid+"&detail="+detail;
        Log.d("URLl",URL);
        reviewActivity.showProgress();
        MyHttpUtil.sendOkhttpGetRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                reviewActivity.hideProgress();
                reviewActivity.subThreadToast(Net.fail);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                reviewActivity.hideProgress();
                reviewActivity.subThreadToast("发送成功！");
                reviewActivity.callBack();
            }
        });
    }
}

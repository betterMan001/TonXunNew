package tx.a316.com.tx_teacher.activites;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.gyf.barlibrary.ImmersionBar;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.youth.banner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tx.a316.com.tx_teacher.Models.NoteModel;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.Utils.GlideImageLoader;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.Utils.ParseVoice;
import tx.a316.com.tx_teacher.services.serviceImp.ReviewServiceImp;
import tx.a316.com.tx_teacher.widget.LineDividerTextView;

public class ReviewActivity extends BaseActivity implements View.OnClickListener {
    //沉浸式状态栏
    public ImmersionBar immersionBar;
    //周记详情相关
    @BindView(R.id.reviwdetial_title_tv)
    TextView reviwdetialTitleTv;
    @BindView(R.id.reviwdetial_detail_tv)
    LineDividerTextView reviwdetialDetailTv;
    @BindView(R.id.reviwdetial_name_tv)
    TextView reviwdetialNameTv;
    @BindView(R.id.reviwdetial_date_tv)
    TextView reviwdetialDateTv;
    @BindView(R.id.review_back_ll)
    ImageView reviewBackLl;
    @BindView(R.id.home_banner)
    Banner homeBanner;
    @BindView(R.id.comment_content)
    EditText commentContent;
    @BindView(R.id.comment_send)
    Button commentSend;
    @BindView(R.id.rl_comment)
    RelativeLayout rlComment;
    //评论相关
    private ImageButton review_voice;
    private EditText comment_content;
    private Button comment_send;
    //轮播图列表
    List<String> image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        //初始化视图
        initView();
    }

    //第一版
    public void initView() {
        NoteModel noteModel = ReviewListActivity.noteModel;
        reviwdetialTitleTv.setText(noteModel.getTitle());
        reviwdetialDetailTv.setText(noteModel.getZhoujiDail());
        reviwdetialNameTv.setText(noteModel.getClassz() + noteModel.getName());
        reviwdetialDateTv.setText(noteModel.getDate());
        review_voice = findViewById(R.id.review_voice);
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_send = (Button) findViewById(R.id.comment_send);
        //设置监听
        review_voice.setOnClickListener(this);
        comment_send.setOnClickListener(this);
        reviewBackLl.setOnClickListener(this);
          getImages();
    }
//获得图片
    private void getImages(){
        String URL= Net.tChartDetail+"?c_cid="+ReviewListActivity.noteModel.getCid();
    Log.d("URL",URL);
   final Context context=this;
    MyHttpUtil.sendOkhttpGetRequest(URL, new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
               String result=response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                response.close();
                JSONObject jsonObject1=jsonObject.getJSONObject("tChartDetail");
                String images=jsonObject1.getString("image");
                String[] imageArray=images.split(",");
                image=new ArrayList<String>();
                 for(int i=0;i<imageArray.length;i++)
                 {
                     image.add(imageArray[i]);
                 }
                startBanner();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });

}
//初始化轮播图
    private void startBanner(){
this.runOnUiThread(new Runnable() {
    @Override
    public void run() {
        homeBanner.setImages(image).setImageLoader(new GlideImageLoader()).start();
    }
});
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //语音听写
            case R.id.review_voice:
                initSpeech();
                break;
            case R.id.comment_send:
                sendComment();
                break;
            case R.id.review_back_ll:
                finish();
            default:
                break;
        }
    }

    private void initSpeech() {
        //1、初始化窗口
        RecognizerDialog dialog = new RecognizerDialog(ReviewActivity.this, null);
        //2、设置听写参数，详见官方文档
        //识别中文听写可设置为"zh_cn",此处为设置英文听写
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3、开始听写
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                if (!b) {
                    String result = new ParseVoice().parseVoice(recognizerResult.getResultString());
                    comment_content.setText(result);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
    }


    private void sendComment() {
        String detail = comment_content.getText().toString();
        new ReviewServiceImp(this).sentReview(ReviewListActivity.noteModel.getCid(), detail);
    }

    public void callBack() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
//                Intent intent = new Intent(ReviewActivity.this, ReviewListActivity.class);
//                startActivity(intent);
            }
        });


    }
}
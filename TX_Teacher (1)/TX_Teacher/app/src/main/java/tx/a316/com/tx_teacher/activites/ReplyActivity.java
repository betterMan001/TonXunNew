package tx.a316.com.tx_teacher.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tx.a316.com.tx_teacher.Models.StudentModel;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.Utils.ParseVoice;
import tx.a316.com.tx_teacher.services.serviceImp.SendReplyServiceImp;

public class ReplyActivity extends BaseActivity {
//计数器,记录题号
    private int count=0;
    //问题序列字符串
    private String queList="";
    @BindView(R.id.reply_title)
    EditText replyTitle;
    @BindView(R.id.reply_format)
    TextView replyFormat;
    @BindView(R.id.reply_content)
    EditText replyContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        ButterKnife.bind(this);
        replyFormat.setText("");
    }

    @OnClick({R.id.reply_back_ll, R.id.main_reply_ib, R.id.reply_voice, R.id.reply_send,R.id.reply_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //退出
            case R.id.reply_back_ll:
                finish();
                break;
                //发送数据到后台
            case R.id.main_reply_ib:
                sendReply();
                break;
            case R.id.reply_voice:
                //语音输入
                initSpeech();
                break;
                //添加信息到上面的editText
            case R.id.reply_send:
                addQuestions();
                break;
            case R.id.reply_rl:
                Intent intent=new Intent(ReplyActivity.this,SelectActivity.class);
                this.startActivity(intent);
                break;
        }
    }
    private void sendReply(){
      //发起网络请求
        //测试版
        String stus="";
        List<StudentModel> stuList=MainActivity.stuList;
        int length=stuList.size();
        for(int i=0;i<length;i++){
            //吧被选中的学生打包到String
            if(MainActivity.booleanArray.get(i)==true){
                stus+=(stuList.get(i).getId())+",";
            }
        }
        MainActivity.booleanArray=new SparseBooleanArray();
        new SendReplyServiceImp(this).sendReply(stus,replyTitle.getText().toString(),queList);
    }
    //添加问题
    private void addQuestions(){
        //获取当前问题
        String question= replyContent.getText().toString();
        //清空输入栏
        replyContent.setText("");
        //生成题号
        String head=new Integer(++count).toString()+".";
        //生成问题尾部
        String tail="?";
        String que_item=(head+question+tail);
        //添加到问题序列,用逗号分隔
        queList+=que_item;
        queList+="#";
        //先获得原来的String在拼接上去
        String pre=replyFormat.getText().toString();
        //添加到上述editText
        replyFormat.setText(pre+que_item+"\n");
    }
    private void initSpeech(){
        //1、初始化窗口
        RecognizerDialog dialog=new RecognizerDialog(ReplyActivity.this,null);
        //2、设置听写参数，详见官方文档
        //识别中文听写可设置为"zh_cn",此处为设置英文听写
        dialog.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT,"mandarin");
        //3、开始听写
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                if(!b){
                    String result=new ParseVoice().parseVoice(recognizerResult.getResultString());
                    replyContent.setText(result);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

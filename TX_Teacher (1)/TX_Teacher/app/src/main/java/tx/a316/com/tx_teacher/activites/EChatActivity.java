package tx.a316.com.tx_teacher.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gyf.barlibrary.ImmersionBar;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

import tx.a316.com.tx_teacher.Fragments.ChatFragment;
import tx.a316.com.tx_teacher.R;

public class EChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echat);
        setFragment();
    }
    private void setFragment(){
        //new出EaseChatFragment或其子类的实例
        ChatFragment chatFragment = new ChatFragment();
        //获得好友信息
        Intent intent=getIntent();
        String friendId=intent.getStringExtra("username");
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, friendId);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.echat_fl, chatFragment).commit();
    }
}

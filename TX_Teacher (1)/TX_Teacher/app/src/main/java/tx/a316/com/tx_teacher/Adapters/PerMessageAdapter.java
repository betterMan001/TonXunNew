package tx.a316.com.tx_teacher.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tx.a316.com.tx_teacher.Models.MessageModel;
import tx.a316.com.tx_teacher.R;

public class PerMessageAdapter extends RecyclerView.Adapter {
 //第一版，点击事件未设置
 private Context context;
    private List<MessageModel> list;

    public PerMessageAdapter(Context context, List<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.permessage_item,parent,false);
    return new perMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      holder=(perMessageHolder)holder;
      MessageModel messageModel=list.get(position);
      //监听LinearOut未实现
        //消息内容
        ((perMessageHolder) holder).textView.setText(messageModel.getDetail());
    }

    @Override
    public int getItemCount() {

        if(list.size()>0)
        return list.size();
        else return 0;
    }
    static class perMessageHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public LinearLayout linearLayout;
        public perMessageHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.per_item_Tv);
            this.linearLayout = itemView.findViewById(R.id.sys_item_Tv);
        }
    }
}

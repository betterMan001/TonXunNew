package tx.a316.com.tx_teacher.Adapters;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import tx.a316.com.tx_teacher.Models.MessageModel;
import tx.a316.com.tx_teacher.R;
public class SystemAdapter extends RecyclerView.Adapter {
    //上下文和数据
private Context context;
private List<MessageModel> list;

    public SystemAdapter(Context context, List<MessageModel> list) {
        this.context = context;
        this.list = list;
    }

    //将目标layout渲染成View
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sysmessage_item,parent,false);
        return new SysMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel model=list.get(position);
      SysMessageHolder holder1= (SysMessageHolder) holder;
    holder1.textView.setText(model.getDetail());
    }

    @Override
    public int getItemCount() {
        if(list.size()>0)
        return list.size();
        else return 0;
    }
    //写一个Holder继承RecyclerView.ViewHolder
    static class SysMessageHolder extends RecyclerView.ViewHolder{
       public TextView textView;
        public SysMessageHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.sys_item_Tv);
        }
    }
}

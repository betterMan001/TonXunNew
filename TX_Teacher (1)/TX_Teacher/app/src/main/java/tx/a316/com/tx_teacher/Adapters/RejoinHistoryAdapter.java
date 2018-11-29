package tx.a316.com.tx_teacher.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tx.a316.com.tx_teacher.Models.RejoinListModel;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.activites.RejoinHistoryActivity;
import tx.a316.com.tx_teacher.activites.RejoinHistoryListActivity;

public class RejoinHistoryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<RejoinListModel> list;

    public RejoinHistoryAdapter(Context context, List<RejoinListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rejoin_list_item,parent,false);
        return new RejoinListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
     RejoinListViewHolder holder1= (RejoinListViewHolder) holder;
     RejoinListModel model=list.get(position);
     final String title=model.getTitle();
     //设置相应的属性和监听事件
        holder1.rejoin_title_tv.setText("标题："+model.getTitle());
        holder1.rejoin_date_tv.setText("时间:"+model.getDate());
        holder1.rejoinList_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //设置要传递的数据
                RejoinHistoryListActivity.title=title;
                //实现跳转到对应答辩主题学生答辩界面
                Intent intent=new Intent(context, RejoinHistoryActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        else
        return 0;
    }
    public class RejoinListViewHolder extends RecyclerView.ViewHolder{
        public CardView rejoinList_item;
        public TextView rejoin_title_tv;
        public TextView rejoin_date_tv;
        //对应select_item中单项
        public RejoinListViewHolder(View itemView) {
            super(itemView);
            rejoinList_item=itemView.findViewById(R.id.rejoinList_item);
            rejoin_title_tv=itemView.findViewById(R.id.rejoin_title_tv);
            rejoin_date_tv=itemView.findViewById(R.id.rejoin_date_tv);;
        }

    }
}

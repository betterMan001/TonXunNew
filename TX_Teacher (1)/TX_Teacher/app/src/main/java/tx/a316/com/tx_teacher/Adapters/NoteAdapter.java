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

import tx.a316.com.tx_teacher.Models.NoteModel;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.activites.ReviewActivity;
import tx.a316.com.tx_teacher.activites.ReviewListActivity;

public class NoteAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<NoteModel> list;

    public NoteAdapter(Context context, List<NoteModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.review_item,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

      NoteViewHolder  holder1=(NoteViewHolder)holder;
        //获取当前位置模型
        final NoteModel noteModel=list.get(position);
        holder1.review_date_tv.setText(noteModel.getDate());
        holder1.reviw_title_tv.setText(noteModel.getTitle());
        //班级+名字
        holder1.review_name_tv.setText(noteModel.getClassz()+noteModel.getName());
        //点击事件
        //1.获取模型
        holder1.reviewlist_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewListActivity.noteModel=noteModel;
                //实现跳转到详细评论界面
                Intent intent=new Intent(context, ReviewActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(list.size()>0)
        return  list.size();
        else
            return 0;
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        public CardView reviewlist_item;
        public TextView reviw_title_tv;
        public TextView review_name_tv;
        public TextView review_date_tv;
        //对应select_item中单项
        public NoteViewHolder(View itemView) {
            super(itemView);
            reviewlist_item=itemView.findViewById(R.id.reviewlist_item);
            reviw_title_tv=itemView.findViewById(R.id.reviw_title_tv);
            review_name_tv=itemView.findViewById(R.id.review_name_tv);
            review_date_tv=itemView.findViewById(R.id.review_date_tv);
        }
    }
}

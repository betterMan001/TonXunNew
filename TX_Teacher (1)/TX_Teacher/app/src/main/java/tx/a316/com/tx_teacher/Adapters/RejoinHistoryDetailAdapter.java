package tx.a316.com.tx_teacher.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tx.a316.com.tx_teacher.Models.RejoinDetailModel;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.activites.RejoinHistoryActivity;
import tx.a316.com.tx_teacher.activites.RejoinHistoryDetailActivity;

public class RejoinHistoryDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<RejoinDetailModel> list;

    public RejoinHistoryDetailAdapter(Context context, List<RejoinDetailModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rejoindetail_item,parent,false);
        return new RejoinDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
      RejoinDetailViewHolder holder1= (RejoinDetailViewHolder) holder;
      RejoinDetailModel model=list.get(position);
      holder1.rejoinDatil_date_tv.setText(model.getData());
      if(model.getReview().equals("0")){
          holder1.rejoinDatil_isReply_tv.setText("未回答");
          holder1.rejoinDatil_isReply_iv.setImageResource(R.drawable.dabianpinglun_normal);
      }
      else {
          holder1.rejoinDatil_isReply_tv.setText("已回答");
          holder1.rejoinDatil_isReply_iv.setImageResource(R.drawable.dabianpinglun_press);
      }
       holder1.rejoinDatil_stuInformation_tv.setText(model.getClassAndName());
      final Boolean isReply=(model.getReview().equals("0"))?false:true;
      holder1.rejoinDetail_item.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(isReply==false){
                  Toast.makeText(context,"该学生没有回答问题",Toast.LENGTH_LONG);
              }
              else{
                  //设置要传递的数据
                  RejoinHistoryActivity.pos=position;
                  //实现跳转到对应答辩主题学生答辩界面
                  Intent intent=new Intent(context, RejoinHistoryDetailActivity.class);
                  context.startActivity(intent);
              }
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
    public class RejoinDetailViewHolder extends RecyclerView.ViewHolder{
        public CardView rejoinDetail_item;
        public TextView rejoinDatil_stuInformation_tv;
        public TextView rejoinDatil_date_tv;
        public TextView rejoinDatil_isReply_tv;
        public ImageView rejoinDatil_isReply_iv;
        //对应select_item中单项
        public RejoinDetailViewHolder(View itemView) {
            super(itemView);
            rejoinDetail_item=itemView.findViewById(R.id.rejoinDetail_item);
            rejoinDatil_stuInformation_tv=itemView.findViewById(R.id.rejoinDatil_stuInformation_tv);
            rejoinDatil_date_tv=itemView.findViewById(R.id.rejoinDatil_date_tv);
            rejoinDatil_isReply_tv=itemView.findViewById(R.id.rejoinDatil_isReply_tv);
            rejoinDatil_isReply_iv=itemView.findViewById(R.id.rejoinDatil_isReply_iv);
        }

    }
}

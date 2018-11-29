package tx.a316.com.tx_teacher.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import tx.a316.com.tx_teacher.Models.StudentModel;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.activites.MainActivity;
import tx.a316.com.tx_teacher.activites.PushActivity;

public class MultiSelectAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<StudentModel>list;

    public MultiSelectAdapter(Context context, List<StudentModel> list) {
        this.context = context;
        this.list = list;
        MainActivity.booleanArray=new SparseBooleanArray();
    }
    private void setItemChecked(int position, boolean isChecked) {
        MainActivity.booleanArray.put(position, isChecked);
    }
    //根据位置判断条目是否选中
    private boolean isItemChecked(int position) {
        return MainActivity.booleanArray.get(position);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.select_item,parent,false);
        return new MultiSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
      //设置学生属性和ChickedBox
        //1.绑定holder
        MultiSelectViewHolder holder1= (MultiSelectViewHolder) holder;
        //2.获取目标学生信息
        StudentModel studentModel=list.get(position);
         holder1.select_name_Tv.setText(studentModel.getName());
         holder1.select_special_Tv.setText(studentModel.getClassz());
         //默认给boolArray插入未选中结果
        if(MainActivity.isCheckedAll==false){
            setItemChecked(position,false);
        }else{
            //选中了全选按钮则全选
            holder1.select_checkbox.setChecked(true);
            setItemChecked(position,true);
        }

         holder1.select_checkbox.setOnClickListener(new View.OnClickListener() {
             //点击checkBox改变状态
             @Override
             public void onClick(View v) {
                 if (isItemChecked(position)) {
                     setItemChecked(position, false);
                 } else {
                     setItemChecked(position, true);
                 }
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
   public class MultiSelectViewHolder extends RecyclerView.ViewHolder{
         public CheckBox select_checkbox;//复选框
         public TextView select_name_Tv;//名字
         public TextView select_special_Tv;
         //对应select_item中单项
       public MultiSelectViewHolder(View itemView) {
           super(itemView);
           select_checkbox=itemView.findViewById(R.id.select_checkbox);
           select_name_Tv=itemView.findViewById(R.id.select_name_Tv);
           select_special_Tv=itemView.findViewById(R.id.select_special_Tv);

       }
   }
}

package tx.a316.com.tx_teacher.Utils;

import com.google.gson.Gson;

public class MyJSONUtil {
    public static Gson gson;
    public static <T>T toObject(String src,Class<T> target){
        //复用同一个Gson对象
       if(gson==null){
           gson=new Gson();
       }
       return gson.fromJson(src,target);
    }

}

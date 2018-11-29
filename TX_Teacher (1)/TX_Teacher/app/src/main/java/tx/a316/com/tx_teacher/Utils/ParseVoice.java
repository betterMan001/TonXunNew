package tx.a316.com.tx_teacher.Utils;

import com.google.gson.Gson;

import java.util.ArrayList;

import tx.a316.com.tx_teacher.activites.ReviewActivity;

public class ParseVoice {
    /**
     * 语音对象封装
     */
    public ArrayList<ParseVoice.WSBean> ws;

    public class WSBean {
        public ArrayList<ParseVoice.CWBean> cw;
    }

    public class CWBean {
        public String w;
    }
    //解析Gson对象
    public String parseVoice(String resultString) {
        Gson gson =  new Gson();
        ParseVoice voiceBean = gson.fromJson(resultString, ParseVoice.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<ParseVoice.WSBean> ws = voiceBean.ws;
        for (ParseVoice.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }
}

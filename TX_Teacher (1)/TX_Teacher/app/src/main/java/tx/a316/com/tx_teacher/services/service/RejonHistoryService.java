package tx.a316.com.tx_teacher.services.service;

public interface RejonHistoryService {
    //获得答辩列表
    public void getRejoinList(String t_tid);
    //获得答辩详情
    public void getRejoinDetailList(String t_tid,String r_title);
}

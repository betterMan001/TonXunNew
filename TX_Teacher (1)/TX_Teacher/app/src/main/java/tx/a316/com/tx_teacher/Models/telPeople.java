package tx.a316.com.tx_teacher.Models;


public class telPeople {
    private String sid;
    private String name;
    private String picture_url;

    public telPeople(String sid, String name) {
        this.sid = sid;
        this.name = name;
    }

    public telPeople(String sid, String name, String picture_url) {
        this.sid = sid;
        this.name = name;
        this.picture_url = picture_url;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }
}

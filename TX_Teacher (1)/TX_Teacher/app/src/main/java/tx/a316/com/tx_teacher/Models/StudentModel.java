package tx.a316.com.tx_teacher.Models;

public class StudentModel {
    private String picture_url;
    private String name;
    private String id;
    private String classz;

    public StudentModel(String picture_url, String name, String id, String classz) {
        this.picture_url = picture_url;
        this.name = name;
        this.id = id;
        this.classz = classz;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassz() {
        return classz;
    }

    public void setClassz(String classz) {
        this.classz = classz;
    }
}

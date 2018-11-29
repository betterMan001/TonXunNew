package tx.a316.com.tx_teacher.Models;

public class Mapinformation {
    String city;
    String latitude;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getClassAndName() {
        return classAndName;
    }

    public void setClassAndName(String classAndName) {
        this.classAndName = classAndName;
    }

    String district;
    String longitude;
    String job;
    String classAndName;

    public Mapinformation(String city, String latitude, String district, String longitude, String job, String classAndName) {
        this.city = city;
        this.latitude = latitude;
        this.district = district;
        this.longitude = longitude;
        this.job = job;
        this.classAndName = classAndName;
    }

    public String getCity() {
        return city;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getDistrict() {
        return district;
    }

    public String getLongitude() {
        return longitude;
    }
}

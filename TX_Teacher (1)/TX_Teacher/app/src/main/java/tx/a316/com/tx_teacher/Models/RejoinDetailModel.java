package tx.a316.com.tx_teacher.Models;

public class RejoinDetailModel {
    private String reviewDetail;
    private String data;
    private String review;
    private String detail;
    private String classAndName;

    public RejoinDetailModel(String reviewDetail, String data, String review, String detail, String classAndName) {
        this.reviewDetail = reviewDetail;
        this.data = data;
        this.review = review;
        this.detail = detail;
        this.classAndName = classAndName;
    }

    public String getReviewDetail() {
        return reviewDetail;
    }

    public void setReviewDetail(String reviewDetail) {
        this.reviewDetail = reviewDetail;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getClassAndName() {
        return classAndName;
    }

    public void setClassAndName(String classAndName) {
        this.classAndName = classAndName;
    }
}

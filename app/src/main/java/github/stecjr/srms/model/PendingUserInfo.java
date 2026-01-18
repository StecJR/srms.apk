package github.stecjr.srms.model;

public class PendingUserInfo {
    public int userId;
    public String shortDept;
    public String email;

    public PendingUserInfo() {
    }

    public PendingUserInfo(int userId, String shortDept, String email) {
        this.userId = userId;
        this.shortDept = shortDept;
        this.email = email;
    }
}

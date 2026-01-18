package github.stecjr.srms.model;

public class GuardianSession {
    public int guardianId;
    public int studentId;
    public int deptId;

    public GuardianSession(int guardianId, int studentId, int deptId) {
        this.guardianId = guardianId;
        this.studentId = studentId;
        this.deptId = deptId;
    }
}

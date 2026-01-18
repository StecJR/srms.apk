package github.stecjr.srms.model;

public class GuardianInfo {
    public int guardianId;
    public String name;
    public String relation;
    public String contact;
    public String email;
    public int studentId;
    public int deptId;
    public String password;

    public GuardianInfo() {
    }

    public GuardianInfo(int guardianId, String name, String relation, String contact, String email, int studentId, int deptId, String password) {
        this.guardianId = guardianId;
        this.name = name;
        this.relation = relation;
        this.contact = contact;
        this.email = email;
        this.studentId = studentId;
        this.deptId = deptId;
        this.password = password;
    }

    public static String getQuery() {
        return "CREATE TABLE guardians" +
                " (" +
                "guardianId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "relation TEXT, " +
                "contact TEXT, " +
                "email TEXT UNIQUE, " +
                "studentId INTEGER, " +
                "deptId INTEGER, " +
                "password TEXT, " +
                "FOREIGN KEY (deptId) REFERENCES dept_info(deptId)" +
                ");";
    }
}

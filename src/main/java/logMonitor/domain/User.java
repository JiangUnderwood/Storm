package logMonitor.domain;

/**
 * @Author : Frank Jiang
 * @Date : 17/05/2018 3:48 PM
 */
public class User {
    private int id;             //用户编号
    private String name;        //用户名称
    private String mobile;      //用户手机
    private String email;       //用户邮箱
    private int isValid;        //用户是否可用

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", isValid=" + isValid +
                '}';
    }

    /*public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Message msg = new Message();
        msg.setKeyword("I am a keyword.");
        msg.setRuleId("885");
        msg.setLine("This's the message detail information.");
        msg.setAppId("883");
        msg.setAppName("What's the app name.");
        msg.setIsEmail(1);
        msg.setIsPhone(1);

        Record record = new Record();
        BeanUtils.copyProperties(record, msg);
        System.out.println(record);
    }*/
}

package logMonitor.domain;

/**
 * 触发报警之后的记录bean
 *
 * @Author : Frank Jiang
 * @Date : 17/05/2018 11:27 AM
 */
public class Record {
    private int id;         //告警信息编号
    private int appId;      //告警信息所属的应用
    private int ruleId;     //告警信息所属的规则
    private int isEmail;    //告警信息是否通过邮件
    private int isPhone;    //告警信息是否通过短信
    private int isClose;    //告警信息是否处理完毕
    private String line;    //原始日志信息

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(int isEmail) {
        this.isEmail = isEmail;
    }

    public int getIsPhone() {
        return isPhone;
    }

    public void setIsPhone(int isPhone) {
        this.isPhone = isPhone;
    }

    public int getIsClose() {
        return isClose;
    }

    public void setIsClose(int isClose) {
        this.isClose = isClose;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", appId=" + appId +
                ", ruleId=" + ruleId +
                ", isEmail=" + isEmail +
                ", isPhone=" + isPhone +
                ", isClose=" + isClose +
                ", line='" + line + '\'' +
                '}';
    }
}

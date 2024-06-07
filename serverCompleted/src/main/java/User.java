public class User {
    private String id;
    private String hashedPassword;
    private int money;
    private int remainingTime;
    private String salt;

    public User() {}

    public User(String id, String hashedPassword, int money, int remainingTime, String salt) {
        this.id = id;
        this.hashedPassword = hashedPassword;
        this.money = money;
        this.remainingTime = remainingTime;
        this.salt = salt;
    }

    public String getId() {
        return id;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public int getMoney() {
        return money;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public String getSalt() {
        return salt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}

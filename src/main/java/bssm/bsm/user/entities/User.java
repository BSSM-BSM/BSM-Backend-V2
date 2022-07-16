package bssm.bsm.user.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private long usercode;

    @Column(nullable = false, length = 20)
    private String id;

    @Column(nullable = false, length = 40)
    private String nickname;

    @Column(name = "uniq_no", nullable = false, length = 10)
    private String uniqNo;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "created_at", nullable = false, length = 40)
    private Date createdAt;

    @Column(nullable = false, length = 64)
    private String pw;

    @Column(name = "pw_salt", nullable = false, length = 64)
    private String pwSalt;


    public long getUsercode() {
        return usercode;
    }

    public void setUsercode(long usercode) {
        this.usercode = usercode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUniqNo() {
        return uniqNo;
    }

    public void setUniqNo(String uniqNo) {
        this.uniqNo = uniqNo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPwSalt() {
        return pwSalt;
    }

    public void setPwSalt(String pwSalt) {
        this.pwSalt = pwSalt;
    }
}

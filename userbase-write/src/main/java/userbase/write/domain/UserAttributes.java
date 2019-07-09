package userbase.write.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_attributes")
public class UserAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "telephone")
    private String telephone;

    /*
    @Column(name = "extension")
    private String extension;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "email")
    private String email;
    */


    @Column(name = "voip")
    private String voip;


    @Column(name = "jobTitle")
    private String jobTitle;


    @Column(name = "lastUpdated")
    private Date lastUpdated;

    public UserAttributes() { }

    public UserAttributes(String telephone, String voip, String jobTitle, Date lastUpdated) {
        this.telephone = telephone;
        this.voip = voip;
        this.jobTitle = jobTitle;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPhone() {
        return telephone;
    }

    public void setPhone(String telephone) {
        this.telephone = telephone;
    }

    public String getVoip() {
        return voip;
    }

    public void setVoip(String voip) {
        this.voip = voip;
    }

    public String getPosition() {
        return jobTitle;
    }

    public void setPosition(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

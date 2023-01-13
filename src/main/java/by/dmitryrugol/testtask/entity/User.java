package by.dmitryrugol.testtask.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String name;

    private Date dateOfBirth;

    @Column(nullable = false, length = 500)
    private String password;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
//            optional = false, fetch = FetchType.LAZY)
//    private Account account;

    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id")
    private List<EmailData> emails = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id")
    private List<PhoneData> phones = new ArrayList<>();

    public User() {
    }

    public User(String name, Date dateOfBirth, String password) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public Account getAccount() {
//        return account;
//    }
//
//    public void setAccount(Account account) {
//        this.account = account;
//    }

    public List<EmailData> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailData> emails) {
        this.emails = emails;
    }

    public List<PhoneData> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneData> phones) {
        this.phones = phones;
    }
}

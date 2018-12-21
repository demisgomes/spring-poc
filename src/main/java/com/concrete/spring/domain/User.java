package com.concrete.spring.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class User {

    /**
     *
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private String token;

    public User(Integer id, String name, String email, String password, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name="user_id")
    private List<Phone> phones;

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public User(Integer id, String name, String email, String password) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(Integer id, String name, String email, String password, Date created, Date modified, Date lastLogin, String token, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
        this.modified = modified;
        this.lastLogin = lastLogin;
        this.token = token;
        this.phones = phones;
    }

    @Override
    public String toString() {
        return String.format("id: "+getId()+", name: "+getName()+", email: "+getEmail()+", password: "+getPassword());
    }
}

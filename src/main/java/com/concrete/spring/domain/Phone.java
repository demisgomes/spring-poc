package com.concrete.spring.domain;

import javax.persistence.*;

@Entity
public class Phone {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String number;
    private Integer ddd;

    public Phone(Integer id, String number, Integer ddd, User user) {
        this.id = id;
        this.number = number;
        this.ddd = ddd;
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Phone(Integer id, String number, Integer ddd) {
        this.id = id;
        this.number = number;
        this.ddd = ddd;
    }

    public Phone(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getDdd() {
        return ddd;
    }

    public void setDdd(Integer ddd) {
        this.ddd = ddd;
    }
}

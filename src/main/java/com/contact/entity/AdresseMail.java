package com.contact.entity;

import jakarta.persistence.*;

@Entity
public class AdresseMail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String mail;

    @ManyToOne
    @JoinColumn(name="contact_fk")
    private Contact contact;

    public AdresseMail(String mail){
        this.mail = mail;
    }

    public Long getId(){
        return this.id;
    }

    public String getMail(){
        return this.mail;
    }

    public Contact getContact(){
        return this.contact;
    }

    public void setId(Long id){
        this.id = id;
    }
    public void setMail(String mail){
        this.mail = mail;
    }

    public void getContact(Contact contact){
        this.contact = contact;
    }

    @Override
    public String toString() {
        return String.format(
                "AdresseMail[id=%d, mail='%s']",
                id, mail);
    }
}

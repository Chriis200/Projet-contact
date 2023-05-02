package com.contact.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collection;

@Entity
public class Adresse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String address;
    @ManyToMany(mappedBy="adresses",
                cascade=CascadeType.ALL,
                fetch = FetchType.EAGER)
    private Collection<Contact> contacts;

    public Adresse(String ad){
        this.address = ad;
    }

    @Override
    public String toString() {
        return String.format(
                "Adresse[id=%d, adress='%s']",
                id, address);
    }

    public Long getId() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public Collection<Contact> getContacts() {
        return this.contacts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContacts(Collection<Contact> contacts) {
        this.contacts = contacts;
    }

}
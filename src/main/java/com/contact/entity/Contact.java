package com.contact.entity;

import com.contact.entity.Adresse;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    @ManyToMany
    @JoinTable(name="ADRESSES")
    private Collection<Adresse> adresses;

    @OneToMany(mappedBy="contact")
    private Collection<AdresseMail> mails;

    public Contact() {}

    public Contact(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Contact[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public Long getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Collection<Adresse> getAdresses() {
        return this.adresses;
    }

    public Collection<AdresseMail> getMails() {
        return this.mails;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setAdresses(Collection<Adresse> adresses){
        this.adresses = adresses;
    }

    public void setMails(Collection<AdresseMail> mails){
        this.mails = mails;
    }

    public Document contactToXml(){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

            // élément de racine
            Document doc = docBuilder.newDocument();
            Element racine = doc.createElement("contact");
            doc.appendChild(racine);

            Element contact = doc.createElement("id");
            contact.setTextContent(this.id+"");
            racine.appendChild(contact);

            contact = doc.createElement("firstName");
            contact.setTextContent(this.firstName);
            racine.appendChild(contact);

            contact = doc.createElement("lastName");
            contact.setTextContent(this.lastName);
            racine.appendChild(contact);

            return doc;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public static Document listToXml(List<Contact> contacts){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

            // élément de racine
            Document doc = docBuilder.newDocument();
            Element racine = doc.createElement("repertoire");
            doc.appendChild(racine);

            for(Contact c : contacts){
                Element contact = doc.createElement("contact");
                racine.appendChild(contact);

                Document d = c.contactToXml();
                Element racineC = d.getDocumentElement();

                NodeList racineNoeuds = racineC.getChildNodes();
                int nbRacineNoeuds = racineNoeuds.getLength();
                for (int i = 0; i<nbRacineNoeuds; i++) {
                    if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element ct = (Element) racineNoeuds.item(i);
                        switch (ct.getNodeName()){
                            case "id":
                                Element id = doc.createElement("id");
                                id.setTextContent(ct.getTextContent());
                                contact.appendChild(id);
                                break;
                            case "firstName":
                                Element fn = doc.createElement("firstName");
                                fn.setTextContent(ct.getTextContent());
                                contact.appendChild(fn);
                                break;
                            case "lastName":
                                Element ln = doc.createElement("lastName");
                                ln.setTextContent(ct.getTextContent());
                                contact.appendChild(ln);
                                break;
                        }
                    }
                }
            }
            return doc;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Contact contactFromXml(Document doc){
        try {
            Element racine = doc.getDocumentElement();

            NodeList racineNoeuds = racine.getChildNodes();
            int nbRacineNoeuds = racineNoeuds.getLength();
            Contact contact = new Contact();
            for (int i = 0; i < nbRacineNoeuds; i++) {
                if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element ct = (Element) racineNoeuds.item(i);
                    switch (ct.getNodeName()) {
                        case "id":
                            contact.setId(Long.parseLong(ct.getTextContent()));
                            break;
                        case "firstName":
                            contact.setFirstName(ct.getTextContent());
                            break;
                        case "lastName":
                            contact.setLastName(ct.getTextContent());
                            break;
                    }
                }
            }

            return contact;
        } catch (DOMException e) {
            throw new RuntimeException(e);
        }
    }
}

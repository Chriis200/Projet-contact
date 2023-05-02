package com.contact.controller;

import com.contact.entity.Contact;
import com.contact.repository.ContactRepository;
import com.util.XmlConverter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
class ContactApiController {
    @Autowired
    private ContactRepository contactRepo;

    @GetMapping(path = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public void operationsContacts(@RequestParam(name = "action") String action,
                             @RequestParam(name = "id", required = false) Long id,
                             HttpServletResponse response) {

        if (action.equals("listContacts")) {
            response.setContentType("application/xml");

            List<Contact> contacts = (List<Contact>) contactRepo.findAll();

            try {
                response.getOutputStream().println(XmlConverter.xmlToString(Contact.listToXml(contacts)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else if (action.equals("getContact")) {
            if (id == null) {
                try {
                    response.getOutputStream().println("Vous devez renseigner l'id du contact");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (contactRepo.findById(id).isEmpty()) {
                    try {
                        response.getOutputStream().println("Contact non trouvé");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    response.setContentType("application/xml");
                    Contact c = contactRepo.findById(id).get();
                    try {
                        response.getOutputStream().println(XmlConverter.xmlToString(c.contactToXml()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        } else if (action.equals("delContact")) {
            if (id == null) {
                try {
                    response.getOutputStream().println("Vous devez renseigner l'id du contact");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (contactRepo.findById(id).isEmpty()) {
                    try {
                        response.getOutputStream().println("Contact non trouvé");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    contactRepo.deleteById(id);
                    try {
                        response.getOutputStream().println("Contact supprimé");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        } else {
            try {
                response.getOutputStream().println("Action non reconnue");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String addContact(HttpEntity<String> httpEntity) {
        String xmlString = httpEntity.getBody();
        Document doc = XmlConverter.StringToXml(xmlString);

        Contact c = Contact.contactFromXml(doc);

        contactRepo.save(c);
        return "Contact sauvegardé";
    }

    @PostMapping(value = "/edit", consumes = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String editContact(HttpEntity<String> httpEntity) {
        String xmlString = httpEntity.getBody();
        Document doc = XmlConverter.StringToXml(xmlString);

        Contact c = Contact.contactFromXml(doc);
        if(contactRepo.findById(c.getId()).isEmpty()){
            return "Ce contact n'a pas été trouvé. Il ne peut donc pas être modifié";
        }
        Contact modif = contactRepo.findById(c.getId()).get();
        modif.setLastName(c.getLastName());
        modif.setFirstName(c.getFirstName());
        contactRepo.save(modif);
        return "Contact modifié";
    }
}

package com.contact.controller;

import com.contact.entity.Contact;
import com.contact.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class ContactController {
    @Autowired
    private ContactRepository contactRepo;

    @GetMapping("/list")
    public String showContactList(Model model) {
        model.addAttribute("contacts", contactRepo.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String addContact(Model model) {
        model.addAttribute("contact", new Contact());
        return "ajouter";
    }

    @PostMapping("/add")
    public String addContactSubmit(@ModelAttribute Contact contact) {
        contactRepo.save(contact);
        return "redirect:/list";
    }

    @GetMapping("/edit/{id}")
    public String editContact(@PathVariable("id") Long id, Model model) {
        if(contactRepo.findById(id).isEmpty()){
            return "redirect:/notFound";
        }
        Contact contact = contactRepo.findById(id).get();
        contact.setId(id);
        model.addAttribute("contact", contact);
        return "modifier";
    }

    @PostMapping("/edit")
    public String editContactSubmit(@ModelAttribute Contact contact) {
        contactRepo.save(contact);
        return "redirect:/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") Long id, Model model) {
        if(contactRepo.findById(id).isEmpty()){
            return "redirect:/notFound";
        }
        contactRepo.deleteById(id);
        return "redirect:/list";
    }

    @GetMapping("/notFound")
    public String showNotFound(Model model) {
        return "non-trouve";
    }


}
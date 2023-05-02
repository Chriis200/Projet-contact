package com.contact.repository;

import com.contact.entity.AdresseMail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdresseMailRepository extends CrudRepository<AdresseMail, Long> { }
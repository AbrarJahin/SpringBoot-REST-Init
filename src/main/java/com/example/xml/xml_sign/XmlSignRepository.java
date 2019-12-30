package com.example.xml.xml_sign;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called xmlSignerRepository
// CRUD refers Create, Read, Update, Delete

public interface XmlSignRepository extends CrudRepository<XmlSign, Integer> {
    XmlSign findByToken(String token);
}

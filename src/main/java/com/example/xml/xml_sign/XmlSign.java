package com.example.xml.xml_sign;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class XmlSign  {     //extends BaseEntityAudit

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique=true, length=256)
    private String token;

    @Column(columnDefinition = "TEXT")
    private String xmlDigest;

    @Column(columnDefinition = "TEXT")
    private String signedXMLDigest;

    public String getSignedXMLDigest() {
        return signedXMLDigest;
    }

    public void setSignedXMLDigest(String signedXMLDigest) {
        this.signedXMLDigest = signedXMLDigest;
    }

    public String getXmlDigest() {
        return xmlDigest;
    }

    public void setXmlDigest(String xmlDigest) {
        this.xmlDigest = xmlDigest;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

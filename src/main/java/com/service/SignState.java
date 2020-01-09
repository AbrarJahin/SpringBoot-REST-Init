package com.service;

import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;

import java.io.Serializable;

public class SignState implements Serializable {
    public XAdESSignatureParameters xAdESSignatureParameters;
    public XAdESService xAdESService;

    public SignState(XAdESSignatureParameters xAdESSignatureParameters, XAdESService xAdESService) {
        this.xAdESService = xAdESService;
        this.xAdESSignatureParameters = xAdESSignatureParameters;
    }
}
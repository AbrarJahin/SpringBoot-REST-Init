package com.ex;

import eu.europa.esig.dss.token.*;
import java.io.IOException;
import java.security.KeyStore;



public class KeyStoreService {

    private AbstractSignatureTokenConnection signingToken=null;
    private DSSPrivateKeyEntry privateKey;

    public KeyStoreService() {
        /*
        signingToken = new MSCAPISignatureToken();
        List<DSSPrivateKeyEntry> list = signingToken.getKeys();
        privateKey = list.get(0);
        */
            String pkcs12TokenFile = "E:\\XML\\keystore-demo\\certificate-sha256.pfx";	//Public info
            try {
				//signingToken = new Pkcs12SignatureToken(pkcs12TokenFile, "pass");
	             signingToken = new Pkcs12SignatureToken(pkcs12TokenFile, new KeyStore.PasswordProtection("User123$".toCharArray()));
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //signingToken = new Pkcs12SignatureToken(pkcs12TokenFile, new KeyStore.PasswordProtection("pass".toCharArray()));

            privateKey = signingToken.getKeys().get(0);
    }

    public AbstractSignatureTokenConnection getSigningToken() {
        return signingToken;
    }

    public void setSigningToken(AbstractSignatureTokenConnection signingToken) {
        this.signingToken = signingToken;
    }

    public DSSPrivateKeyEntry getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(DSSPrivateKeyEntry privateKey) {
        this.privateKey = privateKey;
    }
}

package com.ex;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.util.encoders.Hex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import eu.europa.esig.dss.x509.CertificateToken;

public class Util {
	
	public static CertificateToken[] getCertificateTokensFromJsonString(String  certsJson) throws CertificateException {
		Certificate[] chain=getX509CertsFromJsonString(certsJson);
		CertificateToken[] certificateChain = getCertificateTokensFromX509Certs(chain);
		return certificateChain;
	}
//	private static CertificateToken convert(X509Certificate cert) {
//		CertificateToken certificateToken = new CertificateToken((X509Certificate) cert);
//		return certificateToken ;
//	}
	
	private static Certificate[] getX509CertsFromJsonString(String  certsJson) throws CertificateException {
    	Type listType = new TypeToken<List<String>>() {}.getType();
    	Gson gson = new GsonBuilder().create();
    	List<String> certs = gson.fromJson(certsJson, listType );
    	
    	Certificate[] chain= new Certificate[certs.size()];
    	int i=0;
    	CertificateFactory cf=CertificateFactory
		     .getInstance("X509");
    	for(String certHex:certs)
    	{
    		 Certificate myCert =cf.generateCertificate(
                                         // string encoded with default charset
 					new ByteArrayInputStream(Hex.decode(certHex))
 				     );
    		 System.out.println(myCert);
    	        System.out.println("Certificates from the chain " + myCert  );

    		 chain[i++]= myCert;
    	}
    	return chain;
	}
	
	private static CertificateToken[] getCertificateTokensFromX509Certs(Certificate[] simpleCertificateChain) {
		
		final List<CertificateToken> x509CertificateList = new ArrayList<CertificateToken>();
		for (final Certificate certificate : simpleCertificateChain) {

			x509CertificateList.add(new CertificateToken((X509Certificate) certificate));
		}
		final CertificateToken[] certificateChain_ = new CertificateToken[x509CertificateList.size()];
		CertificateToken[] certificateChain = x509CertificateList.toArray(certificateChain_);
		return certificateChain ;
	}

	

}

package com.webs.itmexicali.rg.BattleShock;

import java.security.MessageDigest;

public class Secure {

	
	public static String sha1Hash( String toHash ){
	    String hash = null;
	    try  {
	        MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
	        byte[] bytes = toHash.getBytes("UTF-8");
	        digest.update(bytes, 0, bytes.length);
	        bytes = digest.digest();
	        StringBuilder sb = new StringBuilder();
	        for( byte b : bytes )
	            sb.append( String.format("%02X", b) );
	        hash = sb.toString();
	    }catch( Exception e ){  e.printStackTrace();   }
	    return hash;
	}
}

package rs.fon.is.movies.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import rs.fon.is.movies.domain.Thing;


public class URIGenerator {
	
	public static <T extends Thing> URI generateURI(T resource) throws URISyntaxException{
		
		String uri = Constants.NS + resource.getClass().getSimpleName()+ "/" + UUID.randomUUID();
	//	System.out.println(uri);
		return new URI(uri);
	}

}


package rs.fon.is.movies.parser;

import java.net.URI;
import java.util.HashMap;

import org.jsoup.nodes.Element;

import rs.fon.is.movies.domain.Person;
import rs.fon.is.movies.util.URIGenerator;


public class MoviePersonParser {

	private static HashMap<String, Person> personsCache = new HashMap<String, Person>();

	public static Person parse(Element personElements) {
		// parse data about persons related to movies e.g. actors, directors etc
		Person person = null;
		try {

			String url = ((personElements.select("[itemprop=url]")).attr("href"));
			URI personUrl = new URI("http://www.rottentomatoes.com" + url);
			String name = ((personElements.select("[itemprop=name]")).text());
			if (!personsCache.containsKey(url)) {
				person = new Person();
				person.setURL(personUrl);
				person.setName(name);
				person.setUri(URIGenerator.generateURI(person));
				personsCache.put(url, person);
			} else {
				person = personsCache.get(url);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return person;
	}
}

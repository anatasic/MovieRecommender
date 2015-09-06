package rs.fon.is.movies.crawler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MovieCrawler {

	public static HashMap<String, URI> moviesLinks = new HashMap<String, URI>();

	public static void collectLinks(Document doc) throws URISyntaxException {
		// collects links that contain movie data
		Elements links = doc.select("a");

		for (Element link : links) {
			int counter = 0;
			String href = link.attr("href");

			if (href.startsWith("/m/") && !exists(href)) {
				for (int i = 0; i < href.length(); i++) {
					if (href.charAt(i) == '/') {
						counter++;
					}
				}
				if (counter < 4) {
					moviesLinks.put(href, new URI("http://www.rottentomatoes.com" + href));
				}
			}
		}

	}

	public static boolean exists(String href) {

		if (moviesLinks.containsKey(href)) {
			return true;
		}
		return false;
	}
}

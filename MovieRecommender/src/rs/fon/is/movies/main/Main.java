package rs.fon.is.movies.main;

import java.util.HashMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.parser.MovieParser;
import rs.fon.is.movies.persistence.DataModelManager;

public class Main {

	static HashMap<String, URI> moviesLinks = new HashMap<String, URI>();

	public static void main(String[] args) throws URISyntaxException {

    for (int i = 1; i < 10; i++) {
			Document doc = null;
			try {
				doc = Jsoup.parse(new URL(
						"http://www.rottentomatoes.com/movie/in-theaters/?page="
								+ i), 17000);
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
			collectLinks(doc);

		}
		for (String href : moviesLinks.keySet()) {
			try {
				Movie movie = MovieParser.parse(moviesLinks.get(href));
				if (movie != null) {
					DataModelManager.getInstance().save(movie);
					System.out.println(movie.getName());
				}

			} catch (Exception e) {
				e.printStackTrace();

			}

		}
		DataModelManager.getInstance().closeDataModel();
	
	//	MovieCategories.getMovieCategories("Stuck in Love", 2013);
	}
	
	private static void collectLinks(Document doc) throws URISyntaxException {
		Elements links = doc.select("a");
		for (Element link : links) {
			String href = link.attr("href");
			if (href.startsWith("/m/") && !exists(href)) {
				moviesLinks.put(href, new URI("http://www.rottentomatoes.com"
						+ href));
			}
		}

	}

	private static boolean exists(String href) {

		if (moviesLinks.containsKey(href)) {
			return true;
		}
		return false;
	}

}

package rs.fon.is.movies.crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.domain.Person;
import rs.fon.is.movies.services.MovieServiceImpl;
import rs.fon.is.movies.util.Constants;

public class MovieCrawler {

	public static HashMap<String, URI> moviesLinks = new HashMap<String, URI>();
	private static MovieServiceImpl movieRepository = new MovieServiceImpl();

	public static void collectLinks(Document doc) throws URISyntaxException {
		// collects links that contain movie data
		Elements links = doc.select("a");

		for (Element link : links) {
			int counter = 0;
			String href = link.attr("href");

			if (href.startsWith("/m/") && !linkExists(href)) {
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

	public static boolean linkExists(String href) {

		if (moviesLinks.containsKey(href)) {
			return true;
		}
		return false;
	}

	public static boolean alreadyExists(Movie movie) {
		// TODO Auto-generated method stub
		String directors = "";
		for (Person director : movie.getDirectors()){
			directors = director.getName() +",";
		}
		Movie movieRep = movieRepository.getMovie(movie.getName(), directors);
		if (movieRep != null){
			return true;
		}
		return false;
	}

}

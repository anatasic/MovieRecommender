package rs.fon.is.movies.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.velocity.exception.MacroOverflowException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import rs.fon.is.movies.domain.Category;
import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.domain.Person;
import rs.fon.is.movies.file.SimilarityWriter;
import rs.fon.is.movies.parser.MovieParser;
import rs.fon.is.movies.persistence.DataModelManager;
import rs.fon.is.movies.similarity.CosineSimilarityCalculator;
import rs.fon.is.movies.similarity.TfIdfCalculator;

public class Main {

	static HashMap<String, URI> moviesLinks = new HashMap<String, URI>();

	public static void main(String[] args) throws URISyntaxException {

		
		Document doc = null;
		try {
			doc = Jsoup.parse(new URL("http://www.rottentomatoes.com/top/bestofrt/"), 17000);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		collectLinks(doc);

		
		List<Movie> movies = new ArrayList<>();
		for (String href : moviesLinks.keySet()) {
			if (movies.size() < 150) {
				try {
					Movie movie = MovieParser.parse(moviesLinks.get(href));					
					if (movie.getCategories().size() != 0) {
						DataModelManager.getInstance().save(movie);
						movies.add(movie);
					}
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		}

		HashMap<String, List<Double>> similarities = new HashMap<>();
		for (Movie movie : movies) {
			List<Double> tfIdfMovie1 = TfIdfCalculator.calculateTfIdfMovie(movie, movies);

			List<Double> values = new ArrayList<>();
			for (Movie m : movies) {

				List<Double> tfIdfMovie = TfIdfCalculator.calculateTfIdfMovie(m, movies);
				values.add(CosineSimilarityCalculator.cosineSimilarity(tfIdfMovie1, tfIdfMovie));
			}
			similarities.put(movie.getName(), values);
		}
		SimilarityWriter.writeInFile(movies, similarities);
		
		
		DataModelManager.getInstance().closeDataModel();

	}

	private static void collectLinks(Document doc) throws URISyntaxException {
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

	private static boolean exists(String href) {

		if (moviesLinks.containsKey(href)) {
			return true;
		}
		return false;
	}

}

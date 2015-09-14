package rs.fon.is.movies.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.URISyntaxException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import rs.fon.is.movies.crawler.MovieCrawler;
import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.file.SimilarityWriter;
import rs.fon.is.movies.parser.MovieParser;
import rs.fon.is.movies.persistence.DataModelManager;
import rs.fon.is.movies.similarity.CosineSimilarityCalculator;
import rs.fon.is.movies.similarity.TfIdfCalculator;

public class Main {

	public static void main(String[] args) throws URISyntaxException {
		Document doc = null;
		try {
			doc = Jsoup.parse(new URL("http://www.rottentomatoes.com/top/bestofrt/"), 17000);
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		// collects all links from this URL
		MovieCrawler.collectLinks(doc);
		List<Movie> movies = new ArrayList<>();
		for (String href : MovieCrawler.moviesLinks.keySet()) {
			if (movies.size() < 150) {
				try {					
						Movie movie = MovieParser.parse(MovieCrawler.moviesLinks.get(href));						
						if (!MovieCrawler.alreadyExists(movie) && movie.getCategories().size() != 0) {
							DataModelManager.getInstance().save(movie);
							movies.add(movie);
						}					
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		}

		HashMap<String, List<Double>> similarities = new HashMap<>();
		List<List<Double>> values = new ArrayList<>();
		// calculate tf-idf for each movie
		// cosine similarity is calculated based on tf-idfs
		for (Movie movie : movies) {
			List<Double> tfIdfMovie = TfIdfCalculator.calculateTfIdfMovie(movie, movies);
			values.add(tfIdfMovie);
		}

		int index = 0;
		List<Double> coefficients = new ArrayList<Double>();
		for (List<Double> coeffs1 : values) {
			for (List<Double> coeffs2 : values) {
				coefficients.add(CosineSimilarityCalculator.cosineSimilarity(coeffs1, coeffs2));
				similarities.put(movies.get(index).getName(), coefficients);
			}
			index++;
			coefficients = new ArrayList<>();
		}

		SimilarityWriter.writeInFile(movies, similarities);
		DataModelManager.getInstance().closeDataModel();

	}

}

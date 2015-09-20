package rs.fon.is.movies.services.rest;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import rs.fon.is.movies.crawler.MovieCrawler;
import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.file.SimilarityReader;
import rs.fon.is.movies.file.SimilarityWriter;
import rs.fon.is.movies.parser.MovieParser;
import rs.fon.is.movies.persistence.DataModelManager;
import rs.fon.is.movies.services.MovieServiceImpl;
import rs.fon.is.movies.similarity.CosineSimilarityCalculator;
import rs.fon.is.movies.similarity.TfIdfCalculator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/movies")
public class MovieRestService {

	private MovieServiceImpl movieRepository;

	public MovieRestService() {

		movieRepository = new MovieServiceImpl();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllMovies(@DefaultValue("0") @QueryParam("offset") String offset,
			@DefaultValue("100") @QueryParam("limit") String limit,
			@DefaultValue("") @QueryParam("minReleaseYear") String minReleaseYear,
			@DefaultValue("") @QueryParam("maxReleaseYear") String maxReleaseYear,
			@DefaultValue("") @QueryParam("actors") String actors,
			@DefaultValue("") @QueryParam("minRatingValue") String minRatingValue,
			@DefaultValue("") @QueryParam("maxRatingValue") String maxRatingValue,
			@DefaultValue("") @QueryParam("productionCompany") String productionCompany,
			@DefaultValue("") @QueryParam("genres") String genres,
			@DefaultValue("") @QueryParam("directedBy") String directedBy,
			@DefaultValue("") @QueryParam("minReviewCount") String minReviewCount,
			@DefaultValue("") @QueryParam("hasAwards") String hasAwards,
			@DefaultValue("") @QueryParam("categories") String categories) throws URISyntaxException {

		Collection<Movie> movies = movieRepository.getMovies(offset, limit, minReleaseYear, maxReleaseYear, actors,
				minRatingValue, maxRatingValue, productionCompany, genres, directedBy, minReviewCount, hasAwards,
				categories);
		if (movies != null && !movies.isEmpty()) {
			JsonArray moviesArray = new JsonArray();
			for (Movie movie : movies) {
				JsonObject movieJson = MovieJsonParser.serialize(movie);
				moviesArray.add(movieJson);
			}

			return moviesArray.toString();
		}

		throw new WebApplicationException(Response.Status.NO_CONTENT);
	}

	@GET
	@Path("{similar}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSimilarMovies(@QueryParam("title") String title,
			@DefaultValue("") @QueryParam("directedBy") String directedBy, @QueryParam("number") int noOfMovies) {

		Movie movie = movieRepository.getMovie(title, directedBy);
		if (movie != null) {
			JsonArray similarArray = new JsonArray();
			HashMap<String, Double> similarMovies = SimilarityReader.getInstance().readSimilarities(movie.getName());
			List<String> titles = SimilarityReader.getInstance().readTitles(similarMovies, noOfMovies);
			for (String t : titles) {
				JsonObject similarJson = MovieJsonParser.serializeSimilarMovies(t, similarMovies.get(t));
				similarArray.add(similarJson);
			}

			return similarArray.toString();
		}

		throw new WebApplicationException(Response.Status.NO_CONTENT);
	}

	@GET
	@Path("{update}/{repository}")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateMovieRepository(@QueryParam("url") String url) throws URISyntaxException {
		// this service updates movie repository
		// reads all movies from the repository
		// saves new movies
		// finally it recalculates all similarities and writes them into csv
		// file
		Collection<Movie> movies = movieRepository.getMovies("", "500", "", "", "", "", "", "", "", "", "", "", "");
		int size = 0;
		if (movies != null){
			size = movies.size();
		}else{
			movies = new ArrayList<>();
		}
		
		Document doc = null;
		try {
			doc = Jsoup.parse(new URL(url), 17000);
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);

		}
		MovieCrawler.collectLinks(doc);
		for (String href : MovieCrawler.moviesLinks.keySet()) {
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

		HashMap<String, List<Double>> similarities = new HashMap<>();
		List<List<Double>> values = new ArrayList<>();
		List<Movie> moviesList = new ArrayList<>();

		moviesList.addAll(movies);
		for (Movie movie : movies) {
			List<Double> tfIdfMovie = TfIdfCalculator.calculateTfIdfMovie(movie, moviesList);
			values.add(tfIdfMovie);
		}

		List<Double> coefficients = new ArrayList<Double>();
		int index = 0;
		for (List<Double> coeffs1 : values) {
			for (List<Double> coeffs2 : values) {
				coefficients.add(CosineSimilarityCalculator.cosineSimilarity(coeffs1, coeffs2));
				similarities.put(moviesList.get(index).getName(), coefficients);
			}
			index++;
			coefficients = new ArrayList<>();
		}
		SimilarityWriter.writeInFile(moviesList, similarities);
		DataModelManager.getInstance().closeDataModel();
		// as a response this service retrieves message with the number of
		// movies that were added in the repository
		return MovieJsonParser.serializeMessage(movies.size() - size).toString();

	}
}

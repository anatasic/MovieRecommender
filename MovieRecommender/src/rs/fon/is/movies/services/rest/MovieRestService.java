package rs.fon.is.movies.services.rest;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.file.SimilarityReader;
import rs.fon.is.movies.services.MovieServiceImpl;
import rs.fon.is.movies.util.Constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


@Path("/movies")
public class MovieRestService {

	private MovieServiceImpl movieRepository;
	
	public MovieRestService(){
		
		movieRepository = new MovieServiceImpl();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllMovies(@DefaultValue("0") @QueryParam("offset") String offset, @DefaultValue("100") @QueryParam("limit") String limit, @DefaultValue("") @QueryParam("minReleaseYear") String minReleaseYear, @DefaultValue("") @QueryParam("maxReleaseYear") String maxReleaseYear, 
			@DefaultValue("") @QueryParam("actors") String actors, @DefaultValue("") @QueryParam("minRatingValue") String minRatingValue, @DefaultValue("") @QueryParam("maxRatingValue") String maxRatingValue, @DefaultValue("") @QueryParam("productionCompany") String productionCompany,
			@DefaultValue("") @QueryParam("genres") String genres, @DefaultValue("") @QueryParam("directedBy") String directedBy, @DefaultValue("") @QueryParam("minReviewCount") String minReviewCount, @DefaultValue("") @QueryParam("hasAwards") String hasAwards, @DefaultValue("") @QueryParam("categories") String categories) throws URISyntaxException {
		
		Collection<Movie> movies = movieRepository.getMovies(offset, limit, minReleaseYear,maxReleaseYear, actors, minRatingValue, maxRatingValue, productionCompany, genres, directedBy, minReviewCount, hasAwards, categories);
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
	
//	@GET
//	@Path("{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String getMovie(@PathParam("id") String id){
//		
//		 Movie movie = movieRepository.getMovie(Constants.NS + (new Movie()).getClass().getSimpleName()+ "/"+id);
//		 if (movie != null) {		
//				JsonObject movieJson = MovieJsonParser.serialize(movie);				
//				return movieJson.toString();
//			}
//			
//			throw new WebApplicationException(Response.Status.NO_CONTENT);
//		}


//
	@GET
	@Path("{similar}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSimilarMovies(@QueryParam("title") String title, @QueryParam("directedBy") String directedBy) {
		
		
		Movie movie = movieRepository.getSimilarMovies(title, directedBy);
		if (movie != null) {
			JsonArray similarArray = new JsonArray();	
			HashMap<String, Double> similarMovies = SimilarityReader.getInstance().readSimilarities(movie.getName());
			List<String> titles = SimilarityReader.getInstance().readTitles(similarMovies);
			for(String t : titles){
				JsonObject similarJson = MovieJsonParser.serializeSimilarMovies(t, similarMovies.get(t));
				similarArray.add(similarJson);
			}
			
			return similarArray.toString();
		}

		throw new WebApplicationException(Response.Status.NO_CONTENT);
	}
}

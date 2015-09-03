package rs.fon.is.movies.services.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import rs.fon.is.movies.domain.AggregateRating;
import rs.fon.is.movies.domain.Category;
import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.domain.Person;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class MovieJsonParser {

	public static JsonObject serialize(Movie movie) {
		JsonObject movieJson = new JsonObject();
		movieJson.addProperty("uri", movie.getUri().toString());
		if (movie.getUrl() != null) {
			movieJson.addProperty("url", movie.getUrl().toString());
		}
		movieJson.addProperty("title", movie.getName());
		if (movie.getImage() != null) {
			movieJson.addProperty("image", movie.getImage().toString());
		}
		if (movie.getDatePublished() != null) {
			Date datePublished = movie.getDatePublished();
			SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
			movieJson.addProperty("datePublished",
					formatDate.format(datePublished));

		}

		if (movie.getDirectors() != null) {
			JsonArray directors = new JsonArray();
			for (Person p : movie.getDirectors()) {
				JsonObject personJson = serializePerson(p);
				directors.add(personJson);
			}
			movieJson.add("directors", directors);
		}
		if (movie.getActors() != null) {
			JsonArray actors = new JsonArray();
			for (Person p : movie.getActors()) {
				JsonObject personJson = serializePerson(p);
				actors.add(personJson);
			}
			movieJson.add("actors", actors);
		}
		JsonArray genres = new JsonArray();
		if (movie.getGenres() != null) {
			for (String genre : movie.getGenres()) {
				genres.add(new JsonPrimitive(genre));
			}
			movieJson.add("genres", genres);
		}
		JsonArray categories = new JsonArray();
		if (movie.getCategories() != null){
			for (Category category : movie.getCategories()){
				categories.add(serializeCategory(category));
			}			
			movieJson.add("categories", categories);
		}

	/*	if (movie.getAwards() != null) {
			movieJson.addProperty("awards", movie.getAwards());
		}*/
		if (movie.getAggregateRating() != null) {
			JsonObject aggRating = serializeAggRating(movie
					.getAggregateRating());
			movieJson.add("aggregateRating", aggRating);
		}
		if (movie.getDescription() != null) {
			movieJson.addProperty("description", movie.getDescription());
		}
		if (movie.getProductionCompany() != null) {
			movieJson.addProperty("productionCompany", movie
					.getProductionCompany().getName());
		}

		return movieJson;
	}

	private static JsonObject serializeAggRating(AggregateRating aggregateRating) {
		JsonObject aggRatingJson = new JsonObject();
		aggRatingJson.addProperty("name", aggregateRating.getName());
		aggRatingJson.addProperty("worstRating",
				aggregateRating.getWorstRating());
		aggRatingJson
				.addProperty("bestRating", aggregateRating.getBestRating());
		aggRatingJson.addProperty("ratingValue",
				aggregateRating.getRatingValue());
		aggRatingJson.addProperty("reviewCount",
				aggregateRating.getReviewCount());
		return aggRatingJson;
	}

	public static JsonObject serializePerson(Person person) {
		JsonObject personJson = new JsonObject();
		personJson.addProperty("name", person.getName());
		personJson.addProperty("url", person.getURL().toString());
		return personJson;
	}
	
	public static JsonObject serializeCategory(Category category){
		JsonObject categoryJson = new JsonObject();
		categoryJson.addProperty("prefLabel", category.getLabel());
		JsonArray broaderCategories = new JsonArray();
		for (Category broader : category.getBroader()){
			JsonPrimitive broaderCat = new JsonPrimitive(broader.getLabel());
			broaderCategories.add(broaderCat);
		}
		
		categoryJson.add("broader", broaderCategories);
		return categoryJson;
	}
	
	public static JsonObject serializeSimilarMovies(String titleUrl, double coefficient){
		JsonObject similarJson = new JsonObject();
		String title = titleUrl.substring(0, titleUrl.indexOf('(')-1);
		String url = titleUrl.substring(titleUrl.indexOf('(')+1, titleUrl.indexOf(')'));
		similarJson.addProperty("title", title);
		similarJson.addProperty("url", url);
		similarJson.addProperty("similar", coefficient);	
		return similarJson;
	}
	
	public static JsonObject serializeMessage (int size){
		JsonObject messageJson = new JsonObject();
		messageJson.addProperty("message", "Reppository updated with "+size+" movies.");
		return messageJson;
	}

}

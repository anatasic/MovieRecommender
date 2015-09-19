package rs.fon.is.movies.services;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.persistence.DataModelManager;
import rs.fon.is.movies.util.Constants;

public class MovieServiceImpl implements MovieService {

	QueryExecutor queryExecutor;

	public MovieServiceImpl() {
		queryExecutor = new QueryExecutor();
	}

	public Collection<Movie> getMovies(String offset, String limit, String minReleaseYear, String maxReleaseYear,
			String actors, String minRatingValue, String maxRatingValue, String productionCompany, String genres,
			String directedBy, String minReviewCount, String hasAwards, String categories) {
		// SPARQL query to get all movies with or without filters
		String prefix = "PREFIX schema: <" + Constants.SCHEMA + "> " + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "SELECT ?movie ";
		String where = " ?movie a schema:Movie . ";
		String constraint = "";

		if (!minRatingValue.isEmpty()) {
			where += "?movie schema:aggregateRating ?aggRating. " + "?aggRating schema:ratingValue ?ratingValue."
					+ "FILTER (?ratingValue > " + minRatingValue + ") ";
		}
		if (!maxRatingValue.isEmpty()) {
			where += "?movie schema:aggregateRating ?aggRating. " + "?aggRating schema:ratingValue ?ratingValue."
					+ "FILTER (?ratingValue < " + maxRatingValue + ") ";
		}
		if (!productionCompany.isEmpty()) {
			where += "?movie schema:productionCompany ?productionCompany. " + "FILTER regex(?productionCompany, \""
					+ productionCompany + "\" , \"i\") ";
		}
		if (!minReleaseYear.isEmpty()) {
			where += "?movie schema:datePublished ?datePublished. " + "FILTER (?datePublished >= \"" + minReleaseYear
					+ "-01-01T00:00:00\"^^xsd:dateTime) ";
		}
		if (!maxReleaseYear.isEmpty()) {
			where += "?movie schema:datePublished ?datePublished. " + "FILTER (?datePublished <= \"" + maxReleaseYear
					+ "-12-31T00:00:00\"^^xsd:dateTime) ";
		}
		if (!directedBy.isEmpty()) {
			where += "?movie schema:director ?director. " + "?director schema:name ?name. " + "FILTER regex(?name, \""
					+ directedBy + "\", \"i\") ";
		}
		if (!minReviewCount.isEmpty()) {
			where += "?movie schema:aggregateRating ?aggRating. " + "?aggRating schema:reviewCount ?reviewCount. "
					+ "FILTER (?reviewCount > " + minReviewCount + ")";
		}
		if (!actors.isEmpty()) {
			String[] movieActors = actors.split(",");
			for (int i = 0; i < movieActors.length; i++) {
				where += "?movie schema:actors ?actors" + i + ". ?actors" + i + " schema:name ?name" + i
						+ " FILTER regex(?name" + i + ", \"" + movieActors[i] + "\") ";
			}
		}
		if (!genres.isEmpty()) {
			String[] movieGegres = genres.split(",");
			for (int i = 0; i < movieGegres.length; i++) {
				where += "?movie schema:genre ?genres" + i + ". " + "FILTER regex(?genres" + i + ", \"" + movieGegres[i]
						+ "\")";
			}
		}
		if (!categories.isEmpty()) {
			String[] movieCategories = categories.split(",");
			for (int i = 0; i < movieCategories.length; i++) {
				where += "?movie schema:categories ?categories" + "i" + "schema:label ?label" + i
						+ "FILTER regex(?label" + i + ",\"" + movieCategories[i] + "\")";
			}
		}
		if (!hasAwards.isEmpty()) {
			where += "?movie schema:awards ?awards. ";
		}
		if (!limit.isEmpty()) {
			constraint += "LIMIT " + limit;
		}
		if (!offset.isEmpty()) {
			constraint += "OFFSET " + offset;
		}
		String query = prefix + " WHERE {" + where + "} " + constraint;
		Collection<String> result = queryExecutor.executeOneVariableSelectSparqlQuery(query, "movie",
				DataModelManager.getInstance().getModel());
		Collection<Movie> movies = new ArrayList<Movie>();

		if (result != null && !result.isEmpty()) {
			for (String s : result) {
				Movie m = (Movie) DataModelManager.getInstance().load(s);
				movies.add(m);
			}

			return movies;
		}

		return null;
	}

	public Movie getMovie(String uri) {
		// service to retrieve one movie
		Movie movie = (Movie) DataModelManager.getInstance().load(uri);
		return movie;
	}

	@Override
	public Movie getMovie(String title, String directors) {
		// service to retrieve movie based on title and director
		String[] movieDirectors = directors.split(",");
		String prefix = "PREFIX schema: <" + Constants.SCHEMA + "> " + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "SELECT ?movie ";
		String where = " ?movie a schema:Movie . ";
		where += "?movie schema:name ?name. " + "FILTER regex(?name, \"" + title + "\", \"i\") ";
		if (!directors.isEmpty()) {
			for (int i = 0; i < movieDirectors.length; i++) {
				where += "?movie schema:directors ?directors" + i + ". ?directors" + i + " schema:name ?name" + i
						+ " FILTER regex(?name" + i + ", \"" + movieDirectors[i] + "\") ";
			}
		}
		String query = prefix + " WHERE {" + where + "} ";
		Collection<String> result = queryExecutor.executeOneVariableSelectSparqlQuery(query, "movie",
				DataModelManager.getInstance().getModel());

		if (result != null && !result.isEmpty()) {
			String s = result.iterator().next();
			Movie m = (Movie) DataModelManager.getInstance().load(s);
			return m;

		}
		return null;
	}

}

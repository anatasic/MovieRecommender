package rs.fon.is.movies.services;

import java.util.Collection;

import rs.fon.is.movies.domain.Movie;

public interface MovieService {

	public Collection<Movie> getMovies(String offset, String limit, String minReleaseYear, String maxRealiseYear, String actors,
			String minRatingValue, String maxRatingValue, String productionCompany, String genres, String directedBy, String minReviewCounts, String hasAwards, String categories);
	
	public Movie getMovie(String uri);
	
	public Movie getSimilarMovies(String title, String director);

}

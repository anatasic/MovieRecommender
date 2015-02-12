package rs.fon.is.movies.domain;



import rs.fon.is.movies.util.Constants;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

@Namespace(Constants.SCHEMA)
@RdfType("AggregateRating")
public class AggregateRating extends Thing {

	@RdfProperty(Constants.SCHEMA + "name")
	private String name;
	
	@RdfProperty(Constants.SCHEMA + "reviewCount")
	private int reviewCount;
	
	@RdfProperty(Constants.SCHEMA + "bestRating")
	private int bestRating;
	
	@RdfProperty (Constants.SCHEMA + "worstRating")
	private int worstRating;
	
	@RdfProperty(Constants.SCHEMA + "ratingValue")
	private int ratingValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public int getBestRating() {
		return bestRating;
	}

	public void setBestRating(int bestRating) {
		this.bestRating = bestRating;
	}

	public int getWorstRating() {
		return worstRating;
	}

	public void setWorstRating(int worstRating) {
		this.worstRating = worstRating;
	}

	public int getRatingValue() {
		return ratingValue;
	}

	public void setRatingValue(int ratingValue) {
		this.ratingValue = ratingValue;
	}
	
}

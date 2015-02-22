package rs.fon.is.movies.domain;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import rs.fon.is.movies.util.Constants;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

	
@Namespace(Constants.SCHEMA)
@RdfType("Movie")
public class Movie extends Thing {

	@RdfProperty(Constants.SCHEMA + "name")
	private String name;
	
	@RdfProperty(Constants.SCHEMA + "genre")
	private Collection<String> genres;
	
	@RdfProperty(Constants.SCHEMA + "datePublished")
	private Date datePublished;
	
/*	@RdfProperty(Constants.SCHEMA + "awards")
	private String awards;
	*/
	@RdfProperty(Constants.SCHEMA + "contentRating")
	private String contentRating;
	
	@RdfProperty(Constants.SCHEMA + "image")
	private URI image;
	
	@RdfProperty(Constants.SCHEMA + "description")
	private String description;
	
	@RdfProperty(Constants.SCHEMA + "actors")
	private Collection<Person> actors;
	
	@RdfProperty(Constants.SCHEMA + "categories")
	private Collection<Category> categories;

	@RdfProperty(Constants.SCHEMA + "directors")
	private Collection<Person> directors;
	
	@RdfProperty(Constants.SCHEMA + "aggregateRating")
	private AggregateRating aggregateRating;
	
	@RdfProperty(Constants.SCHEMA + "productionCompany")
	private Organization productionCompany;
	
	@RdfProperty(Constants.SCHEMA + "url")
	private URI url;
	
	public URI getUrl()  {
		return url;
	}

	public void setUrl(URI movieUrl) throws MalformedURLException {
		this.url = movieUrl;
	}

	public Movie() {
		actors = new ArrayList <Person>();
		genres = new ArrayList<String>();
		categories = new ArrayList<Category>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<String> getGenres() {
		return genres;
	}

	public void setGenres(Collection<String> genres) {
		this.genres = genres;
	}

	public Date getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}

	/*public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}*/

	public String getContentRating() {
		return contentRating;
	}

	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
	}

	public URI getImage() {
		return image;
	}

	public void setImage(URI image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<Person> getActors() {
		return actors;
	}

	public void setActors(Collection<Person> actors) {
		this.actors = actors;
	}

	public Collection<Person> getDirectors() {
		return directors;
	}

	public void setDirectors(Collection<Person> directors) {
		this.directors = directors;
	}

	public AggregateRating getAggregateRating() {
		return aggregateRating;
	}

	public void setAggregateRating(AggregateRating aggregateRating) {
		this.aggregateRating = aggregateRating;
	}

	public Organization getProductionCompany() {
		return productionCompany;
	}

	public void setProductionCompany(Organization productionCompany) {
		this.productionCompany = productionCompany;
	}
	
	public void addActor(Person p){
		this.actors.add(p);
	}

	public Collection<Category> getCategories() {
		return categories;
	}

	public void setCategories(Collection<Category> categories) {
		this.categories = categories;
	}
	
	
	}
	


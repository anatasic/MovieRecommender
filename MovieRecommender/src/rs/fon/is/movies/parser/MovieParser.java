package rs.fon.is.movies.parser;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import rs.fon.is.movies.domain.AggregateRating;
import rs.fon.is.movies.domain.Category;
import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.domain.Organization;
import rs.fon.is.movies.domain.Person;
import rs.fon.is.movies.sparql.MovieCategories;
import rs.fon.is.movies.util.URIGenerator;

public class MovieParser {

	public static Movie parse(URI movieUrl) {
		Movie movie = null;
		try {
			Document doc = Jsoup.parse(movieUrl.toURL(), 7000);
			Elements properties = doc
					.select("[itemtype=http://www.schema.org/Movie]");
			if (properties.size() != 0) {

				movie = new Movie();
				movie.setUrl(movieUrl);

				String name = parseName(properties.select("[itemprop=name]"));
				movie.setName(name);

				if (properties.select("[itemprop=image]").size() != 0) {
					URI imageUrl = parseImage(properties
							.select("[itemprop=image]"));
					movie.setImage(imageUrl);
				} 

				if (properties.select("[itemprop=description]").size() != 0) {
					String description = properties.select(
							"[itemprop=description]").text();
					movie.setDescription(description);
				} 

				if (properties.select("[itemprop=genre]").size() != 0) {
					List<String> genres = parseGenres(properties
							.select("[itemprop=genre]"));
					movie.setGenres(genres);
				} 

				if (properties.select("[itemprop=datePublished]").size() != 0) {
					Date datePublished = parseDate(properties.select(
							"[itemprop=datePublished]").attr("content"));
					movie.setDatePublished(datePublished);
				}

				if (properties.select("[itemprop=awards]").size() != 0) {
					String awards = (properties.select("[itemprop=awards]"))
							.attr("content");
					movie.setAwards(awards);
				} 

				if (properties.select("[itemprop=contentRating]").size() != 0) {
					String contentRating = (properties
							.select("[itemprop=contentRating]")).text();
					movie.setContentRating(contentRating);
				} 

				if (properties.select("[itemprop=director]").size() != 0) {
					Person director = parseDirector(properties
							.select("[itemprop=director]"));
					movie.setDirector(director);
				} 

				if (properties.select("[itemprop=actors]").size() != 0) {
					List<Person> actors = parseActors(properties
							.select("[itemprop=actors]"));
					movie.setActors(actors);
				} 

				if (properties.select("[itemprop=aggregateRating]").size() != 0) {
					AggregateRating rating = parseAggregateRating(properties
							.select("[itemprop=aggregateRating]"));
					movie.setAggregateRating(rating);
				} 

				if (properties.select("[itemprop=productionCompany]").size() != 0) {
					Organization organization = parseOrganization(properties
							.select("[itemprop=productionCompany]"));
					movie.setProductionCompany(organization);
				} 
				movie.setUri(URIGenerator.generateURI(movie));
				Calendar cal = Calendar.getInstance();
				cal.setTime(movie.getDatePublished());
				int yearPublished = cal.get(Calendar.YEAR);
				Collection<Category> categories = MovieCategories.getMovieCategories(movie.getName(), yearPublished);
				movie.setCategories(categories);
			}

		} catch (Exception e) {

			System.out.println("Error while parsing!");
		}
		return movie;

	}

	private static List<String> parseGenres(Elements elements) {
		List<String> genres = new ArrayList<String>();
		for (Element genre : elements) {
			genres.add(genre.text());
		}
		return genres;
	}

	public static List<Person> parseActors(Elements elements)
			throws MalformedURLException, URISyntaxException {
		List<Person> actors = new ArrayList<Person>();
		for (Element e : elements) {
			Person actor = MoviePersonParser.parse(e);
			actors.add(actor);
		}
		return actors;

	}

	public static Person parseDirector(Elements elements)
			throws MalformedURLException, URISyntaxException {
		Person director = null;
		for (Element e : elements) {
			director = MoviePersonParser.parse(e);
		}
		return director;

	}

	public static Organization parseOrganization(Elements elements)
			throws URISyntaxException {
		Organization production = new Organization();
		String productionCompany = elements.select(
				"[itemprop=productionCompany]").text();
		production.setName(productionCompany);
		URI productionUri = URIGenerator.generateURI(production);
		production.setUri(productionUri);
		return production;

	}

	public static AggregateRating parseAggregateRating(Elements elements)
			throws URISyntaxException {
		String agName = (elements.select("[itemprop=name]").attr("content"));
		AggregateRating rating = new AggregateRating();
		try {
			int ratingValue = Integer.parseInt(elements.select(
					"[itemprop=ratingValue]").text());
			int bestRating = Integer.parseInt(elements.select(
					"[itemprop=bestRating]").attr("content"));
			int worstRating = Integer.parseInt((elements
					.select("[itemprop=worstRating]")).attr("content"));
			int reviewCount = Integer.parseInt((elements
					.select("[itemprop=reviewCount]")).text());
			rating.setRatingValue(ratingValue);
			rating.setReviewCount(reviewCount);
			rating.setBestRating(bestRating);
			rating.setWorstRating(worstRating);
		} catch (Exception e) {
			rating.setRatingValue(0);
			rating.setReviewCount(0);
			rating.setBestRating(0);
			rating.setWorstRating(0);
		}

		rating.setName(agName);

		rating.setUri(URIGenerator.generateURI(rating));
		return rating;

	}

	public static String parseName(Elements elements) {
		for (Element el : elements) {
			if (!el.parents().hasAttr("itemprop")) {
				for (Element child : el.children()) {
					System.out.println(child.text());
					if (child.select("span") != null){
						
						child.remove();
					}
				} 
				String movieName = el.text();
				System.out.println(movieName);
				return movieName;
			}
		}
		return null;
	}

	public static URI parseImage(Elements elements) throws URISyntaxException {
		for (Element el : elements) {
			if (!el.parents().hasAttr("itemprop")) {
				URI image = new URI(el.attr("src"));
				return image;
			}
		}
		return null;
	}

	public static Date parseDate(String sDatePublished) throws ParseException {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		if (!sDatePublished.isEmpty()) {
			Date datePublished = formatDate.parse(sDatePublished);
			return datePublished;
		}
		return null;

	}
}

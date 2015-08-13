package rs.fon.is.movies.similarity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jetty.security.ConstraintSecurityHandler;

import rs.fon.is.movies.domain.Category;
import rs.fon.is.movies.domain.Movie;

public class TfIdfCalculator {

	/*
	 * public static double calculateTf(Collection<Category> totalCategories,
	 * String categoryToCheck) { double count = 0; // to count the overall
	 * occurrence of the term // termToCheck double numOfCategories = 0; for
	 * (Category cat : totalCategories) { if
	 * (cat.getLabel().equalsIgnoreCase(categoryToCheck)) { count++; } /* for
	 * (Category broader : cat.getBroader() ){ if
	 * (broader.getLabel().equalsIgnoreCase(categoryToCheck)){ count++; }
	 * numOfCategories++; } for (Category narrower : cat.getNarrower() ){ if
	 * (narrower.getLabel().equalsIgnoreCase(categoryToCheck)){ count++; }
	 * numOfCategories++; }
	 * 
	 * numOfCategories++; } return count; }
	 * 
	 * public static double calculateIdf(List<Movie> movies, String
	 * categoryToCheck) { double count = 0; for (Movie m : movies) { for
	 * (Category cat : m.getCategories()) { if
	 * (cat.getLabel().equalsIgnoreCase(categoryToCheck)) { count++; } } }
	 * return Math.log(movies.size() / (1 + count));
	 * 
	 * }
	 * 
	 * public static List<List<Double>> tfIdfCalculator(List<Movie> movies) {
	 * List<Category> allCategories = getAllCategories(movies);
	 * List<List<Double>> vector = new ArrayList<>(); for (Movie movie : movies)
	 * { List<Double> tfIdfMovie = calculateTfIdfForMovie(movies, allCategories,
	 * movie); vector.add(tfIdfMovie); } return vector; }
	 * 
	 * private static List<Double> calculateTfIdfForMovie(List<Movie> movies,
	 * List<Category> allCategories, Movie movie) { List<Double> tfidfCoeff =
	 * new ArrayList<>(); for (Category cat : allCategories) {
	 * System.out.println(cat.getLabel()); double tf =
	 * calculateTf(movie.getCategories(), cat.getLabel()); double idf =
	 * calculateIdf(movies, cat.getLabel()); double tfidf = tf * idf;
	 * System.out.println("TF: "+tf); System.out.println("IDF" + idf);
	 * tfidfCoeff.add(tfidf);
	 * 
	 * } return tfidfCoeff; }
	 * 
	 */
	private static Collection<Category> getAllCategories(List<Movie> movies) {
		int i = 0;
		List<Category> allCategories = new ArrayList<Category>();
		for (Movie m : movies) {
			if (i == 0) {
				allCategories.addAll(m.getCategories());
			} else {
				for (Category c : m.getCategories()) {
					if (containsCategory(c, allCategories) == 0)
						allCategories.add(c);
				}
			}

		}
		return allCategories;
	}

	private static double containsCategory(Category cat, Collection<Category> listOfCat) {
		double i = 0;
		for (Category c : listOfCat) {
			if (c.getLabel().equalsIgnoreCase(cat.getLabel())) {
				i++;
			} else if (c.getBroader().contains(cat)) {
				for (Category cc : c.getBroader()) {
					if (cc.getLabel().equals(cat.getLabel())) {
						i = i + 0.6;
					}
				}
			} else {
				for (Category broader : cat.getBroader()) {
					if (broader.getLabel().equals(c.getLabel())) {
						i = i + 0.4;
					}
				}
			}
		}
		return i;
	}

	public static List<Double> calculateTfIdfMovie(Movie m, List<Movie> movies) {
		Collection<Category> allCategories = getAllCategories(movies);
		List<Double> tfidf = new ArrayList<Double>();
		double frequency = 0.0;
		for (Category cat : allCategories) {
			frequency = containsCategory(cat, m.getCategories());
			if (frequency != 0.0) {
				double a = calculateFrequencyForAllMovies(cat, movies);
				tfidf.add(frequency * (Math.log(movies.size() / a)));

			}

			else {
				tfidf.add(0.0);

			}
		}
		return tfidf;

	}


	private static double calculateFrequencyForAllMovies(Category cat, List<Movie> movies) {
		// TODO Auto-generated method stub
		double count = 0;
		for (Movie m : movies) {
			for (Category c : m.getCategories()) {
				if (c.getLabel().equalsIgnoreCase(cat.getLabel())) {
					count++;
				} else if (c.getBroader().contains(cat)) {
					for (Category cc : c.getBroader()) {
						if (cc.getLabel().equals(cat.getLabel())) {
							count = count + 0.6;
						}
					}
				} else {
					for (Category broader : cat.getBroader()) {
						if (broader.getLabel().equals(c.getLabel())) {
							count = count + 0.4;
						}
					}
				}
			}
		}

		return count;
	}

}

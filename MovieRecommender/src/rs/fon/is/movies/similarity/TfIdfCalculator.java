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
					if (!containsCategory(c, allCategories))
						allCategories.add(c);
				}
			}

		}
		return allCategories;
	}

	private static boolean containsCategory(Category cat, Collection<Category> listOfCat) {
		for (Category c : listOfCat) {
			if (c.getLabel().equalsIgnoreCase(cat.getLabel()))
				return true;
		}
		return false;
	}

	public static List<Double> calculateTfIdfMovie(Movie m, List<Movie> movies) {
		Collection<Category> allCategories = getAllCategories(movies);
		List<Double> tfidf = new ArrayList<Double>();
		for (Category cat : allCategories) {
			// System.out.println(cat.getLabel());
			if (containsCategory(cat, m.getCategories())) {
				int a = calculateFrequencyForAllMovies(cat, movies);
				tfidf.add(Math.log(movies.size() / a));
//			} else {
//				if (belongsToBroaderCategories(cat, m.getCategories())) {
//					int a = calculateFrequencyForAllMovies(cat, movies);
//					double value = 0.6 * (Math.log(movies.size() / a));
//					tfidf.add(value);
//					System.out.println("Broader: " + value);
//				
				} else {
					tfidf.add(0.0);

			//	}
			}
		}
		return tfidf;

	}

	private static boolean shareBroaderCategories(Category cat, Collection<Category> categories) {
		for (Category c : categories) {
			for (Category broaderCat : c.getBroader()) {
				if (containsCategory(broaderCat, cat.getBroader())) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean belongsToBroaderCategories(Category cat, Collection<Category> categories) {
		for (Category c : categories) {
			if (containsCategory(cat, c.getBroader())) {
				return true;
			}
		}
		return false;
	}

	private static int calculateFrequencyForAllMovies(Category cat, List<Movie> movies) {
		// TODO Auto-generated method stub
		int count = 0;
		for (Movie movie : movies) {
			if (containsCategory(cat, movie.getCategories())) {
				count++;
			}
		}
		return count;
	}

}

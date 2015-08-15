package rs.fon.is.movies.similarity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import rs.fon.is.movies.domain.Category;
import rs.fon.is.movies.domain.Movie;

public class TfIdfCalculator {

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
		boolean exist = false;

		for (Category c : listOfCat) {
			if (c.getLabel().equalsIgnoreCase(cat.getLabel())) {
				exist = true;
				i++;
			} else {
				for (Category cc : c.getBroader()) {
					if (cc.getLabel().equals(cat.getLabel())) {
						exist = true;
						i = i + 0.6;
					}
				}
			}
			if (!exist) {
				for (Category cc : cat.getBroader()) {
					if (cc.getLabel().equals(cat.getLabel())) {
						exist = true;
						i = i + 0.6;
					}
				}
			}
			if (!exist) {
				for (Category broader1 : cat.getBroader()) {
					for (Category broader2 : c.getBroader()) {
						if (broader1.getLabel().equals(broader2.getLabel())) {
							i = i + 0.4;
						}
					}
				}
			}
			exist = false;
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
			count += containsCategory(cat, m.getCategories());
		}

		return count;
	}

}

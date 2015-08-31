package rs.fon.is.movies.similarity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import rs.fon.is.movies.domain.Category;
import rs.fon.is.movies.domain.Movie;

public class TfIdfCalculator {

	private static Collection<Category> getAllCategories(List<Movie> movies) {

		// this method retrieves all categories for movies
		// in the first loop it gets all categories from one movie and puts them
		// in the new list
		// in every next loop there is check if movie is already in the list
		// (based on title)
		// if it is not than movie will be added in the new list

		int i = 0;
		List<Category> allCategories = new ArrayList<Category>();
		for (Movie m : movies) {
			if (i == 0) {
				allCategories.addAll(m.getCategories());
				i++;
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
		double frequency = 0;
		boolean exist = false;
		for (Category c : listOfCat) {
			// first check if category exists in list of all categories; in that
			// case
			// frequency is increased by one
			if (c.getLabel().equalsIgnoreCase(cat.getLabel())) {
				exist = true;
				frequency++;
			} else {
				// in other case all broader categories are read
				// if selected category belongs to broader categories than
				// frequency is increased by 0.6
				// exist flag is set to true
				for (Category cc : c.getBroader()) {
					if (cc.getLabel().equals(cat.getLabel())) {
						exist = true;
						frequency = frequency + 0.6;
					}
				}
			}
			if (!exist) {
				// the other way around
				// checks if category from the list is contained in broader
				// categories
				// of selected category
				// if yes, frequency is increased by 0.6, exist flag is set to
				// true
				for (Category cc : cat.getBroader()) {
					if (cc.getLabel().equals(cat.getLabel())) {
						exist = true;
						frequency = frequency + 0.6;
					}
				}
			}
			if (!exist) {
				// in the last case, broader categories of selected category
				// are compared to broader categories of category in the list
				// if there is match, frequency is increased by 0.4
				for (Category broader1 : cat.getBroader()) {
					for (Category broader2 : c.getBroader()) {
						if (broader1.getLabel().equals(broader2.getLabel())) {
							frequency = frequency + 0.4;
						}
					}
				}
			}
			exist = false;
		}
		return frequency;
	}

	public static List<Double> calculateTfIdfMovie(Movie m, List<Movie> movies) {
		
		// for each category of selected movie, frequency is calculated
		// if frequency is different than 0 than TF-IDF is calculated
		// otherwise, TF-IDF is 0
		
		Collection<Category> allCategories = getAllCategories(movies);
		List<Double> tfidf = new ArrayList<Double>();
		double frequency = 0.0;
		for (Category cat : allCategories) {
			frequency = containsCategory(cat, m.getCategories());			
			if (frequency != 0.0) {
				double a = calculateFrequencyForAllMovies(cat, movies);
				tfidf.add(frequency * (Math.log(movies.size() / a)));
			} else {
				tfidf.add(0.0);
			}
		}
		return tfidf;
	}

	private static double calculateFrequencyForAllMovies(Category cat, List<Movie> movies) {
		// frequency of selected category is calculated for all movies
		double frequency = 0.0;
		for (Movie m : movies) {
			frequency += containsCategory(cat, m.getCategories());
		}

		return frequency;
	}

}

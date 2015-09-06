package rs.fon.is.movies.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rs.fon.is.movies.util.Constants;

public class SimilarityReader {

	private static SimilarityReader INSTANCE;
	private static final String COMMA_DELIMITER = ",";

	private SimilarityReader() {

	}

	public static SimilarityReader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SimilarityReader();
		}
		return INSTANCE;
	}

	public HashMap<String, Double> readSimilarities(String movieTitle) {
		BufferedReader br = null;
		// similarities are read one by one from .csv file
		// similarities are stored in this form
		// title1 url coeff11 coeff12 coeff13 ...
		// title2 url coeff21 coeff22 coeff23 ...
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(Constants.SIMILARITY));
			HashMap<String, String[]> similarities = new HashMap<>();
			int i = 0;
			int movieIndex = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				i++;
				if (!sCurrentLine.startsWith(movieTitle)) {
					String[] tokens = sCurrentLine.split(COMMA_DELIMITER);
					String title = tokens[0];
					String url = tokens[1];
					tokens[0] = tokens[2];
					similarities.put(title + "," + url, tokens);
				} else {
					// index of movie for which similarities are read
					movieIndex = i;
				}
			}
			br.close();

			HashMap<String, Double> similarMovies = new HashMap<>();
			for (String title : similarities.keySet()) {
				String[] coeff = similarities.get(title);
				similarMovies.put(title, Double.parseDouble(coeff[movieIndex - 1]));
			}
			return similarMovies;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<String> readTitles(HashMap<String, Double> similarMovies) {

		// retrieves the title of five most similar movies

		List<Double> values = new ArrayList<>(similarMovies.values());
		List<String> titles = new ArrayList<>();

		// values are sort in ascending order, that is why loop starts from the
		// last one
		Collections.sort(values);
		int count = 5;
		for (int i = values.size() - 1; i > 0; i--) {
			for (String title : similarMovies.keySet()) {
				if (similarMovies.get(title) == values.get(i) && count > 0) {
					titles.add(title);
					count--;
				}
			}
		}
		return titles;

	}

}

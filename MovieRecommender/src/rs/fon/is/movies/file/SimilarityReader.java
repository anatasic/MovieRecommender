package rs.fon.is.movies.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SimilarityReader {

	private static SimilarityReader INSTANCE;
	private static HashMap<String, Double> similarMovies;
	private static List<String> titles;

	private SimilarityReader() {
		// TODO Auto-generated constructor stub
		similarMovies = new HashMap<>();
		titles = new ArrayList<>();
	}

	public static SimilarityReader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SimilarityReader();
		}
		return INSTANCE;
	}


	public HashMap<String, Double> readSimilarities(String movieTitle) {

		BufferedReader br = null;

		try {

			String sCurrentLine;
			br = new BufferedReader(new FileReader("D:/movierecommender/similarities.txt"));
			HashMap<String, String[]> similarities = new HashMap<>();
			int i = 0;
			int movieIndex = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				i++;
				if (!sCurrentLine.startsWith(movieTitle)) {
					String title = sCurrentLine.substring(0, sCurrentLine.indexOf(':'));
					String[] similCoeff = (sCurrentLine.substring(sCurrentLine.indexOf(':') + 2)).split("  ");
					similarities.put(title, similCoeff);
				} else {
					movieIndex = i;
				}
			}
			br.close();
			//HashMap<String, Double> similarMovies = new HashMap<>();
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

	public List<String> readTitles() {
		// TODO Auto-generated method stub

		List<Double> values = new ArrayList<>(similarMovies.values());
		Collections.sort(values);
		int count = 5;
		for (int i = values.size()-1; i > 0; i--) {
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

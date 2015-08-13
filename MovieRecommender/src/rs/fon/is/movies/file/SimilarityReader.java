package rs.fon.is.movies.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SimilarityReader {

	private static SimilarityReader INSTANCE;

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
		// TODO Auto-generated method stub

		List<Double> values = new ArrayList<>(similarMovies.values());
		List<String>titles = new ArrayList<>();
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
	
	public List<String> readAllTitles() {
		// TODO Auto-generated method stub

		BufferedReader br = null;

			String sCurrentLine;
			try {
				br = new BufferedReader(new FileReader("D:/movierecommender/similarities.txt"));				
				List<String> titles = new ArrayList<>();
				while ((sCurrentLine = br.readLine()) != null) {
					titles.add(sCurrentLine.substring(0, sCurrentLine.indexOf(':')));
				}
				br.close();
				return titles;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
	}
}

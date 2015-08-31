package rs.fon.is.movies.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;

import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.util.Constants;

public class SimilarityWriter {

	public static void writeInFile(List<Movie> movies, HashMap<String, List<Double>> similiraties) {
		
		// similarities are written in file in the matrix form (explained in SimilarityReader class)
		File statText = new File(Constants.SIMILARITY);
		FileOutputStream is;
		try {
			is = new FileOutputStream(statText);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			BufferedWriter w = new BufferedWriter(osw);
			String line = "";

			for (Movie m : movies) {
				List<Double> values = similiraties.get(m.getName());
				line += m.getName() + " ("+m.getUrl().toString()+") :  ";
				for (double sim : values) {
					line += sim + "  ";
				}
				w.write(line);
				w.newLine();
				line = "";
			}

			w.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

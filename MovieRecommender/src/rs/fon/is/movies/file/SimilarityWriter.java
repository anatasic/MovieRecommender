package rs.fon.is.movies.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import rs.fon.is.movies.domain.Movie;

public class SimilarityWriter {
	
	public static void writeInFile (List<Movie>movies, HashMap<String, List<Double>> similiraties){
		File statText = new File("D:/movierecommender/similarities.txt");
        FileOutputStream is;
		try {
			is = new FileOutputStream(statText);
			OutputStreamWriter osw = new OutputStreamWriter(is);    
	        BufferedWriter w = new BufferedWriter(osw);
	        String line = "";
	      
	        for (Movie m : movies){
	        	List<Double> values = similiraties.get(m.getName());
	        	line += m.getName() +":  ";
	        	for (double sim : values){
	        		line += sim +"  ";
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

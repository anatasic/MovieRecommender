package rs.fon.is.movies.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.io.ByteArrayBuffer.CaseInsensitive;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.hp.hpl.jena.sparql.engine.http.HttpParams;

import rs.fon.is.movies.crawler.MovieCrawler;
import rs.fon.is.movies.domain.Movie;
import rs.fon.is.movies.file.SimilarityWriter;
import rs.fon.is.movies.parser.MovieParser;
import rs.fon.is.movies.persistence.DataModelManager;
import rs.fon.is.movies.similarity.CosineSimilarityCalculator;
import rs.fon.is.movies.similarity.TfIdfCalculator;

public class Main {

	public static void main(String[] args) throws URISyntaxException {
		/*
		 * Document doc = null; try { doc = Jsoup.parse(new
		 * URL("http://www.rottentomatoes.com/top/bestofrt/"), 17000); } catch
		 * (Exception e) { System.out.println(e.getMessage());
		 * 
		 * } // collects all links from this URL MovieCrawler.collectLinks(doc);
		 * List<Movie> movies = new ArrayList<>(); for (String href :
		 * MovieCrawler.moviesLinks.keySet()) { if (movies.size() < 150) { try {
		 * Movie movie = MovieParser.parse(MovieCrawler.moviesLinks.get(href));
		 * if (!MovieCrawler.alreadyExists(movie) &&
		 * movie.getCategories().size() != 0) {
		 * DataModelManager.getInstance().save(movie); movies.add(movie); } }
		 * catch (Exception e) { e.printStackTrace();
		 * 
		 * } } }
		 * 
		 * HashMap<String, List<Double>> similarities = new HashMap<>();
		 * List<List<Double>> values = new ArrayList<>(); // calculate tf-idf
		 * for each movie // cosine similarity is calculated based on tf-idfs
		 * for (Movie movie : movies) { List<Double> tfIdfMovie =
		 * TfIdfCalculator.calculateTfIdfMovie(movie, movies);
		 * values.add(tfIdfMovie); }
		 * 
		 * int index = 0; List<Double> coefficients = new ArrayList<Double>();
		 * for (List<Double> coeffs1 : values) { for (List<Double> coeffs2 :
		 * values) {
		 * coefficients.add(CosineSimilarityCalculator.cosineSimilarity(coeffs1,
		 * coeffs2)); similarities.put(movies.get(index).getName(),
		 * coefficients); } index++; coefficients = new ArrayList<>(); }
		 * 
		 * SimilarityWriter.writeInFile(movies, similarities);
		 * DataModelManager.getInstance().closeDataModel();
		 */
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to movie recommender system. ");
		System.out.println("If you want to get similar movies for specific movie, enter word similar. "
				+ "If you want to update movie repository with some movie, please enter word update.");
		String input;
		try {
			input = br.readLine();
			while (!input.equals("end")) {
				switch (input) {
				case "similar":
					System.out.println("Please enter movie title for which you want to get similar movies. ");
					String title = br.readLine();
					System.out.println("Please also enter number of similar movies that you expect.");
					String number = br.readLine();
					getSimilarMovies(title, number);
					System.out.println("Enter end to leave application. Enter similar to find similar "
							+ "movies for another movie. Enter update to update repository.");

					break;

				case "update":
					System.out
							.println("Please enter url of the movie from the website http:\\www.rottentommatoes.com.");
					String url = br.readLine();
					updateRepository(url);
					System.out.println("Enter end to leave application. Enter similar to find similar"
							+ "movies for another movie. Enter update to update repository.");

					break;

				default:
					System.out.println("Enter valid command.");

					break;
				}
				input = br.readLine();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void updateRepository(String urlMovie) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		DefaultHttpClient client = new DefaultHttpClient();
		String url = "/Movies/api/movies/update/repository?";
		HttpHost target = new HttpHost("localhost", 8080, "http");
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("url", urlMovie));
		String paramString = URLEncodedUtils.format(params, "utf-8");
		url += paramString;
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(target, get);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		if ((line = rd.readLine()) != null) {

			System.out.println(line);

		} else {
			System.out.println("No data for this movie.");
		}

	}

	private static void getSimilarMovies(String title, String number) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		DefaultHttpClient client = new DefaultHttpClient();
		String url = "/Movies/api/movies/similar?";
		HttpHost target = new HttpHost("localhost", 8080, "http");
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("title", title));
		params.add(new BasicNameValuePair("directedBy", ""));
		params.add(new BasicNameValuePair("number", number));
		String paramString = URLEncodedUtils.format(params, "utf-8");
		url += paramString;
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(target, get);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}

	}
}

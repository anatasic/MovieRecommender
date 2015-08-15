package rs.fon.is.movies.sparql;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rs.fon.is.movies.domain.Category;
import rs.fon.is.movies.domain.Person;
import rs.fon.is.movies.util.URIGenerator;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class MovieCategories {

	public static ArrayList<Category> getMovieCategories(String title, Collection<Person> directors) {
		ArrayList<Category> categories = new ArrayList<Category>();
		for (Person p : directors) {
			categories = findCategories(title, p);
			if (categories.size() > 0) {
				return categories;
			}
		}

		return new ArrayList<Category>();
	}

	private static ArrayList<Category> findCategories(String title, Person director) {
		ParameterizedSparqlString query = new ParameterizedSparqlString(""
				+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "prefix category: <http://dbpedia.org/page/>"
				+ "prefix dbpedia:  <http://dbpedia.org/resource/Category>"
				+ "prefix db:  <http://dbpedia.org/ontology/>" + "prefix dcterms: <http://purl.org/dc/terms/>"
				+ "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>"
				+ "prefix dbpprop: <http://dbpedia.org/property/>" + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "prefix skos: <http://www.w3.org/2004/02/skos/core#>" + "SELECT DISTINCT ?categoryName ?broader"
				+ " WHERE {" + "?movie rdf:type db:Film ." + "?movie dcterms:subject ?category."
				+ "?category rdfs:label ?categoryName." + "?movie dbpprop:name ?title."
				+ "?movie dbpprop:director ?director." + "?director rdfs:label ?name."
				+ "FILTER(lang(?categoryName) = \"en\" )." + "}");

		Model model = ModelFactory.createDefaultModel();
		Literal titleLit = model.createLiteral(title, "en");
		Literal directorLit = model.createLiteral(director.getName(), "en");

		query.setParam("title", titleLit);
		query.setParam("name", directorLit);

		QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query.asQuery());
		ResultSet results = exec.execSelect();
		ArrayList<Category> categories = new ArrayList<Category>();
		// System.out.println(query);
		while (results.hasNext()) {
			String category = results.next().get("categoryName").toString().replace("@en", "");
			List<Category> broader = getBroaderCategories(category);
		//	List<Category> narrower = getNarrowerCategories(category);
			Category cat = new Category();
			cat.setLabel(category);
			cat.setBroader(broader);
//			cat.setNarrower(narrower);
			URI categoryUri;
			try {
				categoryUri = URIGenerator.generateURI(cat);
				cat.setUri(categoryUri);
				categories.add(cat);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return categories;
	}

	private static List<Category> getBroaderCategories(String category) {
		// TODO Auto-generated method stub
		String catName = "<http://dbpedia.org/resource/Category:" + category.replace(" ", "_") + ">";
		String query = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "prefix skos: <http://www.w3.org/2004/02/skos/core#>" + "select ?broader where { " + catName
				+ "skos:broader ?value." + "?value rdfs:label ?broader" + "}";
		Query q = QueryFactory.create(query);
		QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", q);
		ResultSet results = exec.execSelect();
		ArrayList<Category> categories = new ArrayList<Category>();
		while (results.hasNext()) {
			String value = results.next().get("broader").toString().replace("@en", "");
			// System.out.println(value);
			Category broaderCat = new Category();
			broaderCat.setLabel(value);
			URI broaderURI;
			try {
				broaderURI = URIGenerator.generateURI(broaderCat);
				broaderCat.setUri(broaderURI);
				categories.add(broaderCat);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return categories;

	}

//	private static List<Category> getNarrowerCategories(String category) {
//		String categoryUnderscore = category.replace(" ", "_");
//		String categorySearch = "<http://dbpedia.org/resource/Category:" + categoryUnderscore + ">";
//		String catURL = "http://dbpedia.org/resource/Category:";
//		String query = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
//				+ "prefix skos: <http://www.w3.org/2004/02/skos/core#>" + "select distinct ?narrower "
//				+ "where { ?narrower skos:broader " + categorySearch + "}";
//		// System.out.println(query);
//		Query q = QueryFactory.create(query);
//		QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", q);
//		ResultSet results = exec.execSelect();
//		ArrayList<Category> categories = new ArrayList<Category>();
//		while (results.hasNext()) {
//			try {
//				RDFNode node = results.next().get("narrower");
//				String value = node.toString();
//				value = value.substring(catURL.length());
//				value = value.replace("_", " ");
//				Category narrowerCat = new Category();
//				narrowerCat.setLabel(value);
//				URI narrowerURI;
//				narrowerURI = URIGenerator.generateURI(narrowerCat);
//				narrowerCat.setUri(narrowerURI);
//				categories.add(narrowerCat);
//			} catch (URISyntaxException e) {
//				continue;
//			}
//
//		}
//
//		return categories;
//
//	}
}
package rs.fon.is.movies.sparql;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.border.TitledBorder;



import rs.fon.is.movies.domain.Category;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class MovieCategories{

	
	
	public static Collection<Category> getMovieCategories(String title, int datePublished) {
		ParameterizedSparqlString query = new ParameterizedSparqlString("" +
				"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				"prefix category: <http://dbpedia.org/page/>"+
				"prefix dbpedia:  <http://dbpedia.org/resource/Category>"+
				"prefix db:  <http://dbpedia.org/ontology/>"+
				"prefix dcterms: <http://purl.org/dc/terms/>" +
				"prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>"+
				"prefix dbpprop: <http://dbpedia.org/property/>"+
				"prefix xsd: <http://www.w3.org/2001/XMLSchema#>"+
				"prefix skos: <http://www.w3.org/2009/08/skos-reference/skos.html#>"+
				"SELECT DISTINCT ?categoryName" + " WHERE {"
						+ "?movie rdf:type db:Film ."						
						+ "?movie dcterms:subject ?category." +
						"?category rdfs:label ?categoryName."+
						"?movie dbpprop:name ?title." +
						"?movie db:releaseDate ?released."+
						"FILTER((year(xsd:date(?released)) = "+datePublished+") && lang(?categoryName) = \"en\")."+
				"}");
		Model model = ModelFactory.createDefaultModel();
		Literal titleLit = model.createLiteral(title, "en");
	//	Literal titleLit = ResourceFactory.getInstance().createResource(title).
		
		query.setParam("title", titleLit);
		System.out.println(query);
		QueryExecution exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query.asQuery() );
		ResultSet results = exec.execSelect();
		Collection<Category> categories = new LinkedList<Category>();
		while ( results.hasNext() ) {
			String category = results.next().get("category").toString().replace("@en", "");
			Category cat = new Category();
			cat.setLabel(category);
			categories.add(cat);
        }
		
		return categories;
		}

}

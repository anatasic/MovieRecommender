package rs.fon.is.movies.sparql;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.border.TitledBorder;


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

	
	
	public static Collection<String> getMovieCategories(String title) {
		ParameterizedSparqlString query = new ParameterizedSparqlString("" +
				"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				"prefix category: <http://dbpedia.org/page/>"+
				"prefix dbpedia:  <http://dbpedia.org/resource/Category>"+
				"prefix dcterms: <http://purl.org/dc/terms/>" +
				"prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>"+
				"prefix dbpprop: <http://dbpedia.org/property/>"+
				"SELECT DISTINCT ?categoryName" + " WHERE {"
						+ "?movie rdf:type <http://dbpedia.org/ontology/Film>."
						+ "?movie dcterms:subject ?category." +
						"?category rdfs:label ?categoryName."
						+ "?movie dbpprop:name ?title." +
						"FILTER (lang(?categoryName) = \"en\")" +
						"}");
		Model model = ModelFactory.createDefaultModel();
		Literal titleLit = model.createLiteral(title, "en");
	//	Literal titleLit = ResourceFactory.getInstance().createResource(title).
		
		query.setParam("title", titleLit);
		System.out.println(query);
		QueryExecution exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query.asQuery() );
		ResultSet results = exec.execSelect();
		Collection<String> categories = new LinkedList();
		while ( results.hasNext() ) {
            // As RobV pointed out, don't use the `?` in the variable
            // name here. Use *just* the name of the variable.
			String category = results.next().get("categoryName").toString().replace("@en", "");
            categories.add(category);
        }
		
		return categories;
		}

}

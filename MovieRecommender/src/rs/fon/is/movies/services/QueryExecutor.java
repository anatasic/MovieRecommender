package rs.fon.is.movies.services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class QueryExecutor {

	public Collection<String> executeOneVariableSelectSparqlQuery(String query,
			String variable, Model model) {
		List<String> results = new LinkedList<String>();
		Query q = QueryFactory.create(query);
		
		QueryExecution qe = QueryExecutionFactory.create(q, model);
		ResultSet resultSet = qe.execSelect();
		while (resultSet.hasNext()) {
			QuerySolution solution = resultSet.nextSolution();
			RDFNode value = solution.get(variable);
			if (value.isLiteral())
				results.add(((Literal) value).getLexicalForm());
			else
				results.add(((Resource) value).getURI());
		}
		qe.close();

		return results;
	}

}

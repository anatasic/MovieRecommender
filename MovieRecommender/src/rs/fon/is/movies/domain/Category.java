package rs.fon.is.movies.domain;

import java.util.LinkedList;
import java.util.List;

import rs.fon.is.movies.util.Constants;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

@Namespace(Constants.SKOS)
@RdfType("Concept")
public class Category extends Thing{

	@RdfProperty(Constants.SKOS + "prefLabel")
	private String label;
	
	/*@RdfProperty(Constants.SKOS + "broader")
	private List<Category> broaderCategories;
	
	public Category(){
		broaderCategories = new LinkedList<Category>();
	}*/

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/*public List<Category> getBroaderCategories() {
		return broaderCategories;
	}

	public void setBroaderCategories(List<Category> broaderCategories) {
		this.broaderCategories = broaderCategories;
	}*/
	
	
}

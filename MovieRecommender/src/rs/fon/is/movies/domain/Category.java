package rs.fon.is.movies.domain;

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
	
	@RdfProperty(Constants.SKOS + "broader")
	private List<Category> broader;
	
//	@RdfProperty(Constants.SKOS + "narrower")
//	private List<Category> narrower;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

//	public List<Category> getNarrower() {
//		return narrower;
//	}
//
//	public void setNarrower(List<Category> narrower) {
//		this.narrower = narrower;
//	}

	public List<Category> getBroader() {
		return broader;
	}

	public void setBroader(List<Category> broader) {
		this.broader = broader;
	}
	
	
	



	/*public List<Category> getBroaderCategories() {
		return broaderCategories;
	}

	public void setBroaderCategories(List<Category> broaderCategories) {
		this.broaderCategories = broaderCategories;
	}*/
	
	
}

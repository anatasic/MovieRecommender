
package rs.fon.is.movies.persistence;


import rs.fon.is.movies.persistence.dataprovider.DataProvider;
import rs.fon.is.movies.persistence.dataprovider.TDBDataProvider;
import thewebsemantic.Bean2RDF;
import thewebsemantic.RDF2Bean;
import com.hp.hpl.jena.rdf.model.Model;

public class DataModelManager {

	private DataProvider dataProvider;
	private RDF2Bean reader;
	private Bean2RDF writer;

	private static DataModelManager INSTANCE;
	
	private DataModelManager() { 
		dataProvider = new TDBDataProvider();
		
		reader = new RDF2Bean(getModel());		
		writer = new Bean2RDF(getModel());
	}

	public static DataModelManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataModelManager();
		}
		return INSTANCE;
	}

	public Model getModel() {
		return dataProvider.getDataModel();
	}

	public RDF2Bean getReader() {
		return reader;
	}

	public Bean2RDF getWriter() {
		return writer;
	}
	
	public void save(Object o) {
		writer.save(o);
	}
	
	public Object load(String uri) {
		return reader.load(uri);
	}
	
	public void closeDataModel() {
		dataProvider.close();
	}

}

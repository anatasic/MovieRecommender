package rs.fon.is.movies.persistence.dataprovider;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.base.file.Location;

import rs.fon.is.movies.util.Constants;

public class TDBDataProvider implements DataProvider {

	private Dataset dataset;

	public TDBDataProvider() {
		Location location = new Location(Constants.REPOSITORY);
		dataset = TDBFactory.createDataset(location);
	}

	@Override
	public Model getDataModel() {
		return dataset.getDefaultModel();
	}

	@Override
	public void close() {
		dataset.close();
	}

}

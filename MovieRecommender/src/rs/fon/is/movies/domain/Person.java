package rs.fon.is.movies.domain;

import java.net.URI;

import rs.fon.is.movies.util.Constants;
import thewebsemantic.Namespace;
import thewebsemantic.RdfType;
import thewebsemantic.RdfProperty;

@Namespace(Constants.SCHEMA)
@RdfType("Person")
public class Person extends Thing{

	@RdfProperty(Constants.SCHEMA + "name")
	private String name;
	
	/*@RdfProperty(Constants.SCHEMA + "description")
	private String description;
	
	@RdfProperty(Constants.SCHEMA + "image")
	private URI image;*/
	
	@RdfProperty(Constants.SCHEMA + "URI")
	private URI url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public URI getImage() {
		return image;
	}

	public void setImage(URI image) {
		this.image = image;
	}*/

	public URI getURL() {
		return url;
	}

	public void setURL(URI uRI) {
		url = uRI;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
}

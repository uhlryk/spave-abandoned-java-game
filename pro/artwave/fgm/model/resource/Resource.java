package pro.artwave.fgm.model.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;

public abstract class Resource{
	private XmlReader.Element _xmlObject;
	public void init() {
		readResource();
	}
	public void readResource(){
		XmlReader xml=new XmlReader();		
		try {
			this._xmlObject=xml.parse(getResourceFile());
		} catch (Exception e) {
			onNotFound(e);		
		}
	}
	protected void onNotFound(Exception e){
		throw new RuntimeException("There is problem with xml file "+this.getResourceName()+" "+e.getMessage());
	}
	public FileHandle getResourceFile(){
		return Gdx.files.internal(this.getResourceName());
	}
	public abstract String getResourceName();
	
	public XmlReader.Element getXmlObject(){
		return this._xmlObject;
	}
	
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}

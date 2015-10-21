package pro.artwave.fgm.model.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;

public abstract class Translator extends Resource {
	private static String _lang="en";
	public static void setLanguage(String lang){
		_lang=lang;
	}
	public static String getLanguage(){
		return _lang;
	}
	public String _(String key){
		return translate(key);
	}
	@Override
	public FileHandle getResourceFile(){
		return Gdx.files.internal(this.getResourceName()+getLanguage()+".xml");
	}	
	public String translate(String key){
		return this.translate(null, key);
	}
	public String _(String group,String key){
		return translate(group,key);
	}
	public String translate(String group,String key){		
		if(group==null){
			if(getXmlObject()==null){
				return key;
			}
			XmlReader.Element keyVal=getXmlObject().getChildByName(key);
			if(keyVal!=null){
				return keyVal.getText();
			}else{
				return key;
			}
		}else{
			if(getXmlObject()==null){
				return group+"."+key;
			}
			XmlReader.Element groupVal=getXmlObject().getChildByName(group);
			if(groupVal!=null){
				XmlReader.Element keyVal=groupVal.getChildByName(key);
				if(keyVal!=null){
					return keyVal.getText();
				}else{
					return group+"."+key;
				}
			}else{
				return group+"."+key;
			}			
		}
	}
}

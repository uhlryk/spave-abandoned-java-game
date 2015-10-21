package pro.artwave.spaceshooter.model.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

import pro.artwave.fgm.model.resource.Resource;

public class SaveGameResource extends Resource {

	private Map<String,String> _paramMap;
	private int _userId;

	public void init(int id){
		this._userId=id;
		super.init();	
		this.getFileData();
	}
	@Override
	protected void onNotFound(Exception e){
		this._paramMap=new HashMap<String,String>(20);
		this.setIntParam("ship",1);
		this.setIntParam("score",0);
		this.setIntParam("last_mission",0);
		this.setIntParam("weapon_1",1);
		this.setIntParam("weapon_2",2);
		this.saveFile();
		this.readResource();
	}
	public String getParam(String key){
		String val=_paramMap.get(key);
		return val;
	}
	public int getIntParam(String key){
		String val=this.getParam(key);
		if(val==null)return 0;
		return Integer.parseInt(val);
	}
	public void setParam(String key,String value){
		_paramMap.put(key, value);
	}
	public void setIntParam(String key,int value){
		this.setParam(key,Integer.toString(value));
	}
	private void getFileData(){
		Array<XmlReader.Element> xmlMap=this.getXmlObject().getChildrenByName("param");
		this._paramMap=new HashMap<String,String>(20);
		for(XmlReader.Element paramXml:xmlMap){
			_paramMap.put(paramXml.getAttribute("key"), paramXml.getAttribute("value"));
		}
	}
	/**
	 * Zapis punktów danej misji
	 * @param missionId
	 * @param score
	 */
	public void addSaveScore(int score){
		int oldScore=this.getIntParam("score");
		this.setIntParam("score",score+oldScore);
		this.saveFile();
	}
	public void saveScore(int score){
		this.setIntParam("score",score);
		this.saveFile();
	}
	public int getScore(){
		return this.getIntParam("score");
	}
	public void setActive(){
		this.setIntParam("is_active",1);
		this.saveFile();
	}
	public int getIsActive(){
		return this.getIntParam("is_active");
	}	
	/**
	 * Zapis statusu danej gry, jeœli wygraliœmy ustawia true
	 * @param missionId
	 * @param score
	 */
	public void saveMission(int missionId){
		this.setIntParam("last_mission",missionId);
		this.saveFile();
	}	
	public int getLastMission(){
	//	System.out.println(_paramMap);
		return this.getIntParam("last_mission");
	}
	/**
	 * Zmieniamy statek i dodajemy w to miejsce nowy
	 * @param shipId
	 */
	public void saveShip(int shipId){
		this.setIntParam("ship",shipId);
		this.saveFile();
	}
	public int getShip(){
		return this.getIntParam("ship");
	}
	/**
	 * Zmieniamy broñ o danym slocie na inn¹
	 * @param slotId
	 * @param weaponId
	 */
	public void saveWeapon(int slotId,int weaponId){
		switch(slotId){
		case 1:
			this.setIntParam("weapon_1",weaponId);
			break;
		case 2:
			this.setIntParam("weapon_2",weaponId);
			break;			
		}
		this.saveFile();
	}
	public void saveFirstWeapon(int weaponId){
		this.saveWeapon(1,weaponId);
	}
	public int getFirstWeapon(){
		return this.getIntParam("weapon_1");
	}
	public void saveSecondWeapon(int weaponId){
		this.saveWeapon(1,weaponId);
	}	
	public int getSecondWeapon(){
		return this.getIntParam("weapon_2");
	}	
	/**
	 * Metoda zapisuje plik, nale¿y podaæ mapê wszysztkich rekordów. Alternatyw¹ jest druga metoda saveFile
	 * @param map Map<missionId,score>
	 */
	public void saveFile(){
		FileHandle file=getResourceFile();
		StringWriter writer = new StringWriter();
		XmlWriter xmlWriter=new XmlWriter(writer);
		XmlWriter xmlData=null;
		XmlWriter xmlResource=null;
		try {
			xmlResource=xmlWriter.element("resource");
			if(_paramMap!=null){
				for(Entry<String,String> entry:_paramMap.entrySet()){
					String key=entry.getKey();
					String value=entry.getValue();
					xmlData=xmlResource.element("param");
					xmlData.attribute("key",key);
					xmlData.attribute("value",value);
					xmlData.pop();
				}
			}
			xmlResource.pop();
		} catch (IOException e) {
			e.printStackTrace();
			if(xmlWriter!=null){
				try {
					xmlWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		file.writeString(writer.toString(),false);
		if(xmlWriter!=null){
			try {
				xmlWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	@Override
	public String getResourceName() {
		return "save-"+this._userId+".txt";
	}
	@Override
	public FileHandle getResourceFile(){
		return Gdx.files.local(getResourceName());
	}
}

package pro.artwave.spaceshooter.model.resource;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import pro.artwave.fgm.model.resource.Resource;
import pro.artwave.spaceshooter.helper.BackfireParams;
import pro.artwave.spaceshooter.helper.BonusParams;
import pro.artwave.spaceshooter.helper.UnitParams;

public class BonusResource extends Resource {
	private Array<XmlReader.Element> _xmlMap;
	private Map<Integer,BonusParams> _map;
	@Override
	public String getResourceName() {
		return "data/bonus.xml";
	}
	public void init(){
		super.init();
		_xmlMap=this.getXmlObject().getChildrenByName("bonus");
		_map=new HashMap<Integer,BonusParams>();
		for(XmlReader.Element bonusXml:_xmlMap){
			int id=bonusXml.getIntAttribute("id");
			BonusParams params=new BonusParams();
			params.id=id;
			params.text=bonusXml.getAttribute("text");
			Array<XmlReader.Element> _addXmlArray=bonusXml.getChildrenByName("effect");
			for(XmlReader.Element addXml:_addXmlArray){
				BonusParams.Effect effect=new BonusParams.Effect();
				effect.type=addXml.getAttribute("type");
				effect.val1=parse(addXml.get("val1","0"));
				effect.val2=parse(addXml.get("val2","0"));
				effect.val3=parse(addXml.get("val3","0"));
				params.effectList.add(effect);
			}		
			_map.put(id, params);		
		}
	}
	public static int parse(String text){
		if(text.startsWith("0x")){
			return (int)Long.parseLong(text.substring(2),16);
		}else{
			return Integer.parseInt(text);
		}
	}	
	/**
	 * Zwraca mapê sk³adaj¹c¹ siê na klucz w postaci id statku i obiektu klasy ShipParams
	 * @return
	 */
	public Map<Integer,BonusParams> getBonusMap(){
		return _map;
	}
	/**
	 * Zwraca konkretny statek w postaci obiektu klasy ShipParams
	 * @param shipId
	 * @return
	 */
	public BonusParams getBonus(int bonusId){
		return _map.get(bonusId);
	}
}

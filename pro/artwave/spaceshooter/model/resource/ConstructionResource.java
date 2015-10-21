package pro.artwave.spaceshooter.model.resource;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import pro.artwave.fgm.model.resource.Resource;
import pro.artwave.spaceshooter.helper.UnitParams;

public class ConstructionResource extends Resource {
	private Array<XmlReader.Element> _xmlMap;
	private Map<Integer,UnitParams> _map;
	@Override
	public String getResourceName() {
		return "data/construction.xml";
	}
	public void init(){
		super.init();
		_xmlMap=this.getXmlObject().getChildrenByName("construction");
		_map=new HashMap<Integer,UnitParams>();
		for(XmlReader.Element turretXml:_xmlMap){
			int id=turretXml.getIntAttribute("id");
			UnitParams params=new UnitParams();
			params.id=id;
			Array<XmlReader.Element> _xmlArrayStat=turretXml.getChildrenByName("stat");
			for(XmlReader.Element statXml:_xmlArrayStat){
				String stat=statXml.getAttribute("type");
				int value=statXml.getIntAttribute("value");
				if("maxHealth".equals(stat))params.maxHealth=value;
				if("maxShield".equals(stat))params.maxShield=value;
				if("regeneration".equals(stat))params.regeneration=value;
				if("radius".equals(stat))params.radius=value;
				if("resistance".equals(stat))params.resistance=(value==1?true:false);
			}			
			_map.put(id, params);		
		}
	}
	/**
	 * Zwraca mapê sk³adaj¹c¹ siê na klucz w postaci id dzia³ka i obiektu klasy UnitParams
	 * @return
	 */
	public Map<Integer,UnitParams> getConstructionMap(){
		return _map;
	}
	/**
	 * Zwraca konkretny turret w postaci obiektu klasy UnitParams
	 * @param turretId
	 * @return
	 */
	public UnitParams getConstruction(int turretId){
		return _map.get(turretId);
	}
}

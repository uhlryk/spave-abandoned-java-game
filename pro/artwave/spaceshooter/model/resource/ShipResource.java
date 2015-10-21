package pro.artwave.spaceshooter.model.resource;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import pro.artwave.fgm.model.resource.Resource;
import pro.artwave.spaceshooter.helper.BackfireParams;
import pro.artwave.spaceshooter.helper.UnitParams;

public class ShipResource extends Resource {
	private Array<XmlReader.Element> _xmlMap;
	private Map<Integer,UnitParams> _map;
	@Override
	public String getResourceName() {
		return "data/ship.xml";
	}
	public void init(){
		super.init();
		_xmlMap=this.getXmlObject().getChildrenByName("ship");
		_map=new HashMap<Integer,UnitParams>();
		for(XmlReader.Element shipXml:_xmlMap){
			int id=shipXml.getIntAttribute("id");
			UnitParams params=new UnitParams();
			params.id=id;
			Array<XmlReader.Element> _xmlArrayStat=shipXml.getChildrenByName("stat");
			for(XmlReader.Element statXml:_xmlArrayStat){
				String stat=statXml.getAttribute("type");
				int value=statXml.getIntAttribute("value");
				if("maxHealth".equals(stat))params.maxHealth=value;
				if("maxShield".equals(stat))params.maxShield=value;
				if("regeneration".equals(stat))params.regeneration=value;
				if("maxSpeed".equals(stat))params.maxSpeed=value;
				if("rotation".equals(stat))params.rotation=value;
				if("radius".equals(stat))params.radius=value;
				
				if("playability".equals(stat))params.playability=(value==1?true:false);
				if("points".equals(stat))params.points=value;
			}
			params.backfire=new HashMap<Integer,BackfireParams>();
			Array<XmlReader.Element> _xmlArrayBackfire=shipXml.getChildrenByName("backfire");
			for(XmlReader.Element backfireXml:_xmlArrayBackfire){
				int backfireId=backfireXml.getIntAttribute("id");
				BackfireParams backfireParams=new BackfireParams();
				backfireParams.id=backfireId;
				backfireParams.left=backfireXml.getIntAttribute("left")==1?true:false;
				backfireParams.right=backfireXml.getIntAttribute("right")==1?true:false;
				backfireParams.up=backfireXml.getIntAttribute("up")==1?true:false;
				backfireParams.angle=backfireXml.getIntAttribute("angle");
				backfireParams.type=backfireXml.getIntAttribute("type");
				backfireParams.rel=backfireXml.getIntAttribute("rel");
				backfireParams.rad=backfireXml.getIntAttribute("rad");
				params.backfire.put(backfireId,backfireParams);
			}			
			_map.put(id, params);		
		}
	}
	/**
	 * Zwraca mapê sk³adaj¹c¹ siê na klucz w postaci id statku i obiektu klasy ShipParams
	 * @return
	 */
	public Map<Integer,UnitParams> getShipMap(){
		return _map;
	}
	/**
	 * Zwraca konkretny statek w postaci obiektu klasy ShipParams
	 * @param shipId
	 * @return
	 */
	public UnitParams getShip(int shipId){
		return _map.get(shipId);
	}
}

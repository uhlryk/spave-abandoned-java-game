package pro.artwave.spaceshooter.model.resource;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import pro.artwave.fgm.model.resource.Resource;
import pro.artwave.spaceshooter.helper.BackfireParams;
import pro.artwave.spaceshooter.helper.BonusParams;
import pro.artwave.spaceshooter.helper.DecorationParams;
import pro.artwave.spaceshooter.helper.UnitParams;

public class DecorationResource extends Resource {
	private Array<XmlReader.Element> _xmlMap;
	private Map<Integer,DecorationParams> _map;
	@Override
	public String getResourceName() {
		return "data/decoration.xml";
	}
	public void init(){
		super.init();
		_xmlMap=this.getXmlObject().getChildrenByName("decoration");
		_map=new HashMap<Integer,DecorationParams>();
		for(XmlReader.Element decorationXml:_xmlMap){
			int id=decorationXml.getIntAttribute("id");
			DecorationParams params=new DecorationParams();
			params.id=id;
			params.image=decorationXml.getIntAttribute("image");	
			_map.put(id, params);		
		}
	}
	/**
	 * Zwraca mapê sk³adaj¹c¹ siê na klucz w postaci id dekoracji i obiektu klasy DecorationParams
	 * @return
	 */
	public Map<Integer,DecorationParams> getDecorationMap(){
		return _map;
	}
	/**
	 * Zwraca konkretn¹ dekoracjê w postaci obiektu klasy DecorationParams
	 * @param shipId
	 * @return
	 */
	public DecorationParams getDecoration(int decorationId){
		return _map.get(decorationId);
	}
}

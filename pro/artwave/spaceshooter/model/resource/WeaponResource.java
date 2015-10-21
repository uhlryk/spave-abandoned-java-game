package pro.artwave.spaceshooter.model.resource;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import pro.artwave.fgm.model.resource.Resource;
import pro.artwave.spaceshooter.helper.BulletParams;
import pro.artwave.spaceshooter.helper.WeaponParams;

public class WeaponResource extends Resource {
	private Array<XmlReader.Element> _xmlMap;
	private Map<Integer,WeaponParams> _map;
	@Override
	public String getResourceName() {
		return "data/weapon.xml";
	}
	public void init(){
		super.init();
		_xmlMap=this.getXmlObject().getChildrenByName("weapon");
		_map=new HashMap<Integer,WeaponParams>();
		for(XmlReader.Element weaponXml:_xmlMap){
			int id=weaponXml.getIntAttribute("id");
			WeaponParams params=new WeaponParams();
			params.id=id;
			Array<XmlReader.Element> _xmlArrayStat=weaponXml.getChildrenByName("stat");
			for(XmlReader.Element statXml:_xmlArrayStat){
				String stat=statXml.getAttribute("type");
				if("reload".equals(stat))params.reload=statXml.getIntAttribute("value");
				if("speed".equals(stat))params.speed=statXml.getIntAttribute("value");
				if("damage".equals(stat))params.damage=statXml.getIntAttribute("value");
				if("type".equals(stat))params.type=statXml.getIntAttribute("value");
				if("playability".equals(stat))params.playability=(statXml.getIntAttribute("value")==1?true:false);
				if("points".equals(stat))params.points=statXml.getIntAttribute("value");				
			}
			params.bullets=new HashMap<Integer,BulletParams>();
			Array<XmlReader.Element> _xmlArrayBullet=weaponXml.getChildrenByName("bullet");
			for(XmlReader.Element bulletXml:_xmlArrayBullet){
				int bulletId=bulletXml.getIntAttribute("id");
				BulletParams bulletParams=new BulletParams();
				Array<XmlReader.Element> _xmlArrayBulletStat=bulletXml.getChildrenByName("stat");
				for(XmlReader.Element statXml:_xmlArrayBulletStat){
					String stat=statXml.getAttribute("type");
					int value=statXml.getIntAttribute("value");
					if("angle".equals(stat))bulletParams.angle=value;
					if("rel".equals(stat))bulletParams.rel=value;
					if("rad".equals(stat))bulletParams.rad=value;
					if("speed".equals(stat))bulletParams.speed=value;
					if("damage".equals(stat))bulletParams.damage=value;
					if("shieldDamage".equals(stat))bulletParams.shieldDamage=value;
					if("armorDamage".equals(stat))bulletParams.armorDamage=value;
					if("lifetime".equals(stat))bulletParams.lifetime=value;
					if("penetration".equals(stat))bulletParams.penetration=(value==1?true:false);
				}
				bulletParams.id=bulletId;
				bulletParams.type=bulletXml.getIntAttribute("type");
				params.bullets.put(bulletId,bulletParams);
			}
			_map.put(id, params);
		}
	}
	public Map<Integer,WeaponParams> getWeaponMap(){
		return _map;
	}
	public WeaponParams getWeapon(int weaponId){
		return _map.get(weaponId);
	}
}

package pro.artwave.spaceshooter.model.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import pro.artwave.fgm.model.resource.Resource;

public class RankResource extends Resource {

	private Map<String,String> _paramMap;

	@Override
	public String getResourceName() {
		return "data/rank.xml";
	}
	/**
	 * Jeœli mamy missionId to obliczamy checkpoint dla danej misji
	 * W przeciwnym razie nie mamy takiej informacji
	 * @param missionId
	 */
	public void init(){
		super.init();	
		Array<XmlReader.Element> xmlMap=this.getXmlObject().getChildrenByName("param");
		this._paramMap=new HashMap<String,String>(20);
		for(XmlReader.Element paramXml:xmlMap){
			_paramMap.put(paramXml.getAttribute("key"), paramXml.getAttribute("value"));
		}
	}
	private String getParam(String key){
		String val=_paramMap.get(key);
		return val;
	}
	public int getIntParam(String key){
		String value=this.getParam(key);
		return Integer.parseInt(value);
	}
	public String getRank(int score){
		String rank="-";
		int maxScore=0;
		for(Entry<String,String>entry:_paramMap.entrySet()){
			int pScore=Integer.parseInt(entry.getValue());
			String pRank=entry.getKey();
			if(score>=pScore){
				if(maxScore<pScore){
					maxScore=pScore;
					rank=pRank;
				}
			}
		}
		return rank;
	}
}

package pro.artwave.spaceshooter.helper;

import java.util.Map;

public class UnitParams {
	public int id;
	public int maxSpeed;
	public int rotation;
	public int maxHealth;
	public int maxShield;
	public int regeneration;	
	public int radius;	
	public boolean resistance;
	public boolean playability;//czy gracz mo¿e w ogóle u¿yæ tego statku, jeœli nie to nie pojawia siê w sklepie. domyœlnie tak, nie musi byæ wiêc oznaczony 
	public int points;//od ilu punktów mo¿na wybraæ statek. statek nie kosztue, punkty go odblokowuj¹
	public Map<Integer,BackfireParams> backfire;	
	public UnitParams(){
		playability=false;
		points=100000;
		maxSpeed=0;
		rotation=0;
	}
	@Override
	public String toString(){
		return "UnitParams id:"+id+" maxSpeed:"+maxSpeed+" rotation:"+rotation+" maxHealth:"+maxHealth+" maxShield:"+maxShield+" regeneration:"+regeneration+" radius:"+radius;
	}
}

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
	public boolean playability;//czy gracz mo�e w og�le u�y� tego statku, je�li nie to nie pojawia si� w sklepie. domy�lnie tak, nie musi by� wi�c oznaczony 
	public int points;//od ilu punkt�w mo�na wybra� statek. statek nie kosztue, punkty go odblokowuj�
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

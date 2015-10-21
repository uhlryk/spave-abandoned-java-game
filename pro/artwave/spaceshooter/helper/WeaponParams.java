package pro.artwave.spaceshooter.helper;

import java.util.Map;

public class WeaponParams {
	public static final int TYPE_FIRST_WEAPON=1;
	public static final int TYPE_SECOND_WEAPON=2;
	/**
	 * Map<idbullet,bulletParams>
	 */
	public Map<Integer,BulletParams> bullets;
	public int id;
	public int reload;
	public int speed;
	public int damage;
	public int type;
	public boolean playability;//czy gracz mo¿e w ogóle u¿yæ tej broni, jeœli nie to nie pojawia siê w sklepie. domyœlnie tak, nie musi byæ wiêc oznaczony 
	public int points;//od ilu punktów mo¿na wybraæ broñ. statek nie kosztue, punkty go odblokowuj¹
	public Map<Integer,BulletParams> getWeaponMap(){
		return bullets;
	}
	public BulletParams getBullet(int bulletId){
		return bullets.get(bulletId);
	}	
	public WeaponParams(){
		playability=true;
		points=0;
		type=TYPE_FIRST_WEAPON;
	}
}

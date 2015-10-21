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
	public boolean playability;//czy gracz mo�e w og�le u�y� tej broni, je�li nie to nie pojawia si� w sklepie. domy�lnie tak, nie musi by� wi�c oznaczony 
	public int points;//od ilu punkt�w mo�na wybra� bro�. statek nie kosztue, punkty go odblokowuj�
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

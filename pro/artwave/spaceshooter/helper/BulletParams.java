package pro.artwave.spaceshooter.helper;

public class BulletParams {
	public int id;
	public String controllerName;
	public int type;//image
	public int angle;
	public int rel;
	public int rad;
	public int speed;
	public int damage;
	/**
	 * ¿ywotnoœæ pocisku w milisekundach 
	 */
	public int lifetime;
	/**
	 * jeœli aktywny znaczy ¿e pocisk przechodzi przez obiekt i idzie do nastêpnego
	 */
	public boolean penetration;
	/**
	 * W przypadku trafienie zadaje dodatkowe obra¿enia w tarczê. Najpierw zadawane s¹ te obra¿enia
	 * Potem zwyk³e
	 */
	public int shieldDamage;
	/**
	 * Zadaj¹ dodatkowe obra¿enia w pasek zdrowia usera, nawet jak ma tarczê aktywn¹. Jest to wiêc dobre do ataku
	 * na obiekty o bardzo du¿ej tarczy, bo mo¿e je zniszczyæ nawet gdy siê przez tarczê nie przebijemy
	 */
	public int armorDamage;
}

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
	 * �ywotno�� pocisku w milisekundach 
	 */
	public int lifetime;
	/**
	 * je�li aktywny znaczy �e pocisk przechodzi przez obiekt i idzie do nast�pnego
	 */
	public boolean penetration;
	/**
	 * W przypadku trafienie zadaje dodatkowe obra�enia w tarcz�. Najpierw zadawane s� te obra�enia
	 * Potem zwyk�e
	 */
	public int shieldDamage;
	/**
	 * Zadaj� dodatkowe obra�enia w pasek zdrowia usera, nawet jak ma tarcz� aktywn�. Jest to wi�c dobre do ataku
	 * na obiekty o bardzo du�ej tarczy, bo mo�e je zniszczy� nawet gdy si� przez tarcz� nie przebijemy
	 */
	public int armorDamage;
}

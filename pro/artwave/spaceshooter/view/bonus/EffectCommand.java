package pro.artwave.spaceshooter.view.bonus;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa s�u��ca do obs�ugi patternu command
* ob�uguje efekty wzi�cia r�nych bonus�w. Efekty te s� opisane w bonus.xml
 * @author Krzysztof
 *
 */
public class EffectCommand {
	private BonusPool _bonusPool;
	private Map<String,Command> _commandMap;
	public void init(BonusPool bonusPool){
		_bonusPool=bonusPool;
		_commandMap=new HashMap<String,Command>(10);
		_commandMap.put("restoreHealth",new RestoreHealthCommand());
		_commandMap.put("restoreShield",new RestoreShieldCommand());
		_commandMap.put("addPoints",new AddPointsCommand());
	}
	public boolean command(String commandName,int val1,int val2,int val3){
		
		Command command=_commandMap.get(commandName);
		if(command!=null){
			return command.command(val1, val2, val3);
		}
		return true;
	}
	/**
	 * Wywo�ujemy poprzez ten interfejs komendy akcji
	 * je�li true to po danej komendzie od razu idzie nast�pna
	 * je�li false znaczy �e proces musi si� zatrzyma� w oczekiwaniu na akcj� usera lub timer
	 * jako �e nie jest to asynchronicznie to trzeba to obs�u�y� w taskManager
	 * @author Krzysztof
	 *
	 */
	public interface Command{
		public boolean command(int val1,int val2,int val3);
	}
	/**
	 * Klasa uzupe�nia zdrowie gracza o warto�� val1
	 * Komenda restoreHealth
	 * @author Krzysztof
	 *
	 */
	public class RestoreHealthCommand implements Command{
		public boolean command(int val1, int val2, int val3) {
			_bonusPool.getUserShip().setHealth(val1);
			return false;
		}
	}
	/**
	 * Klasa uzupe�nia tarcz� gracza o warto�� val1
	 * Komenda restoreShield
	 * @author Krzysztof
	 *
	 */
	public class RestoreShieldCommand implements Command{
		public boolean command(int val1, int val2, int val3) {
			_bonusPool.getUserShip().setShield(val1);
			return false;
		}
	}	
	/**
	 * Klasa dodaje punkty userowi o warto��  val1 
	 * Komenda addPoints
	 * @author Krzysztof
	 *
	 */
	public class AddPointsCommand implements Command{
		public boolean command(int val1, int val2, int val3) {
			_bonusPool.getCountScore().addScore(val1);
			return false;
		}
	}	
}

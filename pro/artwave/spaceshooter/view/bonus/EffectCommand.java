package pro.artwave.spaceshooter.view.bonus;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa s³u¿¹ca do obs³ugi patternu command
* ob³uguje efekty wziêcia ró¿nych bonusów. Efekty te s¹ opisane w bonus.xml
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
	 * Wywo³ujemy poprzez ten interfejs komendy akcji
	 * jeœli true to po danej komendzie od razu idzie nastêpna
	 * jeœli false znaczy ¿e proces musi siê zatrzymaæ w oczekiwaniu na akcjê usera lub timer
	 * jako ¿e nie jest to asynchronicznie to trzeba to obs³u¿yæ w taskManager
	 * @author Krzysztof
	 *
	 */
	public interface Command{
		public boolean command(int val1,int val2,int val3);
	}
	/**
	 * Klasa uzupe³nia zdrowie gracza o wartoœæ val1
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
	 * Klasa uzupe³nia tarczê gracza o wartoœæ val1
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
	 * Klasa dodaje punkty userowi o wartoœæ  val1 
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

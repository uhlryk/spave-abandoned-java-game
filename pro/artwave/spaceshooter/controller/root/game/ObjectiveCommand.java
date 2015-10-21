package pro.artwave.spaceshooter.controller.root.game;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa s³u¿¹ca do obs³ugi patternu command
 * Ma zbiór poleceñ które s¹ wywo³ywane w TaskManager. U¿ywa bez interfejsu obiektu TaskManager
 * @author Krzysztof
 *
 */
public class ObjectiveCommand {
	private TaskManager _taskManager;
	private Map<String,Command> _commandMap;
	public void init(TaskManager taskManager){
		_taskManager=taskManager;
		_commandMap=new HashMap<String,Command>(10);
		_commandMap.put("gotoCheckpoint",new GotoCheckpointCommand());
		_commandMap.put("collect",new CollectBonusCommand());
		_commandMap.put("destroy",new DestroyEnemyCommand());
	//	_commandMap.put("collect",new CollectBonusCommand());
	//	_commandMap.put("collect",new CollectBonusCommand());
	}
	public int check(String commandName,int val1,int val2,int val3){
		Command command=_commandMap.get(commandName);
		if(command!=null){
			return command.check(val1, val2, val3);
		}
		return 0;
	}
	public interface Command{
		/**
		 * Zadanie polega na doleceniu do okreœlonego checkpointa
		 * @param val1 hexadecimal id checkpointa
		 * @param val2
		 * @param val3
		 * @return 1 oznacza zrealizowane zadanie; - 1 oznacza zadanie przegrane; 0 w trakcie realizacji
		 */
		public int check(int val1,int val2,int val3);
	}
	public class GotoCheckpointCommand implements Command{
		public int check(int val1,int val2,int val3){
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){//nie ma takiego checkpointa ale mo¿e siê pojawiæ wiêc czekamy
				return 0;
			}
			return _taskManager.getCheckpointPool().isHit(val1)?1:0;
		}
	}
	/**
	 * Zadanie polega na zebraniu okreœlonego zasobu, jeœli zasobu nie ma na mapie, traktowane to jest
	 * jakby gracz go zebra³
	 * @author Krzysztof
	 *
	 */
	public class CollectBonusCommand implements Command{
		public int check(int val1,int val2,int val3){
			Boolean status=_taskManager.getBonusPool().getStatus(val1,0);//zwraca status bonusu
			if(status==null){
				return 1;
			}else if(status==true){
				return 0;
			}else{//status==false  oznacza ¿e bonus nie jest ju¿ aktywny, mamy trochê podwójne zaprzeczenie bo ko¿ystny jest false
				return 1;
			}
			
		}
	}
	/**
	 * Zadanie polega na zniszczenu okreœlonego w
	 * @author Krzysztof
	 *
	 */
	public class DestroyEnemyCommand implements Command{
		public int check(int val1,int val2,int val3){
			Boolean isDestroy=_taskManager.getEnemyPool().isDestroyed(val1,0);//zwraca status bonusu
			if(isDestroy==null){
				return 1;
			}else if(isDestroy==true){//wróg zosta³ zniszczony
				return 1;
			}else{//wróg jest na planszy
				return 0;
			}
			
		}
	}
}

package pro.artwave.spaceshooter.controller.root.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.utils.Timer;

import pro.artwave.fgm.view.helper.Button.ClickListener;
import pro.artwave.spaceshooter.controller.root.GameController;
import pro.artwave.spaceshooter.manager.CheckpointPool;
import pro.artwave.spaceshooter.manager.DecorationPool;
import pro.artwave.spaceshooter.manager.EnemyPool;
import pro.artwave.spaceshooter.model.resource.MissionResource;
import pro.artwave.spaceshooter.view.bonus.BonusPool;
import pro.artwave.spaceshooter.view.root.game.GameStoryPopup;
import pro.artwave.spaceshooter.view.root.game.GameMap;
import pro.artwave.spaceshooter.view.root.game.MessageBar;
import pro.artwave.spaceshooter.view.tile.TileBoardIsometric;

/**
 * Zarz¹dza zadaniami jakie ma user wykonaæ by zaliczyæ misjê
 * Otrzymuje powiadomienia o zniszczonych wrogach, pobranych zasobach i odpalonych checkboxach
 * Mo¿e odpalaæ wrogów, zasoby i checkboxy - które nie s¹ wywo³ane domyœlnie
 * Czyli odpalamy checkboxy które jeœli zostan¹ u¿yte powiadamiaj¹ task.
 * Niektóre powiadomienia nie maj¹ znaczenia dla taskManagera
 * Mo¿e siê zdarzyæ ¿e zrealizujemy coœ co dopiero potem bêdzie zadaniem wiêc od razu zostanie wykonane
 * EnemyPool ma rejestr pokonanych wrogów, resource te¿. Taskmanager nie musi wiêc przechowywaæ tych danych sam
 * Ale co jakiœ czas kalkulowaæ co jest wykonane, np raz na 2sekundy
 * @author Krzysztof
 *
 */
public class TaskManager {
	private GameStoryPopup _briefPopup;
	private MessageBar _messageBar;
	private CheckpointPool _checkpointPool;
	private EnemyPool _enemyPool;
	private BonusPool _bonusPool;
	private GameMap _gameMap;
	private DecorationPool _decorationPool;
	private TileBoardIsometric _tileBoard;
	private ArrayList<MissionResource.Task> _gameTaskList;
//	private ArrayList<Integer> _activeTaskIdList;
	/**
	 * Mapa aktywnych zadañ, mo¿e byæ na raz wiêcej aktywowanych
	 * Map<idTaska,Task>
	 */
	private Map<Integer,ActiveTask> _activeTaskMap;
	private ObjectiveCommand _objectiveCommand;
	private ActionCommand _actionCommand;
	private DialogManager _dialogManager;
	private SelectButtonListener _centerButtonListener;
	private SelectButtonListener _leftButtonListener;
	private SelectButtonListener _rightButtonListener;
	private GameController _controller;
	
	private LinkedList<Integer> _preparedTaskList;
	/**
	 * D³u¿szy s³u¿y do obliczania czasu jaki pozosta³ graczowi na wykonanie zadania
	 */
	private float _timerObjective;
	/**
	 * Wywo³ywany w sytuacjach przerw kilkusekundowych miêdzy akcjami
	 */
	private Timer _timerAction;

	//private ArrayList<Objective> _objectiveList;
	public void init(ArrayList<MissionResource.Task> gameTaskList,GameController controller){
	//	System.out.println(gameTaskList);
		_activeTaskMap=new HashMap<Integer,ActiveTask>();
		_gameTaskList=gameTaskList;
		_actionCommand=new ActionCommand();
		_actionCommand.init(this);
		_objectiveCommand=new ObjectiveCommand();
		_objectiveCommand.init(this);
		_controller=controller;
		_timerAction=new Timer();
		_preparedTaskList=new LinkedList<Integer>();
	}
	public GameController getController(){
		return _controller;
	}

	public void setStartTask(){
		startTask(0x01);
	}
	/**
	 * Metoda odpala nowe zadanie
	 * @param taskId
	 */
	public void startTask(int taskId){
		System.out.println("startTask "+taskId);
	//	_activeTaskIdList.add(taskId);
		//_activeTaskLis=null;
		for(MissionResource.Task task:_gameTaskList){
			if(task.id==taskId){
				_activeTaskMap.put(taskId,new ActiveTask(task));
			}
		}
		if(_activeTaskMap.containsKey(taskId)==false){//sprawdzamy czy zosta³ znaleziony task
			System.out.println("Application tries start task id "+taskId+" there is no such task");
			return;
		//	throw new RuntimeException("Application tries start task id "+_activeTaskId+" there is no such task");
		}
	//	System.out.println(_activeTask);
		doStartActionList(taskId);
		setObjectives();
	}
	/**
	 * ustawia kolejne zadania w kolejce do wykonania, tak by jeœli jest wiele ró¿nych zadañ wywo³ywanych
	 * w róznych zadaniach to by by³y wywo³ywane po kolei po zakoñczeniu poprzedniego zadania
	 * @param taskId
	 */
	public void prepareTask(int taskId){
		_preparedTaskList.add(taskId);
	}
	/**
	 * Oblicza kolejne zadanie w kolejce do wywo³ania i je wywo³uje.
	 * Zwykle wywo³ywane na koniec aktualnego zadania.
	 * jeœli zadanie zostanie zawieszone to nie dojdzie do wywo³ania tej metody.
	 * W koñcu zadanie jest resetowane, i w koñcu siê wywo³a, wtedy nast¹pi sprawdzenie
	 * czy mamy jeszcze jakieœ zadania
	 */
	public void calculateNextTask(){
		if(this._preparedTaskList.isEmpty()==false){
			this.startTask(this._preparedTaskList.removeFirst());
		}
	}
	/**
	 * obs³ugujemy akcje z onStart
	 */
	private boolean doStartActionList(int taskId){
		return doActionList(ACTION_TYPE_START,taskId);
	}
	/**
	 * obs³ugujmy akcje z onFailure
	 * Wywo³ywane przez calculate gdy objective siê nie uda
	 */
	private boolean doFailureActionList(int taskId){
		return doActionList(ACTION_TYPE_FAILURE,taskId);
	}	
	/**
	 * obs³ugujemy akcje z onSuccess
	 * Wywo³ujemy gdy wszystkie objectivy z danej akcji zostan¹ zrealizowane
	 */
	private boolean doSuccessActionList(int taskId){
		return doActionList(ACTION_TYPE_SUCCESS,taskId);
	}	
	private int _taskToReset;
	/**
	 * Ustawia task jaki ma byæ gotowy do restartu akcji
	 * @param taskId
	 */
	public void setTaskToReset(int taskId){
		_taskToReset=taskId;
	}
	/**
	 * Zwraca task jaki by³ przerwany, mo¿emy to u¿yæ przede wszystkim w przyciskach
	 * @return
	 */
	public int getTaskToReset(){
		return _taskToReset;
	}
	/**
	 * zwraca true jeœli jakaœ akcja jest zawieszona, false w przeciwnym razie
	 * @return
	 */
	public boolean isTaskToReset(){
		return _taskToReset>0?true:false;
	}
	//private boolean stopFlag;
	//public void setStopAction
	/**
	 * Jeœli dana _actionCommand.command zwróci w metodzie setAction false, wtedy przerywany jest dalsza iteracja
	 * po akcjach do momentu wywo³ania restartAction, która kontynuuje iteracjê od miejsca przerwania
	 */
	public void restartTask(int taskId){
		System.out.println("restartTask taskId "+taskId);
		ActiveTask activeTask=_activeTaskMap.get(taskId);
		if(activeTask==null){
			throw new RuntimeException("Application tries to restart  taskId "+taskId+" there is no such task");
		}
		int lastType=activeTask.stopActionType;
		if(activeTask.stopActionType>-1){
			boolean result=doActionList(ACTION_TYPE_RESET,taskId);
			if(result==true){//koniec akcji w zadaniu, 
				if(lastType==ACTION_TYPE_SUCCESS||lastType==ACTION_TYPE_FAILURE){//a akcja by³a typu success lub failuere
					//mo¿emy wiêc zamkn¹æ ca³e zadanie
					System.out.println("Remove task id "+taskId);
					_activeTaskMap.remove(taskId);
					
				}
				
			}
		}
		System.out.println("restartTask koniec task "+taskId);
	}
	public static final int ACTION_TYPE_RESET=-1;
	public static final int ACTION_TYPE_START=0;
	public static final int ACTION_TYPE_FAILURE=1;
	public static final int ACTION_TYPE_SUCCESS=2;
	
	/**
	 * if true akcja siê skoñczy³a
	 * w przypadku false zosta³a przerwana
	 * @param type
	 * @param taskId
	 * @return
	 */
	private boolean doActionList(int type,int taskId){
		System.out.println("doActionList start type "+type+" taskid"+taskId);
		getTimerAction().clear();
		ActiveTask activeTask=_activeTaskMap.get(taskId);
		if(activeTask==null){
			System.out.println("Application tries to doActionList type:"+type+"  taskId "+taskId+" there is no such task");
		}
		int num=0;
		ArrayList<MissionResource.Task.Element> actionList;
		if(type==ACTION_TYPE_RESET&&activeTask.stopActionType>-1){
			type=activeTask.stopActionType;
			System.out.println("doActionList reset na type "+type+" taskid"+taskId);
		}
		switch(type){
		case ACTION_TYPE_START:
			actionList=activeTask.task.onStartList;
			break;
		case ACTION_TYPE_FAILURE:
			actionList=activeTask.task.onFailureList;
			break;
		case ACTION_TYPE_SUCCESS:
			actionList=activeTask.task.onSuccessList;
			break;
		default:
			throw new RuntimeException("TaskManager.doActionList no type: "+type);
		}
	//	System.out.println("setAction task type"+type+" id"+Integer.toHexString(taskId));
		for(MissionResource.Task.Element action:actionList){	
			if(num>activeTask.stopActionNum){
				System.out.println("   doActionList taskid:"+taskId+" type "+action.type);
				boolean result=_actionCommand.command(action.type, action.val1,action.val2,action.val3,taskId);	
			//	System.out.println("setAction finish action "+result+" taskid:"+taskId+" type "+action.type);
				if(result==false){
					activeTask.stopActionType=type;
					activeTask.stopActionNum=num;
					this.setTaskToReset(taskId);
					return false;
				}
			}
			num++;
			
		}
		
		activeTask.clearStopFlag();
		this.setTaskToReset(0);
		this.calculateNextTask();
		return true;
	}
	/**
	 * w GameController sprawdzamy ustawione zasoby jaki maj¹ poziom realizacji
	 * Czy któryœ dotar³ do celu
	 */
	public void calculate(float delta){
		if(isTaskToReset()){//jeœli mamy jak¹œ akcjê zawieszon¹, np poprzez popup to nie testujemy innych objectiwów
			return;
		}
		for(Entry<Integer,ActiveTask>entry:_activeTaskMap.entrySet()){
			ActiveTask activeTask=entry.getValue();
			if(activeTask.lockFinish==true)continue;
			int taskId=entry.getKey();
		//if(_activeTask==null)return;
			int resultNum=activeTask.task.objectiveList.size();
			int resultSum=0;
			for(MissionResource.Task.Element objective:activeTask.task.objectiveList){
				int result=_objectiveCommand.check(objective.type, objective.val1,objective.val2,objective.val3);
				if(result==1)resultSum++;
				else if(result==-1){
					activeTask.lockFinish=true;
					boolean resultAction=doFailureActionList(taskId);
					if(resultAction==true){
						System.out.println("Remove task id "+taskId);
						_activeTaskMap.remove(taskId);
						
						break;
					}
				}
			}
			if(resultSum==resultNum){//wszystkie objectives zrealizowane
				System.out.println("calculate setSuccessAction task "+taskId);
				activeTask.lockFinish=true;
				boolean resultAction=doSuccessActionList(taskId);
				if(resultAction==true){
					System.out.println("Remove task id "+taskId);
					_activeTaskMap.remove(taskId);
					
					break;
				}
			}
		}
	}
	private boolean _pause;
	public void setPause(boolean pause){
		_pause=pause;
	}
	public boolean isPause(){
		return _pause;
	}
	public void setDelay(int seconds){
		
	}
	/**
	 * Nas³uch na przycisk lewy lub prawy dla przycisków z GameBriefPopup
	 * @author Krzysztof
	 *
	 */
	public class SelectButtonListener extends ClickListener{
		/**
		 * Task do jakiego mo¿emy polecieæ, 
		 */
		private int _taskStart;
		public void setTaskStart(int task){
			_taskStart=task;
		}
		@Override
		public void onClick() {
			TaskManager.this.getBriefPopup().hideMessage();
			if(_taskStart>0){
				_actionCommand.command("startTask",_taskStart,0,0,_taskStart);
			}
			getController().setPause(false);
			restartTask(getTaskToReset());
		}
	}	
	public class ActiveTask{
		/**
		 * Jeœli dla danego zadania odpaliliœmy ju¿ success czy failure to drugi raz nie powinien siê odpalaæ
		 * a poniewa¿ warunek ca³y czas by³by prawdziwy wiêc by siê odpala³o
		 * Nale¿y wiec wtedy daæ lockFinish=true
		 */
		public boolean lockFinish;
		/**
		 * Jeœli true to zadanie zamkniête. Powinniœmy tak zamykaæ zadania zamiast przez remove
		 */
		public boolean closed;
		public MissionResource.Task task;
		/**
		 * Typ akcji na której proces zosta³ przerwany
		 */
		public int stopActionType;
		/**
		 * Numer akcji na której proces zosta³ zatrzymany
		 */
		public int stopActionNum;
		public ActiveTask(MissionResource.Task task){
			this.task=task;
			clearStopFlag();
		}
		public void clearStopFlag(){
			stopActionType=-1;
			stopActionNum=-1;
		}
	}
	/**
	 * Metoda wywo³ywana przy nowym zadaniu w metodzie startTask
	 * Ustawia zadania jakie musz¹ byæ wykonane
	 */
	private void setObjectives(){
		
	}
	public void setDialogManager(DialogManager dialogManager){
		_dialogManager=dialogManager;
	}	
	public DialogManager getDialogManager(){
		return _dialogManager;
	}
	public void setBriefPopup(GameStoryPopup briefPopup){
		_briefPopup=briefPopup;
		_centerButtonListener=new SelectButtonListener();
		_leftButtonListener=new SelectButtonListener();
		_rightButtonListener=new SelectButtonListener();	
		_briefPopup.getButton(GameStoryPopup.BUTTON_ID_CENTER).addClickListener(_centerButtonListener);
		_briefPopup.getButton(GameStoryPopup.BUTTON_ID_LEFT).addClickListener(_leftButtonListener);
		_briefPopup.getButton(GameStoryPopup.BUTTON_ID_RIGHT).addClickListener(_rightButtonListener);
	}
	public GameStoryPopup getBriefPopup(){
		return _briefPopup;
	}	
	public SelectButtonListener getCenterButtonListener(){
		return _centerButtonListener;
	}
	public SelectButtonListener getLeftButtonListener(){
		return _leftButtonListener;
	}
	public SelectButtonListener getRightButtonListener(){
		return _rightButtonListener;
	}	
	public void setMessageBar(MessageBar messageBar){
		_messageBar=messageBar;
	}
	public MessageBar getMessageBar(){
		return _messageBar;
	}
	public void setGameMap(GameMap gameMap){
		_gameMap=gameMap;
	}
	public GameMap getGameMap(){
		return _gameMap;
	}	
	public void setCheckpointPool(CheckpointPool checkpointPool){
		_checkpointPool=checkpointPool;
	}
	public CheckpointPool getCheckpointPool(){
		return _checkpointPool;
	}
	public void setTileBoard(TileBoardIsometric tileBoard){
		_tileBoard=tileBoard;
	}
	public TileBoardIsometric getTileBoard(){
		return _tileBoard;
	}	
	public void setEnemyPool(EnemyPool enemyPool){
		_enemyPool=enemyPool;
	}	
	public EnemyPool getEnemyPool(){
		return _enemyPool;
	}		
	public void setBonusPool(BonusPool bonusPool){
		_bonusPool=bonusPool;
	}	
	public BonusPool getBonusPool(){
		return _bonusPool;
	}		
	public void setDecorationPool(DecorationPool decorationPool){
		_decorationPool=decorationPool;
	}	
	public DecorationPool getDecorationPool(){
		return _decorationPool;
	}		
	public Timer getTimerAction(){
		return _timerAction;
	}
	
}

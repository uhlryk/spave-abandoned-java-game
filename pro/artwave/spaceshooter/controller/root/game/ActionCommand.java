package pro.artwave.spaceshooter.controller.root.game;

import java.util.HashMap;
import java.util.Map;

import pro.artwave.spaceshooter.view.root.game.GameEndPopup;
import pro.artwave.spaceshooter.view.root.game.MessageBar;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * Klasa s³u¿¹ca do obs³ugi patternu command
 * Ma zbiór poleceñ które s¹ wywo³ywane w TaskManager. U¿ywa bez interfejsu obiektu TaskManager
 * @author Krzysztof
 *
 */
public class ActionCommand {
	private TaskManager _taskManager;
	private Map<String,Command> _commandMap;
	public void init(TaskManager taskManager){
		_taskManager=taskManager;
		_commandMap=new HashMap<String,Command>(10);
		_commandMap.put("activeCheckpoint",new ActiveCheckpointCommand());
		_commandMap.put("showMapCheckpoint",new ShowOnMapCheckpointCommand());
		_commandMap.put("hideMapCheckpoint",new HideOnMapCheckpointCommand());
		
		_commandMap.put("showBoardCheckpoint",new ShowOnBoardCheckpointCommand());
		_commandMap.put("hideBoardCheckpoint",new HideOnBoardCheckpointCommand());		
		
		_commandMap.put("radiusCheckpoint",new SetRadiusCheckpointCommand());
		_commandMap.put("createCheckpoint",new CreateCheckpointCommand());
		/**
		 * @deprecated
		 */
		_commandMap.put("checkpointDecoration",new CheckpointDecorationCommand());
		_commandMap.put("deactiveCheckpoint",new DeactiveCheckpointCommand());
		_commandMap.put("startTask",new StartTaskCommand());
		_commandMap.put("showDecoration",new ShowDecorationCommand());
		_commandMap.put("hideDecoration",new HideDecorationCommand());
		_commandMap.put("rotateDecoration",new RotateDecorationCommand());
		_commandMap.put("messageDialog",new ShowMessageDialogCommand());
		_commandMap.put("selectDialog",new ShowSelectDialogCommand());
		_commandMap.put("delay",new DelayCommand());
		_commandMap.put("hideBar",new HideBarCommand());
		_commandMap.put("showBarObjective",new ShowBarObjectiveCommand());
		_commandMap.put("showBarSuccess",new ShowBarSuccessCommand());
		
		_commandMap.put("addPoints",new AddPointsCommand());
		_commandMap.put("finishSuccess",new FinishGameCommand());
		_commandMap.put("finishFailure",new FailureGameCommand());
		_commandMap.put("teleport",new TeleportUserCommand());
		_commandMap.put("pause",new SetPauseCommand());
		_commandMap.put("manipulateTile",new GateControlCommand());
		_commandMap.put("showMapBonus",new ShowOnMapBonusCommand());
		_commandMap.put("setBonusMarked",new SetBonusMarkedCommand());
		_commandMap.put("setBonusStatus",new SetBonusStatusCommand());
		_commandMap.put("createBonus",new CreateBonusCommand());
		_commandMap.put("setEnemyStatus",new SetEnemyStatusCommand());
		_commandMap.put("setEnemyFire",new SetEnemyFireCommand());
		_commandMap.put("createEnemy",new CreateEnemyCommand());
		_commandMap.put("changeDecoration",new ChangeDecorationCommand());
		_commandMap.put("markEnemy",new MarkEnemyCommand());
//		_commandMap.put("showMapBonus",new ShowOnMapBonusCommand());	
//		_commandMap.put("showMapBonus",new ShowOnMapBonusCommand());
//		_commandMap.put("showMapBonus",new ShowOnMapBonusCommand());
//		_commandMap.put("showMapBonus",new ShowOnMapBonusCommand());		
	}
	public boolean command(String commandName,int val1,int val2,int val3,int taskId){
		
		Command command=_commandMap.get(commandName);
		if(command!=null){
			return command.command(val1, val2, val3,taskId);
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
		public boolean command(int val1,int val2,int val3,int taskId);
	}
	/**
	 * S³u¿y do dodania krótkiej przerwy miêdzy metodami, np po wejœciu na checkpoint chcemy wyœwietliæ w messageBar
	 * ¿e uda³o siê osi¹gn¹æ zadanie, nastêpnie po np 5 sec wywo³aæ messagePopup z info o nowym zadaniu
	 * komenda delay gdzie var1 to czas w milisekundach
	 * @author Krzysztof
	 *
	 */
	public class DelayCommand implements Command{
		public boolean command(int val1, int val2, int val3,int taskId) {
			final int id=taskId;
			_taskManager.getTimerAction().scheduleTask(new Task() {				
				@Override
				public void run() {
					_taskManager.getTimerAction().clear();
					_taskManager.restartTask(id);
				}
			}, val1);
			return false;
		}
		
	}
	/**
	 * Oznacza na mapie i na board markerem okreœlony statek wroga. Oba kana³y na raz
	 * val1 s¹ to statki o okreslonym kolorze, val2 jeœli 1 to oznaczony, w przeciwnym razie nie
	 * komenda markEnemy
	 * @author Krzysztof
	 *
	 */
	public class MarkEnemyCommand implements Command{
		@Override
		public boolean command(int val1,int val2,int val3,int taskId){
			System.out.println("MarkEnemyCommand val1"+Integer.toHexString(val1));
			_taskManager.getEnemyPool().setMarked(val1,0,val2==1?true:false);
			return true;
		}
	}
	public class ActiveCheckpointCommand implements Command{
		@Override
		public boolean command(int val1,int val2,int val3,int taskId){
			
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){//nie ma takiego checkpointa trzeba go najpierw utworzyæ
				_taskManager.getCheckpointPool().createCheckpoint(val1);
			}
			_taskManager.getCheckpointPool().setActive(val1);
			if(val2==1){
				_taskManager.getCheckpointPool().setBoardVisible(val1,true);
			}else{
				_taskManager.getCheckpointPool().setBoardVisible(val1,false);
			}
			return true;
		}
	}
	/**
	 * Metoda w³¹cza wyœwietlanie checkpointa o id val1 na mapie
	 * 
	 * Komenda wywo³uj¹ca showMapCheckpoint 
	 * @author Krzysztof
	 *
	 */
	public class ShowOnMapCheckpointCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){
				_taskManager.getCheckpointPool().createCheckpoint(val1);
			}
			Vector2 position=_taskManager.getCheckpointPool().getPosition(val1);
			_taskManager.getGameMap().calculateMarkerPosition(val1,position.x,position.y);
			return true;
		}
		
	}
	/**
	 * Metoda w³¹cza wyœwietlanie bonusu o id val1 na mapie
	 * 
	 * Komenda wywo³uj¹ca showMapBonus 
	 * @author Krzysztof
	 *
	 */
	public class ShowOnMapBonusCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			Vector2 position=_taskManager.getBonusPool().getBonusPosition(val1);
			_taskManager.getGameMap().calculateMarkerPosition(val1,position.x,position.y);
			return true;
		}
		
	}	
	/**
	 * Metoda w³¹cza wyœwietlanie checkpointa na ekranie gry (pulsuj¹ce 3 trójk¹ty) o id val1
	 * Domyslnie checkpointy bêd¹ widoczne na ekranie
	 * Komenda wywo³uj¹ca showBoardCheckpoint 
	 * @author Krzysztof
	 *
	 */
	public class ShowOnBoardCheckpointCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){
				_taskManager.getCheckpointPool().createCheckpoint(val1);
			}			
			_taskManager.getCheckpointPool().setBoardVisible(val1,true);
			return true;
		}
	}
	/**
	 * Metoda w³¹cza/wy³¹cza wyœwietlanie oznaczenia bonusu (trzy trójk¹ty, g³ównie przy checkpointach) o id val1 na planszy.
	 * Jeœli bonus zebrany to nie ma oznaczenia
	 * 
	 * Komenda wywo³uj¹ca setBonusMarked 
	 * @author Krzysztof
	 *
	 */
	public class SetBonusMarkedCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getBonusPool().setMarked(val1,0,val2==1?true:false);
			return true;
		}
		
	}		
	/**
	 * Ustawia status bonusów. Mo¿emy jakieœ specjalne bonusy na pocz¹tku wy³¹czyæ i w dalszej fazie rozgrywki dopiero
	 * w³¹czyæ. Jako id podajemy kolor w val1, a status w³¹czony to val2 o id 1, w przeciwnym razie wy³¹cozny
	 * 
	 * Komenda setBonusStatus
	 * 
	 * @author Krzysztof
	 *
	 */
	public class SetBonusStatusCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getBonusPool().setStatus(val1,0,val2==1?true:false);
			return true;
		}
	}		
	/**
	 * Pozwala wy³¹czyæ lub w³¹czyæ obiekty przeciwnika o okreœlonym id.
	 * Jako id podajemy kolor w val1, a status w³¹czony to val2 o id 1, w przeciwnym razie wy³¹cozny
	 * Komenda setEnemyStatus
	 * Komenda ma pewn¹ niekonsekwentnoœæ, bo dzia³a na wszystkie elementy o danym id.
	 * @author Krzysztof
	 *
	 */
	public class SetEnemyStatusCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getEnemyPool().setStatus(val1,0,val2==1?true:false);
			return true;
		}
	}	
	/**
	 * Pozwala w³¹czaæ lub wy³¹czaæ opcjê strzelania okreœlonej grupie przeciwników
	 * Jako id podajemy kolor w val1, a status w³¹czony to val2 o id 1, w przeciwnym razie wy³¹cozny
	 * Komenda setEnemyFire
	 * Komenda ma pewn¹ niekonsekwentnoœæ, bo dzia³a na wszystkie elementy o danym id.
	 * @author Krzysztof
	 *
	 */
	public class SetEnemyFireCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getEnemyPool().setFire(val1,0,val2==1?true:false);
			return true;
		}
	}	
	/**
	 * pozwala stworzyæ nowy statek przeciwnika na podstawie zdefiniowanego koloru (id)
	 * o id= val1 w pozycji x=val2 i y=val3
	 * komenda createEnemy
	 * @author Krzysztof
	 *
	 */
	public class CreateEnemyCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getEnemyPool().create(val1,val2,val3);
			return true;
		}
	}	
	/**
	 * Tworzy nowy bonus o id= val1 w pozycji x=val2 i y=val3
	 * 
	 * Komenda createBonus
	 * @author Krzysztof
	 *
	 */
	public class CreateBonusCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getBonusPool().create(val1, val2, val3);
			return true;
		}
	}		
	/**
	 * Metoda wy³¹cza wyœwietlanie checkpointa na ekranie gry (pulsuj¹ce 3 trójk¹ty) o id val1
	 * Domyslnie checkpointy bêd¹ widoczne na ekranie
	 * Komenda wywo³uj¹ca hideBoardCheckpoint 
	 * @author Krzysztof
	 *
	 */	
	public class HideOnBoardCheckpointCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){
				_taskManager.getCheckpointPool().createCheckpoint(val1);
			}		
			_taskManager.getCheckpointPool().setBoardVisible(val1,false);
			return true;
		}
	}	
	/**
	 * Metoda wy³¹cza wyœwietlanie checkpointa o id val1 na mapie
	 * 
	 * Komenda wywo³uj¹ca hideMapCheckpoint 
	 * @author Krzysztof
	 *
	 */
	public class HideOnMapCheckpointCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){
				_taskManager.getCheckpointPool().createCheckpoint(val1);
			}
			_taskManager.getGameMap().calculateCheckpointDestroy(val1);
			return true;
		}
		
	}	
	/**
	 * Metoda tworzy nowy checkpoint o id val1 i tile pos val2 i val3(y oblcizane od do³u)
	 * Jeœli istnieje checkpoint o danym id to zmienia jego lokalizacjê
	 * Komenda wywo³uj¹ca createCheckpoint
	 * @author Krzysztof
	 *
	 */
	public class CreateCheckpointCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			_taskManager.getCheckpointPool().createCheckpoint(val1,val2,val3);
			return true;
		}
	}	
	/**
	 * Tworzymy checkpointa pod dekoratorem. id dekoratora to val2 a val1 to id nowego checkpointa
	 * Komenda wywo³uj¹ce checkpointDecorator
	 * @author Krzysztof
	 * @deprecated poniewa¿ znaj¹c id dekoratora mo¿emy go utworzyæ bezpoœrednio u¿ywaj¹c po prostu komendy activeCheckpoint
	 */
	public class CheckpointDecorationCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			Vector2 tile=_taskManager.getDecorationPool().getTilePosition(val2,0);
			_taskManager.getCheckpointPool().createCheckpoint(val1,(int)tile.x,(int)tile.y);
			return true;
		}
	}		
	public class DeactiveCheckpointCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){
				_taskManager.getCheckpointPool().createCheckpoint(val1);
			}			
			_taskManager.getCheckpointPool().setDeactive(val1);
			return true;
		}
	}	
	/**
	 * Modyfikujemy promieñ istniej¹cego checkpointa o id val1 i promieniu val2
	 * Komenda wywo³uj¹sa radiusCheckpoint
	 * @author Krzysztof
	 *
	 */
	public class SetRadiusCheckpointCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){
				_taskManager.getCheckpointPool().createCheckpoint(val1);
			}			
			_taskManager.getCheckpointPool().setRadius(val1,val2);
			return true;
		}
	}		
	public class StartTaskCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			_taskManager.prepareTask(val1);
			return true;
		}
	}	
	/**
	 * POzwala zamieniæ grafikê jednego dekoratora na inn¹
	 * jako val1 podajemy id-kolor, a jako val2 image
	 * komenda changeDecoration
	 * @author Krzysztof
	 *
	 */
	public class ChangeDecorationCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			_taskManager.getDecorationPool().changeImage(val1,val2);
			return true;
		}
	}		
	/**
	 * Wyœwietla dekorator o id przekazanym przez val1 komenda wywo³uj¹ca showDecoration
	 * @author Krzysztof
	 *
	 */
	public class ShowDecorationCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			_taskManager.getDecorationPool().setVisible(val1);
			return true;
		}
	}	
	/**
	 * Ukrywa dekorator o id przekazanym przez val1 komenda wywo³uj¹ca hideDecoration
	 * @author Krzysztof
	 *
	 */
	public class HideDecorationCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			_taskManager.getDecorationPool().setInvisible(val1);
			return true;
		}
	}	
	/**
	 * Obraca dany dekorator o id równym val1 i obrocie val2
	 * komenda wywo³ujaca rotateDecoration
	 * @author Krzysztof
	 *
	 */
	public class RotateDecorationCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			_taskManager.getDecorationPool().rotate(val1,val2);
			return true;
		}
	}	
	/**
	 * Wyœwietlamy okno dialogowe z brief, z jednym przyciskiem
	 * komenda wywo³uj¹ca messageDialog, gdzie val1 to id dialogu
	 * a val2 opcjonalnie oznacza po wduszeniu przycisku wywo³anie startTask o id val2
	 * @author Krzysztof
	 *
	 */
	public class ShowMessageDialogCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
	//		_taskManager.getController().setPause(true);
			_taskManager.getBriefPopup().showMessage(_taskManager.getDialogManager().getMessage(val1),
					_taskManager.getDialogManager().getButtonCenter(val1),
					_taskManager.getDialogManager().getImageId(val1));
			_taskManager.getCenterButtonListener().setTaskStart(val2);
			return false;
		}
	}
	/**
	 * Wyœwietlamy okno dialogowe z brief, z dwoma przyciskami
	 * komenda wywo³uj¹ca selectDialog gdzie wal1 to id dialogu
	 * a val2 opcjolnalnie po wduszeniu przycisku left wywo³uje startTask o id val2
	 * a val3 opcjolnalnie po wduszeniu przycisku right wywo³uje startTask o id val3
	 * @author Krzysztof
	 *
	 */
	public class ShowSelectDialogCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
		//	_taskManager.getController().setPause(true);
			_taskManager.getBriefPopup().showSelect(_taskManager.getDialogManager().getMessage(val1),
					_taskManager.getDialogManager().getButtonLeft(val1),
					_taskManager.getDialogManager().getButtonRight(val1),
					_taskManager.getDialogManager().getImageId(val1));
			_taskManager.getLeftButtonListener().setTaskStart(val2);
			_taskManager.getRightButtonListener().setTaskStart(val3);
			return false;
		}
	}	
	/**
	 * Ustawia pause w grze jeœli val1 = 1 w przeciwnym razie wy³¹cza pause.
	 * Nie wp³ywa to na normaln¹ pause czy koniec gry
	 * komenda pause
	 * @author Krzysztof
	 *
	 */
	public class SetPauseCommand implements Command{
		public boolean command(int val1,int val2,int val3,int taskId){
			if(val1==1){
				_taskManager.getController().setPause(true);
				_taskManager.setPause(true);
			}else{
				_taskManager.getController().setPause(false);
				_taskManager.setPause(false);
			}
			return true;
		}
	}		
	/**
	 * Metoda ustawia pasek wiadomoœci jako niewidoczny
	 * Komenda wywo³uj¹ca hideBar
	 * @author Krzysztof
	 *
	 */
	public class HideBarCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getMessageBar().setVisible(false);
			return true;
		}
		
	}
	/**
	 * Pozwala wyœwietliæ na pasku wiadomoœci tekst o aktualnych zadaniach
	 * W tym celu u¿ywamy z xml pola dialog ale tylko message
	 * val1 oznacza id dialogu
	 * komenda showBarObjective
	 * @author Krzysztof
	 *
	 */
	public class ShowBarObjectiveCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getMessageBar().setVisible(true);
			_taskManager.getMessageBar().setObjective(MessageBar.TITLE_OBJECTIVES,_taskManager.getDialogManager().getMessage(val1));
			return true;
		}
	}
	/**
	 * Podobne do poprzedniego ale tytu³ sukces, wywo³ywane na kilka sekund po realizacji misji
	 * komenda showBarSuccess
	 * @author Krzysztof
	 *
	 */
	public class ShowBarSuccessCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getMessageBar().setVisible(true);
			_taskManager.getMessageBar().setObjective(MessageBar.TITLE_SUCCESS,_taskManager.getDialogManager().getMessage(val1));
			return true;
		}
	}	
	/**
	 * Metoda koñczy grê jako wygran¹
	 * Komenda finishSuccess
	 * @author Krzysztof
	 *
	 */
	public class FinishGameCommand implements Command{

		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getController().setStateSuccess();
		//	_taskManager.getController().getEndPopup().showMessage(GameEndPopup.TITLE_SUCCESS,"");
			GameEndPopup popupEnd=_taskManager.getController().getEndPopup();
			String messageId=GameEndPopup.MESSAGE_SUCCESS_LOW;
			popupEnd.setMessage(GameEndPopup.TITLE_SUCCESS,messageId);
			popupEnd.setButtonText(GameEndPopup.BUTTON_TYPE_RIGHT,GameEndPopup.LABEL_FINISH);
			popupEnd.setButtonText(GameEndPopup.BUTTON_TYPE_LEFT,GameEndPopup.LABEL_RESTART);
			_taskManager.getController().getMenuPopup().hide();
			_taskManager.getMessageBar().setVisible(false);
			_taskManager.getController().getEndPopup().show();
			_taskManager.getController().getUserShip().setSuccess();
			return false;
		}
		
	}
	/**
	 * Metoda koñczy grê jako przegrana
	 * Komenda finishFailure
	 * @author Krzysztof
	 *
	 */
	public class FailureGameCommand implements Command{

		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getController().setStateFailure();
			GameEndPopup popupEnd=_taskManager.getController().getEndPopup();
			popupEnd.setMessage(GameEndPopup.TITLE_FAILED,val1>0?Integer.toString(val1):GameEndPopup.MESSAGE_FAILED_SHIP_DESTROY);
			popupEnd.setButtonText(GameEndPopup.BUTTON_TYPE_LEFT,GameEndPopup.LABEL_MENU);
			popupEnd.setButtonText(GameEndPopup.BUTTON_TYPE_RIGHT,GameEndPopup.LABEL_RESTART);
			_taskManager.getController().getMenuPopup().hide();
			_taskManager.getMessageBar().setVisible(false);
			_taskManager.getController().getEndPopup().show();
			_taskManager.getController().getUserShip().setSuccess();
			return false;
		}
		
	}	
	/**
	 * Metoda dodaje punktu graczowi, tyle ile wynosi val1
	 * Komenda addPoints
	 * @author Krzysztof
	 *
	 */
	public class AddPointsCommand implements Command{

		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			_taskManager.getController().getCountScore().addScore(val1);
			return true;
		}
		
	}
	/**
	 * Teleportujemy usera do checkpointa o id val1
	 * komenda teleport
	 * @author Krzysztof
	 *
	 */
	public class TeleportUserCommand implements Command{

		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
			final int id=val1;
			final int _taskId=taskId;
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){
				_taskManager.getCheckpointPool().createCheckpoint(val1);
			}			
		//	_taskManager.getController().getUserShip().teleport(_taskManager.getCheckpointPool().getPosition(id));
			
			_taskManager.getController().getUserShip().setTargetAnimationAlpha(0.1f);
			_taskManager.getTimerAction().scheduleTask(new Task() {				
				@Override
				public void run() {
					_taskManager.getTimerAction().clear();
					
					_taskManager.getController().getUserShip().teleport(_taskManager.getCheckpointPool().getPosition(id));
					_taskManager.getController().getThreatSchedule().setRunAll();
					_taskManager.getTimerAction().scheduleTask(new Task() {				
						@Override
						public void run() {
							_taskManager.getTimerAction().clear();
							_taskManager.getController().getUserShip().setTargetAnimationAlpha(1);
							_taskManager.restartTask(_taskId);
						}
					},0.5f);	
					_taskManager.getTimerAction().start();
				}
			},0.5f);
			_taskManager.getTimerAction().start();
			return false;
		}
	}
	/**
	 * Klasa pozwala otwieraæ i zamykaæ bramy
	 * Realizuje to poprzez manipulacjê tileBoard.
	 * Nie zmieniamy id danej bramy by potem mo¿na by³o j¹ zamkn¹æ. ale oddzia³ywujemy na parametr image
	 * i odzia³ywujemy na parametr shading. Mo¿emy wiec zrobiæ grafikê na otwart¹ i zamkniêt¹ bramê lub
	 * j¹ wtopiæ w pod³o¿e
	 * komenda manipulateTile
	 * val1 oznacza id tile
	 * val2 oznacza nowy image jeœli 0 to zostanie zmiana grafiki zignorowana
	 * val3 oznacza 1 wall; 2 path
	 * path jest bez shadingu wall ma shading
	 * @author Krzysztof
	 *
	 */
	public class GateControlCommand implements Command{
		@Override
		public boolean command(int val1, int val2, int val3,int taskId) {
	//		System.out.println("Manipulate tile");
			_taskManager.getTileBoard().modifyTile(val1,val3,val2);
		//	_taskManager.getController().getGameMap().createPixMap();
		//	_taskManager.getController().getGameMap().regenerate();
			return true;
		}
		
	}
}

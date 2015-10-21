package pro.artwave.spaceshooter.controller.root.game;

import java.util.HashMap;
import java.util.Map;

import pro.artwave.spaceshooter.view.root.game.GameEndPopup;
import pro.artwave.spaceshooter.view.root.game.MessageBar;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * Klasa s�u��ca do obs�ugi patternu command
 * Ma zbi�r polece� kt�re s� wywo�ywane w TaskManager. U�ywa bez interfejsu obiektu TaskManager
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
	 * Wywo�ujemy poprzez ten interfejs komendy akcji
	 * je�li true to po danej komendzie od razu idzie nast�pna
	 * je�li false znaczy �e proces musi si� zatrzyma� w oczekiwaniu na akcj� usera lub timer
	 * jako �e nie jest to asynchronicznie to trzeba to obs�u�y� w taskManager
	 * @author Krzysztof
	 *
	 */
	public interface Command{
		public boolean command(int val1,int val2,int val3,int taskId);
	}
	/**
	 * S�u�y do dodania kr�tkiej przerwy mi�dzy metodami, np po wej�ciu na checkpoint chcemy wy�wietli� w messageBar
	 * �e uda�o si� osi�gn�� zadanie, nast�pnie po np 5 sec wywo�a� messagePopup z info o nowym zadaniu
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
	 * Oznacza na mapie i na board markerem okre�lony statek wroga. Oba kana�y na raz
	 * val1 s� to statki o okreslonym kolorze, val2 je�li 1 to oznaczony, w przeciwnym razie nie
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
			
			if(_taskManager.getCheckpointPool().isCheckpoint(val1)==false){//nie ma takiego checkpointa trzeba go najpierw utworzy�
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
	 * Metoda w��cza wy�wietlanie checkpointa o id val1 na mapie
	 * 
	 * Komenda wywo�uj�ca showMapCheckpoint 
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
	 * Metoda w��cza wy�wietlanie bonusu o id val1 na mapie
	 * 
	 * Komenda wywo�uj�ca showMapBonus 
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
	 * Metoda w��cza wy�wietlanie checkpointa na ekranie gry (pulsuj�ce 3 tr�jk�ty) o id val1
	 * Domyslnie checkpointy b�d� widoczne na ekranie
	 * Komenda wywo�uj�ca showBoardCheckpoint 
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
	 * Metoda w��cza/wy��cza wy�wietlanie oznaczenia bonusu (trzy tr�jk�ty, g��wnie przy checkpointach) o id val1 na planszy.
	 * Je�li bonus zebrany to nie ma oznaczenia
	 * 
	 * Komenda wywo�uj�ca setBonusMarked 
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
	 * Ustawia status bonus�w. Mo�emy jakie� specjalne bonusy na pocz�tku wy��czy� i w dalszej fazie rozgrywki dopiero
	 * w��czy�. Jako id podajemy kolor w val1, a status w��czony to val2 o id 1, w przeciwnym razie wy��cozny
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
	 * Pozwala wy��czy� lub w��czy� obiekty przeciwnika o okre�lonym id.
	 * Jako id podajemy kolor w val1, a status w��czony to val2 o id 1, w przeciwnym razie wy��cozny
	 * Komenda setEnemyStatus
	 * Komenda ma pewn� niekonsekwentno��, bo dzia�a na wszystkie elementy o danym id.
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
	 * Pozwala w��cza� lub wy��cza� opcj� strzelania okre�lonej grupie przeciwnik�w
	 * Jako id podajemy kolor w val1, a status w��czony to val2 o id 1, w przeciwnym razie wy��cozny
	 * Komenda setEnemyFire
	 * Komenda ma pewn� niekonsekwentno��, bo dzia�a na wszystkie elementy o danym id.
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
	 * pozwala stworzy� nowy statek przeciwnika na podstawie zdefiniowanego koloru (id)
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
	 * Metoda wy��cza wy�wietlanie checkpointa na ekranie gry (pulsuj�ce 3 tr�jk�ty) o id val1
	 * Domyslnie checkpointy b�d� widoczne na ekranie
	 * Komenda wywo�uj�ca hideBoardCheckpoint 
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
	 * Metoda wy��cza wy�wietlanie checkpointa o id val1 na mapie
	 * 
	 * Komenda wywo�uj�ca hideMapCheckpoint 
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
	 * Metoda tworzy nowy checkpoint o id val1 i tile pos val2 i val3(y oblcizane od do�u)
	 * Je�li istnieje checkpoint o danym id to zmienia jego lokalizacj�
	 * Komenda wywo�uj�ca createCheckpoint
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
	 * Komenda wywo�uj�ce checkpointDecorator
	 * @author Krzysztof
	 * @deprecated poniewa� znaj�c id dekoratora mo�emy go utworzy� bezpo�rednio u�ywaj�c po prostu komendy activeCheckpoint
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
	 * Modyfikujemy promie� istniej�cego checkpointa o id val1 i promieniu val2
	 * Komenda wywo�uj�sa radiusCheckpoint
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
	 * POzwala zamieni� grafik� jednego dekoratora na inn�
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
	 * Wy�wietla dekorator o id przekazanym przez val1 komenda wywo�uj�ca showDecoration
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
	 * Ukrywa dekorator o id przekazanym przez val1 komenda wywo�uj�ca hideDecoration
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
	 * Obraca dany dekorator o id r�wnym val1 i obrocie val2
	 * komenda wywo�ujaca rotateDecoration
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
	 * Wy�wietlamy okno dialogowe z brief, z jednym przyciskiem
	 * komenda wywo�uj�ca messageDialog, gdzie val1 to id dialogu
	 * a val2 opcjonalnie oznacza po wduszeniu przycisku wywo�anie startTask o id val2
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
	 * Wy�wietlamy okno dialogowe z brief, z dwoma przyciskami
	 * komenda wywo�uj�ca selectDialog gdzie wal1 to id dialogu
	 * a val2 opcjolnalnie po wduszeniu przycisku left wywo�uje startTask o id val2
	 * a val3 opcjolnalnie po wduszeniu przycisku right wywo�uje startTask o id val3
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
	 * Ustawia pause w grze je�li val1 = 1 w przeciwnym razie wy��cza pause.
	 * Nie wp�ywa to na normaln� pause czy koniec gry
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
	 * Metoda ustawia pasek wiadomo�ci jako niewidoczny
	 * Komenda wywo�uj�ca hideBar
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
	 * Pozwala wy�wietli� na pasku wiadomo�ci tekst o aktualnych zadaniach
	 * W tym celu u�ywamy z xml pola dialog ale tylko message
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
	 * Podobne do poprzedniego ale tytu� sukces, wywo�ywane na kilka sekund po realizacji misji
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
	 * Metoda ko�czy gr� jako wygran�
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
	 * Metoda ko�czy gr� jako przegrana
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
	 * Klasa pozwala otwiera� i zamyka� bramy
	 * Realizuje to poprzez manipulacj� tileBoard.
	 * Nie zmieniamy id danej bramy by potem mo�na by�o j� zamkn��. ale oddzia�ywujemy na parametr image
	 * i odzia�ywujemy na parametr shading. Mo�emy wiec zrobi� grafik� na otwart� i zamkni�t� bram� lub
	 * j� wtopi� w pod�o�e
	 * komenda manipulateTile
	 * val1 oznacza id tile
	 * val2 oznacza nowy image je�li 0 to zostanie zmiana grafiki zignorowana
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

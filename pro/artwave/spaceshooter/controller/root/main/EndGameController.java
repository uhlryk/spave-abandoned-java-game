package pro.artwave.spaceshooter.controller.root.main;

import java.util.Map;
import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.model.resource.GameResource;

/**
 * Kontroler obs�uguje dalsze akcje po zako�czeniu gry
 * W wersji finalnej mo�e zawiera� ekran z podsumowaniem
 * w wersji najprostszej realizuje tylko przekierowanie do
 * a)wyboru kampanii
 * b)wyb�r misji
 * c)wyb�r statku/bronii
 * @author Krzysztof
 *
 */
public class EndGameController extends Controller {
	private GameResource _game;
	private int _playerId;
	private int _missionId;
	
	/**
	 * Status zako�czonej gry, je�li true to gra zako�czona sukcesem
	 * false przegrana
	 */
	private boolean _isStatusSuccess;
	
	/**
	 * Je�li by�a wygrana, to je�li true, znaczy �e gracz zdoby� nowy rekord
	 * je�li false gracz nie zdoby� rekordu
	 */
	private boolean _isRecordScore;
	@Override
	public void onInit() {

	}

	@Override
	public void onLoad() {

	}

	@Override
	public void onPrepare() {
		_game=new GameResource();
		_game.init();			
		_playerId=this.getIntent().getIntegerValue("playerId",1);
		_missionId=this.getIntent().getIntegerValue("missionId",1);
		_isStatusSuccess=this.getIntent().getBooleanValue("gameStatus",false);
		_isRecordScore=this.getIntent().getBooleanValue("recordScore",false);
		if(_isStatusSuccess==false){
			Intent intent=new Intent(MainController.SELECT_MISSION_CONTROLLER);
			intent.addValue("playerId", _playerId);
			System.out.println("EndGameController oldmission "+ _missionId);
			getParent().setDispatch(intent);
		}else{		
			if(_game.getIntParam("mission_number")>_missionId){//mamy w danej kampanii kolejn� misj�
				Intent intent=new Intent(MainController.SELECT_MISSION_CONTROLLER);
				intent.addValue("playerId", _playerId);
				System.out.println("EndGameController nextmission "+( _missionId+1));
				getParent().setDispatch(intent);
			}else{//wszystkie kampanie zosta�y wykonane. nale�y wy�wietli� jaki� finalny popup
					Intent intent=new Intent(MainController.MAIN_MENU_CONTROLLER);
					getParent().setDispatch(intent);
				
			}
		}
	}
}

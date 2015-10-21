package pro.artwave.spaceshooter.controller.root.main;

import java.util.Map;
import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.model.resource.GameResource;

/**
 * Kontroler obs³uguje dalsze akcje po zakoñczeniu gry
 * W wersji finalnej mo¿e zawieraæ ekran z podsumowaniem
 * w wersji najprostszej realizuje tylko przekierowanie do
 * a)wyboru kampanii
 * b)wybór misji
 * c)wybór statku/bronii
 * @author Krzysztof
 *
 */
public class EndGameController extends Controller {
	private GameResource _game;
	private int _playerId;
	private int _missionId;
	
	/**
	 * Status zakoñczonej gry, jeœli true to gra zakoñczona sukcesem
	 * false przegrana
	 */
	private boolean _isStatusSuccess;
	
	/**
	 * Jeœli by³a wygrana, to jeœli true, znaczy ¿e gracz zdoby³ nowy rekord
	 * jeœli false gracz nie zdoby³ rekordu
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
			if(_game.getIntParam("mission_number")>_missionId){//mamy w danej kampanii kolejn¹ misjê
				Intent intent=new Intent(MainController.SELECT_MISSION_CONTROLLER);
				intent.addValue("playerId", _playerId);
				System.out.println("EndGameController nextmission "+( _missionId+1));
				getParent().setDispatch(intent);
			}else{//wszystkie kampanie zosta³y wykonane. nale¿y wyœwietliæ jakiœ finalny popup
					Intent intent=new Intent(MainController.MAIN_MENU_CONTROLLER);
					getParent().setDispatch(intent);
				
			}
		}
	}
}

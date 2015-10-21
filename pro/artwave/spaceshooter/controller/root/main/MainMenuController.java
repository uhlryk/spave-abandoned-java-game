package pro.artwave.spaceshooter.controller.root.main;

import java.util.Map;

import com.badlogic.gdx.Input;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.model.resource.GameResource;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.view.root.main.MainMenuView;

public class MainMenuController extends Controller {
	private MainMenuView _view;
	private SaveGameResource _saveGame;
	private GameResource _game;	
	@Override
	public void onInit() {
	}

	@Override
	public void onLoad() {
	}

	@Override
	public void onPrepare() {
		_view=new MainMenuView();
		_view.init();
//		_saveGame=new SaveGameResource();
//		_saveGame.init();
//		_game=new GameResource();
//		_game.init();	
		
		this.setView(_view);
		this.getParent().getView().addContent(_view);
		_view.onClick(MainMenuView.BUTTON_START_GAME,new Button.ClickListener() {	
			@Override
			public void up() {
				//Intent intent=new Intent(MainController.SELECT_CAMPAIGN_CONTROLLER);
				Intent intent=new Intent(MainController.SELECT_PLAYER_CONTROLLER);
				/**
				 * Ta czêœæ odpowiada za sprawdzenie jaka jest pierwsza nieukoñczona misja
				 * jeœli wszystkie s¹ ukoñczone to odpali siê domyœlna kampania pierwsza
				 * Jeœli znajdzie to odpala t¹ kampaniê.
				 * 
				 * Mo¿na rozsze¿yæ t¹ funkcjonalnoœæ o przypadek gdy brak jest jakiejkolwiek wygranej
				 * misji. wtedy mo¿na by pomin¹æ przejœcie do kampanii i przejœæ od razu do misji
				 * a nawet do wyboru statku czy planszy
				 * Albo do tutorialu
				 */
		/*		Map<Integer,GameResource.Campaign> campaignMap=_game.getCampaignMap();
				for(int i=1;i<campaignMap.size()+1;i++){
					GameResource.Campaign resource=campaignMap.get(i);
					_saveGame.getCampaignStatus(i);
					if(_saveGame.getMissionStatus(i, resource.lastMissionId)!=true){//znaczy ¿e dana kampania nie jest wygrana, mozemy wejœæ w jej misje
						intent.addValue("campaignId", i);
						break;
					}
				}*/
				getParent().setDispatch(intent);
			}
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				
			}
		});	
		_view.onClick(MainMenuView.BUTTON_OPTIONS,new Button.ClickListener() {	
			@Override
			public void onClick() {
				Intent intent=new Intent(MainController.OPTIONS_CONTROLLER);
				getParent().setDispatch(intent);
			}
		});
		_view.onClick(MainMenuView.BUTTON_CREDITS,new Button.ClickListener() {	
			@Override
			public void onClick() {
				Intent intent=new Intent(MainController.CREDITS_CONTROLLER);
				getParent().setDispatch(intent);
			}
		});		
		_view.onClick(MainMenuView.BUTTON_EXIT,new Button.ClickListener() {	
			@Override
			public void up() {
				Intent intent;
				intent=new Intent(MainController.EXIT_CONTROLLER);
				getParent().setDispatch(intent);
			}

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				
			}
		});			
	}
	@Override
	public boolean onKeyUp(int keycode){
		if(keycode==Input.Keys.ESCAPE||Input.Keys.BACK==keycode){			
			Intent intent=new Intent(MainController.EXIT_CONTROLLER);
			getParent().setDispatch(intent);
			return true;
		}
		return false;
	}
}

package pro.artwave.spaceshooter.controller.root.main;

import com.badlogic.gdx.Input;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.controller.RootController;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.controller.root.TransactionController;
import pro.artwave.spaceshooter.model.map.GameMapModel;
import pro.artwave.spaceshooter.model.resource.GameResource;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.view.Loader;
import pro.artwave.spaceshooter.view.root.main.SelectMissionView;
import pro.artwave.spaceshooter.view.root.main.SelectPlayerView;

public class SelectMissionController extends Controller {
	private SaveGameResource _saveGame;
	private GameResource _game;
	private SelectMissionView _view;
	private int _playerId;
	private int _missionId;
	private int _difficulty;
	@Override
	public void onInit() {
	}

	@Override
	public void onLoad() {
		_playerId=this.getIntent().getIntegerValue("playerId",1);
		_saveGame=new SaveGameResource();
		_saveGame.init(_playerId);
		_missionId=_saveGame.getLastMission()+1;
		_difficulty=this.getIntent().getIntegerValue("difficulty",1);
	}

	@Override
	public void onPrepare() {

		_game=new GameResource();
		_game.init();	
		System.out.println("playerid= "+_playerId+" score= "+_saveGame.getScore()+" mission= "+_missionId);
		_view=new SelectMissionView();
		_view.init(_game,_saveGame,_playerId,_missionId,_difficulty);
		this.setView(_view);
		this.getParent().getView().addContent(_view);
		_view.onClick(SelectMissionView.BUTTON_PREV,new Button.ClickListener() {	
			@Override
			public void onClick() {
			}
			@Override
			public void up() {
				Intent intent=new Intent(MainController.SELECT_PLAYER_CONTROLLER);
				intent.addValue("playerId",_playerId);
				intent.addValue("difficulty",_difficulty);
				getParent().setDispatch(intent);
			}
		});	
		_view.onClick(SelectMissionView.BUTTON_NEXT,new Button.ClickListener() {	
			@Override
			public void onClick() {
			}
			@Override
			public void up() {
				System.out.println("SelectMissionController "+_missionId);
				if(_missionId==1){
					Intent intent=new Intent(RootController.GAME_CONTROLLER);
					intent.addValue("missionId", _missionId);
					intent.addValue("playerId",_playerId);
					intent.addValue("difficulty",_difficulty);
					getParent().getParent().setDispatch(intent);
				}else{
					Intent intent=new Intent(MainController.SELECT_EQUIPMENT_CONTROLLER);
					intent.addValue("missionId", _missionId);
					intent.addValue("playerId",_playerId);
					intent.addValue("difficulty",_difficulty);
					getParent().setDispatch(intent);
				}
			}
		});				
		_view.onDifficultyClick(new SelectMissionView.ClickListener() {	
			@Override
			public void onClick(int difficulty) {
				System.out.println("Difficulty "+difficulty);
				_difficulty=difficulty;
			}
		});		
	}
	@Override
	public void onClear() {
		_saveGame=null;
		_game=null;
	}
	@Override
	public boolean onKeyUp(int keycode){
		if(keycode==Input.Keys.ESCAPE||Input.Keys.BACK==keycode){
			Intent intent=new Intent(MainController.MAIN_MENU_CONTROLLER);
			getParent().setDispatch(intent);
			return true;
		}
		return false;
	}
}

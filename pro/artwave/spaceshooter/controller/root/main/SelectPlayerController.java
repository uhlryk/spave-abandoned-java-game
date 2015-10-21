package pro.artwave.spaceshooter.controller.root.main;

import com.badlogic.gdx.Input;
import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.model.resource.GameResource;
import pro.artwave.spaceshooter.view.root.main.SelectPlayerView;

public class SelectPlayerController extends Controller {
	private GameResource _game;
	private SelectPlayerView _view;
	private int _playerId;
	private int _missionId;
	@Override
	public void onInit() {
	}

	@Override
	public void onLoad() {
	//	this.setLoader(new Loader());
	}

	@Override
	public void onPrepare() {
		_game=new GameResource();
		_game.init();				
		_playerId=this.getIntent().getIntegerValue("playerId",0);
		if(_playerId==0){
			_playerId=1;
			//jeœli nie ma przekazanej kampani to system dobiera odpowiedni¹
			//np 1 a jeœli pierwsza by³a wygrana to dopasowuje kolejn¹
		}
		_view=new SelectPlayerView();
		_view.init(_game,_playerId);
		this.setView(_view);
		this.getParent().getView().addContent(_view);
		_view.onClick(SelectPlayerView.BUTTON_PREV,new Button.ClickListener() {	
			@Override
			public void onClick() {
			}
			@Override
			public void up() {
				Intent intent=new Intent(MainController.MAIN_MENU_CONTROLLER);
				getParent().setDispatch(intent);
			}			
		});	
		_view.onClick(SelectPlayerView.BUTTON_NEXT,new Button.ClickListener() {	
			@Override
			public void onClick() {
			}
			@Override
			public void up() {
				Intent intent=new Intent(MainController.SELECT_MISSION_CONTROLLER);
				intent.addValue("playerId", _playerId);
		//		intent.addValue("missionId", _missionId);
				getParent().setDispatch(intent);
			}
		});			
		_view.onPlayerButtonClick(new SelectPlayerView.ClickListener() {	
			@Override
			public void onClick(int playerId,int missionId) {
				System.out.println("PlayerSelected "+playerId+" missionSelected "+missionId);
				_playerId=playerId;
				_missionId=missionId;
			}
		});
	}
	@Override
	public void onClear() {
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

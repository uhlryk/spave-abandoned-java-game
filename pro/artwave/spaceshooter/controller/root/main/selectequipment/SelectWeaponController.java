package pro.artwave.spaceshooter.controller.root.main.selectequipment;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.controller.RootController;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.controller.root.main.selectequipment.SelectShipController.ChangeShipListener;
import pro.artwave.spaceshooter.helper.WeaponParams;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.model.resource.WeaponResource;
import pro.artwave.spaceshooter.view.root.main.selectequipment.SelectWeaponView;
import pro.artwave.spaceshooter.view.root.main.selectequipment.SelectWeaponView.ClickListener;

public class SelectWeaponController extends Controller {
	private WeaponResource _weaponResource;
	private SelectWeaponView _view;
	private int _weaponType;
	private SaveGameResource _saveGame;	
	private int _playerId;
	private int _missionId;	
	private int _difficulty;
	@Override
	public void onInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepare() {
		_playerId=this.getParent().getIntent().getIntegerValue("playerId",1);
		_missionId=this.getParent().getIntent().getIntegerValue("missionId",1);	
		_difficulty=this.getParent().getIntent().getIntegerValue("difficulty",1);
		_weaponResource=new WeaponResource();
		_weaponResource.init();
		_saveGame=new SaveGameResource();
		_saveGame.init(_playerId);		
		_weaponType=this.getIntent().getIntegerValue("weaponType",WeaponParams.TYPE_FIRST_WEAPON);
		_view=new SelectWeaponView();
		if(_weaponType==WeaponParams.TYPE_FIRST_WEAPON){
			_view.init(_weaponResource,_saveGame.getScore(),_saveGame.getFirstWeapon(),_saveGame.getSecondWeapon(),_weaponType);
		}else{
			_view.init(_weaponResource,_saveGame.getScore(),_saveGame.getSecondWeapon(),_saveGame.getFirstWeapon(),_weaponType);
		}
		_view.onClick(SelectWeaponView.BUTTON_ACTIVE_WEAPON, new ClickListener() {
			@Override
			public void onClick(int weaponId) {
			//	System.out.println("aktywowano statek "+weaponId);
				_saveGame.saveWeapon(_weaponType, weaponId);
				if(_listener!=null){
					_listener.onChange(_weaponType,weaponId);
				}				
			}
		});
		_view.onClick(SelectWeaponView.BUTTON_PREV,new Button.ClickListener() {	
			@Override
			public void onClick() {
			}
			@Override
			public void up() {
				Intent intent=new Intent(MainController.SELECT_MISSION_CONTROLLER);
				intent.addValue("missionId", _missionId);
				intent.addValue("playerId",_playerId);	
				intent.addValue("difficulty",_difficulty);
				getParent().getParent().setDispatch(intent);
			}
		});	
		_view.onClick(SelectWeaponView.BUTTON_NEXT,new Button.ClickListener() {	
			@Override
			public void onClick() {
			}
			@Override
			public void up() {
				Intent intent=new Intent(RootController.GAME_CONTROLLER);
				intent.addValue("missionId", _missionId);
				intent.addValue("playerId",_playerId);
				intent.addValue("difficulty",_difficulty);
				getParent().getParent().getParent().setDispatch(intent);
			}
		});			
		this.setView(_view);
		this.getParent().getView().addContent(_view);
	}
	private ChangeWeaponListener _listener;
	public void onWeaponChange(final ChangeWeaponListener listener){
		_listener=listener;
	}
	public static abstract class ChangeWeaponListener{
		public abstract void onChange(int slot1,int weaponId);
	}	
}

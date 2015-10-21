package pro.artwave.spaceshooter.controller.root.main.selectequipment;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.controller.RootController;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.model.resource.ShipResource;
import pro.artwave.spaceshooter.view.root.main.SelectEquipmentView;
import pro.artwave.spaceshooter.view.root.main.selectequipment.SelectShipView;
import pro.artwave.spaceshooter.view.root.main.selectequipment.SelectShipView.ClickListener;

public class SelectShipController extends Controller {
	private SelectShipView _view;
	private ShipResource _shipResource;	
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

	}

	@Override
	public void onPrepare() {
		_playerId=this.getParent().getIntent().getIntegerValue("playerId",1);
		_missionId=this.getParent().getIntent().getIntegerValue("missionId",1);
		_difficulty=this.getParent().getIntent().getIntegerValue("difficulty",1);
		_shipResource=new ShipResource();
		_shipResource.init();
		_saveGame=new SaveGameResource();
		_saveGame.init(_playerId);
		System.out.println("SelectShipController playerid= "+_playerId+" score= "+_saveGame.getScore());
		_view=new SelectShipView();
		_view.init(_shipResource,_saveGame.getScore(),_saveGame.getShip());
		_view.onClick(SelectShipView.BUTTON_ACTIVE_SHIP, new ClickListener() {
			@Override
			public void onClick(int shipId) {
				System.out.println("aktywowano statek "+shipId);
				_saveGame.saveShip(shipId);
				if(_listener!=null){
					_listener.onChange(shipId);
				}
			}
		});
		_view.onClick(SelectShipView.BUTTON_PREV,new Button.ClickListener() {	
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
		_view.onClick(SelectShipView.BUTTON_NEXT,new Button.ClickListener() {	
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
	private ChangeShipListener _listener;
	public void onShipChange(final ChangeShipListener listener){
		_listener=listener;
	}
	public static abstract class ChangeShipListener{
		public abstract void onChange(int shipId);
	}	
}

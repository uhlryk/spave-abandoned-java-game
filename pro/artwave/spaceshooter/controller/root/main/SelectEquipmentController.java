package pro.artwave.spaceshooter.controller.root.main;

import com.badlogic.gdx.Input;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.controller.root.main.selectequipment.SelectShipController;
import pro.artwave.spaceshooter.controller.root.main.selectequipment.SelectShipController.ChangeShipListener;
import pro.artwave.spaceshooter.controller.root.main.selectequipment.SelectWeaponController;
import pro.artwave.spaceshooter.controller.root.main.selectequipment.SelectWeaponController.ChangeWeaponListener;
import pro.artwave.spaceshooter.helper.WeaponParams;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.model.resource.WeaponResource;
import pro.artwave.spaceshooter.view.root.main.SelectEquipmentView;

/**
 * Kontroler daje mo¿liwoœæ graczowi wybrania statków i broni.
 * @author Krzysztof
 * 
 */
public class SelectEquipmentController extends Controller {
	public static int SHIP_CONTROLLER;
	public static int WEAPON_I_CONTROLLER;
	public static int WEAPON_II_CONTROLLER;
	private SelectEquipmentView _view;
	private SaveGameResource _saveGame;	
	private int _playerId;
	private int _missionId;	
	
	private SelectShipController _selectShipController;
	private SelectWeaponController _selectWeapon1Controller;
	private SelectWeaponController _selectWeapon2Controller;
	@Override
	public void onInit() {
		_selectShipController=new SelectShipController();
		SHIP_CONTROLLER=this.addController(_selectShipController);
		_selectWeapon1Controller=new SelectWeaponController();
		WEAPON_I_CONTROLLER=this.addController(_selectWeapon1Controller);
		_selectWeapon2Controller=new SelectWeaponController();
		WEAPON_II_CONTROLLER=this.addController(_selectWeapon2Controller);
	}
	
	@Override
	public void onLoad() {		
//		this.setLoader(new Loader());
	}
	@Override
	public void onPrepare() {
		_playerId=this.getIntent().getIntegerValue("playerId",1);
		_missionId=this.getIntent().getIntegerValue("missionId",1);
		
		_saveGame=new SaveGameResource();
		_saveGame.init(_playerId);		
		System.out.println("SelectEquipmentController playerid= "+_playerId+" score= "+_saveGame.getScore());
		_view=new SelectEquipmentView();
		_view.init(_saveGame);
		this.setView(_view);
		this.getParent().getView().addContent(_view);		
		if(this.getIntent().getChildIntent()!=null){
			int childControllerId=this.getIntent().getChildIntent().getIntentIdClass();
			if(childControllerId==SHIP_CONTROLLER){
				_view.setActive(SelectEquipmentView.BUTTON_SELECT_SHIP);
			}else if(childControllerId==WEAPON_I_CONTROLLER){
				_view.setActive(SelectEquipmentView.BUTTON_SELECT_WEAPON_I);
			}else if(childControllerId==WEAPON_II_CONTROLLER){
				_view.setActive(SelectEquipmentView.BUTTON_SELECT_WEAPON_II);
			}
		}

		_view.onClick(SelectEquipmentView.BUTTON_SELECT_SHIP,new Button.ClickListener() {	
			@Override
			public void onClick() {
				if(_view.isActive(SelectEquipmentView.BUTTON_SELECT_SHIP)==false){
					_view.setActive(SelectEquipmentView.BUTTON_SELECT_SHIP);
					Intent intent=new Intent(SHIP_CONTROLLER);
					setDispatch(intent);
				}
			}
		});	
		_view.onClick(SelectEquipmentView.BUTTON_SELECT_WEAPON_I,new Button.ClickListener() {	
			@Override
			public void onClick() {
				if(_view.isActive(SelectEquipmentView.BUTTON_SELECT_WEAPON_I)==false){
					_view.setActive(SelectEquipmentView.BUTTON_SELECT_WEAPON_I);
					Intent intent=new Intent(WEAPON_I_CONTROLLER);
					intent.addValue("weaponType",WeaponParams.TYPE_FIRST_WEAPON);
					setDispatch(intent);
				}
			}
		});			
		_view.onClick(SelectEquipmentView.BUTTON_SELECT_WEAPON_II,new Button.ClickListener() {	
			@Override
			public void onClick() {
				if(_view.isActive(SelectEquipmentView.BUTTON_SELECT_WEAPON_II)==false){
					_view.setActive(SelectEquipmentView.BUTTON_SELECT_WEAPON_II);
					Intent intent=new Intent(WEAPON_II_CONTROLLER);
					intent.addValue("weaponType",WeaponParams.TYPE_SECOND_WEAPON);
					setDispatch(intent);
				}
			}
		});		
		_selectShipController.onShipChange(new ChangeShipListener() {			
			@Override
			public void onChange(int shipId) {
				_view.setTabImage(SelectEquipmentView.TAB_TYPE_SHIP,shipId);
				
			}
		});
		_selectWeapon1Controller.onWeaponChange(new ChangeWeaponListener() {			
			@Override
			public void onChange(int slotId,int weaponId) {
				_view.setTabImage(SelectEquipmentView.TAB_TYPE_WEAPON1,weaponId);	
			}
		});		
		_selectWeapon2Controller.onWeaponChange(new ChangeWeaponListener() {			
			@Override
			public void onChange(int slotId,int weaponId) {
				_view.setTabImage(SelectEquipmentView.TAB_TYPE_WEAPON2,weaponId);	
			}
		});			
	}

	@Override
	public boolean onKeyUp(int keycode){
		if(keycode==Input.Keys.ESCAPE||Input.Keys.BACK==keycode){
			Intent intent=new Intent(MainController.SELECT_MISSION_CONTROLLER);
			getParent().setDispatch(intent);
			return true;
		}
		return false;
	}	

}

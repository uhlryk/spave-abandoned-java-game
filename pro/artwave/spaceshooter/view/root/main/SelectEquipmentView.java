package pro.artwave.spaceshooter.view.root.main;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.view.root.main.selectequipment.TabButton;

public class SelectEquipmentView extends View {
	public static final int BUTTON_SELECT_SHIP=1;
	public static final int BUTTON_SELECT_WEAPON_I=2;
	public static final int BUTTON_SELECT_WEAPON_II=3;
	

	private TabButton _selectShipButton;
	private TabButton _selectWeaponIButton;
	private TabButton _selectWeaponIIButton;
		
	public static final int TAB_TYPE_SHIP=1;
	public static final int TAB_TYPE_WEAPON1=2;
	public static final int TAB_TYPE_WEAPON2=3;
	
	private SelectAssetAtlas _selectAssetAtlas;
	
	public void init(SaveGameResource saveGameResource){
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		_selectAssetAtlas=new SelectAssetAtlas();

		_selectShipButton=new TabButton();
		_selectShipButton.init(_selectAssetAtlas.createSpriteShip(saveGameResource.getShip()));
		
		_selectShipButton.setActive();
		_selectShipButton.setPosition(this.getWidth()-_selectShipButton.getWidth()+10,440);
		addActor(_selectShipButton);
		
		_selectWeaponIButton=new TabButton();
		_selectWeaponIButton.init(_selectAssetAtlas.createSpriteWeapon(saveGameResource.getFirstWeapon()));
		_selectWeaponIButton.setPosition(this.getWidth()-_selectWeaponIButton.getWidth()+10,310);
		addActor(_selectWeaponIButton);
		
		_selectWeaponIIButton=new TabButton();
		_selectWeaponIIButton.init(_selectAssetAtlas.createSpriteWeapon(saveGameResource.getSecondWeapon()));
		_selectWeaponIIButton.setPosition(this.getWidth()-_selectWeaponIIButton.getWidth()+10,180);
		addActor(_selectWeaponIIButton);		
		
	}
	public void setTabImage(int type,int elementId){
		System.out.println("type "+type+" elementId "+elementId);
		switch(type){
			case TAB_TYPE_SHIP:
				_selectShipButton.setImage(_selectAssetAtlas.createSpriteShip(elementId));
			break;
			case TAB_TYPE_WEAPON1:
				_selectWeaponIButton.setImage(_selectAssetAtlas.createSpriteWeapon(elementId));
			break;
			case TAB_TYPE_WEAPON2:
				_selectWeaponIIButton.setImage(_selectAssetAtlas.createSpriteWeapon(elementId));
			break;			
		}
	}
	public boolean isActive(int buttonId){
		switch(buttonId){
		case BUTTON_SELECT_SHIP:
			return _selectShipButton.isActive();
		case BUTTON_SELECT_WEAPON_I:
			return _selectWeaponIButton.isActive();
		case BUTTON_SELECT_WEAPON_II:
			return _selectWeaponIIButton.isActive();	
		}
		return false;
	}
	public void setActive(int buttonId){
		switch(buttonId){
		case BUTTON_SELECT_SHIP:
			_selectShipButton.setActive();
			_selectWeaponIButton.setDeactive();
			_selectWeaponIIButton.setDeactive();
			break;
		case BUTTON_SELECT_WEAPON_I:
			_selectShipButton.setDeactive();
			_selectWeaponIButton.setActive();
			_selectWeaponIIButton.setDeactive();
			break;
		case BUTTON_SELECT_WEAPON_II:
			_selectShipButton.setDeactive();
			_selectWeaponIButton.setDeactive();
			_selectWeaponIIButton.setActive();
			break;			
		}
	}
	public void onClick(int buttonId,Button.ClickListener listener){
		switch(buttonId){
		case BUTTON_SELECT_SHIP:
			_selectShipButton.addClickListener(listener);
			break;
		case BUTTON_SELECT_WEAPON_I:
			_selectWeaponIButton.addClickListener(listener);
			break;
		case BUTTON_SELECT_WEAPON_II:
			_selectWeaponIIButton.addClickListener(listener);
			break;			
		}
	}
}

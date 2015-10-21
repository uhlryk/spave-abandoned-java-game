package pro.artwave.spaceshooter.view.manipulator;

import pro.artwave.spaceshooter.model.asset.RootAssetAtlas;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ButtonFire extends Actor {
	public static final int WEAPON_SLOT_I=1;
	public static final int WEAPON_SLOT_II=2;
//	private int _weaponSlot;
	private Sprite _background;
	private Sprite _backgroundOn;
	private Sprite _backgroundOff;
	private RootAssetAtlas _rootAssetAtlas;	
	private Sprite _icon;
	private boolean _isFire;
	private float _ready;
	public void init(Sprite icon){
		_ready=1;
	//	_weaponSlot=weaponSlot;
		_rootAssetAtlas=new RootAssetAtlas();

		_background=_rootAssetAtlas.createSpriteButtonTabNeutral();
	//	_background.flip(true,false);
		_backgroundOn=_rootAssetAtlas.createSpriteButtonTabPositive();
		
		_backgroundOff=_rootAssetAtlas.createSpriteButtonTabNegative();
		//_backgroundOn.flip(true,false);
		_isFire=false;
		setSize(150,96);		
		setImage(icon);
	}
	public void setImage(Sprite icon){
		_icon=icon;
	//	_icon.setScale(0.26f);
	}	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {

		if(_isFire){
			_backgroundOn.setPosition(this.getX(),this.getY());
			_backgroundOn.draw(batch, parentAlpha);
		}else{
			_background.setPosition(this.getX(),this.getY());
			_background.draw(batch, parentAlpha);
		}
		_backgroundOff.setPosition(this.getX(),this.getY());
		_backgroundOff.draw(batch, parentAlpha*(1-_ready));
		
		_icon.setPosition(this.getX()+(this.getWidth()-_icon.getWidth())/2+5,this.getY()+(this.getHeight()-_icon.getHeight())/2-3);
		_icon.draw(batch,parentAlpha);
	}	
	public boolean isFire(){
		return _isFire;
	}
	public void setFire(boolean isFire){
		_isFire=isFire;
	}
	/*
	 * ustawia poziom gotowoœci do strza³u, 1 pe³na gotowoœæ wyœwietlamy ikonê _backgroundOn
	 * lub _background
	 * poni¿ej 1 brak gotowoœci i wyœwietlamy backgroundoff
	 */
	public void setReadyFire(float ready){
		_ready=ready;
		if(_ready<0)_ready=0;
		if(_ready>1)_ready=1;
	}	
}

package pro.artwave.spaceshooter.view.root.main.selectequipment;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.model.asset.RootAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;

public class TabButton extends Button {
	private Sprite _icon;
	private Sprite _bg;
	private Sprite _bgOn;
	private boolean _isActive;
	private Effects.Play _clickSound;
	public void init(Sprite icon){
		Effects effects=Effects.getInstance();
		_clickSound=effects.getButtonClick1();
		_isActive=false;
		setSize(150,96);
		RootAssetAtlas rootAssetAtlas=new RootAssetAtlas();
		_bg=rootAssetAtlas.createSpriteButtonTabNeutral();
		_bg.flip(true,false);
	//	_bg.setScale(1);
		this.setBackground(_bg);
		_bgOn=rootAssetAtlas.createSpriteButtonTabPositive();
		_bgOn.flip(true,false);
	//	_bgOn.setScale(1);
	//	System.out.println("TaBBUTTON scale "+_bgOn.getScaleX()+" "+_bgOn.getScaleY());
	//	System.out.println("TaBBUTTON size "+_bgOn.getWidth()+" "+_bgOn.getHeight());
		this.setBackgroundOn(_bgOn);
		init(1);
		this.setImage(icon);
	}
	@Override
	protected void onDown(){
		_clickSound.play();
	}	
	public void setImage(Sprite icon){
		_icon=icon;
		_icon.setScale(0.26f);
	}
	public void setActive(){
		_isActive=true;
		this.setBackground(_bgOn);
	}
	public void setDeactive(){
		_isActive=false;
		this.setBackground(_bg);
	}
	public boolean isActive(){
		return _isActive;
	}
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);
		_icon.setPosition(this.getX()+(this.getWidth()-_icon.getWidth())/2-5,this.getY()+(this.getHeight()-_icon.getHeight())/2-2);
		_icon.draw(batch,parentAlpha);
	}
}

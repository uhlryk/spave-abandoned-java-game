package pro.artwave.spaceshooter.view.root.main.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;

public class MainMenuButton extends Button {
	private Sprite _buttonMenuFireOn;
	private Sprite _buttonMenuFireOff;
	
	private Sprite _buttonEngineAnim1;
	private Sprite _buttonEngineAnim2;
	private Sprite _buttonEngineAnim3;
	private float _time;
	private float _alpha1;
	private float _alpha2;
	private float _alpha3;
	private Effects.Play _clickSound;
	public void init(String text){
		setSize(760,117);
		Effects effects=Effects.getInstance();
		_clickSound=effects.getButtonClick1();
		
		SmallOutlineAssetBitmapFont font=new SmallOutlineAssetBitmapFont();
		SelectAssetAtlas selectAssetAtlas=new SelectAssetAtlas();
		this.setBackground(selectAssetAtlas.createSpriteButtonMenu());
		//this.setBackgroundOn(selectAssetAtlas.createSpriteButton2());
		this.setFontBitmap(font.getFont());
		this.setText(text);
		_buttonMenuFireOn=selectAssetAtlas.createSpriteButtonMenuFlamesOn();
		_buttonMenuFireOff=selectAssetAtlas.createSpriteButtonMenuFlamesOff();
		this.setLabelY(73);
		_buttonEngineAnim1=selectAssetAtlas.createSpriteButtonEngineAnim(3);
		_buttonEngineAnim2=selectAssetAtlas.createSpriteButtonEngineAnim(2);
		_buttonEngineAnim3=selectAssetAtlas.createSpriteButtonEngineAnim(3);
		_time=0;
		_alpha1=0;
		_alpha2=0;
		_alpha3=1;
		init(1);
	}
	@Override
	protected void onDown(){
		_clickSound.play();
	}
	@Override
	public void act (float delta) {
		super.act(delta);
		_time+=delta/2;
		if(_time>3)_time=0;	
		if(_time<1){
			_alpha1=_time;
			_alpha3=1-_time;
			_alpha2=0;
		}else if(_time<2){
			_alpha1=2-_time;
			_alpha2=-1+_time;
			_alpha3=0;
		}else if(_time<3){
			_alpha1=0;
			_alpha2=3-_time;
			_alpha3=-2+_time;
		}		
		if(_alpha1>1)_alpha1=1;
		if(_alpha2>1)_alpha2=1;
		if(_alpha3>1)_alpha3=1;		
	}	
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);
		_buttonMenuFireOn.setPosition(this.getX(),this.getY());
		_buttonMenuFireOn.draw(batch,this.getChangeAlpha());
		_buttonMenuFireOff.setPosition(this.getX(),this.getY());
		_buttonMenuFireOff.draw(batch,1-this.getChangeAlpha());
		
		_buttonEngineAnim1.setPosition(this.getX(),this.getY());
		_buttonEngineAnim1.draw(batch,_alpha1);		
		_buttonEngineAnim2.setPosition(this.getX(),this.getY());
		_buttonEngineAnim2.draw(batch,_alpha2);		
		_buttonEngineAnim3.setPosition(this.getX(),this.getY());
		_buttonEngineAnim3.draw(batch,_alpha3);
		
	}
}

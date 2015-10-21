package pro.artwave.fgm.view.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Background extends Actor {
	private Sprite _background;	
	private float _posX;
	private float _posY;
	public void init(Sprite background){
		setBackground(background);
	}
	protected void setBackground(Sprite background){
		_background=background;
		if(this.getWidth()>0&&this.getHeight()>0){
			backgroundSetSize();
		}
	}	
	public void setSize(float width,float height){
		super.setSize(width, height);
		if(_background!=null){
			backgroundSetSize();
		}
	}	
	public void backgroundSetSize(){
		float ratioW=this.getWidth()/_background.getWidth();	
		float ratioH=this.getHeight()/_background.getHeight();	
		float ratio=1;
		if(ratioW>ratioH){
			ratio=ratioW;
		}else{
			ratio=ratioH;
		}
		_background.setSize(_background.getWidth()*ratioW,_background.getHeight()*ratio);
		_posX=(this.getWidth()-_background.getWidth())/2;
		_posY=(this.getHeight()-_background.getHeight())/2;
	/*	float ratio=this.getWidth()/_background.getWidth();	
		if(_background.getHeight()*ratio>this.getHeight()){
			float delta=_background.getHeight()*ratio-this.getHeight();
			_background.setRegionHeight((int)(_background.getRegionHeight()-delta/ratio/2));
			_background.setRegionY((int) (_background.getRegionY()+delta/ratio/2));
			_background.setSize(_background.getWidth()*ratio,_background.getHeight()*ratio-delta);
		}else{
			_background.setSize(_background.getWidth()*ratio,_background.getHeight()*ratio);
		}*/
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		_background.setPosition(_posX+this.getX(),_posY+this.getY());
		_background.draw(batch);
	}	
}

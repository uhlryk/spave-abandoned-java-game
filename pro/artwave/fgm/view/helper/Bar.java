package pro.artwave.fgm.view.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bar extends Actor {
	private Sprite _barEmpty;
	private Sprite _barFull;
	private int _actVal;
	private int _maxVal;
	private int actWidth;
	public void init(int width,int height){
		actWidth=width;		
		this.setSize(width, height);
	}
	public void setMaxVal(int val){
		_maxVal=val;
		_actVal=_maxVal;
	}
	public void setActVal(int val){
		_actVal=val;
		if(_actVal<0)_actVal=0;
		actWidth=(int) (_actVal*this.getWidth()/_maxVal);
		_barFull.setRegionWidth(actWidth);
		_barFull.setSize(actWidth,_barFull.getHeight());
	}	
	public void setEmptyBar(Sprite bar){
		_barEmpty=bar;
		_barEmpty.setSize(this.getWidth(),this.getHeight());
	}
	public void setFullBar(Sprite bar){
		_barFull=bar;
		_barFull.setSize(this.getWidth(),this.getHeight());
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);
		_barEmpty.setPosition(this.getX(),this.getY());
		_barFull.setPosition(this.getX(),this.getY());
		_barEmpty.draw(batch);
		_barFull.draw(batch);
	}
}

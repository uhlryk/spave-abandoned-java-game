package pro.artwave.fgm.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PulseSprite extends Sprite {
	private boolean _isPulse=false;
	private float _alpha=1;
	private int _step=0;
	private float _d=1f;
	private float _minVal=0;
	public PulseSprite(float minVal,float speed){
		super();
		_minVal=minVal;
		_d=speed;
	}
	public PulseSprite(float minVal){
		super();
		_minVal=minVal;
	}
	public PulseSprite(){
		super();
	}
	public void startPulse(){
		this._isPulse=true;
	}
	public void startPulse(float startAlpha,int startStep){
		this._alpha=startAlpha;
		this._step=startStep;
		this._isPulse=true;
	}
	public void stopPulse(){
		this._isPulse=false;
	}
	public void act (float delta) {
		if(this._isPulse==true){
			if(this._step==0){
				this._alpha-=_d*delta;
				if(this._alpha<_minVal){
					this._alpha=_minVal;
					this._step=1;
				}
			}else if(this._step==1){
				this._alpha+=_d*delta;
				if(this._alpha>1){
					this._alpha=1;
					this._step=0;
				}				
			}
		}
	}	
	@Override
	public void draw (SpriteBatch spriteBatch, float alphaModulation) {
		super.draw(spriteBatch, alphaModulation*_alpha);
	}	
}

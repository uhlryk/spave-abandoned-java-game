package pro.artwave.fgm.view.helper;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class ToggleView extends Group {
	private final static int STEP_SHOW=0;
	private final static int STEP_IS=1;
	private final static int STEP_HIDE=2;	
	private ArrayList<Sprite> _spriteList;
	private int _step=0;
	private int _slide=0;
	private float _alpha=0;
	private float _time=0;
	private float _showTime;
	private float _isTime;
	private float _hideTime;	
	private boolean _isVisible;
	private float _posX;
	private float _posY;	
	private FinishListener _listener;
	public void init(float showTime,float isTime,float hideTime){
		this.setTransform(false);
		this._spriteList=new ArrayList<Sprite>();
		this._showTime=showTime;
		this._isTime=isTime;
		this._hideTime=hideTime;
		this._isVisible=true;
		this.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(_isVisible){
					ToggleView.this._time=0;
					ToggleView.this._slide++;
					if(_spriteList.size()>_slide){
						ToggleView.this._step=STEP_SHOW;
					}else{
						_isVisible=false;
						_listener.onFinish();
					}					
					ToggleView.this._alpha=0;		
				}
				return true;
			}
		});
	}
	public void addFinishListener(FinishListener listener){
		_listener=listener;
	}
	public void addSprite(Sprite sprite){
		this._spriteList.add(sprite);
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		if(this._isVisible){
			_posX=_spriteList.get(_slide).getX();
			_posY=_spriteList.get(_slide).getY();
			_spriteList.get(_slide).setPosition(this.getX()+_posX,this.getY()+_posY);
			_spriteList.get(_slide).draw(batch,parentAlpha*this._alpha);
			_spriteList.get(_slide).setPosition(_posX,_posY);			
		}
	}
	@Override
	public void act (float delta) {
		this._time+=delta;
		switch(this._step){
		case STEP_SHOW:
			if(this._time>this._showTime){
				this._time=0;
				this._step=STEP_IS;
				this._alpha=1;
			}else{
				this._alpha=0+this._time/this._showTime;
			}
			break;
		case STEP_IS:
			if(this._time>this._isTime){
				this._time=0;
				this._step=STEP_HIDE;
				this._alpha=1;
			}else{
				this._alpha=1;
			}			
			break;
		case STEP_HIDE:
			if(this._time>this._hideTime){
				this._time=0;
				this._slide++;
				if(_spriteList.size()>_slide){
					this._step=STEP_SHOW;
				}else{
					_isVisible=false;
					_listener.onFinish();
				}
				this._alpha=0;
			}else{
				this._alpha=1-this._time/this._hideTime;
			}				
			break;			
		}
	}
	public static abstract class FinishListener{
		public abstract void onFinish();
	}
}

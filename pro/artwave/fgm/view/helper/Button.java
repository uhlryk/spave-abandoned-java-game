package pro.artwave.fgm.view.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Button extends Actor {
	private Sprite _background;
	private Sprite _backgroundOn;
	private BitmapFont _font;
	private String _text;
	private float _labelHeight;
	private float _offsetX;
	private int _padding;
	private boolean _isTouchDown;
	private float _alpha;
	private float _time;
	private float _animationTime;
	private ClickListener _listener;	
	private boolean _dragged;
	private float _x;
	private float _y;	
	private Color _color;
	private boolean _isPushedDown;
	public void init(float animationTime){	
		_isPushedDown=false;
		_isTouchDown=false;
		_alpha=0;
		_time=0;
		_animationTime=animationTime;
		_color=new Color(1,1,1,1);
		this.addListener(new InputListener(){
			private float _dist_x=10;
			private float _dist_y=10;
			/**
			 * Wduszenie przycisku wywo³a jednorazowo t¹ metodê
			 */
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				_isTouchDown=true;
				_dragged=false;
				_x=x;
				_y=y;
				if(_listener!=null){
					onDown();
					_listener.down();
				}
				return true;
			}
			/**
			 * Jeœli wskaŸnik siê przesunie to bêdzie oznacza³o ¿e wywo³anie nie by³o wduszeniem a draggiem
			 */
			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				if(x-_x>_dist_x||x-_x<-_dist_x||y-_y>_dist_y||y-_y<-_dist_y){
					_dragged=true;
					_isTouchDown=false;
					if(_listener!=null){
						onDragged();
						_listener.dragged();
					}
				}
			}
			/**
			 * Przycisk odpala akcjê na touchUp ale sprawdza czy mia³ wduszenie czy dragged
			 * Na ka¿de reaguje inaczej
			 */
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				_isTouchDown=false;
				if(_dragged==false){
					if(_listener!=null){
						onClick();
						_listener.onClick();
					}
				}
				if(_listener!=null){
					onUp();
					_listener.up();
				}				
			}			
		});		
	}
	protected void onDown(){}
	protected void onDragged(){}
	protected void onUp(){}
	protected void onClick(){}
	public void triggerButton(){
		if(_listener!=null){
			_listener.down();
			_listener.onClick();
			_listener.up();
		}
	}
	public void setTextColor(Color color){
		_color=color;
	}
	public void setBackground(Sprite background){
		_background=background;
		_background.setSize(this.getWidth(),this.getHeight());
	}
	public void setBackgroundOn(Sprite background){
		_backgroundOn=background;
		_backgroundOn.setSize(this.getWidth(),this.getHeight());
	}	
	public boolean isPushedDown(){
		return _isPushedDown;
	}
	public void setPushedDown(){
		_isPushedDown=true;
	}
	public void setPushedUp(){
		_isPushedDown=false;
	}
	public void setFontBitmap(BitmapFont font){
		_font=font;
		if(_font!=null&&_text!=null){
			setSizeText();
		}		
	}
	public void setText(String text){
		_text=text;
		if(_font!=null&&_text!=null){
			setSizeText();
		}		
	}
	public void setLabelY(float labelHeight){
		_labelHeight=labelHeight;
	}
	public void setOffsetX(float offsetX){
		_offsetX=offsetX;
	}	
	public void setSizeText(){
		float labelHeight=_font.getWrappedBounds(_text,this.getWidth()-_padding*2).height;
		labelHeight=(this.getHeight()-labelHeight)/2+labelHeight;
		setLabelY(labelHeight);
	}
	public void setSize(float width,float height){
		super.setSize(width, height);
		if(_background!=null){
			_background.setSize(this.getWidth(),this.getHeight());
		}
		if(_backgroundOn!=null){
			_backgroundOn.setSize(this.getWidth(),this.getHeight());
		}		
		if(_font!=null&&_text!=null){
			setSizeText();
		}
	}
	/**
	 * Metoda zwraca wartoœæ przezroczystoœci przeznaczonej na stan wdusznia, dziêki
	 * czemu mo¿na inne elementy na innym poziomie podobnie traktowaæ
	 * domyœlnie alpha ma wartoœæ 0. Jak jest wduszona to przechodzi z 0 do 1 
	 * i tak d³ugo jak jest wduszone tak pozostaje
	 * @return
	 */
	public float getChangeAlpha(){
		if(_isPushedDown){
			return 1;
		}
		return _alpha;
	}
	@Override
	public void act (float delta) {
		if(_isTouchDown){
			_time+=delta;
		}else{
			_time-=delta;
		}
		if(_time<0) _time=0;
		if(_time>_animationTime)_time=_animationTime;
		_alpha=_time/_animationTime;
		
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(this.getWidth()==0){
			throw new RuntimeException("Button has not size declared");
		}
		if(_background!=null){
			_background.setPosition(this.getX(),this.getY());
			_background.draw(batch,parentAlpha);
		}
		if(_backgroundOn!=null){
			float locAlpha=_alpha;
			if(_isPushedDown){
				locAlpha=1;
			}
			_backgroundOn.setPosition(this.getX(),this.getY());
			_backgroundOn.draw(batch,parentAlpha*locAlpha);	
		}
		if(_font!=null&&_text!=null){		
			_font.setColor(_color.r,_color.g,_color.b,_color.a*parentAlpha);
			_font.drawWrapped(batch,_text,this.getX()+_padding+_offsetX,this.getY()+_labelHeight,this.getWidth()-_padding*2-_offsetX,HAlignment.CENTER);
		}
		super.draw(batch,parentAlpha);
	}
	public void addClickListener(ClickListener listener){
		_listener=listener;
	}	
	public static abstract class ClickListener{
		public void up(){}
		public void down(){}
		public void dragged(){}
		public abstract void onClick();
	}	
}

package pro.artwave.fgm.view.helper;

import java.util.HashMap;
import java.util.Map;

import pro.artwave.fgm.view.helper.Button.ClickListener;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
/**
 * Klasa odpowiada za wyœwietlenie popupu
 * Posiada metody generowania przycisków i odpowiednich zdarzeñ
 * Z klasy najlepiej korzystaæ jak z klasy abstrakcyjnej i j¹ podziedziczyæ
 * konkretnymi popupami
 * Klasa do dzia³ania potrzebuje minimalnego asseta
 * @author Krzysztof
 *
 */
public class Popup extends Group {
	private Sprite _bgTop;
	private Sprite _bgBottom;
	private Vector2 _backgroundTopPosition;
	private Vector2 _backgroundBottomPosition;	
	private float _alpha=1;
	private int _step=0;
	private float _dShow=1f;	
	private float _dHide=2f;	
	private Map<Integer,Button> _buttonMap;
	/**
	 * Metoda inicjuj¹ca wywo³ywana od razu
	 */
	public void init(){
		this.setTransform(false);
		_buttonMap=new HashMap<Integer,Button>(4);
		_bgTop=null;
		_bgBottom=null;
		this.hide();
		this.setVisible(false);
	}
	public void show(){
		this.setTouchable(Touchable.childrenOnly);
	//	System.out.println("START POPUP CCCC!!!!!!!!!!!!!");
		this._step=0;
		this._alpha=0;
		this.setVisible(true);
	}
	public void hide(){
		this.setTouchable(Touchable.disabled);
		this._step=1;
		this._alpha=1;		
		//this.setVisible(false);
	}
	public boolean isShow(){
		return this.isVisible();
	}
	/**
	 * Dodajemy górn¹ czêœæ menu
	 * @param bg
	 */
	public void setBackgroundTop(Sprite bg,int offsetX,int offsetY){
		_bgTop=bg;
		_backgroundTopPosition=new Vector2(offsetX,offsetY);
	}
	/**
	 * Dodajemy doln¹ czêœæ menu
	 * @param bg
	 */
	public void setBackgroundBottom(Sprite bg,int offsetX,int offsetY){
		_bgBottom=bg;
		_backgroundBottomPosition=new Vector2(offsetX,offsetY);
	}	
	/**
	 * Dodajemy nowe przyciski do popupu
	 * @param buttonId
	 * @param button
	 * @param x
	 * @param y
	 */
	public void addButton(int buttonId,Button button,int x,int y){
		_buttonMap.put(buttonId,button);
		button.setPosition(x, y);
		this.addActor(button);
	}
	/**
	 * Metoda zwraca dodany przycisk, mozemy wiêc nadaæ mu now¹ odpowiedŸ na eventy
	 * @param buttonId
	 * @return
	 */
	public Button getButton(int buttonId){
		return _buttonMap.get(buttonId);
	}
	/**
	 * Metoda pozwala zmieniæ tekst dodanemu wczeœniej przyciskowi
	 * pozwala to u¿ywaæ jednego popupu do wielu zastosowañ
	 * @param buttonId
	 * @param text
	 */
	public void setButtonText(int buttonId,String text){
		Button button=_buttonMap.get(buttonId);
		if(button!=null){
			button.setText(text);
		}
	}
	public void addClickListener(int buttonId,ClickListener listener){
		_buttonMap.get(buttonId).addClickListener(listener);
	}	
	public void act (float delta) {
		if(this._step==1){
			this._alpha-=_dHide*delta;
			if(this._alpha<0){
				this._alpha=0;
				this.setVisible(false);
				
			}
		}else if(this._step==0){
			this._alpha+=_dShow*delta;
			if(this._alpha>1){
				this._alpha=1;
			}				
		}
		super.act(delta);
	}	
	public float getAnimation(){
		return this._alpha;
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		if(_bgTop!=null){
			_bgTop.setPosition(this.getX()+_backgroundTopPosition.x,this.getY()+_backgroundTopPosition.y);
			_bgTop.draw(batch,parentAlpha*this._alpha);
		}
		if(_bgBottom!=null){
			_bgBottom.setPosition(this.getX()+_backgroundBottomPosition.x,this.getY()+_backgroundBottomPosition.y);
			_bgBottom.draw(batch,parentAlpha*this._alpha);
		}		
		super.draw(batch,parentAlpha*this._alpha);
	}
}

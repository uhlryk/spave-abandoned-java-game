package pro.artwave.spaceshooter.view.unit;

import pro.artwave.spaceshooter.helper.UnitParams;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * Obiekt niematerialny, nie da siê tego zniszczyæ. Mo¿e siê nadawaæ jako emiter pocisków
 * @author Krzysztof
 *
 */
public class Construction extends Actor {
	private float _shield;
	private int _health;
	private UnitParams _params;
	private Vector2 _position;
	/**
	 * Wyra¿one w stopniach
	 */
	private float _angle;
	private Sprite _unitImage;
	private Sprite _unitMaskImage;
	private Sprite _shieldImage;
	private float _marginUnitX;
	private float _marginUnitY;	
	private float _marginShieldX;
	private float _marginShieldY;		
	private float _bonusShield;
	private float _bonusHealth;
	private int _warGroupId;
	private int _radius;
	private float _hitTintColorTime;
	private float _shieldTintColorTime;
	private GameAssetAtlas _gameAssetAtlas;
	private float _alpha;
	private float _alphaParam;
	private boolean _isFade;		
	private String _debugName;
	private int _shieldColor;
	private boolean _resistance;
	
	/**
	 * Po trafieniu statek przez jakiœ czas bêdzie niewra¿liwy na ciosy
	 */
	private float _actResistanceTime;
	private float _resistanceTime;
	/**
	 * Inicjacja statku
	 * @param params parametry statku z pliku xml
	 * @param image grafika prezentuj¹ca statek
	 * @param startX pozycja startowa X
	 * @param startY pozycja startowa Y
	 * @param startRotation obrót startowy domyœlnie dla 0 wskazuje w dó³
	 */
	public void init(int warGroupId,UnitParams params,float startX,float startY,float startRotation){
		this.setPosition(0,0);
		_resistanceTime=0.3f;
		_actResistanceTime=0;
		
		_shieldColor=0x0000ffff;
		_resistance=false;
		_alpha=1;
		_alphaParam=1;
		_isFade=false;					
		_gameAssetAtlas=new GameAssetAtlas();
		_hitTintColorTime=0;
		_shieldTintColorTime=0;
		_warGroupId=warGroupId;
		_params=params;
		_position=new Vector2();
		_position.set(startX,startY);
		_angle=startRotation;				
		_radius=_params.radius!=0?_params.radius:30;
		_shield=_params.maxShield;
		_health=_params.maxHealth;
		_bonusShield=1;
		_bonusHealth=1;		
		this.setPosition(getPositionX(),getPositionY());
		if(warGroupId==1){
			setGroupColor(new Color(0,1,0,0.5f));
		}else{
			setGroupColor(new Color(1,0,0,0.5f));
		}
	}
	private Color _groupColor;
	public void setGroupColor(Color groupColor){
		_groupColor=groupColor;
	}
	/**
	 * Jeœli true to jednostka staje siê nieœmiertelna
	 * @param resistance
	 */
	public void setResistance(boolean resistance){
		_resistance=resistance;
	}
	/**
	 * Sprawdza czy jednostka jest nieœmierterlna
	 * @return true- nieœmiertelna; false - œmiertelny
	 */
	public boolean isResistance(){
		return _resistance;
	}
//	public void setShieldColorA(int color){
//		_shieldColor=color;
//	}
	public void setDebugName(String debugName){
		_debugName=debugName;
	}
	public String getDebugName(){
		return _debugName;
	}
	/**
	 * Metoda wywo³ywana zaraz za init. Tworzy grafiki 
	 */
	public void createImage(){
		this.setImageUnit(_gameAssetAtlas.createSpriteConstruction(_params.id),_gameAssetAtlas.createSpriteConstructionMask(_params.id));
		this.setImageShield(_gameAssetAtlas.createSpriteShield());
	}
	protected GameAssetAtlas getGameAssetAtlas(){
		return _gameAssetAtlas;
	}
	protected UnitParams getParams(){
		return _params;
	}
	public void setBonusShield(int factor){
		_bonusShield=(float)factor/100;
		_shield*=_bonusShield;
	}
	public void setBonusHealth(int factor){
		_bonusHealth=(float)factor/100;
		_health*=_bonusHealth;
	}		
	public void setShield(int shield){
		_shield+=shield;
		if(_shield>_params.maxShield)_shield=_params.maxShield;
	}
	public void setHealth(int health){
		_health+=health;
		if(_health>_params.maxHealth)_health=_params.maxHealth;
	}	
	public void regenerateShield(float delta){
		_shield+=delta*_params.regeneration;
		if(_shield>_params.maxShield*_bonusShield){
			_shield=_params.maxShield*_bonusShield;
		}
	}
	public int getShipType(){
		return _params.id;
	}
	public int getRadius(){
		return _radius;
	}
	protected void setImageUnit(Sprite image,Sprite mask){
		_unitImage=image;
		float w=_unitImage.getWidth()/2;
		float h=_unitImage.getHeight()/2;
		_marginUnitX=-w;
		_marginUnitY=-h;
		_unitMaskImage=mask;
		_unitMaskImage.setColor(_groupColor);
	}
	protected void setImageShield(Sprite image){
		_shieldImage=image;
		float w=_shieldImage.getWidth()/2;
		float h=_shieldImage.getHeight()/2;
		_marginShieldX=-w;
		_marginShieldY=-h;
	}	
	@Override
	public void act (float delta) {
		if(_isFade==true){
			_alphaParam-=delta*2f;
			if(_alphaParam<0.3f){
				_alphaParam=0.3f;
				_isFade=false;
			}
		}else{
			_alphaParam+=delta*2f;
			if(_alphaParam>0.9f){
				_alphaParam=0.9f;
				_isFade=true;
			}			
		}
		_alpha=_alphaParam;
		if(_alpha>1)_alpha=1;
		if(_alpha<0.3)_alpha=0.3f;
		super.act(delta);
	}	
	protected void drawUnit(SpriteBatch batch,float parentAlpha){
		_unitImage.setRotation(_angle);
		_unitImage.setPosition(this.getX()+_marginUnitX,this.getY()+_marginUnitY);
		if(_actResistanceTime>0){
			_unitImage.draw(batch,parentAlpha*_alpha);
		}else{
			_unitImage.draw(batch,parentAlpha);
		}
		_unitMaskImage.setRotation(_angle);
		_unitMaskImage.setPosition(this.getX()+_marginUnitX,this.getY()+_marginUnitY);
		if(_actResistanceTime>0){
			_unitMaskImage.draw(batch,parentAlpha*_alpha);
		}else{
			_unitMaskImage.draw(batch,parentAlpha);
		}
		
	}
	protected void drawShield(SpriteBatch batch,float parentAlpha){
		_shieldImage.setColor(_shieldColor);
		_shieldImage.setPosition(this.getX()+_marginShieldX, this.getY()+_marginShieldY);
		_shieldImage.draw(batch,parentAlpha*_alpha*this.getShield()/_bonusShield/_params.maxShield);

	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		drawShield(batch, parentAlpha);
		drawUnit(batch, parentAlpha);
	}
	/**
	 * Metoda sprawdza czy obiekt by³ trafiony, jeœli tak podbarwia mu kolor na czerwono lub niebiesko
	 * w zale¿noœci czy straci³ ¿ycie czy shield, dodatkowo robi chwilowo niewra¿liwy na obra¿enia. Mo¿liwe ¿e zrezygnujemy z metody
	 * @param delta
	 */
	protected void calculateHitColor(float delta){
		if(_hitTintColorTime>0){
			this._unitImage.setColor(1,0.5f,0.5f,1);
		}else if(_shieldTintColorTime>0){
			this._unitImage.setColor(0.5f,0.5f,1,1);
		}
		else{
			this._unitImage.setColor(1,1,1,1);
		}
		_hitTintColorTime-=delta;
		_shieldTintColorTime-=delta;
	}
	/**
	 * Wywo³ywany powinien byæ 1 raz na sec
	 */
	public void calculate(float delta){
		calculateHitColor(delta);
		calculateTimeResistance(delta);
	}
	protected void calculateTimeResistance(float delta){
		if(_actResistanceTime>0){
			_actResistanceTime-=delta;
		//	System.out.println("okresowa niewra¿liwoœæ "+_actResistanceTime);
		}
	}
	/**
	 * W przypadku statków mamy oprócz angle statku drug¹ metodê wyœwietlaj¹c¹ k¹t ruchu
	 * W przypadku innym ni¿ manouver oba k¹ty s¹ te same
	 * @return k¹t wyra¿ony w stopniach okreœlaj¹cy obrót statku lub turretTop lub construction
	 * 
	 */
	public float getAngle(){
		return _angle;
	}
	public Vector2 getPosition(){
		return _position;
	}
	public float getPositionX(){
		return _position.x;
	}
	public float getPositionY(){
		return _position.y;
	}			
	/**
	 * prostrza wersja metody calculateObjectCollision
	 * Wystêpuje tylko promieñ statku, nie uwzglêdnia dodatkowego promienia obiektu z którym jest kolizja
	 * Przyjmujemy ¿e nie ma on promienia
	 * @param position
	 * @param offset
	 * @return
	 */
	public boolean calculateObjectCollisionNoRadius(Vector2 position,int offset){
		int xa0=(int) (_position.x-_radius);
		int xa1=(int) (_position.x+_radius);
		int ya0=(int) (_position.y-_radius);
		int ya1=(int) (_position.y+_radius);	
		int xb=(int) (position.x)+offset;
		int yb=(int) (position.y)+offset;	
		if((xa0<xb&&xa1>xb)&&(ya0<yb&&ya1>yb)){
			return false;
		}
		return true;
	}	
	/**
	 * metoda oblicza czy dany obiekt ma kolizjê z innym obiektem
	 * Jeœli ma kolizjê to metoda zwraca false!!! Kiedyœ trzeba bêdzie to zamieniæ na true
	 * izmieniæ nazwê metody na isObjectCollision
	 * @param position wspó³rzêdne drugiego obiektu
	 * @param radius promieñ drugiego obiektu
	 * @return
	 */	
	public boolean calculateObjectCollision(Vector2 position,int radius){	
		return calculateObjectCollision(position,radius,0);
	}
	/**
	 * metoda oblicza czy dany obiekt ma kolizjê z innym obiektem
	 * Jeœli ma kolizjê to metoda zwraca false!!! Kiedyœ trzeba bêdzie to zamieniæ na true
	 * izmieniæ nazwê metody na isObjectCollision
	 * @param position wspó³rzêdne drugiego obiektu
	 * @param radius promieñ drugiego obiektu
	 * @param offset jeœli wystêpuje jakiœ offset- przesuniêcie to tu podajemy
	 * Najlepiej unikaæ takich sytuacji ustawiaj¹c zawsze origin no ale ju¿ trudno by³o by to
	 * wyczyœciæ
	 * @return
	 */
	public boolean calculateObjectCollision(Vector2 position,int radius,int offset){
		int xa0=(int) (_position.x-_radius);
		int xa1=(int) (_position.x+_radius);
		int ya0=(int) (_position.y-_radius);
		int ya1=(int) (_position.y+_radius);	
		int xb0=(int) (position.x-radius)+offset;
		int xb1=(int) (position.x+radius)+offset;
		int yb0=(int) (position.y-radius)+offset;
		int yb1=(int) (position.y+radius)+offset;	
		if(((xa0>xb0&&xa0<xb1)||(xa1>xb0&&xa1<xb1))&&((ya0>yb0&&ya0<yb1)||(ya1>yb0&&ya1<yb1))){
			return false;
		}
		return true;
	}
	public void setPosition(Vector2 pos){
		_position=pos;
	}	
	public void setAngle(float a){
		_angle=a;
		if(_angle<0)_angle=360+_angle;
		if(_angle>360)_angle=_angle-360;  
	}
	public void addAngle(float a){
		_angle+=a;
		if(_angle<0)_angle=360+_angle;
		if(_angle>360)_angle=_angle-360;  		
	}
	public int getWarGroupId(){
		return _warGroupId;
	}
	/**
	 * Ustawia maksymalny czas przez który gracz jest niewra¿liwy po trafieniu
	 * @param resistanceTime
	 */
	public void setResistanceTime(float resistanceTime){
		_resistanceTime=resistanceTime;
	}
	/**
	 * Pobiera maksymalny czas przez jaki gracz jest niewra¿liwy po trafieniu
	 * @return
	 */
	protected float getResistanceTime(){
		return _resistanceTime;
	}
	/**
	 * Metoda sprawdza czy obiekt po trafieniu jest jeszcze okresowo niewra¿liwy na pociski
	 * @return true jest niewra¿liwy, false wra¿liwy
	 */
	public boolean isTimeResistance(){
		if(_actResistanceTime<=0){
			return false;
		}else{
			return true;
		}
	}
	public void addHit(int damage,int extraShield,int extraArmor){
		if(isResistance()==false&&_actResistanceTime<=0){
			_actResistanceTime=_resistanceTime;
			_shield-=extraShield;
			_health-=extraArmor;
			_shield-=damage;
			if(_shield<0){
				_hitTintColorTime=0.2f;
				_health=(int) (_health+_shield);
				_shield=0;
			}else{
				_shieldTintColorTime=0.2f;
			}
		}
	}
	public int getHealth(){
		return _health;
	}
	public float getShield(){
		return _shield;
	}
}

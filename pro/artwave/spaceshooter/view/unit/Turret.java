package pro.artwave.spaceshooter.view.unit;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import pro.artwave.spaceshooter.helper.UnitParams;
import pro.artwave.spaceshooter.helper.WeaponParams;
import pro.artwave.spaceshooter.manager.BulletPool;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.model.map.GameMapModel;
import pro.artwave.spaceshooter.view.tile.TileBoardIsometric;


/**
 * Odpowiada za tworzenie dzia�ek.
 * Dzia�ka w przeciwie�stwie do statk�w s� nieruchome.
 * Ale sk�adaj� si� z dw�ch cz�ci podstawy i obrotowego dzia�a(dzia�o mo�e by� nieobrotowe)
 * @author Krzysztof
 * TODO
 * je�li chcemy da� podstaw� dla dzia�a to nale�y odkomentowa�
 * drawUnitBottom()
 */
public class Turret extends Construction { 
	/**
	 * obiekt unieruchomiony
	 */
	public static final int MOVE_MODE_NONE=0;	
	/**
	 * je�li tryb w��czony to dzia�� metody sterowania bezpo�rednio silnikami
	 */
	public static final int MOVE_MODE_MANUAL=1;
	/**
	 * W tym trybie obracamy statek w kierunku targetu, a statek porusza si� tylko w danym kierunku
	 */
	public static final int MOVE_MODE_DIRECTIONAL=2;
	/**
	 * statek porusza si� i obraca niezale�nie
	 */
	public static final int MOVE_MODE_MANOUVER=3;
	/**
	 * dok�adno�� w stopniach to jakiej wystarczy �e obr�ci si� obiekt
	 */
	private static final float ANGLE_PRECISION_TARGET=3;
	private boolean _isTurnRightOn;
	private boolean _isTurnLeftOn;
	private boolean _isWeapon1Fire;
	private boolean _isWeapon2Fire;
	private float _lastFire1;
	private float _lastFire2;
	private float _bonusDamage;
	private Sprite _unitImageBottom;
	private float _marginUnitBottomX;
	private float _marginUnitBottomY;		
	private Map<Integer,WeaponParams> _weapon;
	private int _moveMode;
	private Vector2 _targetFirst;
	private boolean _isTargetFirstAiming;
	private Effects.Play _soundFire1;
	private Effects.Play _soundFire2;
	/**
	 * wyra�one w stopniach
	 */
	private float _destAngle;
	public void init(int warGroupId,UnitParams params,float startX,float startY,float startRotation){
		super.init(warGroupId, params, startX, startY, startRotation);
		Effects effects=Effects.getInstance();
		_soundFire1=effects.getFire3(0.3f);		
		_soundFire2=effects.getFire1(0.3f);	
		_isTurnLeftOn=false;
		_isTurnRightOn=false;	
		_weapon=new HashMap<Integer,WeaponParams>();		
		_isWeapon1Fire=false;
		_isWeapon2Fire=false;	
		_bonusDamage=1;
		_moveMode=MOVE_MODE_MANUAL;
		_targetFirst=null;
		_isTargetFirstAiming=false;
	}
	public void setFireVolume(float fireVolume){
		_soundFire1.setVolume(fireVolume);
		_soundFire2.setVolume(fireVolume);
	}
	/**
	 * metoda pozwala doda� target, w przypadku turreta b�d�cy celem
	 * turret umie si� tylko obraca� w danym kierunku
	 * W przypadku turreta MOVE_MODE_DIRECTIONAL i MOVE_MODE_MANOUVER
	 * oznacza to samo
	 * @param targetFirst
	 */
	public void setTargetFirst(Vector2 targetFirst){
		this.setTargetFirst(targetFirst,false);
	}
	public void setTargetFirst(Vector2 targetFirst,boolean isAiming){
		_targetFirst=targetFirst;
		_isTargetFirstAiming=isAiming;
	}
	public boolean isTargetFirstAiming(){
		return _isTargetFirstAiming;
	}
	public void clearTargetFirst(){
		_targetFirst=null;
		_isTargetFirstAiming=false;
	}
	/**
	 * metoda zwraca target pierwszy.
	 * Target pierwszy w przypadku turret�w jest zawsze celem
	 * Ale w przypadku statk�w mo�e by� kierunkiem lotu lub te� celem
	 * @return
	 */
	public Vector2 getTargetFirst(){
		return _targetFirst;
	}
	/**
	 * Pozwala okre�li� tryb poruszania si� obiektu
	 * @param moveMode
	 */
	public void setMoveMode(int moveMode){
		_moveMode=moveMode;
		this.turnLeftOff();
		this.turnRightOff();
		
	}
	/**
	 * zwraca tryb ruchu obiektu
	 * @return
	 */
	public int getMoveMode(){
		return _moveMode;
	}
	@Override
	public void createImage(){
		this.setImageBottomUnit(getGameAssetAtlas().createSpriteTurretBottom(getParams().id));
		this.setImageTopUnit(getGameAssetAtlas().createSpriteTurretTop(getParams().id),getGameAssetAtlas().createSpriteTurretTopMask(getParams().id));
		this.setImageShield(getGameAssetAtlas().createSpriteShield());
	}
	protected void setImageTopUnit(Sprite image,Sprite mask){
		this.setImageUnit(image,mask);
	}
	protected void setImageBottomUnit(Sprite image){
		_unitImageBottom=image;
		float w=_unitImageBottom.getWidth()/2;
		float h=_unitImageBottom.getHeight()/2;
		_marginUnitBottomX=-w;
		_marginUnitBottomY=-h;
	}	
	/**
	 * Warto�� k�ta do jakiego ma doj�� jednostka zanim si� zatrzyma, A wi�c nawet
	 * je�li ma du�y stopie� obroty to nie przekroczy okre�lonego 
	 * @return
	 */
	protected float getDestinationAngle(){
		return _destAngle;
	}
	public void setBonusDamage(int factor){
		_bonusDamage=(float)factor/100;
	}	
	/**
	 * Mo�na zdefiniowa� k�t do jakiego obr�ci si� statek przy obrocie. Je�li obr�t wi�kszy ni�
	 * docelowy to zostanie to wykonane w kilku krokach
	 * @param destinationAngle
	 */
	protected void setDestinationAngle(float destinationAngle){
		_destAngle=destinationAngle;
	}	
	public void setWeapon(int slot,WeaponParams weapon){
		_weapon.put(slot, weapon);
	}
	/**
	 * metoda sprawdza czy mamy w��czony silnik obrotu w prawo
	 * @return
	 */
	protected boolean isTurnRightOn(){
		return _isTurnRightOn;
	}
	/**
	 * metoda sprawdza czy mamy w��czony silnik obrotu w lewo
	 * @return
	 */
	protected boolean isTurnLeftOn(){
		return _isTurnLeftOn;
	}	
	private void turnLeftOn(float destAngle){
		_destAngle=destAngle;
		_isTurnLeftOn=true;
	}	
	public void startLeftEngine(float destAngle){		
		if(this.getMoveMode()==Turret.MOVE_MODE_MANUAL){
			turnLeftOn(destAngle);
		}
	}	
	private void turnLeftOn(){
		_isTurnLeftOn=true;
	}
	public void startLeftEngine(){
		if(this.getMoveMode()==Turret.MOVE_MODE_MANUAL){
		//	System.out.println(this.getDebugName()+" startLeftEngine"+this.getMoveMode());
			turnLeftOn();
		}
	}	
	protected void turnLeftOff(){
		_isTurnLeftOn=false;
	}	
	public void stopLeftEngine(){
		if(this.getMoveMode()==Turret.MOVE_MODE_MANUAL){
			turnLeftOff();
		}
	}		
	private void turnRightOn(float destAngle){	
		_destAngle=destAngle;
		_isTurnRightOn=true;
	}
	public void startRightEngine(float destAngle){
		if(this.getMoveMode()==Turret.MOVE_MODE_MANUAL){
			turnRightOn(destAngle);
		}
	}		
	private void turnRightOn(){
		_isTurnRightOn=true;
	}
	public void startRightEngine(){
		if(this.getMoveMode()==Turret.MOVE_MODE_MANUAL){
			turnRightOn();
		}
	}		
	protected void turnRightOff(){
		_isTurnRightOn=false;
	}	
	public void stopRightEngine(){
		if(this.getMoveMode()==Turret.MOVE_MODE_MANUAL){
			turnRightOff();
		}
	}	
	public void fireWeaponOn(int slotId,boolean reload){
		if(slotId==1){
			if(reload==true){
	//			_lastFire1=(float)(_weapon.get(1).reload)/1000;
			}
			_isWeapon1Fire=true;			
		}else if(slotId==2){
			if(reload==true){
				_lastFire2=(float)(_weapon.get(2).reload)/1000;
			}
			_isWeapon2Fire=true;		
		}
	}
	public void fireWeaponOff(int slotId){
		if(slotId==1){
			_isWeapon1Fire=false;
		}else if(slotId==2){
			_isWeapon2Fire=false;
		}
	}
	public void resetWeapon(float delta){
		_lastFire1-=delta;
		if(_lastFire1<0)_lastFire1=0;
		_lastFire2-=delta;
		if(_lastFire2<0)_lastFire2=0;
	}
	public boolean calculateWeapon(BulletPool bulletPool){
		/**
		 * Je�li true to zosta� wystrzelony pocisk
		 */
		boolean _wasFire=false;
	//	System.out.println("_lastFire1 "+_lastFire1);
		if(_isWeapon1Fire){
			if(_lastFire1==0){
				if(_weapon.get(1)!=null){
			//		System.out.println("calculateWeapon fire");
					_soundFire1.play();
					bulletPool.fireWeapon(getWarGroupId(),_weapon.get(1), getPosition(),getAngle(),this._bonusDamage);
					_lastFire1=(float)(_weapon.get(1).reload)/1000;
					_wasFire=true;
				}
			}
		}
		if(_isWeapon2Fire){
			if(_lastFire2==0){
				if(_weapon.get(2)!=null){
					_soundFire2.play();
					bulletPool.fireWeapon(getWarGroupId(),_weapon.get(2), getPosition(),getAngle(),this._bonusDamage);
					_lastFire2=(float)(_weapon.get(2).reload)/1000;
				//	_lastFire2=0;
					_wasFire=true;
					
				}
			}
		}
		return _wasFire;
	}	
	/**
	 * dla danej broni wy�wietla ile brakuje do gotowo�ci
	 * 1 pe�na gotowo�� 
	 * 0 ca�kowity brak gotowo�ci
	 * @param slotId
	 * @return
	 */
	public float getWeaponReadyProgress(int slotId){
		float progress=1;
		float maxTime;
	//	System.out.println("getWeaponReadyProgress slotId "+slotId);
		switch(slotId){
			case 1:
				maxTime=(float)(_weapon.get(1).reload)/1000;
		//		System.out.println("maxTime "+maxTime);
		//		System.out.println("_lastFire1 "+_lastFire1);
				progress=(maxTime-_lastFire1)/maxTime;
				break;
			case 2:
				maxTime=(float)(_weapon.get(2).reload)/1000;
		//		System.out.println("maxTime "+maxTime);
		//		System.out.println("_lastFire2 "+_lastFire2);				
				progress=(maxTime-_lastFire2)/maxTime;
				break;
		}
		return progress;
	}
	/**
	 * Wy�szy poziom sterowania
	 * Zamiast w��cza� obroty w okre�lonych kierunkach wydajemy polecenie
	 * obrotu. A obiekt sam ma si� zatroszczy� o odpowiednie obr�cenie
	 *targetAngle wyra�one w stopniach
	 */
	public void directRotate(float targetAngle){
		float dist1=targetAngle-this.getAngle();
		if(dist1<0)dist1=360+dist1;
		float dist2=360-targetAngle+this.getAngle();
		if(dist2>360)dist2=dist2-360;
		if(dist1<dist2){
		//	System.out.println(this.getDebugName()+" dist1 "+dist1);
			if(dist1>ANGLE_PRECISION_TARGET){
				this.turnLeftOn(dist1);
				this.turnRightOff();
			}else{
				this.turnLeftOff();
				this.turnRightOff();
			}
		}else{
		//	System.out.println(this.getDebugName()+" dist2 "+dist2);
			if(dist2>ANGLE_PRECISION_TARGET){
				this.turnRightOn(dist2);
				this.turnLeftOff();
			}else{
				this.turnLeftOff();
				this.turnRightOff();
			}
		}
	}
	public void directRotate(Vector2 target){
		float targetAngle=(float)Math.toDegrees(calculateAngle(target));
	//	System.out.println("rotation "+targetAngle);
		directRotate(targetAngle);
	}	
	/**
	 * Obr�t podobnie do ruchu jest zale�ny od trybu poruszania. W przypadku manualnego
	 * jest on nieaktywny
	 * W przypadku direct, obraca si� tam gdzie
	 * Trzeci poziom sterowania, steruje directRotate, kt�re steruje silnikami
	 */
	public void control(){
		switch(this.getMoveMode()){
		case MOVE_MODE_MANUAL:
			
			break;
		case MOVE_MODE_DIRECTIONAL: //w przypadku Turreta zachowuj� si� tak samo poniewa� turret si� nie porusza
		case MOVE_MODE_MANOUVER:
			this.directRotate(this.getTargetFirst());
			break;			
		}
	}
	/**
	 * Wywo�ywany powinien by� 1 raz na sec
	 */
	public void calculate(float delta){
		calculateHitColor(delta);
		calculateTimeResistance(delta);
		float rotationStep=0;
		if(_isTurnLeftOn||_isTurnRightOn){
			rotationStep=getParams().rotation*delta;
			if(_destAngle>0&&rotationStep>_destAngle){
				rotationStep=_destAngle;
			}
		}
		if(_isTurnLeftOn){
			addAngle(rotationStep);
		}
		if(_isTurnRightOn){
			addAngle(-rotationStep);
		}	
		_destAngle=0;       
	}	
	public void drawUnitBottom(SpriteBatch batch,float parentAlpha){
//		_unitImageBottom.setPosition(this.getX()+_marginUnitBottomX,this.getY()+_marginUnitBottomY);
//		_unitImageBottom.draw(batch,parentAlpha);
	}		
		
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		drawShield(batch, parentAlpha);
		drawUnitBottom(batch,parentAlpha);
		drawUnit(batch, parentAlpha);
	}
	/**
	 * Zwraca k�t mi�dzy obiektem a danym wektorem
	 * NIE JEST TO R�ZNICA KAT�W
	 * tylko k�t miedzy dwoma punktami!!!
	 * @param target pozycja obiektu
	 * @return k�t wyra�ony w radianach
	 */
	public float calculateAngle(Vector2 target){
		float posX=target.x-getPositionX();
		float posY=target.y-getPositionY();		
		float angle= (float) (-0.5*Math.PI+Math.atan2(posY,posX));
		if(angle<0)angle=(float) (2*Math.PI+angle);
		return angle;	
	}	
	/**
	 * Na podstawie targetowego k�ta okre�lamy k�t obrotu
	 * metoda wylicza najkr�tsz� drog� obrotu do celu
	 * je�li warto�� dodatnia to obracamy w lewo
	 * Je�li warto�� ujemna to w prawo
	 * Warto�� uejmna to 360+warto��
	 * @param targetAngle
	 * @return
	 */
	public float calculateDeltaAngle(float targetAngle){
		float dist1=targetAngle-this.getAngle();
		if(dist1<0)dist1=360+dist1;
		float dist2=360-targetAngle+this.getAngle();
		if(dist2>360)dist2=dist2-360;
		if(dist1<dist2){
			return dist1;
		}else{
			return -dist2;
		}
	}
	public boolean isTargetTileCollision(Vector2 target,TileBoardIsometric tileBoard){
		int sourceX;
		int sourceY;
		int targetX;
		int targetY;
		if(target.x<getPositionX()){
			sourceX=(int)Math.round(target.x/GameMapModel.TILE_SIZE);
			targetX=(int)Math.round(getPositionX()/GameMapModel.TILE_SIZE);
		}else{
			targetX=(int)Math.round(target.x/GameMapModel.TILE_SIZE);
			sourceX=(int)Math.round(getPositionX()/GameMapModel.TILE_SIZE);
		}
		if(target.y<getPositionY()){
			sourceY=(int)Math.round(target.y/GameMapModel.TILE_SIZE);
			targetY=(int)Math.round(getPositionY()/GameMapModel.TILE_SIZE);
		}else{
			targetY=(int)Math.round(target.y/GameMapModel.TILE_SIZE);
			sourceY=(int)Math.round(getPositionY()/GameMapModel.TILE_SIZE);
		}		
		for(int x=sourceX;x<targetX;x++){
			for(int y=sourceY;y<targetY;y++){
				int type=tileBoard.getTileType(x, y);
				if(type==TileBoardIsometric.TYPE_WALL){
					return true;
				}
			}	
		}
		return false;
	}
	/**
	 * Zwraca odleg�o�� miedzy obiektem a targetem
	 * @param target
	 * @return
	 */
	public float calculateDeltaDistance(Vector2 target){
		float posX=target.x-getPositionX();
		float posY=target.y-getPositionY();		
		return (float) Math.sqrt(posX*posX+posY*posY);
	}	
}

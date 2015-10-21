package pro.artwave.spaceshooter.view.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import pro.artwave.spaceshooter.helper.BackfireParams;
import pro.artwave.spaceshooter.helper.UnitParams;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;

public class Ship extends Turret { 
	/**
	 * dystans który dzieli od celu, i który mo¿na uznaæ za równy celowi
	 */
	public final static int ENGINE_TARGET_DISTANCE=60;
	/**
	 * s³u¿y do okreslenia w metodzie isNeedEngine czy k¹t docelowy jest wiêkszy od tego minimalnego
	 * Jeœli wiêkszy to metoda odpala silniki
	 * jeœli mniejszy to tylko obraca
	 */
	public final static int NOENGINE_ANGLE_MIN=20;
	public final static int NOENGINE_DISTANCE_MIN=140;
	public final static int NOENGINE_ONLY_ANGLE_MIN=70;
	protected Vector2 _oldPosition;
	/**
	 * S³u¿y do zwracania wektorów pozycji mieszanych np x z _position a y z _oldPosition
	 */
	protected Vector2 _temporaryPosition;
    protected Vector2 _velocity;
    private boolean _isEngineOn;
    private float _engineOffTime=0;	
    private Map<Integer,BackfireData> _backfireMap;
    private boolean _isBackposition;
	private float _distanceSpeedFactor;  
	private float _bonusSpeed;
	private float _maxSpeed;	
	protected Vector2 _manouverVelocity; 
	private float _moveAngle;
	private Vector2 _targetSecond;
	
	public void init(int warGroupId,UnitParams params,float startX,float startY,float startRotation){
		super.init(warGroupId, params, startX, startY, startRotation);
		_temporaryPosition=new Vector2();
		_manouverVelocity=new Vector2();
		_oldPosition=getPosition().cpy();
		_velocity = new Vector2();
		_isEngineOn=false;	
		_backfireMap=new HashMap<Integer,BackfireData>();
		for(Entry<Integer, BackfireParams> backfireParams:getParams().backfire.entrySet()){
			BackfireData data=new BackfireData();
			data.image=getGameAssetAtlas().createSpriteBackfire(backfireParams.getValue().type);
			data.offsetX=-data.image.getWidth()/2;
			data.offsetY=-data.image.getHeight()/2;
			_backfireMap.put(backfireParams.getKey(),data);
		}		
		_isBackposition=false;
		_distanceSpeedFactor=1;
		_bonusSpeed=1;		
		_maxSpeed=getParams().maxSpeed;		
		_targetSecond=null;
		_moveAngle=getAngle();
	}
	/**
	 * Statek mo¿e mieæ inny k¹t dla moveAngle i dla angle
	 * Angle okreœla k¹t w który jest odwrócony statek, a moveAngle kierunek ruchu statku
	 * w przypadku manualmode obracamy zarówno angle jak i moveAngle
	 * wprzypadku direcangle podobnie
	 * w przypadku manouver moveangle i angle s¹ ró¿ne
	 * @param moveAngle
	 */
	protected void setMoveAngle(float moveAngle){
		_moveAngle=moveAngle;
		if(_moveAngle<0)_moveAngle=360+_moveAngle;
		if(_moveAngle>360)_moveAngle=_moveAngle-360;  
	}
	public void addMoveAngle(float a){
		_moveAngle+=a;
		if(_moveAngle<0)_moveAngle=360+_moveAngle;
		if(_moveAngle>360)_moveAngle=_moveAngle-360;  
	}	
	public float getMoveAngle(){
		return _moveAngle;
	}
	/**
	 * metoda pozwala dodaæ drugi target, w przypadku statku gdy mamy dwa targety to pierwszy
	 * jest zawsze celem ostrza³u, zwracania siê statku gracza,
	 * a drugi jest kierunkiem ruchu
	 * @param targetFirst
	 */
	public void setTargetSecond(Vector2 targetSecond){
		_targetSecond=targetSecond;
	}
	public void clearTargetSecond(){
		_targetSecond=null;
	}	
	/**
	 * metoda zwraca target pierwszy.
	 * Target pierwszy w przypadku turretów jest zawsze celem
	 * Ale w przypadku statków mo¿e byæ kierunkiem lotu lub te¿ celem
	 * @return
	 */
	public Vector2 getTargetSecond(){
		return _targetSecond;
	}	
	public void setTargets(Vector2 targetFirst,Vector2 targetSecond){
		this.setTargetFirst(targetFirst);
		this.setTargetSecond(targetSecond);
	}
	@Override
	public void setMoveMode(int moveMode){
		super.setMoveMode(moveMode);
		this.engineOff();
		this.clearTargetSecond();
		this.setMoveAngle(this.getAngle());
	}	
	@Override
	public void createImage(){
		this.setImageUnit(getGameAssetAtlas().createSpriteShip(getParams().id),getGameAssetAtlas().createSpriteShipMask(getParams().id));
		this.setImageShield(getGameAssetAtlas().createSpriteShield());
	}	
	public void setBonusSpeed(int factor){
		_bonusSpeed=(float)factor/100*1.5f;
		if(factor<100&&_bonusSpeed>1){
			_bonusSpeed=1;
		}
		_maxSpeed=getParams().maxSpeed*_bonusSpeed*_distanceSpeedFactor;
	}		
	public Vector2 getVelocity(){
		return _velocity;
	}
	private float _targetAnimationAlpha=1;
	private float _currentAnimationAlpha=1;
	/**
	 * Metoda pozwala okreœliæ docelow¹ alpha obiektu, a nastêpnie poprzez animacjê nast¹pi p³ynne przejœcie do tej
	 * wartoœci
	 * @param targetAnimationAlpha
	 * @return
	 */
	public void setTargetAnimationAlpha(float targetAnimationAlpha){
		_targetAnimationAlpha=targetAnimationAlpha;
		if(_targetAnimationAlpha>1)_targetAnimationAlpha=1;
		if(_targetAnimationAlpha<0)_targetAnimationAlpha=0;
	}
	public float getCurrentAlpha(){
		return _currentAnimationAlpha;
	}
	@Override
	public void act(float delta){
		super.act(delta);
		if(_targetAnimationAlpha<_currentAnimationAlpha){
			_currentAnimationAlpha-=0.05f;
			if(_targetAnimationAlpha>_currentAnimationAlpha){
				_currentAnimationAlpha=_targetAnimationAlpha;
			}
		}
		if(_targetAnimationAlpha>_currentAnimationAlpha){
			_currentAnimationAlpha+=0.05f;
			if(_targetAnimationAlpha<_currentAnimationAlpha){
				_currentAnimationAlpha=_targetAnimationAlpha;
			}
		}
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		float actAlpha=parentAlpha*_currentAnimationAlpha;
		for(Entry<Integer, BackfireData> params:_backfireMap.entrySet()){
			BackfireParams backfireParams=getParams().backfire.get(params.getKey());
			if((backfireParams.left&&isTurnLeftOn())||(backfireParams.right&&isTurnRightOn())||(backfireParams.up&&_isEngineOn))
			{
				params.getValue().image.setRotation(getAngle()+backfireParams.angle);
				float x=(float) (Math.sin(Math.toRadians(getAngle()+backfireParams.rad))*backfireParams.rel)+params.getValue().offsetX;
				float y=-(float) (Math.cos(Math.toRadians(getAngle()+backfireParams.rad))*backfireParams.rel)+params.getValue().offsetY;
				params.getValue().image.setPosition(this.getX()+x,this.getY()+y);
				params.getValue().image.draw(batch,actAlpha);
			}
		}		
		drawShield(batch, actAlpha);
		drawUnit(batch, actAlpha);
		
	}	
	public void setBackSpeed(float speed){	
		_velocity.scl(-speed*0.5f);  
		_engineOffTime=0.5f;
		_isBackposition=true;
	}
	public void setBackSpeed(float speed,float angle){
		_velocity.x = (float)Math.sin(angle)*speed;
		_velocity.y = (float)Math.cos(angle)*speed;
		_engineOffTime=0.6f;
		_isBackposition=true;
	}	
	private void engineOn(){
		_isEngineOn=true;
	}
	public void startEngine(){
		engineOn();
	}
	private void engineOff(){
		_isEngineOn=false;
	}	
	public void stopEngine(){
		engineOff();
	}	
	public void setDistanceSpeedFactor(float speedFactor){
		_distanceSpeedFactor=speedFactor;
		_maxSpeed=getParams().maxSpeed*_bonusSpeed*_distanceSpeedFactor;
	}	
	public void control(){
		switch(this.getMoveMode()){
		case MOVE_MODE_MANUAL:
			this.setMoveAngle(getAngle());
			break;
		case MOVE_MODE_DIRECTIONAL:
			if(this.getTargetFirst()!=null){
				float targetDeltaDistance;
				if(this.getTargetSecond()!=null){					
					targetDeltaDistance=calculateDeltaDistance(this.getTargetSecond());
					
					
					if(targetDeltaDistance>ENGINE_TARGET_DISTANCE){
						float targetAngle=(float)Math.toDegrees(calculateAngle(this.getTargetSecond()));
						this.directRotate(targetAngle);
						float deltaAngle=Math.abs(this.calculateDeltaAngle(targetAngle));
						if((NOENGINE_ANGLE_MIN<deltaAngle&&NOENGINE_DISTANCE_MIN>targetDeltaDistance)||NOENGINE_ONLY_ANGLE_MIN<deltaAngle){
							this.engineOff();
						}else{
							this.engineOn();
						}
					}else{
						this.engineOff();
						float targetAngle=(float)Math.toDegrees(calculateAngle(this.getTargetFirst()));
						this.directRotate(targetAngle);
						
					}	
				}else{
					float targetAngle=(float)Math.toDegrees(calculateAngle(this.getTargetFirst()));
					this.directRotate(targetAngle);
					if(this.isTargetFirstAiming()){
						this.engineOff();
					}else{						
						float deltaAngle=Math.abs(this.calculateDeltaAngle(targetAngle));
						targetDeltaDistance=calculateDeltaDistance(this.getTargetFirst());
						if(targetDeltaDistance>ENGINE_TARGET_DISTANCE){
							if((NOENGINE_ANGLE_MIN<deltaAngle&&NOENGINE_DISTANCE_MIN>targetDeltaDistance)||NOENGINE_ONLY_ANGLE_MIN<deltaAngle){
								this.engineOff();
							}else{
								this.engineOn();
							}
						}else{
							this.engineOff();
							this.turnLeftOff();
							this.turnRightOff();
						}
					}
				}
				this.setMoveAngle(getAngle());
			}else{
				this.engineOff();
			}
			break;
		case MOVE_MODE_MANOUVER:
			if(this.getTargetFirst()!=null){//jeœli mamy przynajmnie jeden target
				float moveAngle;
				if(this.getTargetSecond()!=null){
					this.directRotate(this.getTargetFirst());
					float targetDeltaDistance=calculateDeltaDistance(this.getTargetSecond());
					if(targetDeltaDistance>ENGINE_TARGET_DISTANCE){
						moveAngle=(float)Math.toDegrees(this.calculateAngle(this.getTargetSecond()));
						this.setMoveAngle(moveAngle);
						this.engineOn();
					}else{
						this.engineOff();
					}
				}else{ 
					moveAngle=(float)Math.toDegrees(this.calculateAngle(this.getTargetFirst()));
					
					if(this.isTargetFirstAiming()){//mamy jeden target, ale on celuje a my nie chcemy wbiæ siê w celowany obiekt
						this.engineOff();
						this.directRotate(this.getTargetFirst());
						this.setMoveAngle(moveAngle);
					}else{//mamy jeden target który wskazuje woln¹ przestrzeñ, w³aczamy silniki i lecimy
						float targetDeltaDistance=calculateDeltaDistance(this.getTargetFirst());
						if(targetDeltaDistance>ENGINE_TARGET_DISTANCE){
							this.engineOn();
							this.directRotate(this.getTargetFirst());
							this.setMoveAngle(moveAngle);
						}else{
							this.engineOff();
							this.turnLeftOff();
							this.turnRightOff();
						}
					}
				}	
			}else{//nie ma targetów wy³¹czamy silnik
				this.engineOff();
			}
			break;			
		}
	}
	/**
	 * Wywo³ywany powinien byæ 1 raz na sec
	 */
	public void calculate(float delta){
		calculateHitColor(delta);
		calculateTimeResistance(delta);	
		_engineOffTime-=delta;
		if(_engineOffTime<0)_engineOffTime=0;

		if(_isEngineOn&&_engineOffTime==0){
			float angleRadians = (float)-Math.toRadians(getMoveAngle());
			_velocity.x = (float)Math.sin(angleRadians)*_maxSpeed;
			_velocity.y = (float)Math.cos(angleRadians)*_maxSpeed;
		}else{
		//	if(this.getDebugName().equals("UserShip"))
		//				System.out.println("calculate engineOff");
				_velocity.scl(0.93f);

		}
		float rotationStep=0;
		if(isTurnLeftOn()||isTurnRightOn()){
			rotationStep=getParams().rotation*delta;
			if(getDestinationAngle()>0&&rotationStep>getDestinationAngle()){
				rotationStep=getDestinationAngle();
			}
		}
		if(isTurnLeftOn()){
			addAngle(rotationStep);
		}
		if(isTurnRightOn()){
			addAngle(-rotationStep);
		}			
		setDestinationAngle(0);	
		getPosition().add(_velocity.cpy().scl(delta));         
	}
	public void commitPosition(){
		if(_isBackposition==false){
			_oldPosition=getPosition().cpy();
			this.setPosition(getPositionX(),getPositionY());
		}else{
			_isBackposition=false;
			rollbackPosition();
		}
	}	
	public void teleport(Vector2 position){
		setPosition(position.cpy());
		_oldPosition=getPosition().cpy();
		this.setPosition(getPositionX(),getPositionY());
	}	
	public void rollbackPosition(){
		setPosition(_oldPosition.cpy());
	}	
	public void rollbackPositionX(){
		getPosition().x=_oldPosition.x;
	}
	public void rollbackPositionY(){
		getPosition().y=_oldPosition.y;
	}	
	/**
	 * Przekazuje info o wektorze w którym zmienie ulega sk³adowa x. 
	 * Jeœli taka pozycja bêdzie dobra bêdzie mo¿na tak spreparowaæ wektor
	 * @return
	 */
	public Vector2 estimatePositionX(){
		_temporaryPosition.x=getPosition().x;
		_temporaryPosition.y=_oldPosition.y;
		return _temporaryPosition;
	}
	public Vector2 estimatePositionY(){
		_temporaryPosition.y=getPosition().y;
		_temporaryPosition.x=_oldPosition.x;
		return _temporaryPosition;
	}			
	private class BackfireData{
		public Sprite image;
		public float offsetX;
		public float offsetY;
	}	
}

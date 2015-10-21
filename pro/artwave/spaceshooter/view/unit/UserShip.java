package pro.artwave.spaceshooter.view.unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import pro.artwave.spaceshooter.helper.UnitParams;
import pro.artwave.spaceshooter.manager.BulletPool;
import pro.artwave.spaceshooter.manager.EffectAnimationPool;
import pro.artwave.spaceshooter.view.manipulator.ActivePathArea;
import pro.artwave.spaceshooter.view.manipulator.InterfaceFireController;
import pro.artwave.spaceshooter.view.manipulator.ActivePathArea.Target;
import pro.artwave.spaceshooter.view.root.game.GameMap;

public class UserShip extends Ship {
	//private float angleOffset=3;
	private ActivePathArea _manipulator;
	private InterfaceFireController _fireController;
	private int _lastWeapon;
	private EffectAnimationPool _explosionPool;
	private boolean _disableShip;
	private boolean _destroyShip;
	private float _endTime; 
	private GameMap _map;
	
	/**
	 * Oznacza ¿e statek strzela,
	 * Mo¿emy tak ³atwo sprawdziæ moment zaraz po strzale
	 */
	private boolean _isFire;
	public void init(ActivePathArea manipulator,int warGroupId,UnitParams params,float startX,float startY,float startRotation, EffectAnimationPool explosionPool){
		super.init(warGroupId, params,startX, startY, startRotation);
		_isFire=false;
		_manipulator=manipulator;
		_disableShip=false;
		_lastWeapon=1;
		_endTime=0;
		_destroyShip=false;
		_explosionPool=explosionPool;
	//	this.setShieldColor(0xff0000ff);
		setResistanceTime(1.5f);
		this.setFireVolume(1);
	}
	/**
	 * Dodajemy mapê na której znajduje siê user
	 */
	public void setMap(GameMap map){
		_map=map;
	}
	public void setFireControler(InterfaceFireController fireController){
		_fireController=fireController;
	}
	public boolean keyDown(int keycode){
		if(_disableShip)return false;
		switch(keycode){
			case Input.Keys.LEFT:		
				this.startLeftEngine();
				return true;
			case Input.Keys.RIGHT:		
				this.startRightEngine();
				return true;	
			case Input.Keys.UP:		
				this.startEngine();
				return true;	
			case Input.Keys.NUM_1:	
				System.out.println("down");
				_fireController.setFire(1);
				_isFire=true;
			//	this.engineOff();//gdy strza³ z klawiatury to blokujemy silniki
				return true;	
			case Input.Keys.NUM_2:		
				_fireController.setFire(2);
				_isFire=true;
			//	this.engineOff();//gdy strza³ z klawiatury to blokujemy silniki
				return true;
			case Input.Keys.NUM_3:		
				_fireController.setFire(3);
				_isFire=true;
		//		this.engineOff();//gdy strza³ z klawiatury to blokujemy silniki
				return true;				
		}
		return false;
	}
	public boolean keyUp(int keycode){
		if(_disableShip)return false;
		switch(keycode){
			case Input.Keys.LEFT:		
				this.stopLeftEngine();
				return true;	
			case Input.Keys.RIGHT:		
				this.stopRightEngine();
				return true;		
			case Input.Keys.UP:		
				this.stopEngine();
				return true;	
			case Input.Keys.NUM_1:	
				System.out.println("up");
				_isFire=false;
	//			_fireController.setOffFire();
				clearFirstTarget();
				return true;	
			case Input.Keys.NUM_2:	
				_isFire=false;
	//			_fireController.setOffFire();
				clearFirstTarget();
				return true;
			case Input.Keys.NUM_3:	
				_isFire=false;
	//			_fireController.setOffFire();
				clearFirstTarget();
				return true;					
		}
		return false;
	}	
	public void clearFirstTarget(){
		_manipulator.clearPosition();
	}
	private static final int TARGET_DISTANCE=50;
	public void control(float delta){
		if(_disableShip)return;
		boolean isCursorOnEnemy=false;	
		if(_manipulator.getTargetSize()==2){
		//	this.setMoveMode(Turret.MOVE_MODE_MANOUVER);
			Vector2 v1=_manipulator.getTarget(0);
			Vector2 v2=_manipulator.getTarget(1);
			if(
				v1.x+TARGET_DISTANCE>v2.x
				&&v1.x-TARGET_DISTANCE<v2.x
				&&v1.y+TARGET_DISTANCE>v2.y
				&&v1.y-TARGET_DISTANCE<v2.y
			){
		//		System.out.println("a1");
				this.setTargetFirst(v1,_manipulator.isAiming());
		//		this.clearTargetFirst();
				this.clearTargetSecond();
			}else{			
		//		System.out.println("a2");
				this.setTargetFirst(v1);
				this.setTargetSecond(v2);
			}


			if(_manipulator.isAiming()){
	//			System.out.println("a3");
				isCursorOnEnemy=true;
			}
		}else if(_manipulator.getTargetSize()==1){
	//		this.setMoveMode(Turret.MOVE_MODE_DIRECTIONAL);
			this.setTargetFirst(_manipulator.getTarget(0),_manipulator.isAiming());
			this.clearTargetSecond();
			if(_manipulator.isAiming()){
				isCursorOnEnemy=true;
			}
		}else{
			this.clearTargetFirst();
			this.clearTargetSecond();
		}
		if(_fireController!=null){		
			
			int fireWeaponUse=_fireController.getFire();
			
				if(fireWeaponUse>0){
					_isFire=true;
					if(_lastWeapon!=fireWeaponUse){
						this.fireWeaponOff(_lastWeapon);
						_lastWeapon=fireWeaponUse;
						
						this.fireWeaponOn(_lastWeapon,false);
					}else{
						this.fireWeaponOn(_lastWeapon,false);
					}					
				}else{
					if(isCursorOnEnemy==false){
						if(_isFire){//moment po przerwaniu strza³u
							clearFirstTarget();
						}
						_isFire=false;
						this.fireWeaponOff(_lastWeapon);
					}else{
					//		System.out.println("kursor jest na wrogu");
						this.fireWeaponOn(_lastWeapon,false);
					}
				}
		}
		super.control();
	}
	@Override
	public void resetWeapon(float delta){
		super.resetWeapon(delta);
		_fireController.setReady(1,this.getWeaponReadyProgress(1));
		_fireController.setReady(2,this.getWeaponReadyProgress(2));
	}	
	@Override
	public boolean calculateWeapon(BulletPool bulletPool){
		if(ActivePathArea.IS_AUTOFIRE){
			return super.calculateWeapon(bulletPool);
		}else{
			boolean result=false;
			if(_manipulator.fireEnemy()){
				result=super.calculateWeapon(bulletPool);
			}
			else if(_fireController.fireEnemy()){
				result=super.calculateWeapon(bulletPool);
			}
			_manipulator.resetFireEnemy();
			_fireController.resetFireEnemy();
			return result;
		}
		
	}
	@Override
	public void calculate(float delta){
	//	System.out.println("userShip calculate "+this.estimatePosition());
		float targetDeltaDistance=1;
		if(this.getTargetSecond()!=null){
			targetDeltaDistance=calculateDeltaDistance(this.getTargetSecond());
		}else if(this.getTargetFirst()!=null){ 
			targetDeltaDistance=calculateDeltaDistance(this.getTargetFirst());
		}
		targetDeltaDistance=targetDeltaDistance/150;
		if(targetDeltaDistance<0.7f)targetDeltaDistance=0.7f;
		if(targetDeltaDistance>1.3f)targetDeltaDistance=1.3f;
		this.setDistanceSpeedFactor(targetDeltaDistance);
		super.calculate(delta);
	}	
	@Override
	public void rollbackPosition(){
		super.rollbackPosition();
	}
	@Override
	public void addHit(int damage,int extraShield,int extraArmor){
		try{
			Gdx.input.vibrate(200);
		}catch(RuntimeException e){
			
		}
		super.addHit(damage,extraShield,extraArmor);
	}	
	/**
	 * Obliczamy czy statek zosta³ zniszczony, jeœli zosta³ to zwraca true, w przeciwnym razie false;
	 * Tylko za pierwszym razem gdy statek zostanie zniszczony zwraca true, potem ju¿ false
	 * @param gameTime
	 * @param _map
	 * @return
	 */
	public boolean calculateShipDestroy(float gameTime){
		if(_disableShip==false){
			if(this.getHealth()<=0){
				_endTime=gameTime;
		//		System.out.println("game end :"+gameTime);
				this.setVisible(false);
				_disableShip=true;
				_map.destroyUser();
				_explosionPool.newExplosion(EffectAnimationPool.TYPE_EXPLOSION,(int)this.getPosition().x,(int)this.getPosition().y);
				this.stopLeftEngine();
				this.stopRightEngine();
				this.stopEngine();
				this.fireWeaponOff(_lastWeapon);
				_manipulator.clearPosition();
				_manipulator.clearPosition();					
				_manipulator.setVisible(false);

				if(_fireController!=null){
					_fireController.setVisible(false);
				}
				return true;
			}
		}else{
			/**
			 * Po 3 sekundach w³¹czy siê popup informuj¹cy o koñcu gry
			 */
			if(_endTime+3<gameTime){
				_destroyShip=true;
			}
		}
		return false;
	}	
	@Override
	public void teleport(Vector2 position){
		super.teleport(position);
		this.stopLeftEngine();
		this.stopRightEngine();
		this.stopEngine();
		this.fireWeaponOff(_lastWeapon);
		_manipulator.clearPosition();
		_manipulator.clearPosition();					

	}
	/**
	 * metodê wykonujemy gdy koniec gry ale gracz nie zgin¹³
	 * Musi byæ niewidzialny i nieaktywny
	 */
	public void setSuccess(){
	//	_destroyShip=true;
		_disableShip=true;
		this.stopLeftEngine();
		this.stopRightEngine();
		this.stopEngine();	
		this.fireWeaponOff(_lastWeapon);
		this.setVisible(false);
		_manipulator.setVisible(false);
		_manipulator.clearPosition();
		_manipulator.clearPosition();

		if(_fireController!=null){
			_fireController.setVisible(false);
		}		
	}
	public boolean isDisable(){
		return _disableShip;
	}	
	public boolean isDestroy(){
		return _destroyShip;
	}
}

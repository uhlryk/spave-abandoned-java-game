package pro.artwave.spaceshooter.manager;

import java.util.ArrayList;
import pro.artwave.fgm.utility.SimpleStorage;
import pro.artwave.spaceshooter.model.map.GameMapModel;
import pro.artwave.spaceshooter.model.resource.ConstructionResource;
import pro.artwave.spaceshooter.model.resource.MissionResource;
import pro.artwave.spaceshooter.model.resource.MissionResource.Enemy.IdType;
import pro.artwave.spaceshooter.model.resource.ShipResource;
import pro.artwave.spaceshooter.model.resource.TurretResource;
import pro.artwave.spaceshooter.model.resource.WeaponResource;
import pro.artwave.spaceshooter.view.helper.BoardMarker;
import pro.artwave.spaceshooter.view.manipulator.ActivePathArea;
import pro.artwave.spaceshooter.view.root.game.GameMap;
import pro.artwave.spaceshooter.view.tile.TileBoardIsometric;
import pro.artwave.spaceshooter.view.unit.Construction;
import pro.artwave.spaceshooter.view.unit.Ship;
import pro.artwave.spaceshooter.view.unit.Turret;
import pro.artwave.spaceshooter.view.unit.UserShip;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Klasa odpowiada za rysowanie przeciwników, odpowiada za ich pool
 * odpowiada za wywo³anie ich calculate;
 * Klasa ta równie¿ steruje statkami przeciwnika
 * @author Krzysztof
 *
 */
public class EnemyPool{	
	public static final String TYPE_CONSTRUCTION="construction";
	public static final String TYPE_TURRET="turret";
	public static final String TYPE_SHIP="ship";
	
	public static final int GROUP_FRIENDLY=1;
	public static final int GROUP_ENEMY=2;
	
	public static final int DEFAULT_NERF=50;
	
	private static final int DEFAULT_ID=0xff0000ff;	
	
	public static final int RADIUS_MAX_RESPAWN=700;
	public static final int RADIUS_MIN_RESPAWN=400;
	public static final int RADIUS_HIDE=800;
	public static final int RESPAWN_SPEED=10;
	
	public static final int SHIP_LIMIT=10;
	public static final int LIMIT_TURRET=20;
	public static final int LIMIT_CONSTRUCTION=10;
	
	public static final int RADIUS_CURSOR_ENEMY=40;
	
	private SimpleStorage<Respawn> _respawnList;
	private PoolShipElement[] _poolShip;
	private PoolTurretElement[] _poolTurret;
	private PoolConstructionElement[] _poolConstruction;

	private ShipResource _shipResource;
	private TurretResource _turretResource;
	private ConstructionResource _constructionResource;
	private WeaponResource _weaponResource;
	//private GameAssetAtlas _gameAssetAtlas;
	private Pixmap _map;
	private GameMap _gameMap;
	private ActivePathArea _cursorManipulator;
	private SimpleStorage<MapEnemy> _mapEnemyList;
	
	private ViewGroup _view;
	private ArrayList<EnemyListener> _listenerList;
	
	private BoardMarker _boardMarker;
	
	public void init(int difficulty,ConstructionResource constructionResource,TurretResource turretResource,ShipResource shipResource,WeaponResource weaponResource,ArrayList<MissionResource.Enemy> gameEnemyList,Pixmap map){
		_view=new ViewGroup();
		_view.init();
		_listenerList=new ArrayList<EnemyListener>();
		_boardMarker=new BoardMarker();
		_boardMarker.init();
		_mapEnemyList=new SimpleStorage<MapEnemy>(gameEnemyList.size()+1);
		for(MissionResource.Enemy resourceEnemy:gameEnemyList){
			MapEnemy enemy=new MapEnemy();
			enemy.init(resourceEnemy.id,resourceEnemy.type,resourceEnemy.warGroup,resourceEnemy.nerf*difficulty,resourceEnemy.respawn,resourceEnemy.idTypeList);
			enemy.setResistance(resourceEnemy.resistance);
			enemy.setRotation(resourceEnemy.rotation);
			_mapEnemyList.add(resourceEnemy.id,enemy);
		}
		if(_mapEnemyList.get(DEFAULT_ID)==null){
			MapEnemy enemy=new MapEnemy();
			ArrayList<IdType> idTypeList=new ArrayList<IdType>(1);
			IdType idType=new IdType();
			idType.id=1;
			idType.priority=3;
			idType.weaponId=1;
			idTypeList.add(idType);
			idType=new IdType();
			idType.id=2;
			idType.priority=2;
			idType.weaponId=2;
			idTypeList.add(idType);		
			idType=new IdType();
			idType.id=3;
			idType.priority=1;
			idType.weaponId=3;
			idTypeList.add(idType);				
			enemy.init(DEFAULT_ID,TYPE_SHIP,GROUP_ENEMY,DEFAULT_NERF*difficulty,0,idTypeList);
			_mapEnemyList.add(DEFAULT_ID,enemy);
		}	
		_map=map;
		

		_poolTurret=new PoolTurretElement[LIMIT_TURRET];
		for(int i=0;i<LIMIT_TURRET;i++){
			Turret turret=new Turret();
			PoolTurretElement poolTurretElement=new PoolTurretElement();
			poolTurretElement.turret=turret;
			poolTurretElement.isActive=false;
			_poolTurret[i]=poolTurretElement;
			turret.setVisible(false);
			_view.addActor(turret);
		}
		_poolConstruction=new PoolConstructionElement[LIMIT_CONSTRUCTION];
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			Construction construction=new Construction();
			PoolConstructionElement poolConstructionElement=new PoolConstructionElement();
			poolConstructionElement.construction=construction;
			poolConstructionElement.isActive=false;
			_poolConstruction[i]=poolConstructionElement;
			construction.setVisible(false);
			_view.addActor(construction);
		}
		_poolShip=new PoolShipElement[SHIP_LIMIT];
		for(int i=0;i<SHIP_LIMIT;i++){
			Ship ship=new Ship();
			PoolShipElement shipPoolElement=new PoolShipElement();
			shipPoolElement.ship=ship;
			shipPoolElement.isActive=false;
			_poolShip[i]=shipPoolElement;
			ship.setVisible(false);
			_view.addActor(ship);
		}
		_shipResource=shipResource;
		_turretResource=turretResource;
		_constructionResource=constructionResource;
		_weaponResource=weaponResource;
	//	_gameAssetAtlas=new GameAssetAtlas();
	//	_respawnList=new SimpleStorage<Respawn>(160);
		this.generate();
	}
	/**
	 * Pozwala dodaæ do obiektu wskaŸnik na obiekt odpowiadaj¹cy za kursor na mapie i okreœlanie pozycji
	 * Mo¿emy sprawdziæ lokalizacjê kursora i czy wskazuje to na statek wroga. Jeœli tak to
	 * przekazuje do kursora informacjê, a kursor mo¿e zmieniæ wygl¹d i poinformowaæ statek gracza
	 * by siê zatrzyma³ i strzela³
	 * @param cursorManipulator
	 */
	public void setCursorManipulator(ActivePathArea cursorManipulator){
		_cursorManipulator=cursorManipulator;
	}
	public void regenerateShield(float delta){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				poolShipElement.ship.regenerateShield(delta);
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(poolTurretElement.isActive==true){
				poolTurretElement.turret.regenerateShield(delta);
			}
		}	
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			PoolConstructionElement poolConstructionElement=_poolConstruction[i];
			if(poolConstructionElement.isActive==true){
				poolConstructionElement.construction.regenerateShield(delta);
			}
		}		
	}
	public void setMap(GameMap map){
		_gameMap=map;
	}
	public void addShip(int nerfFactor,int warGroup,int respawnId,int typeId,int weaponId,float posX,float posY,int rotation,boolean resistance){
	//	System.out.println("AddShip");
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement shipPoolElement=_poolShip[i];
			if(!shipPoolElement.isActive){
		//		System.out.println("AddShip create");
				shipPoolElement.isActive=true;
				shipPoolElement.respawnId=respawnId;
				Ship ship=shipPoolElement.ship;
				System.out.println("create shipid nerf "+nerfFactor);
				ship.init(warGroup,_shipResource.getShip(typeId),posX,posY,rotation);
				ship.setResistance(resistance);
			//	ship.setShieldColor(0x00ff00ff);
				ship.createImage();
				ship.setBonusDamage(nerfFactor);
				ship.setBonusHealth(nerfFactor);
				ship.setBonusShield(nerfFactor);
				ship.setBonusSpeed(nerfFactor);
				ship.setWeapon(1,_weaponResource.getWeapon(weaponId));
				ship.setVisible(true);
				ship.setDebugName("Ship"+respawnId);
				ship.setMoveMode(Turret.MOVE_MODE_MANOUVER);
				
				break;
			}
		}
	}
	public void addTurret(int nerfFactor,int warGroup,int respawnId,int typeId,int weaponId,float posX,float posY,int rotation,boolean resistance){
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(!poolTurretElement.isActive){
				poolTurretElement.isActive=true;
				poolTurretElement.respawnId=respawnId;
				Turret turret=poolTurretElement.turret;
			//	System.out.println("create shipid "+shipId);
				rotation=270;
				turret.init(warGroup,_turretResource.getTurret(typeId),posX,posY,rotation);
				turret.setResistance(resistance);
		//		turret.setShieldColor(0x00ff00ff);
				turret.createImage();
				turret.setBonusDamage(nerfFactor);
				turret.setBonusHealth(nerfFactor);
				turret.setBonusShield(nerfFactor);
				turret.setWeapon(1,_weaponResource.getWeapon(weaponId));
				turret.setVisible(true);
				turret.setDebugName("Turret"+respawnId);
				turret.setMoveMode(Turret.MOVE_MODE_DIRECTIONAL);
				break;
			}
		}
	}	
	public void addConstruction(int nerfFactor,int warGroup,int respawnId,int typeId,float posX,float posY,int rotation,boolean resistance){
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			PoolConstructionElement poolConstructionElement=_poolConstruction[i];
			if(!poolConstructionElement.isActive){
				poolConstructionElement.isActive=true;
				poolConstructionElement.respawnId=respawnId;
				Construction construction=poolConstructionElement.construction;
				construction.init(warGroup,_constructionResource.getConstruction(typeId),posX,posY,rotation);
				construction.setResistance(resistance);
		//		construction.setShieldColor(0x00ff00ff);
				construction.createImage();
				construction.setBonusHealth(nerfFactor);
				construction.setBonusShield(nerfFactor);
				construction.setVisible(true);
				break;
			}
		}
	}		
	/**
	 * Metoda oblicza czy w pozycji kursora znajduje siê statek wroga.
	 * Metoda w³¹cza to i wyl¹cza przy ponownym sprawdzeniu. Nie musi test byæ za ka¿d¹ klatk¹
	 * 
	 * mo¿e byæ raz na 1s.
	 */
	public void calculateCursorPosition(){
		boolean isTarget=false;
		if(_cursorManipulator.isAiming()){
			if(_cursorManipulator.getTargetSize()==2){
				/**
				 * Jeœli zakomentujemy to, to nie mo¿na zmieniæ celu na inny poprzez najechanie mysz¹
				 * ale jeœli klikniemy na statku wroga bez draggowania, to nie powinien ustawiæ firstTarget na cel
				 * a secondtarget na move
				 * czyli sprawdzamy czy mamy aktywne draggowanie ActivePathArea. jeœli nie, wtedy by³o tylko klikniêcie
				 * i nale¿y sprawdziæ czy nowy target wskazuje na wroga, jeœli tak, to ustawiamy ¿e nowy wróg jest 
				 * targetem, a ruch jest kasowany
				 */
				isTarget=calculateSingleCursorPosition(_cursorManipulator.getSecondTarget(),false);//sprawdza czy jest nowy target i jeœli tak to daje tam kursor
						
				if(_cursorManipulator.isDragging()==false&&isTarget==true){
					_cursorManipulator.clearSecondTarget();
				}else if(isTarget==false){
					calculateSingleCursorPosition(_cursorManipulator.getFirstTarget(),true);//aktualizuje pierwszy cel	
				}
			}else{
				isTarget=calculateSingleCursorPosition(_cursorManipulator.getFirstTarget(),true);
			}
		}else{
			isTarget=calculateSingleCursorPosition(_cursorManipulator.getFirstTarget(),true);
		}
		_cursorManipulator.setIsTarget(isTarget);
	}
	/**
	 * Jeœli celujemy tu wskazujemy w jaki cel celujemy, by siê same cele nie przestawia³y
	 * respawnId jest niepowtarzalny
	 */
	private int _aimRespawnId;;
	/**
	 * True jeœli mamy celowanie
	 * @param cursorPosition
	 * @param canReset
	 * @return
	 */
	private boolean calculateSingleCursorPosition(Vector2 cursorPosition,boolean canReset){
		if(cursorPosition==null)return false;
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			/**
			 * warunek sprawdza czy dany statek jest aktywny, bo tylko taki mo¿e byæ celem
			 * nastêpnie czy mamy ju¿ ustawiony cel na jakis element jeœli _aimRespawnId==0 znaczy ¿e nie mamy
			 * wiêc mo¿emy ustawiaæ cel, alternatyw¹ jest ¿e mamy ustawiony na cel ten statek, a wiêc
			 * wchodzimy by updatowaæ pozycjê na statku, trzecia alternatywa to mamy second target, czyli
			 * tym targetem user ustawia cel, a wiêc wtedy te¿ wchodzimy w cel i ustawiamy nowy
			 */
			if(poolShipElement.isActive&&(_aimRespawnId==0||_aimRespawnId==poolShipElement.respawnId||canReset==false)){
				if(poolShipElement.ship.calculateObjectCollision(cursorPosition,RADIUS_CURSOR_ENEMY)==false){
					/**
					 * Jeœli mamy w³¹czaæ celowanie ondragged
					 */
					if(ActivePathArea.CAN_AIM_ON_DRAGG==true){
					 	_cursorManipulator.setFirstTarget(poolShipElement.ship.getPosition());
						_aimRespawnId=poolShipElement.respawnId;
						_cursorManipulator.setIsBlocadeEnemy(true);
					}else{
						/**
						 * Jeœli celeowanie tylko przez klikniêcie
						 */
						if(_cursorManipulator.isDragging()==false){
							_cursorManipulator.setFirstTarget(poolShipElement.ship.getPosition());
							_aimRespawnId=poolShipElement.respawnId;
							_cursorManipulator.setIsBlocadeEnemy(true);
						}
					}
					return true;
				}
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(poolTurretElement.isActive&&(_aimRespawnId==0||_aimRespawnId==poolTurretElement.respawnId||canReset==false)){
				if(poolTurretElement.turret.calculateObjectCollision(cursorPosition,RADIUS_CURSOR_ENEMY)==false){	
					/**
					 * Jeœli mamy w³¹czaæ celowanie ondragged
					 */
					if(ActivePathArea.CAN_AIM_ON_DRAGG==true){
						_cursorManipulator.setFirstTarget(poolTurretElement.turret.getPosition());
						_aimRespawnId=poolTurretElement.respawnId;	
						_cursorManipulator.setIsBlocadeEnemy(true);					
					}else{
						/**
						 * Jeœli celeowanie tylko przez klikniêcie
						 */					
						if(_cursorManipulator.isDragging()==false){	
							_cursorManipulator.setFirstTarget(poolTurretElement.turret.getPosition());
							_aimRespawnId=poolTurretElement.respawnId;	
							_cursorManipulator.setIsBlocadeEnemy(true);
						}
					}
					return true;
				}
			}
		}	
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			PoolConstructionElement poolConstructionElement=_poolConstruction[i];
			if(poolConstructionElement.isActive&&(_aimRespawnId==0||_aimRespawnId==poolConstructionElement.respawnId||canReset==false)){
				if(poolConstructionElement.construction.calculateObjectCollision(cursorPosition,RADIUS_CURSOR_ENEMY)==false){
					/**
					 * Jeœli mamy w³¹czaæ celowanie ondragged
					 */
					if(ActivePathArea.CAN_AIM_ON_DRAGG==true){	
						_cursorManipulator.setFirstTarget(poolConstructionElement.construction.getPosition());					
						_aimRespawnId=poolConstructionElement.respawnId;		
						_cursorManipulator.setIsBlocadeEnemy(true);
					}else{
						/**
						 * Jeœli celeowanie tylko przez klikniêcie
						 */					
						if(_cursorManipulator.isDragging()==false){
							_cursorManipulator.setFirstTarget(poolConstructionElement.construction.getPosition());					
							_aimRespawnId=poolConstructionElement.respawnId;		
							_cursorManipulator.setIsBlocadeEnemy(true);
						}
					}
					return true;
				}
			}
		}		
		if(canReset){
			_aimRespawnId=0;
			_cursorManipulator.setIsBlocadeEnemy(false);
		}
		return false;
	}
	/**
	 * Odpowiada za rysowanie cieni na statkach
	 */
	public void calculateShadow(ShadowPool shadow){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				Vector2 pos=poolShipElement.ship.getPosition();
				float angle=poolShipElement.ship.getAngle();
				shadow.addShadow(pos.x,pos.y,angle,poolShipElement.ship.getCurrentAlpha());
			}
		}
	}
	public void calculateMapPosition(){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				Respawn respawn=_respawnList.get(poolShipElement.respawnId);
			//	MapEnemy mapEnemy=respawn.mapEnemy;
				if(respawn.isMarked){
					_gameMap.calculateEnemyPosition(poolShipElement.respawnId,poolShipElement.ship.getPositionX(),poolShipElement.ship.getPositionY(),true);
				}else{
					_gameMap.calculateEnemyPosition(poolShipElement.respawnId,poolShipElement.ship.getPositionX(),poolShipElement.ship.getPositionY(),false);
				}
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(poolTurretElement.isActive==true){
				Respawn respawn=_respawnList.get(poolTurretElement.respawnId);
		//		MapEnemy mapEnemy=respawn.mapEnemy;
				if(respawn.isMarked){				
					_gameMap.calculateEnemyPosition(poolTurretElement.respawnId,poolTurretElement.turret.getPositionX(),poolTurretElement.turret.getPositionY(),true);
				}else{
					_gameMap.calculateEnemyPosition(poolTurretElement.respawnId,poolTurretElement.turret.getPositionX(),poolTurretElement.turret.getPositionY(),false);
				}
			}
		}	
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			PoolConstructionElement poolConstructionElement=_poolConstruction[i];
			if(poolConstructionElement.isActive==true){
				Respawn respawn=_respawnList.get(poolConstructionElement.respawnId);
			//	MapEnemy mapEnemy=respawn.mapEnemy;
				if(respawn.isMarked){					
					_gameMap.calculateEnemyPosition(poolConstructionElement.respawnId,poolConstructionElement.construction.getPositionX(),poolConstructionElement.construction.getPositionY(),true);
				}else{
					_gameMap.calculateEnemyPosition(poolConstructionElement.respawnId,poolConstructionElement.construction.getPositionX(),poolConstructionElement.construction.getPositionY(),true);
				}
			}
		}		
	}
	public void calculateTileCollision(TileBoardIsometric tileBoard){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				if(tileBoard.checkPosition(poolShipElement.ship.getPositionX(),poolShipElement.ship.getPositionY(),poolShipElement.ship.getRadius())==false){
				//	poolShipElement.ship.setBackSpeed(2);
					Vector2 pos=poolShipElement.ship.estimatePositionX();
					if(tileBoard.checkPosition(pos.x,pos.y,poolShipElement.ship.getRadius())==false){
						poolShipElement.ship.rollbackPositionX();
					}
					pos=poolShipElement.ship.estimatePositionY();
					if(tileBoard.checkPosition(pos.x,pos.y,poolShipElement.ship.getRadius())==false){
						poolShipElement.ship.rollbackPositionY();
					}				
				}
			}
		}
	}	
	/**
	 * Sprawdzamy czy statki wroga s¹ odpowiednio daleko od statku gracza. Wtedy mo¿emy je usun¹æ.
	 * @param userShipPosition
	 */
	public void calculateHideEnemy(Vector2 userShipPosition){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				Vector2 enemyPosition=poolShipElement.ship.getPosition();
				if(enemyPosition.x<userShipPosition.x-RADIUS_HIDE||enemyPosition.x>userShipPosition.x+RADIUS_HIDE||
					enemyPosition.y<userShipPosition.y-RADIUS_HIDE||enemyPosition.y>userShipPosition.y+RADIUS_HIDE){
					poolShipElement.isActive=false;
					Respawn respawn=_respawnList.get(poolShipElement.respawnId);
					respawn.isActive=false;
					respawn.time=0;
				//	System.out.println("ship is cleared");
					poolShipElement.ship.setVisible(false);
					_gameMap.calculateEnemyDestroy(poolShipElement.respawnId);
				}
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(poolTurretElement.isActive==true){
				Vector2 enemyPosition=poolTurretElement.turret.getPosition();
				if(enemyPosition.x<userShipPosition.x-RADIUS_HIDE||enemyPosition.x>userShipPosition.x+RADIUS_HIDE||
					enemyPosition.y<userShipPosition.y-RADIUS_HIDE||enemyPosition.y>userShipPosition.y+RADIUS_HIDE){
					poolTurretElement.isActive=false;
					Respawn respawn=_respawnList.get(poolTurretElement.respawnId);
					respawn.isActive=false;
					respawn.time=0;
					poolTurretElement.turret.setVisible(false);
					_gameMap.calculateEnemyDestroy(poolTurretElement.respawnId);
				}
			}
		}
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			PoolConstructionElement poolConstructionElement=_poolConstruction[i];
			if(poolConstructionElement.isActive==true){
				Vector2 enemyPosition=poolConstructionElement.construction.getPosition();
				if(enemyPosition.x<userShipPosition.x-RADIUS_HIDE||enemyPosition.x>userShipPosition.x+RADIUS_HIDE||
					enemyPosition.y<userShipPosition.y-RADIUS_HIDE||enemyPosition.y>userShipPosition.y+RADIUS_HIDE){
					poolConstructionElement.isActive=false;
					Respawn respawn=_respawnList.get(poolConstructionElement.respawnId);
					respawn.isActive=false;
					respawn.time=0;
					poolConstructionElement.construction.setVisible(false);
					_gameMap.calculateEnemyDestroy(poolConstructionElement.respawnId);
				}
			}
		}		
	}
	/**
	 * Metoda sprawdza który ze statków powinien wybuchn¹æ i w to miejsce wstawia wybuch.
	 */
	public void calculateShipDestroy(float gameTime){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				if(poolShipElement.ship.getHealth()<=0){				
					poolShipElement.isActive=false;
					_gameMap.calculateEnemyDestroy(poolShipElement.respawnId);
					Respawn respawn=_respawnList.get(poolShipElement.respawnId);
					respawn.isActive=false;
					respawn.time=gameTime+RESPAWN_SPEED;
					poolShipElement.ship.setVisible(false);
					for(EnemyListener listener:_listenerList){
						listener.onDestroy(poolShipElement.ship.getPosition(),TYPE_SHIP);
					}
				}
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(poolTurretElement.isActive==true){
				if(poolTurretElement.turret.getHealth()<=0){				
					poolTurretElement.isActive=false;
					_gameMap.calculateEnemyDestroy(poolTurretElement.respawnId);
					Respawn respawn=_respawnList.get(poolTurretElement.respawnId);
					respawn.isActive=false;
					respawn.time=gameTime+RESPAWN_SPEED;
					poolTurretElement.turret.setVisible(false);
					for(EnemyListener listener:_listenerList){
						listener.onDestroy(poolTurretElement.turret.getPosition(),TYPE_SHIP);
					}					
				}
			}
		}
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			PoolConstructionElement poolConstructionElement=_poolConstruction[i];
			if(poolConstructionElement.isActive==true){
				if(poolConstructionElement.construction.getHealth()<=0){				
					poolConstructionElement.isActive=false;
					_gameMap.calculateEnemyDestroy(poolConstructionElement.respawnId);
					Respawn respawn=_respawnList.get(poolConstructionElement.respawnId);
					respawn.isActive=false;
					respawn.time=gameTime+RESPAWN_SPEED;
					poolConstructionElement.construction.setVisible(false);
					for(EnemyListener listener:_listenerList){
						listener.onDestroy(poolConstructionElement.construction.getPosition(),TYPE_SHIP);
					}					
				}
			}
		}		
	}
	/**
	 * Metoda wywo³ywana w kontrolerze. Mo¿e byæ 1/60
	 * Metoda dla aktywnych obiektów okreœla targety.
	 * W przypadku turretów tylko wskazuje cel na gracza
	 * W przypadku statków równie¿ kierunek lotu, ale przed kierunkiem lotu musi okreœliæ
	 * czy lot jest kolizyjny. Kierunek ma byæ trochê losowy.
	 * @param userShipPosition
	 */
	public void calculateAI(Vector2 userShipPosition,TileBoardIsometric tileBoard){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				poolShipElement.ship.setTargetFirst(userShipPosition,true);
				if(poolShipElement.ship.isTargetTileCollision(userShipPosition,tileBoard)==false){
					poolShipElement.ship.fireWeaponOn(1,false);
					float distance=poolShipElement.ship.calculateDeltaDistance(userShipPosition);
					/**
					 * Bardziej rozbudowaæ by unika³ pocisków, manewrowa³ na boki
					 */
					if(distance>300){
						poolShipElement.ship.setTargetSecond(userShipPosition);
					}else{
						poolShipElement.ship.clearTargetSecond();
					}
				}else{
					poolShipElement.ship.fireWeaponOff(1);
				}				
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(poolTurretElement.isActive==true){	
				//jak bêdziemy sprawdzaæ przeszkody, to jeœli bêdzie przeszkoda to false
				//a jeœli droga czysta to true.
				//przy true bêdzie strzela³, przy false nie;
				poolTurretElement.turret.setTargetFirst(userShipPosition,true);
				if(poolTurretElement.turret.isTargetTileCollision(userShipPosition,tileBoard)==false){
					poolTurretElement.turret.fireWeaponOn(1,false);
				}else{
					poolTurretElement.turret.fireWeaponOff(1);
				}
			}
		}
	}
	/**
	 * Sprawdzamy czy na drodze gracza w respawn pointach mamy statki wroga. Jeœli tak to je tworzymy.
	 * @param gameTime
	 * @param userShipPosition
	 */
	public void calculateRespawnEnemy(float gameTime,Vector2 userShipPosition,float minRadius){
		int x=(int)userShipPosition.x;
		int y=(int)userShipPosition.y;
		float radiusExcluded=minRadius;
		int x0=(int) Math.floor((x-RADIUS_MAX_RESPAWN)/GameMapModel.TILE_SIZE);
		int x1=(int) Math.floor((x+RADIUS_MAX_RESPAWN)/GameMapModel.TILE_SIZE);
		int y0=(int) Math.floor((y-RADIUS_MAX_RESPAWN)/GameMapModel.TILE_SIZE);
		int y1=(int) Math.floor((y+RADIUS_MAX_RESPAWN)/GameMapModel.TILE_SIZE);		
		
		int xE0=(int) Math.floor((x-radiusExcluded)/GameMapModel.TILE_SIZE);		
		int xE1=(int) Math.floor((x+radiusExcluded)/GameMapModel.TILE_SIZE);
		int yE0=(int) Math.floor((y-radiusExcluded)/GameMapModel.TILE_SIZE);
		int yE1=(int) Math.floor((y+radiusExcluded)/GameMapModel.TILE_SIZE);				
		for(int i=x0;i<=x1;i++){
			for(int j=y0;j<=y1;j++){
				if((i>xE0&&i<xE1)&&(j>yE0&&j<yE1))continue;
				
			//	long millis1 = System.nanoTime();

				int respawnId=getElementId(i,j);
				Respawn respawn=_respawnList.get(respawnId);
				if(respawn==null)continue;
				MapEnemy mapEnemy=respawn.mapEnemy;
				if(mapEnemy._isActive==true){
					if((mapEnemy._respawn>0&&respawn.time<gameTime)||respawn.time==0){//jeœli dane pole ma respawna (jeœli nie ma to siê nic nie utworzy)							
						if(respawn.isActive==false){
							if(mapEnemy._type==TYPE_SHIP){
								addShip(mapEnemy._nerf,mapEnemy._group,respawnId,respawn.typeId,respawn.weaponId,i*GameMapModel.TILE_SIZE+GameMapModel.TILE_SIZE*0.5f,j*GameMapModel.TILE_SIZE+GameMapModel.TILE_SIZE*0.5f,mapEnemy.getRotation(),mapEnemy.getResistance());
							}else if(mapEnemy._type==TYPE_TURRET){
								addTurret(mapEnemy._nerf,mapEnemy._group,respawnId,respawn.typeId,respawn.weaponId,i*GameMapModel.TILE_SIZE+GameMapModel.TILE_SIZE*0.5f,j*GameMapModel.TILE_SIZE+GameMapModel.TILE_SIZE*0.5f,mapEnemy.getRotation(),mapEnemy.getResistance());
							}else if(mapEnemy._type==TYPE_CONSTRUCTION){
								addConstruction(mapEnemy._nerf,mapEnemy._group,respawnId,respawn.typeId,i*GameMapModel.TILE_SIZE+GameMapModel.TILE_SIZE*0.5f,j*GameMapModel.TILE_SIZE+GameMapModel.TILE_SIZE*0.5f,mapEnemy.getRotation(),mapEnemy.getResistance());
							}
							respawn.isActive=true;
						}
					}
				}
				
			//	long millis2 = System.nanoTime();
			//	System.out.println("working enemy "+(millis2-millis1));
			}
		}	
	}
	/**
	 * Dla danego koloru i indexu, otrzymany statek czy by³ zniszczony
	 * Zastosowanie w objectives. Mo¿emy sprawdziæ czy zadanie zniszczenia wroga siê powiod³o.
	 * @param color id koloru identyfikuj¹cego statek
	 * @param indes okreœla o który obiekt nam chodzi. Jeœli mamy zniszczyæ np 4ech przeciwników to mo¿emy
	 * wywo³aæ sprawdzenie 4 razy z ró¿nymi indeksami
	 * @return null jeœli nie ma takiego koloru, false jeœli jest niezniszczony, true jeœli jest zniszczony
	 */
	public Boolean isDestroyed(int color,int index){
		int findCount=0;
		for(int i=0;i<_respawnList.length;i++){
			Respawn respawn=_respawnList.getValByIndex(i);
			
			if(respawn.mapEnemy.getId()==color){
				if(respawn.mapEnemy._isActive==true){
					if(findCount==index){
						if(respawn.isActive==false){//statek nie jest aktywny, ale móg³ nie byæ jeszcze odpalony lub zosta³ zniszczony
							if(respawn.time>0){//jeœli time jest inny ni¿ zero znaczy ¿e by³ kiedyœ zniszczony, a fakt ¿e jest nieaktywny daje nam pewnoœæ ¿e traktujemy go jako zniszcozny
								return true;
							}else{//znaczy ¿e time ma 0 a wiec nie by³ nigdy zniszczony, a ¿e jest nieaktywny wiêc nie by³ odpalany
								return false;
							}
								
						}else{//jest aktywny a wiêc nie mo¿e byæ zniszczony
							return false;
						}
					}
					findCount++;
				}else{//jest nieaktywna ca³a grupa wiêc odpowiada to byciu zniszczonym
					return true;
				}
			}			
		}
		return null;
	}
	/**
	 * Generuje listê respawn zawieraj¹c¹ minimum informacji o mo¿liwych obiektach
	 */
	private void generate(){
		_respawnList=new SimpleStorage<Respawn>(180);
		int maxX=_map.getWidth();
		int maxY=_map.getHeight();
		for(int x=0;x<maxX;x++){
			for(int y=0;y<maxY;y++){
				int mapEnemyId=_map.getPixel(x,_map.getHeight()-y);
				MapEnemy mapEnemy=_mapEnemyList.get(mapEnemyId);
				if(mapEnemy==null)continue;
				
				MissionResource.Enemy.IdType idType=mapEnemy.getIdType();
				Respawn respawn=new Respawn();
				respawn.typeId=idType.id;
				respawn.weaponId=idType.weaponId;
				respawn.mapEnemy=mapEnemy;
				int respawnId=getElementId(x,y);
				_respawnList.add(respawnId, respawn);				
			}
		}
	}
	public void calculate(float delta){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				poolShipElement.ship.control();
				poolShipElement.ship.calculate(delta);
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(poolTurretElement.isActive==true){
				poolTurretElement.turret.control();
				poolTurretElement.turret.calculate(delta);
			}
		}
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			PoolConstructionElement poolConstructionElement=_poolConstruction[i];
			if(poolConstructionElement.isActive==true){
				poolConstructionElement.construction.calculate(delta);
			}
		}		
	}
	public void calculateBulletCollision(BulletPool bulletPool){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				if(poolShipElement.ship.isTimeResistance()==false){
					BulletPool.BulletDamage bulletDamage=bulletPool.calculateCollision(poolShipElement.ship.getWarGroupId(),poolShipElement.ship.getPosition(),poolShipElement.ship.getRadius());
					if(bulletDamage!=null){
						poolShipElement.ship.addHit(bulletDamage.damage,bulletDamage.shieldDamage,bulletDamage.armorDamage);
						for(EnemyListener listener:_listenerList){
							listener.onDamage(poolShipElement.ship.getPosition(),TYPE_SHIP,(bulletDamage.damage+bulletDamage.shieldDamage+bulletDamage.armorDamage),poolShipElement.ship.isResistance());
						}						
					}
				}
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(poolTurretElement.isActive==true){
				if(poolTurretElement.turret.isTimeResistance()==false){
					BulletPool.BulletDamage bulletDamage=bulletPool.calculateCollision(poolTurretElement.turret.getWarGroupId(),poolTurretElement.turret.getPosition(),poolTurretElement.turret.getRadius());
					if(bulletDamage!=null){
						poolTurretElement.turret.addHit(bulletDamage.damage,bulletDamage.shieldDamage,bulletDamage.armorDamage);
						for(EnemyListener listener:_listenerList){
							listener.onDamage(poolTurretElement.turret.getPosition(),TYPE_TURRET,(bulletDamage.damage+bulletDamage.shieldDamage+bulletDamage.armorDamage),poolTurretElement.turret.isResistance());
						}	
					}
				}
			}
		}
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			PoolConstructionElement poolConstructionElement=_poolConstruction[i];
			if(poolConstructionElement.isActive==true){
				if(poolConstructionElement.construction.isTimeResistance()==false){
					BulletPool.BulletDamage bulletDamage=bulletPool.calculateCollision(poolConstructionElement.construction.getWarGroupId(),poolConstructionElement.construction.getPosition(),poolConstructionElement.construction.getRadius());
					if(bulletDamage!=null){
						poolConstructionElement.construction.addHit(bulletDamage.damage,bulletDamage.shieldDamage,bulletDamage.armorDamage);
						for(EnemyListener listener:_listenerList){
							listener.onDamage(poolConstructionElement.construction.getPosition(),TYPE_CONSTRUCTION,(bulletDamage.damage+bulletDamage.shieldDamage+bulletDamage.armorDamage),poolConstructionElement.construction.isResistance());
						}								
					}
				}
			}
		}		
	}
	public void calculateUserShipCollision(UserShip userShip){
		int collisionDamage=20;
		if(userShip.isDisable())return;
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				if(userShip.calculateObjectCollision(poolShipElement.ship.getPosition(),poolShipElement.ship.getRadius())==false){
					float angle=(float) Math.atan2(userShip.getPosition().x-poolShipElement.ship.getPosition().x,userShip.getPosition().y-poolShipElement.ship.getPosition().y);
					userShip.setBackSpeed(160,angle);
					poolShipElement.ship.setBackSpeed(160,(float) (Math.PI+angle));
					poolShipElement.ship.addHit(collisionDamage,0,0);
					userShip.addHit(collisionDamage,0,0);
					for(EnemyListener listener:_listenerList){
						listener.onUserShipCollision(poolShipElement.ship.getPosition(),TYPE_SHIP,collisionDamage,poolShipElement.ship.isResistance());
					}	
				}
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			if(poolTurretElement.isActive==true){
				if(userShip.calculateObjectCollision(poolTurretElement.turret.getPosition(),poolTurretElement.turret.getRadius())==false){
					float angle=(float) Math.atan2(userShip.getPosition().x-poolTurretElement.turret.getPosition().x,userShip.getPosition().y-poolTurretElement.turret.getPosition().y);
					userShip.setBackSpeed(160,angle);
					poolTurretElement.turret.addHit(collisionDamage,0,0);
					userShip.addHit(collisionDamage,0,0);
					for(EnemyListener listener:_listenerList){
						listener.onUserShipCollision(poolTurretElement.turret.getPosition(),TYPE_TURRET,collisionDamage,poolTurretElement.turret.isResistance());
					}	
				}
			}
		}
		for(int i=0;i<LIMIT_CONSTRUCTION;i++){
			PoolConstructionElement poolConstructionElement=_poolConstruction[i];
			if(poolConstructionElement.isActive==true){
				if(userShip.calculateObjectCollision(poolConstructionElement.construction.getPosition(),poolConstructionElement.construction.getRadius())==false){
					float angle=(float) Math.atan2(userShip.getPosition().x-poolConstructionElement.construction.getPosition().x,userShip.getPosition().y-poolConstructionElement.construction.getPosition().y);
					userShip.setBackSpeed(160,angle);	
					poolConstructionElement.construction.addHit(collisionDamage,0,0);
					userShip.addHit(collisionDamage,0,0);
					for(EnemyListener listener:_listenerList){
						listener.onUserShipCollision(poolConstructionElement.construction.getPosition(),TYPE_CONSTRUCTION,collisionDamage,poolConstructionElement.construction.isResistance());
					}	
				}
			}
		}		
	}
	public void commitPosition(){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			if(poolShipElement.isActive==true){
				poolShipElement.ship.commitPosition();
			}
		}
	}
	public void calculateWeapon(float delta,BulletPool bulletPool){
		for(int i=0;i<SHIP_LIMIT;i++){
			PoolShipElement poolShipElement=_poolShip[i];
			Respawn respawn=_respawnList.get(poolShipElement.respawnId);
			
			if(poolShipElement.isActive==true&&respawn.mapEnemy._isFire){
				poolShipElement.ship.resetWeapon(delta);
				poolShipElement.ship.calculateWeapon(bulletPool);	
			}
		}
		for(int i=0;i<LIMIT_TURRET;i++){
			PoolTurretElement poolTurretElement=_poolTurret[i];
			Respawn respawn=_respawnList.get(poolTurretElement.respawnId);
			if(poolTurretElement.isActive==true&&respawn.mapEnemy._isFire){
				poolTurretElement.turret.resetWeapon(delta);
				poolTurretElement.turret.calculateWeapon(bulletPool);	
			}
		}
	}
	/**
	 * Konfiguruje czy obiekty o id color s¹ aktywne czy nie.
	 * Mo¿emy okreœlony statek wy³¹czyæ lub w³¹czyæ do okreœlonego momentu
	 * Komenda ma pewn¹ niekonsekwentnoœæ, bo dzia³a na wszystkie elementy o danym id.
	 * Index nie ma znaczenia
	 * @param color
	 * @param isActive
	 */
	public void setStatus(int color,int index,boolean isActive){
		MapEnemy mapEnemy=_mapEnemyList.get(color);
		mapEnemy.setActive(isActive);
	}
	/**
	 * Pozwala zablokowaæ strzelanie okreœlonym przeciwnikom
	 * Mo¿e siê przydaæ by deaktywowaæ dzia³ka wroga.
	 * 
	 * Komenda ma pewn¹ niekonsekwentnoœæ, bo dzia³a na wszystkie elementy o danym id.
	 * Index nie ma znaczenia
	 * @param color
	 * @param isFire
	 */
	public void setFire(int color,int index,boolean isFire){
		MapEnemy mapEnemy=_mapEnemyList.get(color);
		mapEnemy.setFire(isFire);
	}
	/**
	 * Tworzy statek w liœcie respawn. Lista ta generuje statki wg okreœlonych regó³. Tworzy statek
	 * w okreœlonym obszarze od gracza.
	 * 
	 */
	public void create(int color,int posx,int posY){
		MapEnemy mapEnemy=_mapEnemyList.get(color);
		if(mapEnemy==null)return;
		MissionResource.Enemy.IdType idType=mapEnemy.getIdType();
		Respawn respawn=new Respawn();
		respawn.typeId=idType.id;
		respawn.weaponId=idType.weaponId;
		respawn.mapEnemy=mapEnemy;
		int respawnId=getElementId(posx,posY);
		_respawnList.add(respawnId, respawn);	
	}	
	public void setMarked(int color,int index,boolean isMarked){
		int findCount=0;
		for(int i=0;i<_respawnList.length;i++){
			Respawn respawn=_respawnList.getValByIndex(i);
			
			if(respawn.mapEnemy.getId()==color){
				if(respawn.mapEnemy._isActive==true){
					if(findCount==index){
						respawn.isMarked=true;
					}
					findCount++;
				}
			}			
		}
	}
	
	private static int getElementId(int x,int y){
		return (int) (x*1e5)+y;
	}
	public void addListener(EnemyListener listener){
		_listenerList.add(listener);
	}
	public static abstract class EnemyListener{
		public void onDestroy(Vector2 position,String type){}
		/**
		 * To samo co onHit
		 */
		public void onDamage(Vector2 position,String type,int damage,boolean resistance){}
		public void onUserShipCollision(Vector2 position,String type,int damage,boolean resistance){}
	}
	public Group getView(){
		return _view;
	}
	/**
	 * Klasa przedstawia widok danego managera
	 * @author Krzysztof
	 *
	 */
	private class ViewGroup extends Group{
		public void init(){
			this.setTransform(false);
		}
		@Override
		public void act (float delta) {
			_boardMarker.act(delta);
			super.act(delta);
		}
		@Override
		public void draw(SpriteBatch batch,float alpha){
			for(int i=0;i<SHIP_LIMIT;i++){
				PoolShipElement poolShipElement=_poolShip[i];
				if(poolShipElement.isActive==true){
					Respawn respawn=_respawnList.get(poolShipElement.respawnId);
					MapEnemy mapEnemy=respawn.mapEnemy;
					if(respawn.isMarked){
						_boardMarker.setPosition(poolShipElement.ship.getPosition().x,poolShipElement.ship.getPosition().y);
						_boardMarker.draw(batch,alpha);
					}
				}
			}
			for(int i=0;i<LIMIT_TURRET;i++){
				PoolTurretElement poolTurretElement=_poolTurret[i];
				if(poolTurretElement.isActive==true){
					Respawn respawn=_respawnList.get(poolTurretElement.respawnId);
					MapEnemy mapEnemy=respawn.mapEnemy;
					if(respawn.isMarked){				
						_boardMarker.setPosition(poolTurretElement.turret.getPosition().x,poolTurretElement.turret.getPosition().y);
						_boardMarker.draw(batch,alpha);
					}
				}
			}	
			for(int i=0;i<LIMIT_CONSTRUCTION;i++){
				PoolConstructionElement poolConstructionElement=_poolConstruction[i];
				if(poolConstructionElement.isActive==true){
					Respawn respawn=_respawnList.get(poolConstructionElement.respawnId);
				//	MapEnemy mapEnemy=respawn.mapEnemy;
					if(respawn.isMarked){					
						_boardMarker.setPosition(poolConstructionElement.construction.getPosition().x,poolConstructionElement.construction.getPosition().y);
						_boardMarker.draw(batch,alpha);
					}
				}
			}				
			super.draw(batch,alpha);
		}
	}
	/**
	 * Klasa zawiera informacje o zniszczonych obiektach które mog¹ mieæ respawn, oblicza dla nich
	 * czas kiedy mog¹ siê pojawiæ
	 * @author Krzysztof
	 *
	 */
	private class Respawn{
		public boolean isActive=false;
		public float time;
		public int typeId;
		public int weaponId;
		public boolean isMarked;
		public MapEnemy mapEnemy;
	}
	/**
	 * Klasa zawiera pool statku
	 * @author Krzysztof
	 *
	 */
	private class PoolShipElement{
		public boolean isActive=false;
	    public Ship ship;
	    public int respawnId;
	}	
	private class PoolTurretElement{
		public boolean isActive=false;
	    public Turret turret;
	    public int respawnId;
	}	
	private class PoolConstructionElement{
		public boolean isActive=false;
	    public Construction construction;
	    public int respawnId;
	}	
	/**
	 * Klasa zawiera informacje o grupie obiektów o danym id koloru
	 * @author Krzysztof
	 *
	 */
	private class MapEnemy{
		private int _id;
		private String _type;
		private int _group;
		private int _nerf;
		private int _respawn;
		private boolean _isActive;//mo¿emy deaktywowaæ wszystkie obiekty po id
		private boolean _isFire;
		private boolean _resistance;//jeœli true to statek niezniszczalny
	//	private boolean _isMarked;//jeœli true to znaczy, ¿e obiekt ma byæ oznaczony na mapie i boardzie
		private ArrayList<MissionResource.Enemy.IdType> _typeId;
		private int _rotation;
		public void init(int id,String typeName,int group,int nerf,int respawn,ArrayList<MissionResource.Enemy.IdType> idTypeList){
			_typeId=idTypeList;
			if(_typeId==null||_typeId.size()==0){
				throw new RuntimeException("Brak TypeId w xml");
			}
		//	setIdType(idTypeList);
			setId(id);
			setGroup(group);
			if(typeName.equals("construction")){
				_type=TYPE_CONSTRUCTION;
			}else if(typeName.equals("turret")){
				_type=TYPE_TURRET;
			}else if(typeName.equals("ship")){
				_type=TYPE_SHIP;
			}
			setNerf(nerf);
			setRespawn(respawn);
			_resistance=false;
			_rotation=0;
			_isActive=true;
			_isFire=true;
		}
		/**
		 * Jeœli damy true to obiekt niezniszczalny
		 * @param resistance
		 */
		public void setResistance(boolean resistance){
			_resistance=resistance;
		}
		public void setActive(boolean isActive){
			_isActive=isActive;
		}
	/*	public void setMarked(boolean isMarked){
			_isMarked=isMarked;
		}
		public boolean isMarked(){
			return _isMarked;
		}*/
		public void setFire(boolean isFire){
			_isFire=isFire;
		}		
		private boolean getResistance(){
			return _resistance;
		}
		public void setRotation(int rotation){
			_rotation=rotation;
		}
		private int getRotation(){
			return _rotation;
		}				
		@Override
		public String toString(){
			return "MapEnemy {id:"+Integer.toHexString(_id)+" type:"+_type+" group:"+_group+" nerf:"+_nerf+" respawn"+_respawn+"}";
		}
		/**
		 * Okreœla id w 16 odpowiadaj¹ce mapie, jest to id okreœlane przy konfiguracji parametrów respawn pointa
		 * @param id
		 */
		public void setId(int id){
			_id=id;
		}
		public int getId(){
			return _id;
		}
		public void setGroup(int group){
			_group=group;
		}
		public void setNerf(int nerf){
			_nerf=nerf;
		}
		public void setRespawn(int respawn){
			_respawn=respawn;
		}
	/*	public void setIdType(ArrayList<MissionResource.Enemy.IdType> idTypeList){
			for(MissionResource.Enemy.IdType idType:idTypeList){
				int priority=idType.priority;
				for(int i=0;i<priority;i++){
					_typeId.add(idType.id);
				}
			}
		}*/
		/**
		 * Jeœli jest wiêcej idType to nastêpuje losowanie z uwzglêdnieniem priorytetów
		 * @return
		 */
		public IdType getIdType(){
			if(_typeId.size()==1){
				return _typeId.get(0);
			}
			int size=0;
			for(IdType idType:_typeId){
				size+=idType.priority;
			}

			int rand=(int) Math.floor(Math.random()*size);
			size=0;
			for(IdType idType:_typeId){
				size+=idType.priority;
				if(size>rand)return idType;
			}
			return null;			
		}
	}
}

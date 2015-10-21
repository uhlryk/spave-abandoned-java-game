package pro.artwave.spaceshooter.controller.root;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.PseudoThreatQueue;
import pro.artwave.fgm.controller.PseudoThreatQueue.Phase;
import pro.artwave.fgm.controller.dispatcher.TemplateDispatcher;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button.ClickListener;
import pro.artwave.spaceshooter.controller.RootController;
import pro.artwave.spaceshooter.controller.root.game.DialogManager;
import pro.artwave.spaceshooter.controller.root.game.TaskManager;
import pro.artwave.spaceshooter.manager.BulletPool;
import pro.artwave.spaceshooter.manager.CheckpointPool;
import pro.artwave.spaceshooter.manager.DecorationPool;
import pro.artwave.spaceshooter.manager.EnemyPool;
import pro.artwave.spaceshooter.manager.EffectAnimationPool;
import pro.artwave.spaceshooter.manager.ShadowPool;
import pro.artwave.spaceshooter.model.CountScore;
import pro.artwave.spaceshooter.model.asset.BonusAnimation;
import pro.artwave.spaceshooter.model.asset.ExplosionAnimation;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.GameBackgroundAssetImage;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.XSmallAssetBitmapFont;
import pro.artwave.spaceshooter.model.map.GameMapModel;
import pro.artwave.spaceshooter.model.resource.BonusResource;
import pro.artwave.spaceshooter.model.resource.ConstructionResource;
import pro.artwave.spaceshooter.model.resource.DecorationResource;
import pro.artwave.spaceshooter.model.resource.GameResource;
import pro.artwave.spaceshooter.model.resource.MissionResource;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.model.resource.ShipResource;
import pro.artwave.spaceshooter.model.resource.TurretResource;
import pro.artwave.spaceshooter.model.resource.WeaponResource;
import pro.artwave.spaceshooter.view.Loader;
import pro.artwave.spaceshooter.view.bonus.BonusPool;
import pro.artwave.spaceshooter.view.bonus.BonusPool.Listener;
import pro.artwave.spaceshooter.view.helper.LabelPool;
import pro.artwave.spaceshooter.view.manipulator.ActivePathArea;
import pro.artwave.spaceshooter.view.manipulator.BlockButtonFire;
import pro.artwave.spaceshooter.view.root.GameView;
import pro.artwave.spaceshooter.view.root.game.GameStoryPopup;
import pro.artwave.spaceshooter.view.root.game.GameEndPopup;
import pro.artwave.spaceshooter.view.root.game.GameMap;
import pro.artwave.spaceshooter.view.root.game.GameMenuPopup;
import pro.artwave.spaceshooter.view.root.game.MessageBar;
import pro.artwave.spaceshooter.view.tile.TileBoardIsometric;
import pro.artwave.spaceshooter.view.unit.Turret;
import pro.artwave.spaceshooter.view.unit.UserShip;

public class GameController extends Controller {
	private GameView _view;
	//private ImageBoard _imageBoard;
	
	private TileBoardIsometric _tileBoard;
	private BulletPool _bulletPool;
	private EnemyPool _enemyPool;
	private LabelPool _labelPool;
	private GameMapModel _gameMap;

	private EffectAnimationPool _effectAnimationPool;
	private CheckpointPool _checkpointPool;
	private GameBackgroundAssetImage _gameBackgroundAssetImage;
	private ExplosionAnimation _explosionAnimation;	
	private BonusAnimation _bonusAnimation;	
	private GameAssetAtlas _gameAssetAtlas;	
	private ShipResource _shipResource;	
	private ConstructionResource _constructionResource;	
	private TurretResource _turretResource;	
	private WeaponResource _weaponResource;
	private BonusResource _bonusResource;
	private DecorationResource _decorationResource;
	private UserShip _userShip;
	private BigOutlineAssetBitmapFont _bigOutlineAssetBitmapFont;		
	private MediumOutlineAssetBitmapFont _mediumOutlineAssetBitmapFont;	
	private SmallOutlineAssetBitmapFont _smallOutlineAssetBitmapFont;	
	private XSmallAssetBitmapFont _xSmallOutlineAssetBitmapFont;		
	private ActivePathArea _activePathArea;
	private float _gameTime;
	private CountScore _countScore;
	private BlockButtonFire _blockButtonFire;
	private GameEndPopup _popupEnd;
	private GameStoryPopup _popupStory;
	private GameMenuPopup _popupMenu;
	private boolean _isPause;
	private SaveGameResource _saveGame;
	private Integer _missionId;
	private Integer _playerId;
	private Integer _difficulty;
	private int _startScore;
	private BonusPool _bonusPool;
	private DecorationPool _decorationPool;
	private MissionResource _missionResource;
	private MessageBar _messageBar;
	private DialogManager _dialogManager;
	private PseudoThreatQueue _thread;
	private ShadowPool _shadowPool;
	/**
	 * globalna flaga oznaczaj¹ca ¿e koniec gry
	 */
	private boolean _isEnd;
	
	/**
	 * opcja ustawiana w menu g³ównym>opcje
	 * pozwala wy³¹czyæ izometryczne tile i daæ tylko wersjê prost¹ jednowarstwow¹
	 * Na tablecie nie ma zauwa¿alnej ró¿nicy
	 */
	private boolean _isIsometricOn;
	/**
	 * Mo¿emy wy³¹czyæ t³o,
	 * Znacz¹co przyœpiesza to dzia³anie gry
	 */
	private boolean _isBackgroundOn;
	/**
	 * domyœlnie na false obra¿enia s¹ zadawane. funkcja pozwala na true wy³¹czyæ obra¿enia, by testowaæ np celowanie
	 */
	private boolean _isDebugNoDamageOn;
	
	/**
	 * Przy zakoñczeniu gry sprawdzana jest ta zmienna, jeœli jest false to znaczy ¿e user przegra³
	 * Zostanie wyœwietlona informacja o przegranej, nie dostanie za to punktów, a jeœli bêd¹ ¿ycia to równie¿
	 * straci.
	 * Jeœli state jest true to znaczy ¿e gracz wygra³, Nale¿y daæ mu za to punkty i wyœwietliæ stosowny komentarz.
	 */
	private boolean _stateFinish;
	
	private TaskManager _taskManager;
	@Override
	public void onInit() {
	}
	@Override
	public void onLoad() {

		_missionId=this.getIntent().getIntegerValue("missionId",1);
		_playerId=this.getIntent().getIntegerValue("playerId",1);
		_difficulty=this.getIntent().getIntegerValue("difficulty",1);
		
		System.out.println("Poziom trudnoœci "+_difficulty);
		_saveGame=new SaveGameResource();
		_saveGame.init(_playerId);
		_saveGame.setActive();
		_startScore=_saveGame.getScore();
		_isIsometricOn=true;
		_isBackgroundOn=true;
		_isDebugNoDamageOn=false;
		
		GameResource gameResource=new GameResource();
		gameResource.init();

		_missionResource=new MissionResource();
		if(_isBackgroundOn==true){
			_gameBackgroundAssetImage=new GameBackgroundAssetImage();
			_gameBackgroundAssetImage.init(_playerId);		
			addModelToControl(_gameBackgroundAssetImage);
		}
		_explosionAnimation=new ExplosionAnimation();
		addModelToControl(_explosionAnimation);
	
		_bonusAnimation=new BonusAnimation();
		addModelToControl(_bonusAnimation);		
		
		_gameAssetAtlas=new GameAssetAtlas();
		addModelToControl(_gameAssetAtlas);
		_gameMap=new GameMapModel();
		_gameMap.init(_missionId);
		
		_bigOutlineAssetBitmapFont=new BigOutlineAssetBitmapFont();
		this.addModelToControl(_bigOutlineAssetBitmapFont);	
		_mediumOutlineAssetBitmapFont=new MediumOutlineAssetBitmapFont();
		this.addModelToControl(_mediumOutlineAssetBitmapFont);	
		_smallOutlineAssetBitmapFont=new SmallOutlineAssetBitmapFont();
		this.addModelToControl(_smallOutlineAssetBitmapFont);		
		_xSmallOutlineAssetBitmapFont=new XSmallAssetBitmapFont();
		this.addModelToControl(_xSmallOutlineAssetBitmapFont);	
		this.setLoader(new Loader());
	}
	/**
	 * Metoda sprawdza czy mamy wystarczaj¹co punktów do osi¹gniêcia nowego rekordu
	 * Rekord jest wtedy jak w danej grze zdobyliœmy wiêcej ni¿ 1000 punktów
	 * @return true mamy nowy rekord; false nie ma rekordu
	 */
	public boolean isNewRecord(){
		if(_countScore.getScore()-_saveGame.getScore()>1000){
			return true;
		}
		return false;
	}
	@Override
	public void onPrepare() {
		System.out.println("START GAME DIFFICULTY "+_difficulty);
		_isEnd=false;
		_stateFinish=false;
		_smallOutlineAssetBitmapFont.setSmooth();
		_mediumOutlineAssetBitmapFont.setSmooth();
		_bigOutlineAssetBitmapFont.setSmooth();
		_missionResource.init(_missionId);
		_popupEnd=new GameEndPopup();
		_popupEnd.init();
		_popupEnd.addClickListener(GameEndPopup.BUTTON_TYPE_LEFT,new ClickListener() {
			@Override
			public void onClick() {
				
				if(_stateFinish==true){//znaczy ¿e gracz wygra³ ale chce zrobiæ restart
					Intent intent=new Intent(RootController.GAME_CONTROLLER);
					intent.addValue("missionId", _missionId);
					intent.addValue("playerId",_playerId);
					getParent().setDispatch(intent);
				}else{//znaczy ze gracz przegra³ i chce wyjœæ do menu
					Intent subIntent=new Intent(MainController.SELECT_MISSION_CONTROLLER);
					subIntent.addValue("playerId", _playerId);
					subIntent.addValue("missionId", _missionId);
					Intent intent=new Intent(RootController.MAIN_CONTROLLER);
					intent.setChildIntent(subIntent);
					getParent().setDispatch(intent);
				}
			}
		});		
		_popupEnd.addClickListener(GameEndPopup.BUTTON_TYPE_RIGHT,new ClickListener() {
			@Override
			public void onClick() {
				if(_stateFinish==true){//znaczy ¿e gracz wygra³ i chce przejœæ do kolejnej misji
					_saveGame.saveScore(_countScore.getScore());
					_saveGame.saveMission(_missionId);
					Intent subIntent=new Intent(MainController.END_GAME_CONTROLLER);
					//		subIntent.addValue("sectionId", _campaignId);
							subIntent.addValue("debugName","gameSubIntent");
							subIntent.addValue("playerId", _playerId);
							subIntent.addValue("missionId", _missionId);
							subIntent.addValue("gameStatus",_stateFinish);
							/**
							 * Ile punktów zosta³o zarobione w tej grze
							 */
							subIntent.addValue("earnScore",_countScore.getScore()-_startScore);
							System.out.println("GameController gameStatus "+_stateFinish);
							Intent intent=new Intent(RootController.MAIN_CONTROLLER);
							intent.setChildIntent(subIntent);
							intent.addValue("debugName","gameIntent");
							getParent().setDispatch(intent);
				}else{//znaczy ¿e gracz przegra³ ale chce zrobiæ restart
					Intent intent=new Intent(RootController.GAME_CONTROLLER);
					intent.addValue("missionId", _missionId);
					intent.addValue("playerId",_playerId);
					getParent().setDispatch(intent);
				}				

			}
		});				
		_popupMenu=new GameMenuPopup();
		_popupMenu.init();	
		_popupMenu.addClickListener(GameMenuPopup.BUTTON_ID_EXIT,new ClickListener() {
			@Override
			public void onClick() {
	/*			if(_saveGame.getScore(_missionId)<_countScore.getScore()){
					_saveGame.saveScore(_missionId,_countScore.getScore());
				}*/
				Intent subIntent=new Intent(MainController.SELECT_MISSION_CONTROLLER);
				subIntent.addValue("playerId", _playerId);
				subIntent.addValue("missionId", _missionId);
				Intent intent=new Intent(RootController.MAIN_CONTROLLER);
				intent.setChildIntent(subIntent);
				getParent().setDispatch(intent);
			}
		});
		_popupMenu.addClickListener(GameMenuPopup.BUTTON_ID_RETURN,new ClickListener() {
			@Override
			public void onClick() {
				_view.getBoard().setTouchable(Touchable.enabled);
				_blockButtonFire.setTouchable(Touchable.enabled);
				_popupMenu.hide();
				_isPause=false;
			}
		});		
		_popupStory=new GameStoryPopup();
		_popupStory.init();
		_popupStory.setTouchable(Touchable.childrenOnly);
		_isPause=false;
		_countScore=new CountScore();
		_countScore.init(_difficulty);
		_countScore.addScoreRaw(_startScore);
		_labelPool=new LabelPool();
		_labelPool.init();
		_gameTime=0;
		Integer shipId=_saveGame.getShip();
		
		Integer weapon1Id=_saveGame.getFirstWeapon();
		Integer weapon2Id=_saveGame.getSecondWeapon();
		//System.out.println("shipId:"+shipId+" w1:"+weapon1Id+" w2:"+weapon2Id+" w3:"+weapon3Id);
		_gameAssetAtlas.setSmooth();
		//_mediumAssetBitmapFont.setSmooth();	
	//	_gameBackgroundAssetImage.setSmooth();
		_shipResource=new ShipResource();
		_shipResource.init();
		_turretResource=new TurretResource();
		_turretResource.init();	
		_constructionResource=new ConstructionResource();
		_constructionResource.init();
		_weaponResource=new WeaponResource();
		_weaponResource.init();
		_bonusResource=new BonusResource();
		_bonusResource.init();
		_decorationResource=new DecorationResource();
		_decorationResource.init();
		_effectAnimationPool=new EffectAnimationPool();
		_effectAnimationPool.init();		
		_effectAnimationPool.addEffect(EffectAnimationPool.TYPE_EXPLOSION,_explosionAnimation);
		_effectAnimationPool.addEffect(EffectAnimationPool.TYPE_BONUS, _bonusAnimation);
		
		_bonusPool=new BonusPool();
		_bonusPool.init(_bonusResource,_missionResource.getBonusList(),_gameMap.getMap()); 
		_bonusPool.initLabelPool(_labelPool);

		_decorationPool=new DecorationPool();
		_decorationPool.init(_decorationResource,_missionResource.getDecorationList(),_gameMap.getMap());
		_userShip=new UserShip();
		_activePathArea=new ActivePathArea();
		_activePathArea.init(0,-50,1);		
		Vector2 userShipStartPosition=_gameMap.getStartPosition();
		_userShip.init(_activePathArea,EnemyPool.GROUP_FRIENDLY,_shipResource.getShip(shipId),userShipStartPosition.x,userShipStartPosition.y,0,_effectAnimationPool);
		_userShip.setDebugName("UserShip");
		_userShip.createImage();
		
		//	System.out.println(_userShip.getRadius());
		_userShip.setWeapon(1,_weaponResource.getWeapon(weapon1Id));
		_userShip.setWeapon(2,_weaponResource.getWeapon(weapon2Id));
	//	_userShip.setMoveMode(Turret.MOVE_MODE_DIRECTIONAL);
		_userShip.setMoveMode(Turret.MOVE_MODE_MANOUVER);
		_tileBoard=new TileBoardIsometric();
		_tileBoard.init(_gameMap.getMap(),9,8,_missionResource.getTileList());
		_bonusPool.setCountScore(_countScore);
		_bonusPool.setUserShip(_userShip);
		
		_enemyPool=new EnemyPool();
		_enemyPool.init(_difficulty,_constructionResource,_turretResource,_shipResource,_weaponResource,_missionResource.getEnemyList(),_gameMap.getMap());
		_bulletPool=new BulletPool();
		_bulletPool.init(200);		

		_enemyPool.setCursorManipulator(_activePathArea);
	//	_userShip.setManipulator(_activeArea);
		_blockButtonFire=new BlockButtonFire();
		_blockButtonFire.init(weapon1Id,weapon2Id);
		_userShip.setFireControler(_blockButtonFire);
		_checkpointPool=new CheckpointPool();
		_checkpointPool.init(_gameMap.getMap());
		
		_messageBar=new MessageBar();
		_messageBar.init();
		
		_shadowPool=new ShadowPool();
		_shadowPool.init();
		//_messageBar.setObjective("OBJECTIVE","GO TO CHECKPOINT");
		_view=new GameView();
		this.setView(_view);
		this.getParent().getParent().getView().addContent(_view);
		_view.init(_shipResource.getShip(shipId).maxHealth,_shipResource.getShip(shipId).maxShield);
		if(_isBackgroundOn){
			_view.setBackground(_gameBackgroundAssetImage.createSprite());
		}
		
		_view.setMap(_gameMap.getMap(),(int)_tileBoard.getWidth(),(int)_tileBoard.getHeight(),GameMapModel.TILE_SIZE);
		//_view.setActiveArea(_activeArea);
		_view.setActiveArea(_activePathArea);
		_userShip.setMap(_view.getMap());
//		_view.setJoystick(_joystick);
		_view.setBlockFireButton(_blockButtonFire);
	//	_view.getBoard().setImageBoard(_imageBoard);//-
		
		if(_isIsometricOn==true){
			_view.getBoard().setFloorTile(_tileBoard.getFloorTileActor());	
			_view.getBoard().setBottomTile(_tileBoard.getBottomWallActor());	
		}else{
			_view.getBoard().setFloorTile(_tileBoard.getFlatTileActor());	
		}
		_view.getBoard().setCheckpointPool(_checkpointPool.getView());//+
		_view.getBoard().setDecorationPool(_decorationPool.getView());//+
		_view.getBoard().setShadow(_shadowPool.getView());//+
		_view.getBoard().setBulletPool(_bulletPool.getView());//+
		_view.getBoard().setUserShip(_userShip);//+
		_view.getBoard().setEnemyPool(_enemyPool.getView());//+
		_view.getBoard().setExplosionPool(_effectAnimationPool.getView());//-
		
		
		_view.getBoard().setBonusPool(_bonusPool);//+
		_view.setMessageBar(_messageBar);//+
		if(_isIsometricOn==true){
			_view.getBoard().setTopTile(_tileBoard.getTopWallTileActor());	
		}
		
		_view.getBoard().setLabelPool(_labelPool);//+
		
		
		_view.getBoard().setDrawingArea(_activePathArea.getDrawingArea());
		_enemyPool.calculateRespawnEnemy(_gameTime,_userShip.getPosition(),0);
		_enemyPool.setMap(_view.getMap());
	//	_checkpointPool.setMap(_view.getMap());
		
		_view.addPopup(_popupStory);
		_view.addPopup(_popupEnd);
		_view.addPausePopup(_popupMenu);
		_view.getMap().initTileBoard(_tileBoard);
		_view.getMap().createPixMap();
		_view.getMap().regenerate();
		
		_dialogManager=new DialogManager();
		_dialogManager.init(_missionResource.getDialogList());
		_taskManager=new TaskManager();
		
		_taskManager.init(_missionResource.getTaskList(),this);
		_taskManager.setDialogManager(_dialogManager);
		_taskManager.setBriefPopup(_popupStory);
		_taskManager.setBonusPool(_bonusPool);
		_taskManager.setMessageBar(_messageBar);
		_taskManager.setCheckpointPool(_checkpointPool);
		_taskManager.setDecorationPool(_decorationPool);
		_taskManager.setTileBoard(_tileBoard);
		_taskManager.setGameMap(_view.getMap());
		_taskManager.setEnemyPool(_enemyPool);
		_taskManager.setStartTask();
		
		_enemyPool.addListener(new EnemyPool.EnemyListener() {			
			@Override
			public void onDestroy(Vector2 position, String type) {
				_effectAnimationPool.newExplosion(EffectAnimationPool.TYPE_EXPLOSION,(int)position.x,(int)position.y);
				_countScore.addScore(10);
			}
			@Override
			public void onDamage(Vector2 position, String type,int damage,boolean resistance){
				if(resistance){
					_labelPool.newRed("resistance",(int)position.x,(int)position.y);
				}else{
					_labelPool.newRed("-"+damage+" HP",(int)position.x,(int)position.y);
				}
			}
			@Override
			public void onUserShipCollision(Vector2 position, String type,int damage,boolean resistance){
				if(resistance){
					_labelPool.newRed("resistance",(int)position.x,(int)position.y);
				}else{
					_labelPool.newRed("-"+damage+" HP",(int)position.x,(int)position.y);
				}
				Vector2 pos=_userShip.getPosition();
				_labelPool.newRed("-"+damage+" HP",(int)pos.x,(int)pos.y);
				
			}
		});
		_bonusPool.addListener(new Listener() {
			@Override
			public void onHit(Vector2 position,int type){
				_effectAnimationPool.newExplosion(EffectAnimationPool.TYPE_BONUS,(int)position.x,(int)position.y);
			}
		});
		//_effectAnimationPool.newExplosion(EffectAnimationPool.TYPE_EXPLOSION,(int)position.x,(int)position.y);
		_thread=new PseudoThreatQueue();
		_thread.init(60);
		Phase p=_thread.addPhase(new Phase(){//faza 1
			@Override
			public void calculate() {
				Vector2 pos=_userShip.getPosition();
				_enemyPool.calculateRespawnEnemy(_gameTime,pos,EnemyPool.RADIUS_MIN_RESPAWN);
				_enemyPool.calculateHideEnemy(pos);
				_enemyPool.calculateShipDestroy(_gameTime);
				_enemyPool.calculateAI(pos,_tileBoard);
			}			
		});
		p.setName("Enemy First Calculation");
		_thread.addSchedule(p,3,30);
		p=_thread.addPhase(new Phase(){//faza 2
			@Override
			public void calculate() {
				Vector2 pos=_userShip.getPosition();
				_enemyPool.calculateMapPosition();
		//		_checkpointPool.calculateMapPosition();
				if(_userShip.isDisable()==false){
					_view.getMap().calculateUserPosition(pos.x,pos.y);
				}
			//	_view.getMap().regenerate();
			}			
		});
		p.setName("Map Regeneration");
		_thread.addSchedule(p,2,20);	

		p=_thread.addPhase(new Phase(){
			@Override
			public void calculate() {
				Vector2 pos=_userShip.getPosition();
				_tileBoard.calculateVisibleTile(pos.x,pos.y);
			}			
		});
		p.setName("Tile Calculation");
		_thread.addSchedule(p,19);			
		p=_thread.addPhase(new Phase(){
			@Override
			public void calculate() {
				_enemyPool.calculateCursorPosition();
			}			
		});
		p.setName("Enemy cursor Calculation");
		_thread.addSchedule(p,1,1);		
		
		p=_thread.addPhase(new Phase(){
			@Override
			public void calculate() {	
				_bulletPool.calculateTileCollision(_tileBoard);
				if(_isDebugNoDamageOn==false){
					_enemyPool.calculateBulletCollision(_bulletPool);
				}
				_enemyPool.calculateWeapon(this.getDelta(),_bulletPool);
				_userShip.resetWeapon(this.getDelta());
				_userShip.calculateWeapon(_bulletPool);
				if(_userShip.isDisable()==false){
					if(_isDebugNoDamageOn==false){
						BulletPool.BulletDamage bulletDamage=_bulletPool.calculateCollision(_userShip.getWarGroupId(),_userShip.getPosition(),_userShip.getRadius());
						if(bulletDamage!=null){
							if(_userShip.isTimeResistance()==false){
								_userShip.addHit(bulletDamage.damage,bulletDamage.shieldDamage,bulletDamage.armorDamage);
							
								_labelPool.newRed("-"+(bulletDamage.damage+bulletDamage.shieldDamage+bulletDamage.armorDamage)+" HP",(int)_userShip.getPosition().x,(int)_userShip.getPosition().y);
							
							}
						}
					}
					_userShip.regenerateShield(this.getDelta());
				}		
				if(_userShip.calculateShipDestroy(_gameTime)){
					setStateFailure();
					_popupEnd.setMessage(GameEndPopup.TITLE_FAILED,GameEndPopup.MESSAGE_FAILED_SHIP_DESTROY);
					_popupEnd.setButtonText(GameEndPopup.BUTTON_TYPE_LEFT,GameEndPopup.LABEL_MENU);
					_popupEnd.setButtonText(GameEndPopup.BUTTON_TYPE_RIGHT,GameEndPopup.LABEL_RESTART);
				}
				if(_userShip.isDestroy()&&_isEnd==false){
					_isEnd=true;
					_popupEnd.show();
					_messageBar.setVisible(false);
					_popupMenu.hide();
				}	
				_enemyPool.regenerateShield(this.getDelta());
			}			
		});
		p.setName("Bullet collision Calculation");
		_thread.addSchedule(p,5,4);		
		
		p=_thread.addPhase(new Phase(){
			@Override
			public void calculate() {
				_bonusPool.calculateUserShipCollision(_gameTime);
				_bonusPool.calculateRespawn(_gameTime);
				_checkpointPool.calculateCollision(_userShip);
				_taskManager.calculate(this.getDelta());
				
			}			
		});
		p.setName("Resource Calculation");
		_thread.addSchedule(p,7,5);
		
		p=_thread.addPhase(new Phase(){
			@Override
			public void calculate() {
				_shadowPool.clear();
				if(_userShip.isDisable()==false){
					Vector2 pos=_userShip.getPosition();
					_shadowPool.addShadow(pos.x, pos.y, _userShip.getAngle(),_userShip.getCurrentAlpha());
				}
				_enemyPool.calculateShadow(_shadowPool);
			}			
		});
		p.setName("Show Shadow");
		_thread.addAllSchedule(p);		
		
		_thread.recalculateSchedule();
	}
	public boolean getStateFinish(){
		return _stateFinish;
	}
	public GameMap getGameMap(){
		return _view.getMap();
	}
	public void setStateSuccess(){
		_stateFinish=true;
	}
	public void setStateFailure(){
		_stateFinish=false;
	}	
	public UserShip getUserShip(){
		return _userShip;
	}
	public CountScore getCountScore(){
		return _countScore;
	}
	public GameEndPopup getEndPopup(){
		return _popupEnd;
	}
	public GameMenuPopup getMenuPopup(){
		return _popupMenu;
	}	
	public PseudoThreatQueue getThreatSchedule(){
		return _thread;
	}
	@Override
	public void onClear() {
		_view.clear();
		_view.remove();
		_view=null;	
	}
	@Override
	public void onPlayLoop(float delta){	
		if(_popupStory.commandShow()==true){
			if(_popupEnd.isShow()==false&&_popupMenu.isShow()==false&&_popupStory.isVisible()==false){
				_popupStory.show();
			}
		}
		if(_taskManager.isPause()==true){
			if(_popupEnd.isShow()==false&&_popupMenu.isShow()==false){
				_isPause=true;
			}
		}		
		if(_isPause==false){
			_view.getBoard().setTouchable(Touchable.enabled);
			_blockButtonFire.setTouchable(Touchable.enabled);
			_effectAnimationPool.setDelta(delta);
			_gameTime+=delta;
			_thread.calculate(delta);
			_activePathArea.setReferencePosition(_userShip.getPosition());
			_userShip.control(delta);
			_userShip.calculate(delta);
			_bulletPool.calculate(delta);
			_enemyPool.calculate(delta);
			_enemyPool.calculateTileCollision(_tileBoard);
			_enemyPool.calculateUserShipCollision(_userShip);
			
			Vector2 pos=_userShip.getPosition();
			if(_tileBoard.checkPosition(pos.x,pos.y,_userShip.getRadius())==false){
				pos=_userShip.estimatePositionX();
				if(_tileBoard.checkPosition(pos.x,pos.y,_userShip.getRadius())==false){
					_userShip.rollbackPositionX();
				}
				pos=_userShip.estimatePositionY();
				if(_tileBoard.checkPosition(pos.x,pos.y,_userShip.getRadius())==false){
					_userShip.rollbackPositionY();
				}
			}
			_userShip.commitPosition();
			_enemyPool.commitPosition();

			this._view.getBoard().setCameraCenter(_userShip.getPositionX(),_userShip.getPositionY());		
			_view.setPoints(_countScore.getScore());
			_view.setActHealth(_userShip.getHealth());
			_view.setActShield((int) _userShip.getShield());
		}else{
			_view.getBoard().setTouchable(Touchable.disabled);
			_blockButtonFire.setTouchable(Touchable.disabled);
		}
	}
	@Override
	public boolean onKeyUp(int keycode){
		if(_isPause==false){
			if(_userShip.keyUp(keycode))return true;
		}
		switch(keycode){
			case Input.Keys.BACK:	
			case Input.Keys.ESCAPE:
				if(_popupEnd.isShow()){
					
				}else if(_userShip.isDisable()){
			//		refreshPopupText();
					_popupMenu.hide();
					_popupStory.hide();
					_popupEnd.show();
					_messageBar.setVisible(false);
				}else{
					if(_popupMenu.isShow()==false){//mieliœmy pausePopup niewidoczny wiêc go wyœwietlamy i w³aczamy pause
						_isPause=true;
						_popupMenu.show();
						_popupStory.hide();
					}else{
						_isPause=false;
						_popupMenu.hide();
						if(_popupStory.commandShow()){
						//	_isPause=true;
							_popupStory.show();
						}
					}
				}
				return true;
			case Input.Keys.P:

				if(_popupMenu.isShow()==false){
					_isPause=true;
					_popupMenu.show();
					_popupStory.hide();
				}else{
					_isPause=false;
					_popupMenu.hide();
					if(_popupStory.commandShow()){
					//	_isPause=true;
						_popupStory.show();
					}
				}
				return true;
		}
		return false;
	}
	public boolean isPause(){
		return _isPause;
	}
	public void setPause(boolean pause){
		_isPause=pause;
	}
	@Override
	public boolean onKeyDown(int keycode){
		if(_isPause==false){
			if(_userShip.keyDown(keycode))return true;
		}
		return false;
	}	
	@Override
	public void onSystemPause(){
		if(this.getParent().getDispatcher().getStep()==TemplateDispatcher.Step.ON_PLAY_LOOP){
			if(_userShip.isDisable()==false){
				_isPause=true;
			//	refreshPopupText();
				_popupMenu.show();
				_popupStory.hide();
			}
		}
	}
	@Override
	public void onSystemResume(){
		if(this.getParent().getDispatcher().getStep()==TemplateDispatcher.Step.ON_PLAY_LOOP){
			_view.getMap().regenerate();
			Vector2 pos=_userShip.getPosition();
			_view.getMap().calculateUserPosition(pos.x,pos.y);
			
		}
	}	
}

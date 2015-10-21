package pro.artwave.spaceshooter.view.bonus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pro.artwave.fgm.utility.SimpleStorage;
import pro.artwave.spaceshooter.helper.BonusParams;
import pro.artwave.spaceshooter.helper.BonusParams.Effect;
import pro.artwave.spaceshooter.manager.EffectAnimationPool;
import pro.artwave.spaceshooter.manager.EnemyPool.EnemyListener;
import pro.artwave.spaceshooter.model.CountScore;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.model.map.GameMapModel;
import pro.artwave.spaceshooter.model.resource.BonusResource;
import pro.artwave.spaceshooter.model.resource.MissionResource;
import pro.artwave.spaceshooter.model.resource.MissionResource.Bonus.IdType;
import pro.artwave.spaceshooter.view.helper.BoardMarker;
import pro.artwave.spaceshooter.view.helper.LabelPool;
import pro.artwave.spaceshooter.view.unit.UserShip;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class BonusPool extends Actor {
	public static final int DEFAULT_ID=0x006300ff;
	
	public static final int RESPAWN_SPEED=10;	
	public final int _offsetCollision=25;
	
	/**
	 * Lista zawiera wszystkie bonusy z czasami wywp³ania
	 */
	private PoolRespawnElement[] _respawnList;
	private int _respawnSize;
	private SimpleStorage<MapBonus> _mapBonusList;
	private GameAssetAtlas _gameAssetAtlas;
	private Map<Integer,Sprite> _resourceImage;
	private CountScore _countScore;	
	private UserShip _ship;
	private Pixmap _map;
//	private static final int IMAGE_OFFSET=35;
	private float _alpha;
	private float _alphaParam;
	private boolean _isFade;	
	private LabelPool _labelPool;
	private EffectCommand _effectCommand;
	private BonusResource _bonusResource;
	
	private BoardMarker _boardMarker;
	
	private ArrayList<Listener> _listenerList;
	private Effects.Play _sound;
	public void init(BonusResource bonusResource,ArrayList<MissionResource.Bonus> gameBonusList,Pixmap map){
		Effects effects=Effects.getInstance();
		_sound=effects.getBonusSound();		
		_listenerList=new ArrayList<Listener>();
		_boardMarker=new BoardMarker();
		_boardMarker.init();
		_alpha=1;
		_alphaParam=1;
		_isFade=false;
		_map=map;
		_bonusResource=bonusResource;
		_gameAssetAtlas=new GameAssetAtlas();
		_effectCommand=new EffectCommand();
		_effectCommand.init(this);
		_resourceImage=new HashMap<Integer,Sprite>(_bonusResource.getBonusMap().size());
		for(Entry<Integer,BonusParams> entry:_bonusResource.getBonusMap().entrySet()){
			int id=entry.getKey();
			_resourceImage.put(id,_gameAssetAtlas.createSpriteResource(id));
		}
		
		_mapBonusList=new SimpleStorage<MapBonus>(gameBonusList.size()+1);
		for(MissionResource.Bonus resourceBonus:gameBonusList){
			MapBonus bonus=new MapBonus();
			bonus.init(resourceBonus.id,resourceBonus.respawn,resourceBonus.idTypeList);
			_mapBonusList.add(resourceBonus.id,bonus);
		}		
		if(_mapBonusList.get(DEFAULT_ID)==null){
			MapBonus bonus=new MapBonus();
			ArrayList<IdType> idTypeList=new ArrayList<IdType>(1);
			IdType idType=new IdType();
			idType.id=3;
			idType.priority=3;
			idTypeList.add(idType);
			idType=new IdType();
			idType.id=4;
			idType.priority=2;
			idTypeList.add(idType);	
			idType=new IdType();
			idType.id=5;
			idType.priority=1;
			idTypeList.add(idType);				
			bonus.init(DEFAULT_ID,0,idTypeList);
			_mapBonusList.add(DEFAULT_ID,bonus);
		}	
		
		generate();
	}
	public void setCountScore(CountScore countScore){
		 _countScore=countScore;
	}
	public CountScore getCountScore(){
		return _countScore;
	}
	public void setUserShip(UserShip ship){
		_ship=ship;
	}
	public UserShip getUserShip(){
		return _ship;
	}	
	private void startEffect(PoolRespawnElement respawn){
		int bonusId=respawn.type;
		BonusParams bonusParams=_bonusResource.getBonus(bonusId);
		ArrayList<Effect> effectList=bonusParams.effectList;
		if(effectList!=null){
			for(Effect effect:effectList){
				_effectCommand.command(effect.type,effect.val1,effect.val2,effect.val3);
			}
		}
		showLabel(respawn,bonusParams.text);
	}
	public void showLabel(PoolRespawnElement respawn,String text){
		if(_labelPool!=null){
			_labelPool.newGreen(text,(int)respawn.position.x+35,(int)respawn.position.y+35);
		}
	}	
	private void generate(){
		_respawnList=new PoolRespawnElement[300];
		int maxX=_map.getWidth();
		int maxY=_map.getHeight();
		int count=0;
		for(int x=0;x<maxX;x++){
			for(int y=0;y<maxY;y++){
				int mapBonusId=_map.getPixel(x,_map.getHeight()-y);
				MapBonus mapBonus=_mapBonusList.get(mapBonusId);
				if(mapBonus==null)continue;
				int id=getElementId(x,y);
				PoolRespawnElement respawn=new PoolRespawnElement();
				respawn.isActive=true;
				respawn.type=mapBonus.lotteryIdType();
				respawn.time=0;
				respawn.id=id;
				respawn.mapBonusId=mapBonusId;
				Vector2 position=new Vector2(GameMapModel.TILE_SIZE*x+GameMapModel.TILE_SIZE/2,GameMapModel.TILE_SIZE*y+GameMapModel.TILE_SIZE/2);
				respawn.position=position;
				_respawnList[count]=respawn;
				count++;
			}
		}	
		_respawnSize=count;
	}
	/**
	 * Metoda zwraca pozycjê bonusu o danym id mapy. Czêsto bonusów jest wiêcej w danym kolorze wiec
	 * mo¿na podaæ opcjonalnie index by okreœliæ który ma zostaæ zwrócony.
	 * Przeszukuje tylko aktywnego
	 * @param color
	 * @param index
	 * @return
	 */
	public Vector2 getBonusPosition(int color,int index){
		int findCount=0;
		for(int i=0;i<_respawnSize;i++){
			PoolRespawnElement respawn=_respawnList[i];
			if(respawn.isActive){
				if(respawn.mapBonusId==color){
					if(findCount==index){
						return respawn.position;
					}
					findCount++;
				}
			}
		}
		return null;
	}
	public Vector2 getBonusPosition(int color){
		return getBonusPosition(color,0);
	}	
	/**
	 * Ustawia dla danego bonus o danym indexie czy ma byæ oznaczony markerem na boardzie czy nie.
	 * W teorii lepsza by³a by identyfikacja po id, na id siê sk³ada pozycja na tile.
	 * Ale w praktyce user buduj¹cy mapê nie bêdzie liczy³ dla ka¿dego bonusu tilów. A za³o¿enie koloru to
	 * ma byæ id. Po prostu nie nale¿y dawaæ za du¿ej iloœci bonusów o tym samym kolorze
	 * @param color
	 * @param index
	 * @param isMarked
	 */
	public void setMarked(int color,int index,boolean isMarked){
		int findCount=0;
		for(int i=0;i<_respawnSize;i++){
			PoolRespawnElement respawn=_respawnList[i];
			if(respawn.isActive){
				if(respawn.mapBonusId==color){
					if(findCount==index){
						respawn.isMarked=isMarked;
						return;
					}
					findCount++;
				}
			}
		}
	}
	public void setMarked(int color){
		setMarked(color,0,true);
	}	
	public void unsetMarked(int color){
		setMarked(color,0,false);
	}	
	/**
	 * ustala jaki status ma dany bonus, bonus identyfikuje po kolorze i indexie
	 * @param color
	 * @param index
	 * @param isActive
	 */
	public void setStatus(int color,int index,boolean isActive){
		int findCount=0;
		for(int i=0;i<_respawnSize;i++){
			PoolRespawnElement respawn=_respawnList[i];
			if(respawn.mapBonusId==color){
				if(findCount==index){
					respawn.isActive=isActive;
					return;
				}
				findCount++;
			}			
		}
	}
	/**
	 * Zwraca status danego resouce
	 * @param color
	 * @param index
	 * @return true jeœli jest aktywny, false oznacza nieaktywny, poniewa¿ zebrany lub deaktywowany w inny sposób
	 * null jeœli nie znaleziono
	 */
	public Boolean getStatus(int color,int index){
		int findCount=0;
		for(int i=0;i<_respawnSize;i++){
			PoolRespawnElement respawn=_respawnList[i];
			if(respawn.mapBonusId==color){
				if(findCount==index){
					return respawn.isActive;
				}
				findCount++;
			}			
		}
		return null;
	}
	/**
	 * tworzymy w danym miejscu nowy bonus. Warunkiem jest ¿e musi byæ zadeklarowany taki kolor w resource.
	 * Jeœli pod danym id ju¿ jest jakiœ zasób to zostanie on zast¹piony
	 * @param color
	 * @param posX
	 * @param posY
	 */
	public void create(int color,int posX,int posY){
		MapBonus mapBonus=_mapBonusList.get(color);
		if(mapBonus==null)return;
		int id=getElementId(posX,posY);
		PoolRespawnElement respawn=new PoolRespawnElement();
		respawn.isActive=true;
		respawn.type=mapBonus.lotteryIdType();
		respawn.time=0;
		respawn.id=id;
		respawn.mapBonusId=color;
		Vector2 position=new Vector2(GameMapModel.TILE_SIZE*posX+GameMapModel.TILE_SIZE/2,GameMapModel.TILE_SIZE*posY+GameMapModel.TILE_SIZE/2);
		respawn.position=position;
		_respawnList[_respawnSize]=respawn;
		_respawnSize++;
		
	}
	public void initLabelPool(LabelPool labelPool){
		_labelPool=labelPool;
	}
	private static int getElementId(int x,int y){
		return (int) (x*1e5)+y;
	}
	public void calculateRespawn(float gameTime){
		for(int i=0;i<_respawnSize;i++){
			PoolRespawnElement respawn=_respawnList[i];
			MapBonus mapBonus=_mapBonusList.get(respawn.mapBonusId);
			if(respawn.isActive==false){
				if(mapBonus._respawn>0){
					if(respawn.time<gameTime){
						respawn.isActive=true;
						respawn.type=mapBonus.lotteryIdType();
					}
				}
			}
		}	
	}
	public void calculateUserShipCollision(float gameTime){
		if(_ship.isDisable())return;
		for(int i=0;i<_respawnSize;i++){
			PoolRespawnElement respawn=_respawnList[i];
			if(respawn.isActive){
				
				if(_ship.calculateObjectCollision(respawn.position,50,0)==false){	
					respawn.isActive=false;
					respawn.time=gameTime+RESPAWN_SPEED;
					_sound.play();
					startEffect(respawn);
					
					for(Listener listener:_listenerList){
						listener.onHit(respawn.position,respawn.type);
					}					
				}
			}
		}
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
			if(_alphaParam>2){
				_alphaParam=1;
				_isFade=true;
			}			
		}
		_alpha=_alphaParam;
		if(_alpha>1)_alpha=1;
		if(_alpha<0.3)_alpha=0.3f;
		_boardMarker.act(delta);
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		for(int i=0;i<_respawnSize;i++){
			PoolRespawnElement respawn=_respawnList[i];
			if(respawn.isActive==true){
				int type=respawn.type;
				Sprite im=_resourceImage.get(type);
				float x=respawn.position.x-im.getWidth()/2;
				float y=respawn.position.y-im.getHeight()/2;
				if(respawn.isMarked){
					_boardMarker.setPosition(respawn.position.x,respawn.position.y);
					_boardMarker.draw(batch,parentAlpha);
				}
				
		//		System.out.println("resource"+type);
				im.setPosition(this.getX()+x, this.getY()+y);
				im.draw(batch,parentAlpha*_alpha);
			}
		}
	}
	public void addListener(Listener listener){
		_listenerList.add(listener);
	}
	public static abstract class Listener{
		public void onHit(Vector2 position,int type){}
	}	
	private class MapBonus{
		private int _id;
		private ArrayList<Integer> _typeId;
		private int _respawn;
		public void init(int id,int respawn,ArrayList<MissionResource.Bonus.IdType> idTypeList){
			setId(id);
			_typeId=new ArrayList<Integer>(20);
			setIdType(idTypeList);
			setRespawn(respawn);
		}
		public void setId(int id){
			_id=id;
		}		
		public void setIdType(ArrayList<MissionResource.Bonus.IdType> idTypeList){
			for(MissionResource.Bonus.IdType idType:idTypeList){
				int priority=idType.priority;
				for(int i=0;i<priority;i++){
					_typeId.add(idType.id);
				}
			}
		}
		public void setRespawn(int respawn){
			_respawn=respawn;
		}		
		public int lotteryIdType(){
			int size=_typeId.size();
			if(size==1){
				return _typeId.get(0);
			}else{
				int rand=(int) Math.floor(Math.random()*size);
				return _typeId.get(rand);
			}
		}
	}
	private class PoolRespawnElement{
		public boolean isActive=false;
	    public int type;
	    public float time;
	    public boolean isMarked=false;
	    public Vector2 position;
	    public int id;
	    public int mapBonusId;
	}	
}

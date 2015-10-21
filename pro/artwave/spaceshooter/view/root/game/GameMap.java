package pro.artwave.spaceshooter.view.root.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.view.tile.TileBoardIsometric;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameMap extends Actor {
	private static final int COLOR_WALL=0x000000ff;
	private static final int COLOR_EMPTY=0xffffffff;

	
	private static final int TYPE_USER=2;
	private static final int TYPE_ENEMY=1;
	private static final int TYPE_DESTINATION=3;
	private Pixmap _map;
	//private int _gameWidth;
	//private int _gameHeight;
	private float _posX;
	private float _posY;
	private TextureRegion _textureRegion;
	private Pixmap _gameMap;
	private int _tileSize;
	//private static final int OFFSET_X=255;
	//private static final int OFFSET_Y=354;
	private static final int TARGET_WIDTH=215;
	private static final int TARGET_HEIGHT=135;
	
	private Sprite _userSprite;
	private ArrayList<DoublePointerPosition> _enemyPosition;
	private ArrayList<PointPosition> _checkpointPosition;
	private Map<Integer,Sprite> _decoratorMap;
	//private float _delta;
	private TileBoardIsometric _tileBoard;
	private static final int ZOOM=4;

	/**
	 * Do mrugania na mapie elementów
	 */
	private float _alpha;
	private float _alphaParam;
	private boolean _isFade;		
	
	public void init(Pixmap map,int width,int height,int tileSize){
		_alpha=1;
		_alphaParam=1;
		_isFade=false;		
		
		_tileSize=tileSize;
		_map=map;
		_enemyPosition=new ArrayList<GameMap.DoublePointerPosition>(15);
		for(int i=0;i<15;i++){
			DoublePointerPosition pos=new DoublePointerPosition();
			_enemyPosition.add(pos);
		}
		_checkpointPosition=new ArrayList<GameMap.PointPosition>(10);
		for(int i=0;i<10;i++){
			PointPosition pos=new PointPosition();
			_checkpointPosition.add(pos);
		}
		GameAssetAtlas gameAssetAtlas=new GameAssetAtlas();
		_decoratorMap=new HashMap<Integer,Sprite>();
		Sprite enemy=gameAssetAtlas.createSpriteMap(1);
		enemy.setColor(1,0,0,1);
		_decoratorMap.put(TYPE_ENEMY,enemy);	
			
		
		_userSprite=gameAssetAtlas.createSpriteMap(2);
		_userSprite.setColor(0,0,1,1);

		Sprite destination=gameAssetAtlas.createSpriteMap(5);
		destination.setColor(0.7f,0.7f,0.0f,1);
		_decoratorMap.put(TYPE_DESTINATION, destination);			
	}
	public void initTileBoard(TileBoardIsometric tileBoard){
		_tileBoard=tileBoard;
	}
	public void calculateUserPosition(float x,float y){
		_posX=-x/_tileSize*ZOOM+TARGET_WIDTH/2;
		_posY=-(y/_tileSize-1)*ZOOM+TARGET_HEIGHT/2;;
		

		_textureRegion.setRegion((int)-_posX,(int)(_textureRegion.getTexture().getHeight()-TARGET_HEIGHT+_posY),TARGET_WIDTH,TARGET_HEIGHT);
	}
	/**
	 * Metoda oblicza i ustawia na mapie wroga o danym id i jego pozycji
	 * Jeœli wróg by³ ju¿ ustawiony to zmienia jego pozycjê jeœli jest nowy
	 * to ustawia ostatni wolny i ustawia na nim. Jeœli nie znajdzie ¿adnego
	 * wolnego to bierze pierwszy na mapie
	 * @param id
	 * @param x
	 * @param y
	 */
	public void calculateEnemyPosition(int id,float x,float y,boolean isMarked){
		PointPosition empty=_enemyPosition.get(0);
		for(DoublePointerPosition pos:_enemyPosition){
			if(pos.getId()==id){
				if(isMarked){
					pos.setPosition(id,TYPE_ENEMY,TYPE_DESTINATION,x, y);
				}else{
					pos.setPosition(id,TYPE_ENEMY,x, y);
				}
				return;
			}
			if(pos.isActive()==false){
				empty=pos;
			}
		}
		empty.setPosition(id,TYPE_ENEMY, x, y);
	}
	public void calculateEnemyDestroy(int id){
		for(PointPosition pos:_enemyPosition){
			if(pos.getId()==id){
				pos.destroy();
				return;
			}
		}
	}	
	public void destroyUser(){
	//	_userSprite.
	}
	/**
	 * Okreœlamy marker na mapie. Wska
	 * Bierzemy z puli pierwszy marker. Nastêpnie szukamy innego markera który nie jest
	 * w u¿yciu, jeœli znajdziemy to zastêpujemy obecny. Jeœli znajdziemy marker o danym id
	 * to go u¿ywamy. Jeœli nie znajdziemy to u¿ywamy ostatniego nieaktywnego.
	 * W najgorszym razie u¿ywamy pierwszego.
	 * @param id
	 * @param x
	 * @param y
	 */
	public void calculateMarkerPosition(int id,float x,float y){
		PointPosition empty=_checkpointPosition.get(0);
		for(PointPosition pos:_checkpointPosition){
			if(pos.getId()==id){
				pos.setPosition(id,TYPE_DESTINATION,x, y);
				return;
			}
			if(pos.isActive()==false){
				empty=pos;
			}
		}
		empty.setPosition(id,TYPE_DESTINATION, x, y);
	}
	public void calculateCheckpointDestroy(int id){
		for(PointPosition pos:_checkpointPosition){
			if(pos.getId()==id){
				pos.destroy();
				return;
			}
		}
	}
	public void drawPoint(int color,int x,int y){
		_gameMap.setColor(color);
		_gameMap.drawRectangle(x,y,ZOOM,ZOOM);
		_gameMap.fillRectangle(x,y,ZOOM,ZOOM);
	}
	/**
	 * Metoda do wywo³ania tylko gdy zmienia siê coœ na mapie
	 */
	public void createPixMap(){
		System.out.println("!!!Wywo³ano createPixMap!");
		_gameMap=new Pixmap(_map.getWidth()*ZOOM,_map.getHeight()*ZOOM, Format.RGB888);
		for(int x=0;x<_map.getWidth();x++){
			for(int y=0;y<_map.getHeight();y++){
				int id=_map.getPixel(x, y);
				if((_tileBoard!=null&&_tileBoard.getTileType(id)==TileBoardIsometric.TYPE_WALL)||id==TileBoardIsometric.DEFAULT_WALL_ID){
					drawPoint(COLOR_WALL,x*ZOOM, y*ZOOM);
				}else{
					drawPoint(COLOR_EMPTY,x*ZOOM, y*ZOOM);
				}
			}
		}
	}
	/**
	 * Czasoch³onna metoda, powinna byæ minimalnie u¿ywana
	 */
	public void regenerate(){
		System.out.println("!!!Wywo³ano regenerate!");
		if(_textureRegion!=null)_textureRegion.getTexture().dispose();
		_textureRegion=new TextureRegion(new Texture(_gameMap));
		_textureRegion.getTexture().setFilter(TextureFilter.Linear,TextureFilter.Linear);
		_textureRegion.setRegion((int)-_posX,(int)(_textureRegion.getTexture().getHeight()-TARGET_HEIGHT+_posY),TARGET_WIDTH,TARGET_HEIGHT);
	}
	public void act(float delta){
		if(_isFade==true){
			_alphaParam-=delta*3f;
			if(_alphaParam<0f){
				_alphaParam=0f;
				_isFade=false;
			}
		}else{
			_alphaParam+=delta*3f;
			if(_alphaParam>1){
				_alphaParam=1;
				_isFade=true;
			}			
		}
		_alpha=_alphaParam;
		if(_alpha>1)_alpha=1;
		if(_alpha<0)_alpha=0;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {	
		batch.draw(_textureRegion,this.getX(),this.getY());
		_userSprite.setPosition(this.getX()+TARGET_WIDTH/2-_userSprite.getWidth()/2,this.getY()+TARGET_HEIGHT/2-_userSprite.getHeight()/2);	
		_userSprite.draw(batch, parentAlpha);
		for(PointPosition pos:_checkpointPosition){
		
			pos.draw(batch, parentAlpha*_alpha);
		}
		for(PointPosition pos:_enemyPosition){
			pos.draw(batch, parentAlpha);
		}		
	}	
	private class DoublePointerPosition extends PointPosition{
		private int _type2;
		public void setPosition(int id,int type,float x, float y){
			setPosition(id,type,0,x,y);
		}
		public void setPosition(int id,int type,int type2,float x, float y){
			super.setPosition(id,type,x,y);
			_type2=type2;
		}
		@Override
		public void draw(SpriteBatch batch,float parentAlpha){
			super.draw(batch, parentAlpha);
			if(this.isActive()==true&&_type2>0){
				if(_objY+_posY>0&&_objX+_posX<TARGET_WIDTH-10){
					_decoratorMap.get(_type2).setPosition(getX()+_objX+_posX,getY()+_objY+_posY);
					_decoratorMap.get(_type2).draw(batch,parentAlpha);
				}
			}
		}
	}
	private class PointPosition{
		private boolean _isActive;
		private int _type;
		private int _id;
		public int _objX;
		public int _objY;
		public PointPosition(){
			_isActive=false;
			_type=-1;
			_id=-1;
		}
		public boolean isActive(){
			return _isActive;
		}
		public void setPosition(int id,int type,float x, float y){
			float xTile=x/_tileSize;
			float yTile=y/_tileSize-1;
			_type=type;
			if(_id!=id){
				_isActive=true;
				_id=id;
			}			
			_objX=(int) (xTile*ZOOM)-6;
			_objY=(int) (yTile*ZOOM)-6;		
		}
		public void draw(SpriteBatch batch,float parentAlpha){
			if(_isActive==true){
				if(_objY+_posY>0&&_objX+_posX<TARGET_WIDTH-10){
					_decoratorMap.get(_type).setPosition(getX()+_objX+_posX,getY()+_objY+_posY);
					_decoratorMap.get(_type).draw(batch,parentAlpha);
				}
			}
		}
		public void destroy(){
			_id=-1;
			_isActive=false;
		}
		public int getId(){
			return _id;
		}
		
	}
}

package pro.artwave.spaceshooter.view.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pro.artwave.fgm.utility.SimpleStorage;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.model.map.GameMapModel;
import pro.artwave.spaceshooter.model.resource.MissionResource;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * Klasa rysuje na podstawie bitmapy planszê. Reprezentuje piksele bitmapy odpowiednimi teksturami
 * Dziêki czemu mo¿na otrzymaæ rozbudowane mapy
 * Nale¿y zainicjowaæ klasê a nastêpnie dodaæ tekstury i wartoœci które prezentuje
 * Domyœlnie mapa ustawiona jest wyœrodkowana na 0,0 grupy, nale¿y umieœciæ grupê na œrodku
 * Wersja Klasy, rysuje œciany z rzutem izometrycznym, Nie mamy cieniowania
 * Mamy kilka wersji scian pod wzglêdem funkcjonalnym, mamy trzy Group
 * for path, for wall bottom i for wall top
 * 

 * @author Krzysztof
 *
 */
public class TileBoardIsometric{
	public static final int TYPE_WALL=1;
	public static final int TYPE_PATH=2;
	public static final int DEFAULT_PATH_ID=0xffffffff;
	public static final int DEFAULT_WALL_ID=0x000000ff;		
	
	private int _minTileX;
	private int _minTileY;
	private int _maxTileX;
	private int _maxTileY;	
	private Pixmap _map;
	
	private float _screenX;
	private float _screenY;		
	
	private int _width;
	private int _height;
	
	private GameAssetAtlas _gameAssetAtlas;
	
	private PrepareRender[] _renderList;
	/**
	 * Zawiera typy tile po ich id
	 * Map<id,<klasa z parametrami tile>>
	 */
	private SimpleStorage<MapTile> _mapTileList;
	private FlatTileActor _flatTileActor;
	/**
	 * Rysuje tile które s¹ œcie¿k¹, czyli na najni¿szym poziomie
	 */
	private FloorTileActor _floorTileActor;
	
	/**
	 * Rysuje podstawê œciany
	 */
	private BottomTileActor _bottomWallTileActor;
	
	/**
	 * Rysuje górê œciany
	 */
	private TopTileActor _topWallTileActor;
	/**
	 * Nale¿y zawsze zainicjowaæ klasê
	 * @param map zawiera bitmapê której piksele bêd¹ rzutowane na grafikê
	 * @param tileSizeX szerokoœæ w jednostkach viewport textury prezentuj¹cej piksel
	 * @param tileSizeY wysokoœæ w jednostkach viewport textury prezentuj¹cej piksel
	 * @param numTileX liczba tekstur X jakie maj¹ byæ widoczne na ekranie. Klasa nie prezentuje wszystkich tylko ze wzglêdu
	 * na wydajnoœc tylko te widoczne na ekranie
	 * @param numTileY liczba tekstur X jakie maj¹ byæ widoczne na ekranie. Klasa nie prezentuje wszystkich tylko ze wzglêdu
	 * na wydajnoœc tylko te widoczne na ekranie
	 */
	public void init(Pixmap map,int numTileX,int numTileY,ArrayList<MissionResource.Tile> gameTileList){	
		_gameAssetAtlas=new GameAssetAtlas();
		_map=map;
		_minTileX=numTileX;
		_maxTileX=numTileX;
		_minTileY=numTileY;
		_maxTileY=numTileY;
		_mapTileList=new SimpleStorage<MapTile>(gameTileList.size()+2);
		for(MissionResource.Tile resourceTile:gameTileList){
			MapTile tile=new MapTile();
			tile.init(resourceTile.id,resourceTile.type,resourceTile.image);
			_mapTileList.add(resourceTile.id,tile);
		}
		/**
		 * Sprawdzamy czy zosta³y przedefiniowane bazowe pola
		 */
		if(_mapTileList.get(DEFAULT_WALL_ID)==null){
			MapTile tile=new MapTile();
			tile.init(DEFAULT_WALL_ID,TYPE_WALL,1);
			_mapTileList.add(DEFAULT_WALL_ID,tile);
		}		
		if(_mapTileList.get(DEFAULT_PATH_ID)==null){
			MapTile tile=new MapTile();
			tile.init(DEFAULT_PATH_ID,TYPE_PATH,2);
			_mapTileList.add(DEFAULT_PATH_ID,tile);
		}
		int listSize=(_minTileX+_maxTileX)*(_minTileY+_maxTileY);
		_renderList=new PrepareRender[listSize];
		for(int i=0;i<listSize;i++){
			_renderList[i]=new PrepareRender();
		}
		this.setSize(_map.getWidth()*GameMapModel.TILE_SIZE, _map.getHeight()*GameMapModel.TILE_SIZE);
		
		_flatTileActor=new FlatTileActor();
		_flatTileActor.setSize(_width,_height);
		_flatTileActor.init(_renderList);
		
		_floorTileActor=new FloorTileActor();
		_floorTileActor.setSize(_width,_height);
		_floorTileActor.init(_renderList);
		_bottomWallTileActor=new BottomTileActor();
		_bottomWallTileActor.setSize(_width,_height);
		_bottomWallTileActor.init(_renderList);
		_topWallTileActor=new TopTileActor();
		_topWallTileActor.setSize(_width,_height);		
		_topWallTileActor.init(_renderList);
	}
	public Actor getFlatTileActor(){
		return _flatTileActor;
	}	
	public Actor getFloorTileActor(){
		return _floorTileActor;
	}
	public Actor getBottomWallActor(){
		return _bottomWallTileActor;
	}
	public Actor getTopWallTileActor(){
		return _topWallTileActor;
	}	
	public void setSize(int width,int height){
		_width=width;
		_height=height;
	}
	public int getWidth(){
		return _width;
	}
	public int getHeight(){
		return _height;
	}
	public int getTileType(int id){
		MapTile tile=_mapTileList.get(id);
		if(tile!=null){
			return tile._type;
		}else{
			return TYPE_PATH;
		}
	}
	public int getTileType(int x,int y){
		int id=_map.getPixel(x,_map.getHeight()-y);
		MapTile tile=_mapTileList.get(id);
		if(tile==null){
			tile=_mapTileList.get(DEFAULT_PATH_ID);
		}
		return tile._type;
	}
	public void modifyTile(int tileId,int type,int imageId){
		MapTile mapTile=_mapTileList.get(tileId);
		if(mapTile==null){
			return;
		}else{
			if(type==TYPE_WALL){
				mapTile.setType(TYPE_WALL);
			//	mapTile.setShading(true);
			}else{
				mapTile.setType(TYPE_PATH);
			//	mapTile.setShading(false);
			}
			if(imageId>0){
				mapTile.setImage(imageId);
			}			
		}
	}
	/**
	 * Metoda sprawdza czy w danej pozycji s¹ przeszkody.
	 * @param screenX pozycja x
	 * @param screenY pozucja y
	 * @return true droga wolna; false mamy przeszkody
	 */
	public boolean checkPosition(float screenX,float screenY,int collisionRadius){
		ArrayList<Vector2> hitArea=this._checkPositionTiles(screenX, screenY,collisionRadius);
		for(Vector2 point:hitArea){
		int id=_map.getPixel((int)point.x,_map.getHeight()-(int)point.y);
			MapTile tile=_mapTileList.get(id);
			if(tile!=null&&tile._type==TYPE_WALL){
				return false;
			}
		}
		return true;
	}
	/**
	 * Zwraca listê punktów które sprawdzamy pod k¹tem kolizji
	 * @param screenX
	 * @param screenY
	 * @param collisionRadius
	 * @return
	 */
	private ArrayList<Vector2> _checkPositionTiles(float screenX,float screenY,int collisionRadius){
		_screenX=screenX;
		_screenY=screenY;
		ArrayList<Vector2> hitArea=new ArrayList<Vector2>(16);
		float radius=collisionRadius;
		int x0=(int) Math.floor((_screenX-radius)/GameMapModel.TILE_SIZE);
		int x1=(int) Math.floor((_screenX+radius)/GameMapModel.TILE_SIZE);
		int y0=(int) Math.floor((_screenY-radius)/GameMapModel.TILE_SIZE);
		int y1=(int) Math.floor((_screenY+radius)/GameMapModel.TILE_SIZE);		
		for(int i=x0;i<=x1;i++){
			for(int j=y0;j<=y1;j++){
				hitArea.add(new Vector2(i,j));
			}
		}	
		return hitArea;
	}
	/**
	 * Metodê mo¿na wywo³ywaæ co 30 wywo³anie, oblicza tile na mapie
	 * @param screenX
	 * @param screenY
	 */
	public void calculateVisibleTile(float screenX,float screenY){
		_screenX=screenX;
		_screenY=screenY;
		int posX=Math.round(_screenX/GameMapModel.TILE_SIZE);
		int minX=posX-_minTileX;
		
		if(minX<0)minX=0;
		int maxX=posX+_maxTileX;
		if(maxX>_map.getWidth())maxX=_map.getWidth();
		int posY=Math.round(_screenY/GameMapModel.TILE_SIZE);
		
		int minY=posY-_minTileY;
		if(minY<0)minY=0;
		int maxY=posY+_maxTileY;		
		if(maxY>_map.getHeight())maxY=_map.getHeight();
		for(int i=0;i<_renderList.length;i++){		
			_renderList[i].isSet=false;
		}
	//	Map<Integer,Integer> typeMap=getTypeMap(minX,minY,maxX,maxY);

		int count=0;
		for(int y=maxY-1;y>=minY;y--){
			for(int x=minX;x<maxX;x++){
				int id=_map.getPixel(x,_map.getHeight()-y);
				if(id==0)continue;
				MapTile tile=_mapTileList.get(id);
				if(tile==null){
					tile=_mapTileList.get(DEFAULT_PATH_ID);
				}			
				_renderList[count].mapTile=tile;
				_renderList[count].isSet=true;
				/**
				 * do x i y musi byæ dodany getX() aktora!!!
				 */
				_renderList[count].x=x*GameMapModel.TILE_SIZE;
				_renderList[count].y=y*GameMapModel.TILE_SIZE;
				count++;
			}				
		}	
	//	System.out.println("tileBoard count tile:"+count+" teoretical "+_renderList.length+" _minTileX"+minX+" _maxTileX"+maxX+" _minTileY"+_minTileY+" _maxTileY"+_maxTileY);
	}
	protected class MapTile{
		private int _id;
		private int _type;//wall || path
		private int _imageId;
		private TextureRegion _regionFlat;
		private TextureRegion _regionBottom;
		private TextureRegion _regionTop;
		public void init(int id,String typeName,int imageId){
			int type=2;
			if(typeName.equals("wall")){
				type=TYPE_WALL;
			}else if(typeName.equals("path")){
				type=TYPE_PATH;
			}
			init(id,type,imageId);
		}
		@Override
		public String toString(){
			return "MapTile _id:"+_id+" _type:"+_type+" _imageId:"+_imageId+" _regionFloor"+_regionFlat+" _regionBottom"+_regionBottom+" _regionTop"+_regionTop+"\n";
		}
		public void init(int id,int type,int imageId){
			_id=id;
			setType(type);
			setImage(imageId);
		}
		public int getType(){
			return _type;
		}
		public void setType(int type){
			_type=type;
		}
		public void setImage(int imageId){
			_imageId=imageId;
			_regionFlat=(TextureRegion)_gameAssetAtlas.getTile(_imageId,GameAssetAtlas.TILE_TYPE_FLAT);
		//	System.out.println("img "+_regionFlat);
			_regionBottom=(TextureRegion)_gameAssetAtlas.getTile(_imageId,GameAssetAtlas.TILE_TYPE_BOTTOM);
			_regionTop=(TextureRegion)_gameAssetAtlas.getTile(_imageId,GameAssetAtlas.TILE_TYPE_TOP);
		}
		public TextureRegion getRegionFlat(){
			return _regionFlat;
		}
		public TextureRegion getRegionBottom(){
			return _regionBottom;
		}
		public TextureRegion getRegionTop(){
			return _regionTop;
		}		
	}
	protected class PrepareRender{
		public boolean isSet;
		public MapTile mapTile;
	//	public int angle;
		public float x;
		public float y;
	}
}

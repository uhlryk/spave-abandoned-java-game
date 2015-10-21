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
 * 
 * Klasa ró¿ni siê od TileBoard tym ¿e tu cieniujemy bloki widoczne z góry
 * a w przypadku TileBoard je przedstawiamy izometrycznie
 * Nie ma cieniowañ ale s¹ rózne typy tilów pod wzglêdem wyœwietlañ
 * @author Krzysztof
 * @see TileBoardIsometric
 *
 */
@Deprecated
public class TileBoardShader extends Actor {
	public static final int TYPE_WALL=1;
	public static final int TYPE_PATH=2;
	public static final int TYPE_OTHER=0;
	public static final int DEFAULT_PATH_ID=0xffffffff;
	public static final int DEFAULT_WALL_ID=0x000000ff;		
	
	private int _minTileX;
	private int _minTileY;
	private int _maxTileX;
	private int _maxTileY;	
	private Pixmap _map;
	
	private float _screenX;
	private float _screenY;		
	
	
	private GameAssetAtlas _gameAssetAtlas;
	
	private PrepareRender[] _renderList;
	/**
	 * Zawiera typy tile po ich id
	 * Map<id,<klasa z parametrami tile>>
	 */
	private SimpleStorage<MapTile> _mapTileList;

	/**
	 * Obie zmienne s³u¿¹ do znalezienie pozycji typów wzorów na tile
	 */
	private int[] _numX;
	private int[] _numY;
	
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
			tile.init(resourceTile.id,resourceTile.type,resourceTile.shading,resourceTile.image);
			_mapTileList.add(resourceTile.id,tile);
		}
		/**
		 * Sprawdzamy czy zosta³y przedefiniowane bazowe pola
		 */
		if(_mapTileList.get(DEFAULT_WALL_ID)==null){
			MapTile tile=new MapTile();
			tile.init(DEFAULT_WALL_ID,TYPE_WALL,true,1);
			_mapTileList.add(DEFAULT_WALL_ID,tile);
		}		
		if(_mapTileList.get(DEFAULT_PATH_ID)==null){
			MapTile tile=new MapTile();
			tile.init(DEFAULT_PATH_ID,TYPE_PATH,true,2);
			_mapTileList.add(DEFAULT_PATH_ID,tile);
		}
		int listSize=(_minTileX+_maxTileX)*(_minTileY+_maxTileY);
		_renderList=new PrepareRender[listSize];
		for(int i=0;i<listSize;i++){
			_renderList[i]=new PrepareRender();
		}
		this.setSize(_map.getWidth()*GameMapModel.TILE_SIZE, _map.getHeight()*GameMapModel.TILE_SIZE);
		
		_numX=new int[8];
		_numX[0]=-1;
		_numX[1]=0;
		_numX[2]=1;
		_numX[3]=1;
		_numX[4]=1;
		_numX[5]=0;
		_numX[6]=-1;
		_numX[7]=-1;
		_numY=new int[8];
		_numY[0]=1;
		_numY[1]=1;
		_numY[2]=1;
		_numY[3]=0;
		_numY[4]=-1;
		_numY[5]=-1;
		_numY[6]=-1;		
		_numY[7]=0;	
	}

	public int getTileType(int id){
		MapTile tile=_mapTileList.get(id);
		if(tile!=null){
			return tile._type;
		}else{
			return TYPE_OTHER;
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
				mapTile.setShading(true);
			}else{
				mapTile.setType(TYPE_PATH);
				mapTile.setShading(false);
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
			_renderList[i].region=null;
		}
	//	Map<Integer,Integer> typeMap=getTypeMap(minX,minY,maxX,maxY);

		int count=0;
		for(int y=minY;y<maxY;y++){
			for(int x=minX;x<maxX;x++){
				int id=_map.getPixel(x,_map.getHeight()-y);
				MapTile tile=_mapTileList.get(id);
				if(tile==null){
					tile=_mapTileList.get(DEFAULT_PATH_ID);
				}			
				if(tile.isShading()){
					ShadingTile shadingTile=getTileShading(id,x,y);
					_renderList[count].angle=shadingTile.angle;
					_renderList[count].region=tile.getRegion(shadingTile.type);
				
				}else{
					_renderList[count].angle=0;
					_renderList[count].region=tile.getRegion();
				}
				_renderList[count].isSet=true;
				_renderList[count].x=getX()+x*GameMapModel.TILE_SIZE;
				_renderList[count].y=getY()+y*GameMapModel.TILE_SIZE;
				count++;
			}				
		}	
	//	System.out.println("tileBoard count tile:"+count+" teoretical "+_renderList.length+" _minTileX"+minX+" _maxTileX"+maxX+" _minTileY"+_minTileY+" _maxTileY"+_maxTileY);
	}
	/**
	 * CIENIOWANIE
	 * super wa¿na metoda, sprawdza s¹siedztwo danego tile i rysuje go w wariancie odpowiednio wycieniowanym do tego
	 * @param typeMap
	 * @param x
	 * @param y
	 * @return
	 */
	private ShadingTile getTileShading(int id,int x,int y){
		ShadingTile shadingTile=new ShadingTile();
	//	int type=typeMap.get(getElementId(x,y));
		int status=1;

			
		for(int z=0;z<8;z++){
			int px=_numX[z];
			int py=_numY[z];
			status=status<<1;
			int sx=x+px;
			int sy=y+py;
			if(sx<0||sy<0){
				status=status|1;
				continue;
			}
			int subId=_map.getPixel(sx,_map.getHeight()-sy);
	//		int subId=(pixel&PATTERN_ID)>>>8;
			if(subId!=id){
				status=status|1;
			}
		}
		shadingTile.type=1;
		if(status==288){//288 100100000
			shadingTile.type=7;
			shadingTile.angle=0;
		}
		else if(status==384){//384 110000000
			shadingTile.type=7;
			shadingTile.angle=90;
		}
		else if(status==258){//258 100000010
			shadingTile.type=7;
			shadingTile.angle=180;
		}
		else if(status==264){//264 100001000
			shadingTile.type=7;
			shadingTile.angle=270;
		}		
		else if(status==296){//296 100101000
			shadingTile.type=8;
			shadingTile.angle=0;
		}
		else if(status==416){//416 416
			shadingTile.type=8;
			shadingTile.angle=90;
		}
		else if(status==210){//210 11010010
			shadingTile.type=8;
			shadingTile.angle=180;
		}
		else if(status==266){//266 100001010
			shadingTile.type=8;
			shadingTile.angle=270;
		}			
		else if(status==298){//298 100101010
			shadingTile.type=9;
			shadingTile.angle=0;
		}
		else if(status==424){//424 110101000
			shadingTile.type=9;
			shadingTile.angle=90;
		}
		else if(status==418){//418 110100010
			shadingTile.type=9;
			shadingTile.angle=180;
		}
		else if(status==394){//394 110001010
			shadingTile.type=9;
			shadingTile.angle=270;
		}		
		
		else if(status==426){//426 110101010
			shadingTile.type=10;
			shadingTile.angle=0;
		}			

		else if(status==290){//290 100100010 
			shadingTile.type=15;
			shadingTile.angle=0;
		}	
		else if(status==392){//392 110001000
			shadingTile.type=15;
			shadingTile.angle=90;
		}				
		
		else if((status&341)==341){//341 101010101
			shadingTile.type=6;
			shadingTile.angle=0;
		}
		else if((status&341)==340){//340 101010100
			shadingTile.type=5;
			shadingTile.angle=0;
		}
		else if((status&341)==337){//337 101010001
			shadingTile.type=5;
			shadingTile.angle=90;
		}
		else if((status&341)==325){//325  101000101
			shadingTile.type=5;
			shadingTile.angle=180;
		}
		else if((status&341)==277){//277 100010101
			shadingTile.type=5;
			shadingTile.angle=270;
		}
		else if((status&324)==324){//324 101000100
			shadingTile.type=4;
			shadingTile.angle=0;
		}
		else if((status&273)==273){//277 100010001
			shadingTile.type=4;
			shadingTile.angle=90;
		}		
		else if((status&343)==336){//343 101010111 ; 336 101010000
			shadingTile.type=3;
			shadingTile.angle=0;
		}		
		else if((status&349)==321){//349 101011101 ; 321 101000001
			shadingTile.type=3;
			shadingTile.angle=90;
		}	
		else if((status&373)==261){//373 101110101 ; 261 100000101
			shadingTile.type=3;
			shadingTile.angle=180;
		}	
		else if((status&469)==276){//469 111010101 ; 276 100010100
			shadingTile.type=3;
			shadingTile.angle=270;
		}	
		else if((status&351)==320){//351 101011111 ; 320 101000000
			shadingTile.type=2;
			shadingTile.angle=0;
		}		
		else if((status&381)==257){//381 101111101 ; 257 100000001
			shadingTile.type=2;
			shadingTile.angle=90;
		}	
		else if((status&501)==260){//501 111110101 ; 260 100000100
			shadingTile.type=2;
			shadingTile.angle=180;
		}	
		else if((status&471)==272){//471 111010111 ; 272 100010000
			shadingTile.type=2;
			shadingTile.angle=270;
		}	
		else if((status&381)==289){//381 101111101 ; 289 100100001
			shadingTile.type=11;
			shadingTile.angle=0;
		}		
		else if((status&501)==388){//501 111110101 ; 388 110000100
			shadingTile.type=11;
			shadingTile.angle=90;
		}	
		else if((status&471)==274){//471 111010111 ; 274 100010010
			shadingTile.type=11;
			shadingTile.angle=180;
		}	
		else if((status&351)==328){//351 101011111 ; 328 101001000
			shadingTile.type=11;
			shadingTile.angle=270;
		}	
		else if((status&381)==265){//381 101111101 ; 265 100001001
			shadingTile.type=14;
			shadingTile.angle=0;
		}		
		else if((status&501)==292){//501 111110101 ; 292 100100100
			shadingTile.type=14;
			shadingTile.angle=90;
		}	
		else if((status&471)==400){//471 111010111 ; 400 110010000
			shadingTile.type=14;
			shadingTile.angle=180;
		}	
		else if((status&351)==322){//351 101011111 ; 322 101000010
			shadingTile.type=14;
			shadingTile.angle=270;
		}		
		
		else if((status&373)==293){//373 101110101 ; 293 100100101
			shadingTile.type=13;
			shadingTile.angle=0;
		}		
		else if((status&469)==404){//469 111010101 ; 404 110010100
			shadingTile.type=13;
			shadingTile.angle=90;
		}	
		else if((status&343)==338){//343 101010111 ; 338 101010010
			shadingTile.type=13;
			shadingTile.angle=180;
		}	
		else if((status&349)==329){//349 101011101 ; 329 101001001
			shadingTile.type=13;
			shadingTile.angle=270;
		}	
		
		else if((status&351)==330){//349 101011111 ; 329 101001010
			shadingTile.type=12;
			shadingTile.angle=270;
		}	
		else if((status&381)==297){//349 101111101 ; 329 100101001
			shadingTile.type=12;
			shadingTile.angle=00;
		}	
		else if((status&501)==420){//349 111110101 ; 329 110100100
			shadingTile.type=12;
			shadingTile.angle=90;
		}	
		else if((status&471)==402){//349 111010111 ; 329 110010010
			shadingTile.type=12;
			shadingTile.angle=180;
		}	
		
		return shadingTile;
	}
	private class ShadingTile{
		public int type;
		public int angle;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		for(int i=0;i<_renderList.length;i++){		
			if(_renderList[i].isSet){
				//batch.draw(_renderList[i].region,_renderList[i].x,_renderList[i].y,_tileSizeX,_tileSizeY);				
				batch.draw(_renderList[i].region,_renderList[i].x,_renderList[i].y,GameMapModel.TILE_SIZE/2,GameMapModel.TILE_SIZE/2,GameMapModel.TILE_SIZE,GameMapModel.TILE_SIZE,1,1,(float)_renderList[i].angle);
			}
		}	
	}
	private class MapTile{
		private int _id;
		private int _type;//wall || path
		private int _imageId;
		private boolean _isShading;// czy cieniowania u¿ywamy
		private Map<Integer,TextureRegion> _regionList;
		public void init(int id,String typeName,int isShading,int imageId){
			int type=2;
			if(typeName.equals("wall")){
				type=TYPE_WALL;
			}else if(typeName.equals("path")){
				type=TYPE_PATH;
			}
			init(id,type,(isShading==1?true:false),imageId);
		}
		@Override
		public String toString(){
			return "MapTile _id:"+_id+" _type:"+_type+" _imageId:"+_imageId+" _isShading:"+_isShading+" _regionList:\n"+_regionList+"\n";
		}
		public void init(int id,int type,boolean isShading,int imageId){
			_id=id;
			setType(type);
			setShading(isShading);
			setImage(imageId);
		}
		public void setType(int type){
			_type=type;
		}
	/*	public int getType(){
			return _type;
		}
		public int getId(){
			return _id;
		}*/
		public void setShading(boolean shading){
			_isShading=shading;
		}	
		public void setImage(int imageId){
			_imageId=imageId;
			_regionList=new HashMap<Integer,TextureRegion>(17);
			_regionList.put(1,(TextureRegion)_gameAssetAtlas.getTile(_imageId+"_s1"));
			if(_isShading){
				for(int i=2;i<16;i++){
					_regionList.put(i,(TextureRegion)_gameAssetAtlas.getTile(_imageId+"_s"+i));
				}
			}			
		}
		public boolean isShading(){
			return _isShading;
		}
		/**
		 * Zwracamy region dla danego tile domyœlny, niezale¿nie czy ma shading czy nie ten jeden pusty musi
		 * @return
		 */
		public TextureRegion getRegion(){
			return _regionList.get(1);
		}
		/**
		 * Zwraca odpowiedni wycieniowany region. Jeœli nie wspiera go to zwraca domyœlny
		 * @param shadingType
		 * @return
		 */
		public TextureRegion getRegion(int shadingType){
			TextureRegion region=_regionList.get(shadingType);
			if(region!=null){
				return region;
			}else{
				return getRegion();//ka¿dy tile musi mieæ domyœlny o type 1; pozosta³e s¹ opcjonalne
			}
		}
	}
	private class PrepareRender{
		public boolean isSet;
		public TextureRegion region;
		public int angle;
		public float x;
		public float y;
	}
}

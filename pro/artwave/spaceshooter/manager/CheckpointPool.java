package pro.artwave.spaceshooter.manager;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.model.asset.XSmallAssetBitmapFont;
import pro.artwave.spaceshooter.model.map.GameMapModel;
import pro.artwave.spaceshooter.view.helper.BoardMarker;
import pro.artwave.spaceshooter.view.unit.Ship;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CheckpointPool{
	public static final boolean DEBUG=false;
	public static final int DEFAULT_RADIUS=50;
	private Map<Integer,Element> _elementList;
	private Pixmap _map;

	private BitmapFont _debugFont;	
	private ViewActor _view;
	
	public void init(Pixmap map){
		XSmallAssetBitmapFont _smallFontAsset=new XSmallAssetBitmapFont();
		_view=new ViewActor();
		_view.init();
		_debugFont=_smallFontAsset.getFont();
		_debugFont.setColor(1,1,1,1);		
	
		_map=map;
		_elementList=new HashMap<Integer,Element>(20);
	}
	/**
	 * Pozwala przekazaæ widok danego managera do widoku g³ównego, dziêki czemu mamy rozdzielanie 
	 * managera od widoku
	 * @return
	 */
	public Actor getView(){
		return _view;
	}
	/**
	 * Podajemy vektor pozycji tile czyli x i y a system wylicza pozycjê
	 * @param tile
	 * @return
	 */
	private Vector2 calculatePositionFromTile(Vector2 tile){
		return new Vector2(tile.x*GameMapModel.TILE_SIZE+(GameMapModel.TILE_SIZE)/2,tile.y*GameMapModel.TILE_SIZE+(GameMapModel.TILE_SIZE)/2);
	}
	/**
	 * Tworzy nowy checkpoint z danego koloru wystêpuj¹cego na mapie.
	 * Sam wyszukuje ten kolor i jeœli znajdzie ustawia tam checkpointa. 
	 * Mo¿e siê zdarzyæ ¿e bêdzie kilka takich samych checkpointów w wyniku 
	 * tego samego koloru u¿ytego na mapie w kilku miejscach. W tym celu u¿yty bêdzie tylko pierwszy przypadek 
	 * pojawienia siê koloru. Oznacza to te¿ ¿e dany kolor mapy mo¿e mieæ tylko jeden checkpoint.
	 * Zgadza siê to z definicj¹
	 * 
	 * Mamy jeszcze drug¹ metodê tworz¹c¹ checpointa. Ró¿nica miêdzy nimi polega na tym, ¿e ta metoda deklaruje checkpoint
	 * dla istniej¹cego koloru. A druga metoda tworzy jakby nowy kolor w danym miejscu nie zmieniaj¹c orygina³u
	 * @see createCheckpoint(int id,int x,int y)
	 * @param id pixel kolor którego szukamy na mapie
	 */
	public void createCheckpoint(int idColor){
		for(int x=0;x<_map.getWidth();x++){
			for(int y=0;y<_map.getHeight();y++){
				int pixel=_map.getPixel(x,_map.getHeight()-y);
				if(idColor==pixel){					
		//			System.out.println("x "+x+" y "+y+" generate cp"+Integer.toHexString(pixel));
					Element element=new Element();
					Vector2 tile=new Vector2(x,y);
					element.tile=tile;
					element.position=calculatePositionFromTile(tile);		
					element.isActive=false;
					element.isBoardVisible=true;
					element.radius=DEFAULT_RADIUS;
					_elementList.put(idColor, element);
					return;//po pierwszym znalezieniu koloru koñczymy
				}
			}
		}	System.out.println("Error  CheckpointPool.createCheckpoint("+Integer.toHexString(idColor)+") there is no such color on map it was impossible to create checkpoint");
	}
	/**
	 * tworzymy wektor. y ma pozycje normalnie id¹c od do³u
	 * 
	 * Nie mo¿emy kodem tworzyæ nowych id, poniewa¿ inne komendy z xml jak maj¹ u¿ywaæ checkpointa to musz¹ znaæ
	 * jego id. Czyli id nowego checkpointa musi byæ znane na poziomie projektowym
	 * 
	 * Jeœli obiekt istnieje pod danym id to zmienia jego lokalizacje na now¹
	 * W przeciwnym razie tworzy nowy
	 * @param x
	 * @param y
	 * @return
	 */
	public void createCheckpoint(int id,int x,int y){
		if(_elementList.containsKey(id)){
			Element element=_elementList.get(id);
			Vector2 tile=new Vector2(x,y);
			element.tile=tile;
			element.position=calculatePositionFromTile(tile);	
			return;
		}
		Vector2 tile=new Vector2(x,y);
		Element element=new Element();
		element.tile=tile;
		element.position=calculatePositionFromTile(tile);		
		element.isActive=false;
		element.isBoardVisible=true;
		element.radius=DEFAULT_RADIUS;
		_elementList.put(id, element);	

	}
	/**
	 * Sprawdza akcje odpalane na kolizje
	 * @param x
	 * @param y
	 * @param radius
	 */
	public void calculateCollision(Ship ship){
		for(Entry<Integer, Element> elementEntry:_elementList.entrySet()){
			Element element=elementEntry.getValue();
			if(element.isActive){
				if(ship.calculateObjectCollision(element.position,element.radius,0)==false){
					element.isHit=true;
				}
			}
		}
		
	}
	public boolean isHit(int id){
		return _elementList.get(id).isHit;
	}
	public void setHit(int id,boolean isHit){
		_elementList.get(id).isHit=isHit;
	}
	/**
	 * Metoda zwraca pozycjê checkpointa po jego id
	 * @param id
	 * @return
	 */
	public Vector2 getPosition(int id){
		if(_elementList.containsKey(id)){
			return _elementList.get(id).position;
		}else{
			System.out.println("Error CheckpointPool.getPosition("+Integer.toHexString(id)+") there is no such checkpoint");
			return null;
		}
	}
	/**
	 * Aktywuje checkpoint o danym id
	 * @param id
	 */
	public void setActive(int id){
		if(_elementList.containsKey(id)){
			_elementList.get(id).isHit=false;
			_elementList.get(id).isActive=true;			
	//		System.out.println("active Checkpoint id"+id);
		}else{
			System.out.println("Error CheckpointPool.setActive("+Integer.toHexString(id)+") there is no such checkpoint");
		}
	}
	public void setDeactive(int id){
		if(_elementList.containsKey(id)){
			_elementList.get(id).isHit=false;
			_elementList.get(id).isActive=false;
		}else{
			System.out.println("Error CheckpointPool.setDeactive("+Integer.toHexString(id)+") there is no such checkpoint");
		}
	}	
	/**
	 * ustawia promieñ aktywnoœci checkpointa. czyli jak blisko musi byæ statek gracza by checkpoint odpali³ trigger
	 * @param id
	 * @param radius
	 */
	public void setRadius(int id,int radius){
		if(_elementList.containsKey(id)){
			_elementList.get(id).radius=radius;		
		}else{
			System.out.println("Error CheckpointPool.setDeactive("+Integer.toHexString(id)+") there is no such checkpoint");
		}
	}	
	/**
	 * Metoda pozwala w³¹czyæ/wy³¹czyæ widocznoœæ pusuj¹cych trójk¹tów przedstawiaj¹cych checkpointa.
	 * Mog¹ byæ wiêc niewidoczne triggery na mapie, realizuj¹ce jakieœ zadanie
	 * @param id
	 * @param visibility
	 */
	public void setBoardVisible(int id,boolean visibility){
		if(_elementList.containsKey(id)){
			_elementList.get(id).isBoardVisible=visibility;		
		}else{
			System.out.println("Error CheckpointPool.setDeactive("+Integer.toHexString(id)+") there is no such checkpoint");
		}
	}		
	/**
	 * Metoda sprawdza czy mamy ju¿ checkpoint o danym id
	 * @param id
	 * @return true mamy; false nie ma
	 */
	public boolean isCheckpoint(int id){
		return _elementList.containsKey(id);
	}
	/**
	 * Wydzielona czêœæ kontrolera bêd¹ca widokiem
	 * @author Krzysztof
	 *
	 */
	private class ViewActor extends Actor{
		private BoardMarker _boardMarker;
		public void init(){
			_boardMarker=new BoardMarker();
			_boardMarker.init();
		}
		@Override
		public void act (float delta) {
			_boardMarker.act(delta);
		}		
		@Override
		public void draw(SpriteBatch batch,float alpha){
			for(Entry<Integer, Element> elementEntry:_elementList.entrySet()){
				Element element=elementEntry.getValue();
				if(element.isActive&&element.isBoardVisible){
					_boardMarker.setPosition(this.getX()+element.position.x,this.getY()+element.position.y);
					_boardMarker.draw(batch, alpha);			
				}
				
				
				if(DEBUG){
					_debugFont.draw(batch,"cp id:"+Integer.toHexString(elementEntry.getKey())+" x:"+element.tile.x+" y:"+element.tile.y,element.position.x-35,element.position.y+50);
				}
			//	System.out.println(checkpoint.image.getX()+"  "+checkpoint.image.getY());
			}
		}
	}


	private class Element{
		private int radius;
		private boolean isActive;
		private boolean isBoardVisible;
		/**
		 * W momencie gdy statek dotar³ do danego punktu
		 */
		private boolean isHit;
		public Vector2 tile;
		public Vector2 position;			
	}
}

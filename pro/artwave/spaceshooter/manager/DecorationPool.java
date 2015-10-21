package pro.artwave.spaceshooter.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pro.artwave.spaceshooter.helper.DecorationParams;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.model.asset.XSmallAssetBitmapFont;
import pro.artwave.spaceshooter.model.map.GameMapModel;
import pro.artwave.spaceshooter.model.resource.DecorationResource;
import pro.artwave.spaceshooter.model.resource.MissionResource;


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
/**
 * Klasa obs³uguje dekoracje wyœwietlane na mapie
 * Same dekoracje nie maj¹ interakcji
 * Ale inne kontrolery mog¹ j¹ modyfikowaæ
 * Np strza³ki mog¹ byæ wyœwietlane lub usuwane, obracane
 * Mo¿emy mieæ tu strza³ki, obiekty teleportów itp
 * Dekoracje nie maj¹ kolizji
 * Teleport jest tylko grafik¹
 * Przemieszcza TaskManager!
 * Dekoracje s¹ zaczytywane z xml i bitmapy
 * @author Krzysztof
 *
 */
public class DecorationPool{
	public static final boolean DEBUG=false;

	private Map<Integer,Element> _elementList;
	private Pixmap _map;	
	

	private BitmapFont _debugFont;
	private ViewActor _view;
	private GameAssetAtlas _gameAssetAtlas;
	public void init(DecorationResource decorationResource,ArrayList<MissionResource.Decoration> gameDecorationList,Pixmap map){
		_view=new ViewActor();
		_view.init();
		XSmallAssetBitmapFont _smallFontAsset=new XSmallAssetBitmapFont();
		_debugFont=_smallFontAsset.getFont();
		_debugFont.setColor(1,1,1,1);
		_gameAssetAtlas=new GameAssetAtlas();
		_map=map;
		_elementList=new HashMap<Integer,Element>(gameDecorationList.size());
		Map<Integer,DecorationParams> decorationParamsMap=decorationResource.getDecorationMap();
		for(int i=0;i<gameDecorationList.size();i++){
			MissionResource.Decoration decoration=gameDecorationList.get(i);
			DecorationParams decorationParams=decorationParamsMap.get(decoration.image);
			if(decorationParams==null){
				throw new RuntimeException("There is no decoration image having id "+decoration.image);
			}
			if(decorationParams!=null){
				Element element=new Element(decorationParams);
				element.isVisible=decoration.visible==1?true:false;
				element.setRotation(decoration.rotation);
				_elementList.put(decoration.id, element);
			}
		}

		generate();
	}
	private void generate(){
		int maxX=_map.getWidth();
		int maxY=_map.getHeight();
		for(int x=0;x<maxX;x++){
			for(int y=0;y<maxY;y++){
				int mapDecorationId=_map.getPixel(x,_map.getHeight()-y);
				Element element=_elementList.get(mapDecorationId);
				if(element==null)continue;
				Vector2 tile=new Vector2(x,y);
				Vector2 position=new Vector2(tile.x*GameMapModel.TILE_SIZE,tile.y*GameMapModel.TILE_SIZE);		
				element.addPosition(position, tile);
			}
		}	
	}	
	public void changeImage(int id,int image){
		Element element=_elementList.get(id);
		if(element==null){
			throw new RuntimeException("There is no decoration id "+Integer.toHexString(id));
		}
		element.setImage(image);
	}
	/**
	 * Pozwala ustawiæ by niewidzialny element by³ widzialna
	 * @param id
	 */
	public void setVisible(int id){
		_elementList.get(id).isVisible=true;
	}
	/**
	 * Ustawia by dany element by³ niewidzialny
	 * @param id
	 */
	public void setInvisible(int id){
		_elementList.get(id).isVisible=false;
	}
	/**
	 * Zwraca tile pos danego dekoratora o id i indexie (o danym id mo¿e byæ wiêcej elementów)
	 * @param id
	 * @param index
	 * @return
	 */
	public Vector2 getTilePosition(int id,int index){
		return _elementList.get(id)._positionList.get(index).tile;
	}
	/**
	 * Obraca dekorator o wskazany k¹t
	 * @param id
	 * @param angle
	 */
	public void rotate(int id,int angle){
		_elementList.get(id).getImage().rotate(angle);
	}
	public Actor getView(){
		return _view;
	}
	private class ViewActor extends Actor{
		private float _alpha;
		private float _alphaParam;
		private boolean _isFade;			
		public void init(){
			_alpha=1;
			_alphaParam=1;
			_isFade=false;		
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
		}			
		@Override
		public void draw(SpriteBatch batch,float alpha){
			for(Entry<Integer, Element> elementEntry:_elementList.entrySet()){
				Element element=elementEntry.getValue();
				if(element.isVisible){
					for(Element.Position pos:element.getPositionList()){
						//System.out.println("Draw Decoration");
						element.getImage().setPosition(this.getX()+pos.position.x+element._offset.x,this.getY()+pos.position.y+element._offset.y);
						element.getImage().draw(batch,alpha);
					}
				}				
				if(DEBUG){
					for(DecorationPool.Element.Position pos:element.getPositionList()){
						_debugFont.draw(batch,"d id:"+Integer.toHexString(elementEntry.getKey())+" x:"+pos.tile.x+" y:"+pos.tile.y,pos.position.x-35,pos.position.y+35);
					}
				}
			//	System.out.println(checkpoint.image.getX()+"  "+checkpoint.image.getY());
			}
		}		
	}
	private class Element{
		private DecorationParams _params;
		private int _angle;
		public Element(DecorationParams params){
			_params=params;
			_positionList=new ArrayList<Position>(5);
			setImage(_gameAssetAtlas.createSpriteDecoration(_params.image));
		}
		private Sprite _image;
		public Vector2 _offset;
		public void setImage(int imageType){
			setImage(_gameAssetAtlas.createSpriteDecoration(imageType));
		}
		private void setImage(Sprite image){
			_image=image;
			_image.setRotation(_angle);
			_offset=new Vector2((GameMapModel.TILE_SIZE-image.getWidth())/2,(GameMapModel.TILE_SIZE-image.getHeight())/2);
			
		}
		public void setRotation(int angle){
			_angle=angle;
			_image.setRotation(angle);
		}
		public Sprite getImage(){
			return _image;
		}		
		public boolean isVisible;
		
		private ArrayList<Position> _positionList;
		public void addPosition(Vector2 position,Vector2 tile){
			Position pos=new Position();
			pos.position=position;
			pos.tile=tile;
			_positionList.add(pos);
		}
		public ArrayList<Position> getPositionList(){
			return _positionList;
		}
		public class Position{
			public Vector2 position;	
			public Vector2 tile;
		}
	}	
}

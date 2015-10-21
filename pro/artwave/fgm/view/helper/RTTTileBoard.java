package pro.artwave.fgm.view.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * TODO
 * Przepisaæ tak by cachowa³ ca³oœæ w blokach tekstur
 */

/**
 * Klasa rysuje na podstawie bitmapy planszê. Reprezentuje piksele bitmapy odpowiednimi teksturami
 * Dziêki czemu mo¿na otrzymaæ rozbudowane mapy
 * Nale¿y zainicjowaæ klasê a nastêpnie dodaæ tekstury i wartoœci które prezentuje
 * Domyœlnie mapa ustawiona jest wyœrodkowana na 0,0 grupy, nale¿y umieœciæ grupê na œrodku
 * @author Krzysztof
 *
 */
@Deprecated
public class RTTTileBoard extends Group {
	private int _tileSizeX;
	private int _tileSizeY;
	private Pixmap _map;
	
	private float _screenX;
	private float _screenY;		
	private float _centerX;
	private float _centerY;

	private Texture _texture;
	
	private Map<Integer,TextureRegion> _mapTile;
	/**
	 * Nale¿y zawsze zainicjowaæ klasê
	 * @param map zawiera bitmapê której piksele bêd¹ rzutowane na grafikê
	 * @param tileSize wysokoœæ i szerokoœæ w jednostkach viewport textury prezentuj¹cej piksel
	 * @param widthTexture liczba tekstur X jakie maj¹ byæ widoczne na ekranie. Klasa nie prezentuje wszystkich tylko ze wzglêdu
	 * na wydajnoœc tylko te widoczne na ekranie
	 * @param heightTexture liczba tekstur X jakie maj¹ byæ widoczne na ekranie. Klasa nie prezentuje wszystkich tylko ze wzglêdu
	 * na wydajnoœc tylko te widoczne na ekranie
	 */
	public void init(Pixmap map,int tileSize,int numTileXTexture,int numTileYTexture){
		this.init(map,tileSize,tileSize,numTileXTexture,numTileYTexture);
	}
	/**
	 * Nale¿y zawsze zainicjowaæ klasê
	 * @param map zawiera bitmapê której piksele bêd¹ rzutowane na grafikê
	 * @param tileSizeX szerokoœæ w jednostkach viewport textury prezentuj¹cej piksel
	 * @param tileSizeY wysokoœæ w jednostkach viewport textury prezentuj¹cej piksel
	 * @param numTileXTexture liczba tilów które bêd¹ ³¹czone w bloki dla poprawienia wydajnoœci
	 * Bloki bêd¹ wyœwietlane w momencie gdy bêd¹ potrzebne. Teoretycznie wystarczy jeden blok. ale przy
	 * du¿ych mapach mo¿e byæ lepiej podzieliæ na kilka
	 * @param numTileYTexture liczba tilów które bêd¹ ³¹czone w bloki dla poprawienia wydajnoœci
	 */
	public void init(Pixmap map,int tileSizeX,int tileSizeY,int numTileXTexture,int numTileYTexture){	
		_map=map;
		_tileSizeX=tileSizeX;
		_tileSizeY=tileSizeY;	
		_mapTile=new HashMap<Integer,TextureRegion>();
		/**
		 * Czy punkt 0,0 gry ma byæ w œrodku mapy czy w dolnym prawym rogu
		 * to obecne rozwi¹zanie ustawia na œrodku
		 */
		_centerX=_map.getWidth()*_tileSizeX/2;
		_centerY=_map.getHeight()*_tileSizeY/2;
	}
	public void createTextures(int numTileXTexture,int numTileYTexture){
	/*	int actNumXTiles=0;
		while(actNumXTiles<_map.getWidth()){
			int actNumYTiles=0;
			while(actNumYTiles<_map.getHeight()){
				int maxXTile=actNumXTiles+numTileXTexture;
				if(maxXTile>_map.getWidth())maxXTile=_map.getWidth();
				int maxYTile=actNumXTiles+numTileXTexture;
				if(maxYTile>_map.getHeight())maxYTile=_map.getHeight();
				Pixmap pixmap=new Pixmap(maxXTile-actNumXTiles,maxYTile-actNumYTiles, Format.RGBA8888);
				pixmap.setColor(0,0,0,0);
				pixmap.fill();
				for(int x=actNumXTiles;x<maxXTile;x++){
					for(int y=actNumYTiles;y<maxYTile;y++){
						int tile=_map.getPixel(x, y);
						if(_mapTile.containsKey(tile)){
							TextureRegion region=_mapTile.get(tile);
							region.getTexture().getTextureData();
						}
					}
				}
				actNumYTiles+=numTileYTexture;
			}
			actNumXTiles+=numTileXTexture;
		}*/
		SpriteBatch batch=new SpriteBatch();
		System.out.println("oczekiwany rozmiar buforu "+_map.getWidth()*_tileSizeX);
		FrameBuffer fbo=new FrameBuffer(Format.RGBA8888,_map.getWidth()*_tileSizeX,_map.getHeight()*_tileSizeY, false);
		if(_texture!=null)_texture.dispose();
		
		fbo.begin();
		batch.begin();
		System.out.println("rozmiar buforu "+fbo.getWidth());
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		for(int x=0;x<_map.getWidth();x++){
			for(int y=0;y<_map.getHeight();y++){
				int tile=_map.getPixel(x, y);
				if(_mapTile.containsKey(tile)){
					batch.draw(_mapTile.get(tile),x*_tileSizeX,y*_tileSizeY,_tileSizeX,_tileSizeY);
				}
			}
		}

		batch.end();
		fbo.end();
		_texture = fbo.getColorBufferTexture();
	//	fbo.dispose();		
		batch.dispose();
		
		System.out.println("tex size:"+_texture.getWidth()+" "+_texture.getHeight());
	}
	/**
	 * Mo¿emy zdefiniowaæ jaka tekstura ma byæ prezentowana dla danej wartoœci piksela
	 * Nie musz¹ byæ wszystkie zdefiniowane, wtedy bêdzie puste pole
	 * @param region textureRegion
	 * @param key wartoœæ koloru
	 */
	public void defineTile(TextureRegion region,int key){
		_mapTile.put(key,region);
	}
	public void setScreenPosition(float screenX,float screenY){
		_screenX=screenX/4+_centerX;
		_screenY=screenY/4+_centerY;	
	}
	public float getScreenX(){
		return _screenX;
	}
	public float getScreenY(){
		return _screenY;
	}	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(_texture,this.getX()-_screenX,this.getY()-_screenY);
	}
}

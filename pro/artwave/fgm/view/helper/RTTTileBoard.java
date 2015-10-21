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
 * Przepisa� tak by cachowa� ca�o�� w blokach tekstur
 */

/**
 * Klasa rysuje na podstawie bitmapy plansz�. Reprezentuje piksele bitmapy odpowiednimi teksturami
 * Dzi�ki czemu mo�na otrzyma� rozbudowane mapy
 * Nale�y zainicjowa� klas� a nast�pnie doda� tekstury i warto�ci kt�re prezentuje
 * Domy�lnie mapa ustawiona jest wy�rodkowana na 0,0 grupy, nale�y umie�ci� grup� na �rodku
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
	 * Nale�y zawsze zainicjowa� klas�
	 * @param map zawiera bitmap� kt�rej piksele b�d� rzutowane na grafik�
	 * @param tileSize wysoko�� i szeroko�� w jednostkach viewport textury prezentuj�cej piksel
	 * @param widthTexture liczba tekstur X jakie maj� by� widoczne na ekranie. Klasa nie prezentuje wszystkich tylko ze wzgl�du
	 * na wydajno�c tylko te widoczne na ekranie
	 * @param heightTexture liczba tekstur X jakie maj� by� widoczne na ekranie. Klasa nie prezentuje wszystkich tylko ze wzgl�du
	 * na wydajno�c tylko te widoczne na ekranie
	 */
	public void init(Pixmap map,int tileSize,int numTileXTexture,int numTileYTexture){
		this.init(map,tileSize,tileSize,numTileXTexture,numTileYTexture);
	}
	/**
	 * Nale�y zawsze zainicjowa� klas�
	 * @param map zawiera bitmap� kt�rej piksele b�d� rzutowane na grafik�
	 * @param tileSizeX szeroko�� w jednostkach viewport textury prezentuj�cej piksel
	 * @param tileSizeY wysoko�� w jednostkach viewport textury prezentuj�cej piksel
	 * @param numTileXTexture liczba til�w kt�re b�d� ��czone w bloki dla poprawienia wydajno�ci
	 * Bloki b�d� wy�wietlane w momencie gdy b�d� potrzebne. Teoretycznie wystarczy jeden blok. ale przy
	 * du�ych mapach mo�e by� lepiej podzieli� na kilka
	 * @param numTileYTexture liczba til�w kt�re b�d� ��czone w bloki dla poprawienia wydajno�ci
	 */
	public void init(Pixmap map,int tileSizeX,int tileSizeY,int numTileXTexture,int numTileYTexture){	
		_map=map;
		_tileSizeX=tileSizeX;
		_tileSizeY=tileSizeY;	
		_mapTile=new HashMap<Integer,TextureRegion>();
		/**
		 * Czy punkt 0,0 gry ma by� w �rodku mapy czy w dolnym prawym rogu
		 * to obecne rozwi�zanie ustawia na �rodku
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
	 * Mo�emy zdefiniowa� jaka tekstura ma by� prezentowana dla danej warto�ci piksela
	 * Nie musz� by� wszystkie zdefiniowane, wtedy b�dzie puste pole
	 * @param region textureRegion
	 * @param key warto�� koloru
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

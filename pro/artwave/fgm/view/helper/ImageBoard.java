package pro.artwave.fgm.view.helper;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Klasa pozwala dodaæ kilka te³ pod planszê, t³a przesuwaj¹ siê przeciwnie do lotu statku. 
 * Mamy dodatkowy factor dla ka¿dego t³a. Okreœla jak bardzo przesuniêcie statku przesuwa grafikê.
 * Czyli gdy wartoœæ dodatnia to grafika wolniej przesuwa siê. Dziêki czemu niektóre t³a sprawiaj¹
 * wra¿enie jakby by³y dalej ni¿ inne.
 * Nale¿y dodaæ t³o.
 * Domyœlnie grafiki uk³adaj¹ siê wyœrodkowane dla wspó³rzêdnych 0,0 grupy, Nale¿y grupê przesun¹æ na œrodek
 * @author Krzysztof
 *
 */
@Deprecated
public class ImageBoard extends Group {
//	private float _posX;
//	private float _posY;	
	private float _offsetX;
	private float _offsetY;
	private ArrayList<Texture> _textureList;
	private ArrayList<Integer> _widthList;
	private ArrayList<Integer> _heightList;
	private ArrayList<Float> _marginXList;
	private ArrayList<Float> _marginYList;
	
	private ArrayList<Float> _posXList;
	private ArrayList<Float> _posYList;
	/**
	 * Nale¿y inicjowaæ klasê.
	 */
	public void init(float offset){
		this.setTransform(false);
		_offsetX=offset;
		
		_textureList=new ArrayList<Texture>();
		_widthList=new ArrayList<Integer>();
		_heightList=new ArrayList<Integer>();
		_marginXList=new ArrayList<Float>();
		_marginYList=new ArrayList<Float>();
		
	
		
		_offsetY=_offsetX*this.getHeight()/this.getWidth();
	}
	/**
	 * Mo¿emy dodaæ nowe t³o
	 * @param texture t³a
	 * @param factor wspó³czynnik okreœlaj¹cy odleg³oœæ, czyli gdy wartoœæ jest dodatnia to takie t³o odpowiednio
	 * tyle razy wolniej siê cofa
	 * @param width wielkoœæ do jakiej t³o zostanie powiêkszone
	 * @param height wysokoœæ do jakiej t³o zostanie podniesione
	 */
	public void addBackground(Texture texture,float width){
		_textureList.add(texture);
		float ratio=width/texture.getWidth();
		_widthList.add((int) (ratio*texture.getWidth()));
		_heightList.add((int) (ratio*texture.getHeight()));	
		//_marginXList.add((float)((this.getWidth()-width)/2)+_offset);
		//_marginYList.add((float)((this.getHeight()-height)/2)+_offset);	
	}
	public void calculateVisibleArea(float posX,float posY){
//		_posX=posX;
//		_posY=posY;
		_posXList=new ArrayList<Float>();
		_posYList=new ArrayList<Float>();			
		for(int i=0;i<_textureList.size();i++){
			_posXList.add(posX+posX*2*_offsetX/this.getWidth()-posX*_widthList.get(i)/this.getWidth()-_offsetX);
			_posYList.add(posY+posY*2*_offsetY/this.getHeight()-posY*_heightList.get(i)/this.getHeight()-_offsetY);
		}
	}	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		for(int i=0;i<_textureList.size();i++){

	//		posX=_posX+_posX*2*_offsetX/this.getWidth()-_posX*_widthList.get(i)/this.getWidth()-_offsetX;
	//		posY=_posY+_posY*2*_offsetY/this.getHeight()-_posY*_heightList.get(i)/this.getHeight()-_offsetY;
	//		batch.draw(_textureList.get(i),getX()+posX,getY()+posY,_widthList.get(i),_heightList.get(i));
			batch.draw(_textureList.get(i),getX()+_posXList.get(i),getY()+_posYList.get(i),_widthList.get(i),_heightList.get(i));
		}
	}	
}

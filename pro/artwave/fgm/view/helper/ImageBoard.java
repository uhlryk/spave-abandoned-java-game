package pro.artwave.fgm.view.helper;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Klasa pozwala doda� kilka te� pod plansz�, t�a przesuwaj� si� przeciwnie do lotu statku. 
 * Mamy dodatkowy factor dla ka�dego t�a. Okre�la jak bardzo przesuni�cie statku przesuwa grafik�.
 * Czyli gdy warto�� dodatnia to grafika wolniej przesuwa si�. Dzi�ki czemu niekt�re t�a sprawiaj�
 * wra�enie jakby by�y dalej ni� inne.
 * Nale�y doda� t�o.
 * Domy�lnie grafiki uk�adaj� si� wy�rodkowane dla wsp�rz�dnych 0,0 grupy, Nale�y grup� przesun�� na �rodek
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
	 * Nale�y inicjowa� klas�.
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
	 * Mo�emy doda� nowe t�o
	 * @param texture t�a
	 * @param factor wsp�czynnik okre�laj�cy odleg�o��, czyli gdy warto�� jest dodatnia to takie t�o odpowiednio
	 * tyle razy wolniej si� cofa
	 * @param width wielko�� do jakiej t�o zostanie powi�kszone
	 * @param height wysoko�� do jakiej t�o zostanie podniesione
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

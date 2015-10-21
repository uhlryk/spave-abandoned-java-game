package pro.artwave.spaceshooter.view.root;

import pro.artwave.fgm.utility.Setting;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
/**
 * Klasa operuje na dodatkowej kamerze, kt�ra przesuwa si� za statkiem sterowanym przez gracza.
 * Elementy interfejsu s� niezmienne
 * @author Krzysztof
 *
 */
public class GameBoardHelper extends Group {
	private Camera _gameCamera;
	private Camera _stageCamera;
	/**
	 * Inicjujemy dane, pobieramy kamer� domy�ln�
	 * Tworzymy now� kamer� �ledz�c�.
	 * Okre�lamy parametry kamery �ledz�cej na identyczne z kamer� domy�ln�.
	 */
	public void init(){
		this.setTransform(false);
		_stageCamera=this.getStage().getCamera();
	//	_stageCamera.rotate(45,0,0,1);
		_gameCamera=new OrthographicCamera(_stageCamera.viewportWidth,_stageCamera.viewportHeight);
		_gameCamera.projection.set(_stageCamera.projection);
	//	_gameCamera.rotate(45,0,0,1);
		_gameCamera.translate(0,0,0);
	}
	public Camera getGameCamera(){
		return _gameCamera;
	}
	public Vector3 getCameraCenter(){
		return this.getGameCamera().position;
	}
	/**
	 * Metoda pozwala dynamicznie zmienia� po�o�enie kamery.
	 * Mamy ten sam viewport co kamery domy�lnej, pozycja kamery uwzgl�dnia wy�rodkowanie kamery domy�lnej
	 * @param x
	 * @param y
	 */
	public void setCameraCenter(float x,float y){
		_gameCamera.viewportWidth = _stageCamera.viewportWidth;
		_gameCamera.viewportHeight =_stageCamera.viewportHeight;
		this.getGameCamera().position.set(x-Setting.getGutter().width,y-Setting.getGutter().height+50, 0);
	//	System.out.println("Camera: pos:"+_gameCamera.position);
	}
	public void setDrawingArea(Actor drawingArea){
		addActor(drawingArea);
	}
	public void setFloorTile(Actor tileBoardActor){
		tileBoardActor.setPosition(0,0);
		addActor(tileBoardActor);
	}	
	public void setBottomTile(Actor tileBoard){
		tileBoard.setPosition(0,0);
		addActor(tileBoard);
	}	
	public void setTopTile(Actor tileBoard){
		tileBoard.setPosition(0,0);
		addActor(tileBoard);
	}	
	public void setBonusPool(Actor resourcePool){
		addActor(resourcePool);
	}	
	public void setLabelPool(Actor labelPool){
		addActor(labelPool);
	}		
	public void setCheckpointPool(Actor checkpointPool){
		addActor(checkpointPool);
	}	
	public void setDecorationPool(Actor decorationPool){
		addActor(decorationPool);
	}	
	public void setShadow(Actor shadow){
		addActor(shadow);
	}	
	public void setUserShip(Actor userShip){
		addActor(userShip);
	}
	public void setEnemyPool(Group enemyPool){
		addActor(enemyPool);
	}	
	public void setExplosionPool(Actor explosionPool){
		addActor(explosionPool);
	}
	public void setBulletPool(Actor bulletPool){
		addActor(bulletPool);
	}
	/**
	 * Aktualizujemy kamer�. Nast�nie do SpriteBatch wprowadzamy nowe parametry z dodatkowej kamery
	 * Rysujemy obiektu kt�re maj� by� narysowane przez dodatkow� kamer�, nast�pnie do SpriteBatch
	 * wprowadzamy parametry domy�lnej kamery
	 * W ten spos�b mo�emy operowa� na dowolnych kamerach
	 */
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		_gameCamera.update();
		batch.setProjectionMatrix(_gameCamera.combined);
		super.draw(batch, parentAlpha);
		batch.setProjectionMatrix(_stageCamera.combined);
	}
	
}

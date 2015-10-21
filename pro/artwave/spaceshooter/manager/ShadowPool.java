package pro.artwave.spaceshooter.manager;

import java.util.ArrayList;

import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Odpowiada za rysowanie cieni pod statkami
 * @author Krzysztof
 *
 */
public class ShadowPool {
	public static final int MAX_SIZE=20;
	public static final int OFFSET=20;
	private int _actSize;
	private ViewGroup _view;
	private ArrayList<Shadow> _posList;
	public void init(){
		_view=new ViewGroup();
		_view.init();
		_posList=new ArrayList<Shadow>(MAX_SIZE);
		_actSize=0;
		for(int i=0;i<MAX_SIZE;i++){
			Shadow pos=new Shadow();
			_posList.add(pos);
		}
	}
	public void clear(){
		_actSize=0;
	}
	public void addShadow(float x,float y,float angle,float alpha){
		Shadow pos=_posList.get(_actSize);
		pos.x=x;
		pos.y=y;
		pos.angle=angle;
		pos.alpha=alpha;
		_actSize++;
	}
	public Actor getView(){
		return _view;
	}	
	private class ViewGroup extends Actor{
		private Sprite _shadow;

		public void init(){
			GameAssetAtlas gameAssetAtlas=new GameAssetAtlas();
			_shadow=gameAssetAtlas.createSpriteShadow();
			_shadow.setOrigin(_shadow.getWidth()/2,_shadow.getHeight()/2);
			Vector2 position=new Vector2();

		}
		@Override
		public void act (float delta) {
			super.act(delta);
		}
		@Override
		public void draw(SpriteBatch batch,float alpha){
			for(int i=0;i<_actSize;i++){
				Shadow pos=_posList.get(i);
				_shadow.setPosition(pos.x-_shadow.getWidth()/2+this.getX()+OFFSET,pos.y-_shadow.getHeight()/2+this.getY()-OFFSET);
				_shadow.setRotation(pos.angle);
				_shadow.draw(batch,alpha*pos.alpha);
			}
		}
	}
	private class Shadow{
		float x;
		float y;
		float angle;
		float alpha;
	}
}

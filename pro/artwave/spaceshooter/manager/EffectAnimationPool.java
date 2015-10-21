package pro.artwave.spaceshooter.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.model.asset.ExplosionAnimation;
import pro.artwave.spaceshooter.model.asset.InterfaceEffectAnimation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
/**
 * Klasa rysuje wybuch po statku i jego animacjê.
 * U¿ywa ExplosionAssetImage
 * @author Krzysztof
 *
 */
public class EffectAnimationPool{
	public static final int TYPE_EXPLOSION=0;
	public static final int TYPE_BONUS=1;
	
	private float _delta;
	private int _explosionLimit=20;
	private ArrayList<Explosion> _explosionList;
	private Map<Integer,InterfaceEffectAnimation> _effectMap;
	private ViewActor _view;
	private Effects.Play _sound;
	public void init(){
		Effects effects=Effects.getInstance();
		_sound=effects.getExplosion();				
		_view=new ViewActor();
		_view.init();
		_explosionList=new ArrayList<Explosion>(_explosionLimit);
		for(int i=0;i<_explosionLimit;i++){
			Explosion explosion=new Explosion();
			_explosionList.add(explosion);
		}
		_effectMap=new HashMap<Integer, InterfaceEffectAnimation>(5);
	}
	public void addEffect(int id,InterfaceEffectAnimation atlas){
		_effectMap.put(id,atlas);
	}
	public void setDelta(float delta){
		_delta=delta;
	}

	public void newExplosion(int type,int x,int y){
		if(type==TYPE_BONUS)return;
		for(Explosion explosion:_explosionList){
			if(explosion.isActive==false){
				_sound.play();
				explosion.isActive=true;
				explosion.frame=0;
				explosion.type=type;
				int size=_effectMap.get(explosion.type).getSize();
				explosion.max=_effectMap.get(explosion.type).getMaxNum();
				explosion.speedFactor=_effectMap.get(explosion.type).getSpeedFactor();
				explosion.x=x-size/2;
				explosion.y=y-size/2;
				break;
			}
		}
	}
	public Actor getView(){
		return _view;
	}
	private class ViewActor extends Actor{
		public void init(){
			
		}
		@Override
		public void draw(SpriteBatch batch,float parentAlpha){
			for(Explosion explosion:_explosionList){
				if(explosion.isActive==true){
					batch.draw(_effectMap.get(explosion.type).getFrame((int)explosion.frame),explosion.x,explosion.y);
					explosion.frame+=_delta*explosion.speedFactor;
					
					if(explosion.max<explosion.frame){
						explosion.isActive=false;
						
					}
				}
			}
		}		
	}
	private class Explosion{
		public int x;
		public int y;
		public int max;
		public int speedFactor;
		public float frame;
		public int type;
		public boolean isActive;
	}
}

package pro.artwave.spaceshooter.view.helper;

import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Klasa rysuje trójk¹ty pulsuj¹ce na mapie
 * @author Krzysztof
 *
 */
public class BoardMarker{
	private float _alpha;
	private float _alphaParam;
	private boolean _isFade;		
	private Sprite _image;
	private float _x;
	private float _y;
	public void init(){
		GameAssetAtlas gameAssetAtlas=new GameAssetAtlas();
		_image=gameAssetAtlas.createSpriteCheckpointArrow();
		_alpha=1;
		_alphaParam=1;
		_isFade=false;				
	}
	public void act(float delta){
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
	public void setPosition(float x, float y){
		_x=x-_image.getWidth()/2;
		_y=y-30;
	}
	public void draw(SpriteBatch batch,float alpha){
		_image.setRotation(0);
		_image.setPosition(this._x,(float) (this._y+10-50*(Math.sin(_alpha)+0.4f)));
		_image.draw(batch,alpha);
		
		_image.setRotation(120);
		_image.setPosition((float) (this._x+50*(Math.sin(_alpha)+0.4f)),(float) (this._y+10+27*(Math.sin(_alpha)+0.4f)));
		_image.draw(batch,alpha);	
		
		_image.setRotation(240);
		_image.setPosition((float) (this._x-50*(Math.sin(_alpha)+0.4f)),(float) (this._y+10+27*(Math.sin(_alpha)+0.4f)));
		_image.draw(batch,alpha);	
	}
}

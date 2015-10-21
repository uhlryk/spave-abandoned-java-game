package pro.artwave.spaceshooter.view.root.game;

import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class GameHudLeft extends Group {
	private Sprite _bg;
	public void init(){
		this.setTransform(false);
		GameAssetAtlas gameAssetAtlas=new GameAssetAtlas();
		_bg=gameAssetAtlas.createSpriteHudLeft();
		this.setSize(222,181);
	}
	
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		
		_bg.setPosition(this.getX(),this.getY());
		_bg.draw(batch);
		super.draw(batch, parentAlpha);
	}
}

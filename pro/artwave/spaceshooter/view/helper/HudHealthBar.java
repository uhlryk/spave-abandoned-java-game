package pro.artwave.spaceshooter.view.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pro.artwave.fgm.view.helper.Bar;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;

public class HudHealthBar extends Bar {
	private Sprite _icon;
	
	public void init(int maxVal){	
		super.init(146,24);
		GameAssetAtlas _gameAssetAtlas=new GameAssetAtlas();
		this.setEmptyBar(_gameAssetAtlas.createSpriteBarEmpty());
		this.setFullBar(_gameAssetAtlas.createSpriteBarHealth());
		this.setMaxVal(maxVal);
		GameAssetAtlas gameAssetAtlas=new GameAssetAtlas();
		_icon=gameAssetAtlas.createSpriteResource(GameAssetAtlas.RESOURCE_HEALTH);
		_icon.setSize(45,45);
		
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);
		_icon.setPosition(this.getX()-50,this.getY()-10);
		_icon.draw(batch, parentAlpha);
	}
}

package pro.artwave.fgm.model.asset;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AssetAtlas extends Asset {
	@Override
	public abstract String getResourceName();

	@Override
	public Class<TextureAtlas> getClassName() {
		return TextureAtlas.class;
	}
	public TextureAtlas getAtlas(){
		return LoaderManager.getAssetManager().get(this.getResourceName(),this.getClassName());
	}
	public Sprite createSprite(String textureName){
		return getAtlas().createSprite(textureName);
	}
	public void setSmooth(){
		for(Texture texture:getAtlas().getTextures()){
			texture.setFilter(TextureFilter.Linear,TextureFilter.Linear);
		}
	}
}

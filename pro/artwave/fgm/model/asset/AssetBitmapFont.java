package pro.artwave.fgm.model.asset;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AssetBitmapFont extends Asset {

	public abstract String getResourceName();

	@Override
	public Class<BitmapFont> getClassName() {
		return BitmapFont.class;
	}
	public BitmapFont getFont(){
		return LoaderManager.getAssetManager().get(this.getResourceName(),this.getClassName());
	}
	public void setSmooth(){
		for(TextureRegion region:getFont().getRegions()){
			region.getTexture().setFilter(TextureFilter.Linear,TextureFilter.Linear);
		}
	}
}

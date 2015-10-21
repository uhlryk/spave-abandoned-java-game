package pro.artwave.fgm.model.asset;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Klasa do ³adowania jako modeli assetów typu pojedyñcza du¿a grafika.
 * @author Krzysztof
 *
 */
public abstract class AssetImage extends Asset {
	@Override
	public abstract String getResourceName();

	@Override
	public Class<Texture> getClassName() {
		return Texture.class;
	}
	public Texture getTexture(){
		return LoaderManager.getAssetManager().get(this.getResourceName(),this.getClassName());
		
	}
	public Sprite createSprite(){
		return new Sprite(getTexture());
	}
	public void setSmooth(){
		getTexture().setFilter(TextureFilter.Linear,TextureFilter.Linear);
	}
}

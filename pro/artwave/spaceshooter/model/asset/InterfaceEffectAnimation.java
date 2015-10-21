package pro.artwave.spaceshooter.model.asset;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface InterfaceEffectAnimation {
	int getSize();
	int getMaxNum();
	int getSpeedFactor();
	TextureRegion getFrame(int num);
}

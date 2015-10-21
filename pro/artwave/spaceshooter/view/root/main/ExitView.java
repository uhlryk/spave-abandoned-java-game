package pro.artwave.spaceshooter.view.root.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;

public class ExitView extends View {
	private GlobalTranslation _translation;		
	private float _fontX;
	public void init(){
		_translation=new GlobalTranslation();
		_translation.init();			
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);

	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
	}
}

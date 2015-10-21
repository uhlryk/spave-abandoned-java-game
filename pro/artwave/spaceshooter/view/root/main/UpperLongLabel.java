package pro.artwave.spaceshooter.view.root.main;

import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Blok wyœwietlany na samej górze na œrodku ekranu.
 * Wyœwietla siê w sekcji select
 * @author Krzysztof
 *
 */
public class UpperLongLabel extends Actor {
	private Sprite _background;
	private BitmapFont _font;
	private BitmapFont _debugFont;
	private String _text;
	private float _labelOffset;	
	public void init(String text){
		setSize(900,111);
		SmallOutlineAssetBitmapFont debugAssetFont=new SmallOutlineAssetBitmapFont();
		_debugFont=debugAssetFont.getFont();
		SmallOutlineAssetBitmapFont assetFont=new SmallOutlineAssetBitmapFont();
		_text=text;
		_font=assetFont.getFont();
		_labelOffset=(this.getWidth()-_font.getBounds(_text).width)/2;	
		SelectAssetAtlas selectAssetAtlas=new SelectAssetAtlas();
		_background=selectAssetAtlas.createSpriteUpperMenu();	
	}
	public void setText(String text){
		_text=text;	
		_labelOffset=(this.getWidth()-_font.getBounds(_text).width)/2;
	}	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		_background.setPosition(this.getX(),this.getY());
		_background.draw(batch, parentAlpha);
		if(_font!=null&&_text!=null){
			_font.setColor(1,1,1,parentAlpha);
			_font.draw(batch,_text,this.getX()+_labelOffset,this.getY()+80);				
		}	
		_debugFont.setColor(1,1,1,1);
		_debugFont.draw(batch,Integer.toString(Gdx.app.getGraphics().getFramesPerSecond()),this.getX()+25,this.getY()+75);
	}
}

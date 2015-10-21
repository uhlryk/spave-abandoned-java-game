package pro.artwave.spaceshooter.view.root.game;

import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.RootAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Blok wyœwietlany na samej górze na œrodku ekranu.
 * Wyœwietla siê w grze, na górze ma nazwê misji a poni¿ej punkty
 * @author Krzysztof
 *
 */
public class UpperShortMenu extends Actor {
	private Sprite _backgroundName;
//	private Sprite _backgroundScore;
	private BitmapFont _font;
	private BitmapFont _debugFont;
	private String _text;
	
//	private Sprite _starSprite;
	private float _labelOffset;	
	public void init(String text){
		setSize(515,90);
		
		SmallOutlineAssetBitmapFont debugAssetFont=new SmallOutlineAssetBitmapFont();
		_debugFont=debugAssetFont.getFont();
		BigOutlineAssetBitmapFont assetFont=new BigOutlineAssetBitmapFont();
		
		_text=text;
		_font=assetFont.getFont();
		_font.setScale(0.5f);
		_labelOffset=(this.getWidth()-_font.getBounds(_text).width)/2;
		
		GameAssetAtlas gameAssetAtlas=new GameAssetAtlas();
//		_starSprite=rootAssetAtlas.createSpriteStar();
//		_backgroundScore=gameAssetAtlas.createSpriteUpperScore();
		_backgroundName=gameAssetAtlas.createSpriteUpperMenu();
//		_backgroundScore=gameAssetAtlas.createSpriteUpperScore();
//		_starSprite.setSize(50,50);		
	}
	public void setText(String text){
		_text=text;	
		_font.setScale(0.5f);
		_labelOffset=(this.getWidth()-_font.getBounds(_text).width)/2;
	}	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
	//	_backgroundScore.setPosition(this.getX()+(this.getWidth()-_backgroundScore.getWidth())/2,this.getY());
	//	_backgroundScore.draw(batch, parentAlpha);			
		_backgroundName.setPosition(this.getX(),this.getY());
		_backgroundName.draw(batch, parentAlpha);
		_font.setScale(0.5f);
		if(_font!=null&&_text!=null){
			_font.setColor(1,1,1,parentAlpha);
			//_font.drawWrapped(batch,_text,this.getX(),this.getY()+65,this.getWidth()-_padding*2,HAlignment.CENTER);
			_font.draw(batch,_text,this.getX()+_labelOffset,this.getY()+70);
	//		_starSprite.setPosition(this.getX()+_labelOffset-60,this.getY()+20);
	//		_starSprite.draw(batch,parentAlpha);
	//		_starSprite.setPosition(this.getX()+this.getWidth()-_labelOffset+10,this.getY()+20);
	//		_starSprite.draw(batch,parentAlpha);				
		}	
		_debugFont.setColor(1,1,1,1);
		_debugFont.draw(batch,Integer.toString(Gdx.app.getGraphics().getFramesPerSecond()),this.getX()+25,this.getY()+75);
	}
}

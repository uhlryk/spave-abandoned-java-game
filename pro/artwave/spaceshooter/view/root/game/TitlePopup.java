package pro.artwave.spaceshooter.view.root.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pro.artwave.fgm.view.helper.Popup;
import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;

public class TitlePopup extends Popup {
	private BitmapFont _font;
	private String _label;
	private Color _color;
	private GlobalTranslation _translation;
	private int _offsetY;
	@Override 
	public void init(){
		super.init();
		_offsetY=0;
		_color=new Color(1,1,1,1);
	//	MediumAssetBitmapFont font=new MediumAssetBitmapFont();
		BigOutlineAssetBitmapFont font=new BigOutlineAssetBitmapFont();
		_font=font.getFont();
		_translation=new GlobalTranslation();
		_translation.init();	
		this.setSize(700,370);
	}
	public void setTitleOffset(int offsetY){
		_offsetY=offsetY;
	}
	public void setTitle(String label){
		_label=label;
	}
	public String _(String key){
		return _translation._(key);
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);
		_font.setColor(_color.r,_color.g,_color.b,_color.a*parentAlpha*this.getAnimation());
		_font.drawWrapped(batch,_label,this.getX(),this.getY()+this.getHeight()-65+_offsetY,this.getWidth(),HAlignment.CENTER);
	}
}

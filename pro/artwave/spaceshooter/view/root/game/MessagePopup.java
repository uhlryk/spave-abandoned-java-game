package pro.artwave.spaceshooter.view.root.game;

import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

public class MessagePopup extends TitlePopup {
	private String _textSmall;
	private String _textBig;
	private BitmapFont _mediumFont;
	private BitmapFont _smallFont;	
	private int _offsetSmallY;	
	private int _offsetSmallX;	
	private int _smallWidth;
	private int _offsetBigY;		
	private Color _color;
	public void init(){
		super.init();
		BigOutlineAssetBitmapFont mediumFont=new BigOutlineAssetBitmapFont();
		_mediumFont=mediumFont.getFont();
		_mediumFont.setScale(0.8f);
		SmallOutlineAssetBitmapFont smallFont=new SmallOutlineAssetBitmapFont();
		_smallFont=smallFont.getFont();
	//	_smallFont.setScale(1);
		_color=new Color(1,1,1,1);
		this.setBigTextOffset(150);
		this.setSmallTextYOffset(200);
	}
	@Override
	public void setSize(float width, float height){
		super.setSize(width, height);
		if(this._smallWidth==0){
			this._smallWidth=(int) width-60;
			this._offsetSmallX=30;
		}
	}
	public void setSmallTextYOffset(int offsetY){
		_offsetSmallY=offsetY;
	}	
	public void setSmallTextXOffset(int offsetX){
		_offsetSmallX=offsetX;
	}	
	public void setSmallTextWidth(int smallWidth){
		_smallWidth=smallWidth;
	}		
	public void setBigTextOffset(int offsetY){
		_offsetBigY=offsetY;
	}		
	public void setSmallText(String text){
		_textSmall=text;
	}	
	public void setBigText(String text){
		_textBig=text;
	}		
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);
		_mediumFont.setColor(_color.r,_color.g,_color.b,_color.a*parentAlpha*this.getAnimation());
		_mediumFont.drawWrapped(batch,_textBig,this.getX()+50,this.getY()+this.getHeight()+_offsetBigY,this.getWidth()-100,HAlignment.CENTER);
		_smallFont.setScale(1);
		_smallFont.setColor(_color.r,_color.g,_color.b,_color.a*parentAlpha*this.getAnimation());
		_smallFont.drawWrapped(batch,_textSmall,this.getX()+_offsetSmallX,this.getY()+this.getHeight()+_offsetSmallY,this._smallWidth,HAlignment.LEFT);		
	}
}

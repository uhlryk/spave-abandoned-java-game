package pro.artwave.spaceshooter.view.helper;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import pro.artwave.fgm.view.PulseSprite;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;

public class LabeledCheckbox extends Checkbox {
	private boolean _isFlip;
	public void init(String label,boolean isFlip){
		this.init(label,isFlip,TYPE_NONE_OFF,false,TYPE_NONE,false);
	}
	public void init(String label,boolean isFlip,int typeStart,boolean isPulse){
		this.init(label,isFlip,typeStart,isPulse,TYPE_NONE,false);
	}
	public void init(String label,boolean isFlip,int typeStart,int typeEnd){
		this.init(label,isFlip,typeStart,false,typeEnd,false);
	}
	public void init(String label,boolean isFlip,int type){
		this.init(label,isFlip,type,false,TYPE_NONE,false);
	}
	public void init(String label,boolean isFlip,int typeStart,boolean isPulseStart,int typeEnd,boolean isPulseEnd){
		this._isFlip=isFlip;
		super.init(typeStart, isPulseStart, typeEnd, isPulseEnd);
		setSize(300,92);
		this.setText(label);
		if(_isFlip){
			this.setOffsetX(-45);
		}else{
			this.setOffsetX(40);
		}
		
		SmallOutlineAssetBitmapFont labelAssetBitmapFont=new SmallOutlineAssetBitmapFont();
		BitmapFont font=labelAssetBitmapFont.getFont();
		font.setScale(1);
		this.setFontBitmap(font);
		this.setLabelY(61);
	}@Override
	public void setText(String text){
		super.setText(text);
		if(_isFlip){
			this.setOffsetX(-45);
		}else{
			this.setOffsetX(40);
		}	
		this.setLabelY(61);
	}	
	@Override
	protected Sprite getTypeSprite(int type){
		Sprite s;
		switch(type){
			case TYPE_POSITIVE:
				s=this.getRootAssetAtlas().createSpriteButton2Positive();
				break;
			case TYPE_NEGATIVE:
				s=this.getRootAssetAtlas().createSpriteButton2Negative();
				break;
			case TYPE_NEUTRAL:
				s=this.getRootAssetAtlas().createSpriteButton2Neutral();
				break;
			case TYPE_NONE_OFF:
				s=this.getRootAssetAtlas().createSpriteButton2Off();	
				break;
			case TYPE_POSITIVE_OFF:
				s=this.getRootAssetAtlas().createSpriteButton2PositiveOff();
				break;
			case TYPE_NEGATIVE_OFF:
				s=this.getRootAssetAtlas().createSpriteButton2NegativeOff();
				break;
			case TYPE_NEUTRAL_OFF:
				s=this.getRootAssetAtlas().createSpriteButton2NeutralOff();	
				break;
			case TYPE_NONE:
			default:	
				s=this.getRootAssetAtlas().createSpriteButton2();	
				break;
		}
		if(_isFlip){
			s.flip(true,false);
		}
		s.setScale(0.8f);
		return s;	
	}
}

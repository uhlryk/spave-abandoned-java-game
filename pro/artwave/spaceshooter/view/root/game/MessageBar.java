package pro.artwave.spaceshooter.view.root.game;

import java.util.ArrayList;

import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.resource.GameTranslation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class MessageBar extends Actor {
	public static final String TITLE_OBJECTIVES="objectives";
	public static final String TITLE_SUCCESS="success";
	public static final ArrayList<Integer> LABEL_COLOR=new ArrayList<Integer>(5);
	{
		LABEL_COLOR.add(0xffffffff);
		LABEL_COLOR.add(0xffaaaaff);
		LABEL_COLOR.add(0xaaffaaff);
		LABEL_COLOR.add(0xaaaaffff);
	}
	private Label _bigSection;
	private Label _mediumSection;
	private ArrayList<Label> _smallSectionList;
	private BitmapFont _smallSectionFont;
	private float _alpha;
	private float _alphaParam;
	private boolean _isFade;		
	private GameTranslation _translation;
	public void init(){
		this.setTouchable(Touchable.disabled);
		this.setSize(500,150);
		_alpha=1;
		_alphaParam=1;
		_isFade=false;		
		
		BigOutlineAssetBitmapFont bigOutlineAssetBitmapFont=new BigOutlineAssetBitmapFont();
		_bigSection=new Label();
		BitmapFont _bigFont=bigOutlineAssetBitmapFont.getFont();
		_bigFont.setScale(0.8f);
		_bigSection.init(_bigFont);
		MediumOutlineAssetBitmapFont mediumOutlineAssetBitmapFont=new MediumOutlineAssetBitmapFont();
		_mediumSection=new Label();
		_mediumSection.init(mediumOutlineAssetBitmapFont.getFont());
		SmallOutlineAssetBitmapFont smallOutlineAssetBitmapFont=new SmallOutlineAssetBitmapFont();
		_smallSectionFont=smallOutlineAssetBitmapFont.getFont();
		_smallSectionList=new ArrayList<Label>();
		//_smallSection.init(smallOutlineAssetBitmapFont.getFont());
		_translation=new GameTranslation();
		_translation.init();			
	}
	public void setObjective(String label){
		setObjective(label,null);
	}
	public void setObjective(String labelMedium,String labelSmall){
		_mediumSection._y=(int) (this.getHeight()-30);
		_mediumSection.setWidth(400);
		_mediumSection.setVisibility(true);
		_mediumSection.setLabel(this._translation.getBarTitle(labelMedium));
		if(labelSmall!=null){
			_smallSectionList.clear();
			addObjective(this._translation.getStoryMessage(labelSmall),LABEL_COLOR.get(0));
		}
		_bigSection.setVisibility(false);
		
	}
	public void addObjective(String labelSmall){
		addObjective(labelSmall,LABEL_COLOR.get(_smallSectionList.size()));
	}
	public void addObjective(String labelSmall,int color){
		Label smallLabel=new Label();
		smallLabel.init(_smallSectionFont);
		smallLabel.setLabel(labelSmall);
		int prevHeight=0;
		if(_smallSectionList.size()>0){
			prevHeight=_smallSectionList.get(_smallSectionList.size())._y;
		}
		smallLabel._y=(int) (this.getHeight()-_mediumSection.getHeight()-50-prevHeight);
		smallLabel.setVisibility(true);
		smallLabel.setColor(color);
		_smallSectionList.add(smallLabel);
	}
	
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		_bigSection.draw(batch, parentAlpha*_alpha);
		
		_mediumSection.draw(batch, parentAlpha*_alpha);
		for(Label smallLabel:_smallSectionList){
			smallLabel.draw(batch, parentAlpha*_alpha);
		}
	}
	private class Label{
		private BitmapFont _font;
		private int _x;
		private int _y;
		private int _width;
		private int _height;
		private String _label;
		private Color _color;
		private boolean _visible;
		public void setVisibility(boolean visible){
			_visible=visible;
		}
		public void setWidth(int width){
			_width=width;
			calculateXOffset();
		}
		public void init(BitmapFont font){
			_visible=true;
			setFont(font);
			_color=new Color(1,1,1,0.6f);
			_label="";
			_width=(int) MessageBar.this.getWidth();
			calculateXOffset();
		}
		private void calculateXOffset(){
			_x=(int) ((MessageBar.this.getWidth()-_width)/2);
		}
		public void setFont(BitmapFont font){
			_font=font;
		}
		public void setColor(int color){
			_color=new Color(color);
		}		
		public void setLabel(String label){
			_label=label;
			TextBounds bounds=_font.getWrappedBounds(_label,_width);
			_height=(int) bounds.height;
		}
		public int getHeight(){
			if(_visible==true){
				return _height;
			}else{
				return 0;
			}
		}
		public void draw(SpriteBatch batch,float parentAlpha){
			if(_visible){
				_font.setScale(1);
				_font.setColor(_color.r,_color.g,_color.b,_color.a*parentAlpha);
				_font.drawWrapped(batch,_label,MessageBar.this.getX()+this._x,MessageBar.this.getY()+this._y,_width,HAlignment.CENTER);
			}
		}
	}
}

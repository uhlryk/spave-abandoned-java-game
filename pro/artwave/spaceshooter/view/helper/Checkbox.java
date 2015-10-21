package pro.artwave.spaceshooter.view.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import pro.artwave.fgm.view.PulseSprite;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.model.asset.RootAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;

public class Checkbox extends Button {
	public static final int TYPE_NONE=0;
	public static final int TYPE_NONE_OFF=1;
	public static final int TYPE_NEUTRAL=2;
	public static final int TYPE_NEUTRAL_OFF=3;
	public static final int TYPE_POSITIVE=4;
	public static final int TYPE_POSITIVE_OFF=5;
	public static final int TYPE_NEGATIVE=6;
	public static final int TYPE_NEGATIVE_OFF=7;
	private PulseSprite _pulseStartSprite;
	private PulseSprite _pulseEndSprite;
	private RootAssetAtlas _rootAssetAtlas;
	private Effects.Play _sound;
	protected RootAssetAtlas getRootAssetAtlas(){
		return _rootAssetAtlas;
	}
	public void init(){
		this.init(TYPE_NONE_OFF,false,TYPE_NONE,false);
	}
	public void init(int typeStart,boolean isPulse){
		this.init(typeStart,isPulse,TYPE_NONE,false);
	}
	public void init(int typeStart,int typeEnd){
		this.init(typeStart,false,typeEnd,false);
	}
	public void init(int type){
		this.init(type,false,TYPE_NONE,false);
	}
	public void setSound(Effects.Play sound){
		_sound=sound;
	}
	public void init(int typeStart,boolean isPulseStart,int typeEnd,boolean isPulseEnd){
		setSize(92,92);
		_rootAssetAtlas=new RootAssetAtlas();
		if(isPulseStart==true){
			_pulseStartSprite=new PulseSprite(0.6f,1f);
			_pulseStartSprite.set(this.getTypeSprite(typeStart));
			this.setBackground(_pulseStartSprite);
		}else{
			this.setBackground(this.getTypeSprite(typeStart));
		}
		if(isPulseEnd==true){
			_pulseEndSprite=new PulseSprite(0.6f,1f);
			_pulseEndSprite.set(this.getTypeSprite(typeEnd));
			this.setBackgroundOn(_pulseEndSprite);
		}else{
			this.setBackgroundOn(this.getTypeSprite(typeEnd));
		}		
		if(_pulseStartSprite!=null){
			_pulseStartSprite.startPulse();
		}
		if(_pulseEndSprite!=null){
			_pulseEndSprite.startPulse();
		}		
		super.init(1);
	}
	@Override
	protected void onDown(){
		if(_sound!=null){
			_sound.play();
		}
	}	
	protected Sprite getTypeSprite(int type){
		switch(type){
			case TYPE_NONE:
				return this.getRootAssetAtlas().createSpriteButton3();		
			case TYPE_POSITIVE:
				return this.getRootAssetAtlas().createSpriteButton3Positive();
			case TYPE_NEGATIVE:
				return this.getRootAssetAtlas().createSpriteButton3Negative();
			case TYPE_NEUTRAL:
				return this.getRootAssetAtlas().createSpriteButton3Neutral();
			case TYPE_NONE_OFF:
				return this.getRootAssetAtlas().createSpriteButton3Off();		
			case TYPE_POSITIVE_OFF:
				return this.getRootAssetAtlas().createSpriteButton3PositiveOff();
			case TYPE_NEGATIVE_OFF:
				return this.getRootAssetAtlas().createSpriteButton3NegativeOff();
			case TYPE_NEUTRAL_OFF:
				return this.getRootAssetAtlas().createSpriteButton3NeutralOff();					
		}
		return this.getRootAssetAtlas().createSpriteButton3();	
	}
	@Override
	public void act(float delta){
		if(_pulseStartSprite!=null){
			_pulseStartSprite.act(delta);
		}
		if(_pulseEndSprite!=null){
			_pulseEndSprite.act(delta);
		}			
		super.act(delta);
	}
}

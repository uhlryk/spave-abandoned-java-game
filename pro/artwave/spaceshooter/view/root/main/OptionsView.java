package pro.artwave.spaceshooter.view.root.main;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.RootAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;
import pro.artwave.spaceshooter.view.helper.Checkbox;
import pro.artwave.spaceshooter.view.helper.LabeledCheckbox;

public class OptionsView extends View {
	public static final int BUTTON_CANCEL=0;
	public static final int BUTTON_ACCEPT=1;
	
	public static final int BUTTON_MUSIC_ON=2;
	public static final int BUTTON_MUSIC_OFF=3;	
	
	public static final int BUTTON_EFFECTS_ON=4;
	public static final int BUTTON_EFFECTS_OFF=5;		
	
	public static final int SLIDER_MUSIC=0;
	public static final int SLIDER_EFFECTS=1;
	
	
	private LabeledCheckbox _cancelButton;
	private LabeledCheckbox _acceptButton;
	private GlobalTranslation _translation;	
	private UpperLongLabel _upperLabel;	
	
	private boolean _isMusicOn;
	private boolean _isEffectsOn;
	private int _musicLevel;
	private int _effectLevel;
	private Sprite _textBackground;
	private BitmapFont _mediumFont;
	private String _effectsLabel;
	private String _musicLabel;
	
	private Checkbox _effectsTurnOnButton;
	private Checkbox _effectsTurnOffButton;
	
	private Checkbox _musicTurnOnButton;
	private Checkbox _musicTurnOffButton;
	
	private Slider _effectsVolume;
	private Slider _musicVolume;
	
	public void init(boolean isMusicOn,boolean isEffectsOn,float musicLevel,float effectLevel){
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		SelectAssetAtlas selectAssetAtlas=new SelectAssetAtlas();
		
		Effects effects=Effects.getInstance();
		_isMusicOn=isMusicOn;
		_isEffectsOn=isEffectsOn;
		_translation=new GlobalTranslation();
		_translation.init();	
		
		_upperLabel=new UpperLongLabel();
		_upperLabel.init(_translation._("Options","OptionLabel"));
		addActor(_upperLabel);
		_upperLabel.setPosition((this.getWidth()-_upperLabel.getWidth())/2, this.getHeight()-_upperLabel.getHeight());
		
		_textBackground=selectAssetAtlas.createSpriteTextBackground();
	//	System.out.println("scale "+_textBackground.getScaleX());
		_textBackground.setScale(1.5f);
		
		_cancelButton=new LabeledCheckbox();
		_cancelButton.init(_translation._("Options","OptionCancel"),true,Checkbox.TYPE_NONE_OFF,false,Checkbox.TYPE_NEGATIVE,false);
		_cancelButton.setSound(effects.getButtonClickNegative());
		_cancelButton.setPosition(this.getX()+(this.getWidth())/2-30-_cancelButton.getWidth(),this.getY()+30);
		this.addActor(_cancelButton);			
		
		_acceptButton=new LabeledCheckbox();
		_acceptButton.init(_translation._("Options","OptionSave"),false,Checkbox.TYPE_NONE_OFF,false,Checkbox.TYPE_POSITIVE,false);
		_acceptButton.setSound(effects.getButtonClickPositive());
		_acceptButton.setPosition(this.getX()+(this.getWidth())/2+30,this.getY()+30);
		this.addActor(_acceptButton);	
		
		MediumOutlineAssetBitmapFont _fontAssetBitmapFont=new MediumOutlineAssetBitmapFont();
		_mediumFont=_fontAssetBitmapFont.getFont();
		_effectsLabel=_translation._("Options","SoundLabel");
		_musicLabel=_translation._("Options","MusicLabel");
		
		_effectsTurnOnButton=new Checkbox();
		_effectsTurnOnButton.init(Checkbox.TYPE_NONE_OFF,false,Checkbox.TYPE_POSITIVE,false);
		_effectsTurnOnButton.setSound(effects.getButtonClickPositive());
		_effectsTurnOnButton.setPosition((this.getWidth()-_effectsTurnOnButton.getWidth())/2+290,this.getY()+this.getHeight()-_upperLabel.getHeight()-290);
		this.addActor(_effectsTurnOnButton);
		
		_effectsTurnOffButton=new Checkbox();
		_effectsTurnOffButton.init(Checkbox.TYPE_NONE_OFF,false,Checkbox.TYPE_NEGATIVE,false);
		_effectsTurnOffButton.setSound(effects.getButtonClickNegative());
		_effectsTurnOffButton.setPosition((this.getWidth()-_effectsTurnOffButton.getWidth())/2-290,this.getY()+this.getHeight()-_upperLabel.getHeight()-290);
		this.addActor(_effectsTurnOffButton);		
		
		_musicTurnOnButton=new Checkbox();
		_musicTurnOnButton.init(Checkbox.TYPE_NONE_OFF,false,Checkbox.TYPE_POSITIVE,false);
		_musicTurnOnButton.setSound(effects.getButtonClickPositive());
		_musicTurnOnButton.setPosition((this.getWidth()-_musicTurnOnButton.getWidth())/2+290,this.getY()+this.getHeight()-_upperLabel.getHeight()-160);
		this.addActor(_musicTurnOnButton);	
		
		_musicTurnOffButton=new Checkbox();
		_musicTurnOffButton.init(Checkbox.TYPE_NONE_OFF,false,Checkbox.TYPE_NEGATIVE,false);
		_musicTurnOffButton.setSound(effects.getButtonClickNegative());
		_musicTurnOffButton.setPosition((this.getWidth()-_musicTurnOffButton.getWidth())/2-290,this.getY()+this.getHeight()-_upperLabel.getHeight()-160);
		this.addActor(_musicTurnOffButton);		
		
		if(_isMusicOn){
			_musicTurnOnButton.setPushedDown();
		}else{
			_musicTurnOffButton.setPushedDown();
		}
		if(_isEffectsOn){
			_effectsTurnOnButton.setPushedDown();
		}else{
			_effectsTurnOffButton.setPushedDown();
		}	
		RootAssetAtlas rootAssetAtlas=new RootAssetAtlas();;
		SliderStyle style=new SliderStyle(new SpriteDrawable(rootAssetAtlas.createSpriteSliderBar()),new SpriteDrawable(rootAssetAtlas.createSpriteSliderKnob()));
		_effectsVolume=new Slider(0, 100,1,false,style);
		_effectsVolume.setSize(472, 56);
		_effectsVolume.setPosition((this.getWidth()-_effectsVolume.getWidth())/2,this.getY()+this.getHeight()-_upperLabel.getHeight()-140);
		_effectsVolume.setValue(effectLevel);
		addActor(_effectsVolume);
		//_effectsVolume.addListener();
		

		_musicVolume=new Slider(0, 100,1,false,style);
		_musicVolume.setSize(472, 56);
		_musicVolume.setPosition((this.getWidth()-_musicVolume.getWidth())/2,this.getY()+this.getHeight()-_upperLabel.getHeight()-270);
		_musicVolume.setValue(musicLevel);
		addActor(_musicVolume);		
	}
	public void setButtonState(int buttonId,boolean setOn){
		switch(buttonId){
		case BUTTON_EFFECTS_OFF:
			if(setOn){
				_effectsTurnOffButton.setPushedDown();
			}else{
				_effectsTurnOffButton.setPushedUp();
			}
			break;
		case BUTTON_EFFECTS_ON:
			if(setOn){
				_effectsTurnOnButton.setPushedDown();
			}else{
				_effectsTurnOnButton.setPushedUp();
			}
			break;
		case BUTTON_MUSIC_OFF:
			if(setOn){
				_musicTurnOffButton.setPushedDown();
			}else{
				_musicTurnOffButton.setPushedUp();
			}
			break;
		case BUTTON_MUSIC_ON:
			if(setOn){
				_musicTurnOnButton.setPushedDown();
			}else{
				_musicTurnOnButton.setPushedUp();
			}
			break;				
		}
	}
	public void onChange(int sliderId,ChangeListener listener){
		switch(sliderId){
		case SLIDER_MUSIC:
			_musicVolume.addListener(listener);
			break;
		case SLIDER_EFFECTS:
			_effectsVolume.addListener(listener);
			break;
		}
	}
	public void onClick(int buttonId,Button.ClickListener listener){
		switch(buttonId){
			case BUTTON_CANCEL:
				_cancelButton.addClickListener(listener);
				break;
			case BUTTON_ACCEPT:
				_acceptButton.addClickListener(listener);
				break;
			case BUTTON_EFFECTS_OFF:
				_effectsTurnOffButton.addClickListener(listener);
				break;
			case BUTTON_EFFECTS_ON:
				_effectsTurnOnButton.addClickListener(listener);
				break;
			case BUTTON_MUSIC_OFF:
				_musicTurnOffButton.addClickListener(listener);
				break;
			case BUTTON_MUSIC_ON:
				_musicTurnOnButton.addClickListener(listener);
				break;				
		}
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		
		_textBackground.setPosition(this.getX()+(this.getWidth()-_textBackground.getWidth())/2,this.getY()+this.getHeight()-_upperLabel.getHeight()-_textBackground.getHeight()-50);
		_textBackground.draw(batch,parentAlpha*0.8f);
		super.draw(batch, parentAlpha);
		_mediumFont.setColor(1,1,1,1);
		_mediumFont.drawWrapped(batch,_effectsLabel,this.getX()+(this.getWidth()-760)/2,this.getY()+this.getHeight()-_upperLabel.getHeight()-30,760,HAlignment.CENTER);	
		
		_mediumFont.setColor(1,1,1,1);
		_mediumFont.drawWrapped(batch,_musicLabel,this.getX()+(this.getWidth()-760)/2,this.getY()+this.getHeight()-_upperLabel.getHeight()-165,760,HAlignment.CENTER);			
	}	
}

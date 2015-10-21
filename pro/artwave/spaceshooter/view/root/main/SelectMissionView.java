package pro.artwave.spaceshooter.view.root.main;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.XSmallAssetBitmapFont;
import pro.artwave.spaceshooter.model.resource.GameResource;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.model.resource.SelectGameTranslation;
import pro.artwave.spaceshooter.view.helper.Checkbox;
import pro.artwave.spaceshooter.view.helper.LabeledCheckbox;

public class SelectMissionView extends View {

	public static final int BUTTON_PREV=0;
	public static final int BUTTON_NEXT=1;
	
	public static final int BUTTON_SELECT_MISSION=1;
	private ButtonPrev _buttonPrev;
	private ButtonNext _buttonNext;

	private GlobalTranslation _translation;	
	
	private SelectGameTranslation _selectTranslation;	
	private GameResource _gameResource;
	private UpperLongLabel _upperLabel;	
	private BitmapFont _mediumFont;
	private BitmapFont _smallFont;

	private int _difficulty;
	
	private int _playerId;
	private String _missionDetails;
	private String _missionName;
	
	private String _dificultyResourceLabel;
	private float _offsetYMissionDetails;


	private Sprite _decorationLeft;
	private Sprite _decorationRight;
	
	private Sprite _textBackground;
//	private Sprite _starSprite;
	private ClickListener _listener;
	
	private LabeledCheckbox _difficultyNormalButton;
	private LabeledCheckbox _difficultyHardButton;
	
	
	public void init(GameResource gameResource,SaveGameResource saveGameResource,int playerId,int missionId,int difficulty){
		_playerId=playerId;
		_difficulty=difficulty;
		BigOutlineAssetBitmapFont _fontAssetBitmapFont=new BigOutlineAssetBitmapFont();
		_mediumFont=_fontAssetBitmapFont.getFont();
		
		MediumOutlineAssetBitmapFont xsmallAssetBitmapFont=new MediumOutlineAssetBitmapFont();
		System.out.println(xsmallAssetBitmapFont.getResourceName()+" AAAA");
		_smallFont=xsmallAssetBitmapFont.getFont();
		_smallFont.setScale(1);
		_gameResource=gameResource;
		_translation=new GlobalTranslation();
		_translation.init();	
		_selectTranslation=new SelectGameTranslation();
		_selectTranslation.init();	
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		SelectAssetAtlas selectAssetAtlas=new SelectAssetAtlas();
		_decorationLeft=selectAssetAtlas.createSpriteDecorationFrame();
		_decorationRight=selectAssetAtlas.createSpriteDecorationFrame();
		_decorationRight.flip(true,false);
		_textBackground=selectAssetAtlas.createSpriteTextBackground();
	//	System.out.println("scale "+_textBackground.getScaleX());
		_textBackground.setScale(1.3f);
		_missionName=_selectTranslation.getMissionName(missionId);
		_missionDetails=_selectTranslation.getMissionDetails(missionId);
				
		_buttonPrev=new ButtonPrev();
		_buttonPrev.init();
		_buttonPrev.setPosition(20,5);
		this.addActor(_buttonPrev);
		_buttonNext=new ButtonNext();
		_buttonNext.init();
		_buttonNext.setPosition(this.getWidth()-_buttonNext.getWidth()-20,5);
		this.addActor(_buttonNext);	
		
		_upperLabel=new UpperLongLabel();
		_upperLabel.init(_translation._("SelectMission","MissionLabel")+" "+missionId);
		addActor(_upperLabel);
		_upperLabel.setPosition((this.getWidth()-_upperLabel.getWidth())/2, this.getHeight()-_upperLabel.getHeight());

		_difficultyNormalButton=new LabeledCheckbox();
		_difficultyNormalButton.init(_translation._("SelectMission","DifficultyNormal"),true,Checkbox.TYPE_NONE_OFF,false,Checkbox.TYPE_POSITIVE,false);
		_difficultyNormalButton.setPosition(this.getX()+(this.getWidth())/2-30-_difficultyNormalButton.getWidth(),this.getY()+this.getHeight()-_upperLabel.getHeight()-415);

		this.addActor(_difficultyNormalButton);			
		
		_difficultyHardButton=new LabeledCheckbox();
		_difficultyHardButton.init(_translation._("SelectMission","DifficultyHard"),false,Checkbox.TYPE_NONE_OFF,false,Checkbox.TYPE_NEGATIVE,false);
		_difficultyHardButton.setPosition(this.getX()+(this.getWidth())/2+30,this.getY()+this.getHeight()-_upperLabel.getHeight()-415);
		if(_difficulty==1){
			_difficultyNormalButton.setPushedDown();
		}else{
			_difficultyHardButton.setPushedDown();
			_difficulty=2;
		}
		
		_difficultyNormalButton.addClickListener(new Button.ClickListener() {	
			@Override
			public void onClick() {
				_difficultyNormalButton.setPushedDown();
				_difficultyHardButton.setPushedUp();
				_difficulty=1;
				if(_listener!=null){
					_listener.onClick(_difficulty);
				}				
			}
		});		
		_difficultyHardButton.addClickListener(new Button.ClickListener() {	
			@Override
			public void onClick() {
				_difficultyHardButton.setPushedDown();
				_difficultyNormalButton.setPushedUp();
				_difficulty=2;
				if(_listener!=null){
					_listener.onClick(_difficulty);
				}
			}
		});
		this.addActor(_difficultyHardButton);			
	//	_dificultyResourceLabel=_translation._("SelectMission","XMorePoints");
	}
	public void onClick(int buttonId,Button.ClickListener listener){
		switch(buttonId){
		case BUTTON_PREV:
			_buttonPrev.addClickListener(listener);
			break;
		case BUTTON_NEXT:
			_buttonNext.addClickListener(listener);
			break;
		}
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);
	/*	if(_getItButton.isVisible()){
			_mediumFont.setColor(1,0.3f,0.3f,1);
			_mediumFont.drawWrapped(batch,_missionNotAvailable+"\n"+_requiredId,(this.getWidth()-300)/2,360,300,HAlignment.CENTER);
		}else{
			
			if(_difficultyLevel==DifficultyLevel.DIFFICULTY_HARD||_difficultyLevel==DifficultyLevel.DIFFICULTY_VERYHARD){			
				_mediumFont.setColor(1-0.3f*_difficultyLevel,1,1-0.3f*_difficultyLevel,1);
				_mediumFont.drawWrapped(batch,_difficultyLevel+_dificultyResourceLabel,(this.getWidth()-300)/2,200,300,HAlignment.CENTER);
			}
		}*/
		_textBackground.setPosition(this.getX()+(this.getWidth()-_textBackground.getWidth())/2,this.getY()+this.getHeight()-_upperLabel.getHeight()-_textBackground.getHeight());
		_textBackground.draw(batch,parentAlpha*0.8f);
		
		_decorationRight.setPosition(this.getX()+20,this.getY()+150);
		_decorationRight.draw(batch,parentAlpha);
		_decorationLeft.setPosition(this.getX()+this.getWidth()-_decorationRight.getWidth()-20,this.getY()+150);
		_decorationLeft.draw(batch,parentAlpha);	
		
		_mediumFont.setColor(1,1,1,1);
		_mediumFont.drawWrapped(batch,_missionName,this.getX()+(this.getWidth()-760)/2,this.getY()+this.getHeight()-_upperLabel.getHeight()+10,760,HAlignment.CENTER);
		_smallFont.setColor(1,1,1,1);
		_smallFont.drawWrapped(batch,_missionDetails,this.getX()+(this.getWidth()-760)/2,this.getY()+this.getHeight()-_upperLabel.getHeight()-75,760,HAlignment.CENTER);
		
	}
	public static abstract class ClickListener{
		public abstract void onClick(int difficultLevel);
	}	
	public void onDifficultyClick(ClickListener listener){
		_listener=listener;
	}		
}

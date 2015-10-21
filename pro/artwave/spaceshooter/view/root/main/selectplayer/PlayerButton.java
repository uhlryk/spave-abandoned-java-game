package pro.artwave.spaceshooter.view.root.main.selectplayer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import pro.artwave.fgm.view.PulseSprite;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.XSmallAssetBitmapFont;
import pro.artwave.spaceshooter.model.resource.GameResource;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;
import pro.artwave.spaceshooter.model.resource.RankResource;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.model.resource.SelectGameTranslation;

public class PlayerButton extends Button {
	/**
	 * grafika znajduj¹ca siê w polu kampanii
	 */
	private Sprite _imageBackground;
	
	private PulseSprite _addictionalOverlay;
	private Sprite _buttonSprite;
	private ClickListener _listener;	
	private BitmapFont _smallFont;
	private BitmapFont _mediumFont;
	private String _playerNameLabel;

	private boolean _isMissionFinish;
	private String _scoreLabel;
	private float _scoreLabelOffset;
//	private String _campaignDetails;
	private GlobalTranslation _translation;	
	private int _playerId;
	private int _missionId;
	
	private String _actualMissionLabel;
	private String _actualMissionString;
	private String _rankLabel;
	private String _rankString;

	private Sprite _icon;
	
	public void init(final int playerId,SaveGameResource saveGameResource,GameResource gameResource,RankResource rankResource){
		super.init(1);
//		System.out.println("playerId "+playerId+" "+saveGameResource);
		_missionId=saveGameResource.getLastMission();
		_playerId=playerId;
		_actualMissionString=Integer.toString(_missionId);
		SelectAssetAtlas selectAssetAtlas=new SelectAssetAtlas();
		boolean isActiveUser=saveGameResource.getIsActive()==1?true:false;
		_translation=new GlobalTranslation();
		_translation.init();	
		
		setSize(222,380);
		SelectGameTranslation _selectTranslation=new SelectGameTranslation();
		_selectTranslation.init();
		/**
		 * Dodaæ t³umaczenie
		 */
		_playerNameLabel=_translation._("SelectPlayer","player")+" "+_playerId;
		_actualMissionLabel=_translation._("ActualMission");
		_rankLabel=_translation._("Rank");
		
		MediumOutlineAssetBitmapFont mediumFontAssset=new MediumOutlineAssetBitmapFont();
		_mediumFont=mediumFontAssset.getFont();
		XSmallAssetBitmapFont xSmallFontAssset=new XSmallAssetBitmapFont();
		_smallFont=xSmallFontAssset.getFont();
		BitmapFont font=mediumFontAssset.getFont();
		font.setScale(0.8f);
		this.setFontBitmap(font);
		int score=saveGameResource.getScore();
		
		_scoreLabel=Integer.toString(score);
		_scoreLabelOffset=(this.getWidth()-_mediumFont.getBounds(_scoreLabel).width)/2;
		this.setText("");
		/**
		 * Zmieniæ grafikê i metodê j¹ wywo³uj¹ca
		 */
		_imageBackground=selectAssetAtlas.createSpriteImage(playerId);
		_imageBackground.setSize(187, 131);
		if(isActiveUser){
			_buttonSprite=selectAssetAtlas.createSpriteButtonActivePlayer();
			_rankString=_translation._("rankNames",rankResource.getRank(score));
			
			_icon=selectAssetAtlas.createSpriteShip(saveGameResource.getShip());
			_icon.setScale(0.33f);
		}else{
			_buttonSprite=selectAssetAtlas.createSpriteButtonPlayer();
			_rankString="-";
		}

		_addictionalOverlay=new PulseSprite(0.4f);
		_addictionalOverlay.set(selectAssetAtlas.createSpriteActivePlayer());
		_addictionalOverlay.startPulse();
		addClickListener(new Button.ClickListener() {
			@Override
			public void onClick() {
				if(_listener!=null){
					_listener.onClick(_playerId,_missionId);
				}	
			}
		});	
		
	}
	public int getCampaignId(){
		return _playerId;
	}
	/**
	 * Pozwala wybraæ by dana kampania by³a aktywna/wybrana
	 */
	public void setActive(){
		this.setPushedDown();
	}
	public void setDeactive(){
		this.setPushedUp();
	}	
	@Override
	public void act (float delta) {
		_addictionalOverlay.act(delta);
		super.act(delta);
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		_imageBackground.setPosition(this.getX()+25,this.getY()+192);
		_imageBackground.draw(batch, parentAlpha);
		_buttonSprite.setPosition(this.getX(),this.getY());
		_buttonSprite.draw(batch, parentAlpha);
		super.draw(batch, parentAlpha);
		if(_addictionalOverlay!=null){
			float alpha=1;
			alpha=this.getChangeAlpha();
			_addictionalOverlay.setPosition(this.getX(),this.getY()+180);
			_addictionalOverlay.draw(batch, parentAlpha*alpha);
		}

		_smallFont.setColor(1f,1f,1f,1);
		_smallFont.drawWrapped(batch,_playerNameLabel,this.getX()+31,this.getY()+358, this.getWidth()-25,HAlignment.CENTER);
	//	_smallFont.drawWrapped(batch,_campaignDetails,this.getX()+21,this.getY()+178, this.getWidth()-20,HAlignment.CENTER);
		
		_smallFont.setColor(1f,1f,1f,1);
		_smallFont.drawWrapped(batch,_actualMissionLabel,this.getX()+20,this.getY()+165, this.getWidth()-20,HAlignment.CENTER);

		_mediumFont.setColor(1,1,1,1);
		_mediumFont.drawWrapped(batch,_actualMissionString,this.getX()+20,this.getY()+145, this.getWidth()-20,HAlignment.CENTER);		
		
		_smallFont.setColor(1f,1f,1f,1);
		_smallFont.drawWrapped(batch,_rankLabel,this.getX()+20,this.getY()+105, this.getWidth()-20,HAlignment.CENTER);		
		
		_mediumFont.setColor(1,1,1,1);
		_mediumFont.drawWrapped(batch,_rankString,this.getX()+20,this.getY()+83, this.getWidth()-20,HAlignment.CENTER);			
		
		_mediumFont.setColor(1,1,1,1);
		_mediumFont.draw(batch,_scoreLabel,this.getX()+_scoreLabelOffset+8,this.getY()+33);	
		
		if(_icon!=null){
			_icon.setPosition(this.getX()+(this.getWidth()-_icon.getWidth())/2+10,this.getY()+this.getHeight()-240);
			_icon.draw(batch,parentAlpha);
		}
	}
	public void addClickListener(ClickListener listener){
		_listener=listener;
	}		
	public static abstract class ClickListener{
		public abstract void onClick(int playerId,int missionId);
	}	
}

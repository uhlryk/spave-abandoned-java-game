package pro.artwave.spaceshooter.view.root.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.resource.GameResource;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;
import pro.artwave.spaceshooter.model.resource.RankResource;
import pro.artwave.spaceshooter.model.resource.SaveGameResource;
import pro.artwave.spaceshooter.view.root.main.selectplayer.PlayerButton;

public class SelectPlayerView extends View {
	public static final int BUTTON_PREV=0;
	public static final int BUTTON_NEXT=1;
//	private LeftButton _leftButton;
//	private RightButton _rightButton;
	private ButtonPrev _buttonPrev;
	private ButtonNext _buttonNext;
//	private SelectEquipmentButton _equipmentButton;
	private GlobalTranslation _translation;	

	private GameResource _gameResource;
	private UpperLongLabel _sumScore;	
	//private BitmapFont _font;
	private Map<Integer,PlayerButton> _playerButtonMap;
	
	private ScrollPane _scroll;
	private Table _table;
	private ClickListener _listener;
	
	public void init(GameResource gameResource,int playerId){
		_gameResource=gameResource;
		_translation=new GlobalTranslation();
		_translation.init();	
		int numPlayers=_gameResource.getIntParam("user_number");
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		RankResource rankResource=new RankResource();
		rankResource.init();
	//	final Map<Integer,GameResource.Campaign> campaignMap=gameResource.getCampaignMap();
		_table=new Table();
		if(numPlayers>0){
			_playerButtonMap=new HashMap<Integer,PlayerButton>();
			for(int i=1;i<numPlayers+1;i++){
				SaveGameResource _saveGame=new SaveGameResource();
				_saveGame.init(i);
				PlayerButton playerButton=new PlayerButton();
				playerButton.init(i,_saveGame,_gameResource,rankResource);
				if(i==playerId){
					playerButton.setActive();
				}
				_table.add(playerButton).padRight(30);
				_playerButtonMap.put(i, playerButton);
				playerButton.addClickListener(new PlayerButton.ClickListener() {					
					@Override
					public void onClick(int playerId,int missionId) {
						for(Entry<Integer,PlayerButton> campaign:_playerButtonMap.entrySet()){
							if(campaign.getValue().getCampaignId()==playerId){
								campaign.getValue().setPushedDown();
							}else{
								campaign.getValue().setPushedUp();
							}
						}
						if(_listener!=null){
							_listener.onClick(playerId,missionId);
						}
					}
				});
			}
		}
		_table.padLeft(50);
		_table.padRight(50);
		_scroll=new ScrollPane(_table);
		_scroll.setSize(this.getWidth(),380);
		_scroll.setPosition(0,150);
		this.addActor(_scroll);
		
	//	_equipmentButton=new SelectEquipmentButton();
	//	_equipmentButton.init();
	//	_equipmentButton.setPosition(this.getWidth()/2-_equipmentButton.getWidth()-176,this.getHeight()-_equipmentButton.getHeight());
	//	this.addActor(_equipmentButton);
		_buttonPrev=new ButtonPrev();
		_buttonPrev.init();
		_buttonPrev.setPosition(20,5);
		this.addActor(_buttonPrev);
		_buttonNext=new ButtonNext();
		_buttonNext.init();
		_buttonNext.setPosition(this.getWidth()-_buttonNext.getWidth()-20,5);
		this.addActor(_buttonNext);		
		
		_sumScore=new UpperLongLabel();
	//	_sumScore.init(Integer.toString(saveGameResource.getSumScore()));
		
		_sumScore.init(_translation._("SelectPlayer","Select_Player_Label"));
		addActor(_sumScore);
		_sumScore.setPosition((this.getWidth()-_sumScore.getWidth())/2, this.getHeight()-_sumScore.getHeight());
//		checkButtonArrow();
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
	public static abstract class ClickListener{
		public abstract void onClick(int playerId,int missionId);
	}		
	public void onPlayerButtonClick(ClickListener listener){
		_listener=listener;
	}	
	
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);
	}
}

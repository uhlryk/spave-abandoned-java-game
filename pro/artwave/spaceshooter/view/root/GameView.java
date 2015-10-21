package pro.artwave.spaceshooter.view.root;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Background;
import pro.artwave.spaceshooter.view.helper.HudHealthBar;
import pro.artwave.spaceshooter.view.helper.HudShieldBar;
import pro.artwave.spaceshooter.view.root.game.GameHudLeft;
import pro.artwave.spaceshooter.view.root.game.GameMap;
import pro.artwave.spaceshooter.view.root.game.UpperShortMenu;
import pro.artwave.spaceshooter.view.root.main.UpperLongLabel;

public class GameView extends View {
	public static final int BUTTON_FORWARD=1;
	public static final int BUTTON_LEFT=2;	
	public static final int BUTTON_RIGHT=3;		
	private Background _backgroundImage;
	private GameBoardHelper _boardHelper;
	private UpperShortMenu _sumScore;	
	private GameHudLeft _hudLeft;

	private HudHealthBar _hudHealthBar;
	private HudShieldBar _hudShieldBar;
	private GameMap _gameMap;
	public void init(int maxHealthVal,int maxShieldVal){
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);

		_boardHelper=new GameBoardHelper();
		this.addActor(_boardHelper);
		_boardHelper.init();
		_hudLeft=new GameHudLeft();
		_hudLeft.init();
		_hudLeft.setPosition(0,this.getHeight()-_hudLeft.getHeight()+3);
		addActor(_hudLeft);

		_hudHealthBar=new HudHealthBar();
		_hudHealthBar.init(maxHealthVal);
		_hudShieldBar=new HudShieldBar();
		_hudShieldBar.init(maxShieldVal);	
		
		_hudHealthBar.setPosition(this.getWidth()-_hudHealthBar.getWidth()-_hudShieldBar.getWidth()-70,10);
		addActor(_hudHealthBar);
		_hudShieldBar.setPosition(this.getWidth()-_hudShieldBar.getWidth()-10,10);
		addActor(_hudShieldBar);
		_sumScore=new UpperShortMenu();
		_sumScore.init("0");
		addActor(_sumScore);
		_sumScore.setPosition((this.getWidth()-_sumScore.getWidth())/2, this.getHeight()-_sumScore.getHeight());	
	}
	public void setBackground(Sprite bg){
		_backgroundImage=new Background();
		_backgroundImage.init(bg);
		_backgroundImage.setSize(this.getWidth(),this.getHeight());		
		this.addActorAt(0,_backgroundImage);		
	}
	public void setMap(Pixmap map,int width,int height,int setTileSize){
		_gameMap=new GameMap();
		_gameMap.init(map,width,height,setTileSize);
		_gameMap.setPosition(0,43);
		_hudLeft.addActor(_gameMap);
	}
	public GameMap getMap(){
		return _gameMap;
	}
	public void setMessageBar(Actor messageBar){
		addActor(messageBar);
		messageBar.setPosition((this.getWidth()-messageBar.getWidth())/2, this.getHeight()-200);	
	}		
	public void setPoints(int points){
		_sumScore.setText(Integer.toString(points));
	}
	public void setActHealth(int val){
		_hudHealthBar.setActVal(val);
	}
	public void setActShield(int val){
		_hudShieldBar.setActVal(val);
	}
	public void setMaxHealth(int val){
		_hudHealthBar.setMaxVal(val);
	}
	public void setMaxShield(int val){
		_hudShieldBar.setMaxVal(val);
	}	
	public GameBoardHelper getBoard(){
		return _boardHelper;
	}
	public void setActiveArea(Actor activeArea){
		this.addActor(activeArea);		
		activeArea.setPosition(0,0);
	}	
	public void setBlockFireButton(Group block){
		this.addActor(block);		
		block.setPosition(0,0);
	}
	public void addPausePopup(Group popup){
		this.addActor(popup);		
		popup.setPosition((this.getWidth()-popup.getWidth())/2,this.getHeight()-popup.getHeight()-230);
	}
	public void addPopup(Group popup){
		this.addActor(popup);		
		popup.setPosition((this.getWidth()-popup.getWidth())/2,(this.getHeight()-popup.getHeight())/2-20);
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		batch.setColor(1,1,1,1);
		super.draw(batch, parentAlpha);
	}
}

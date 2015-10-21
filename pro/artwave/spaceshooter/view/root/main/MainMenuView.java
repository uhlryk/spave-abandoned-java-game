package pro.artwave.spaceshooter.view.root.main;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Background;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.RootAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;
import pro.artwave.spaceshooter.view.root.main.mainmenu.MainMenuButton;

public class MainMenuView extends View {
	public static final int BUTTON_START_GAME=1;
	public static final int BUTTON_OPTIONS=2;	
	//public static final int BUTTON_INSTRUCTIONS=3;		
	public static final int BUTTON_CREDITS=4;		
	public static final int BUTTON_EXIT=5;			
	private MainMenuButton _buttonStartGame;
	private MainMenuButton _buttonOption;
	//private MainMenuButton _buttonInstruction;
	private MainMenuButton _buttonCredit;
	private MainMenuButton _buttonExit;
	private GlobalTranslation _translation;
	//private SelectAssetAtlas _selectAssetAtlas;	
	private RootAssetAtlas _rootAssetAtlas;
	private Sprite _logo;
	private Background _background;
	public void init(){
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		_translation=new GlobalTranslation();
		_translation.init();
	//	_selectAssetAtlas=new SelectAssetAtlas();
		_rootAssetAtlas=new RootAssetAtlas();
		_background=new Background();
		_background.init(_rootAssetAtlas.createSpriteLoadingBackground());
		_background.setSize(this.getWidth(),this.getHeight());
		this.addActor(_background);
		_logo=_rootAssetAtlas.createSpriteLogo();
		_buttonStartGame=new MainMenuButton();

		_buttonStartGame.init(_translation._("MainMenu","Play"));
		_buttonStartGame.setPosition((this.getWidth()-_buttonStartGame.getWidth())/2,380);
	//	_buttonStartGame.setPushedDown();
	//	_buttonStartGame.setPosition((this.getWidth()-_buttonStartGame.getWidth())/2,this.getHeight()-120);
		this.addActor(_buttonStartGame);
		_buttonOption=new MainMenuButton();
		_buttonOption.init(_translation._("MainMenu","Options"));
		_buttonOption.setPosition((this.getWidth()-_buttonOption.getWidth())/2,260);
		this.addActor(_buttonOption);		
	/*	_buttonInstruction=new MainMenuButton();
		_buttonInstruction.init(_translation._("MainMenu","Instruction"));
		_buttonInstruction.setPosition((this.getWidth()-_buttonInstruction.getWidth())/2,this.getHeight()-370);
		this.addActor(_buttonInstruction);	*/
		_buttonCredit=new MainMenuButton();
		_buttonCredit.init(_translation._("MainMenu","Credits"));
		//_buttonCredit.setPosition((this.getWidth()-_buttonCredit.getWidth())/2,this.getHeight()-495);
		_buttonCredit.setPosition((this.getWidth()-_buttonCredit.getWidth())/2,140);
		this.addActor(_buttonCredit);	
		_buttonExit=new MainMenuButton();
		_buttonExit.init(_translation._("MainMenu","Exit"));
		//_buttonExit.setPosition((this.getWidth()-_buttonExit.getWidth())/2,this.getHeight()-620);
		_buttonExit.setPosition((this.getWidth()-_buttonExit.getWidth())/2,20);
		this.addActor(_buttonExit);			
	}
	public void onClick(int buttonId,Button.ClickListener listener){
		switch(buttonId){
		case BUTTON_START_GAME:
			_buttonStartGame.addClickListener(listener);
			break;
		case BUTTON_OPTIONS:
			_buttonOption.addClickListener(listener);
			break;	
	/*	case BUTTON_INSTRUCTIONS:
			_buttonInstruction.addClickListener(listener);
			break;*/
		case BUTTON_CREDITS:
			_buttonCredit.addClickListener(listener);
			break;		
		case BUTTON_EXIT:
			_buttonExit.addClickListener(listener);
			break;			
		}
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);	
		_logo.setPosition(this.getX()+(this.getWidth()-_logo.getWidth())/2,this.getY()+this.getHeight()-160);
		_logo.draw(batch, parentAlpha);
	}
}

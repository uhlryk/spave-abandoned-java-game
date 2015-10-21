package pro.artwave.spaceshooter.controller.root;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.spaceshooter.controller.root.main.CreditsController;
import pro.artwave.spaceshooter.controller.root.main.EndGameController;
import pro.artwave.spaceshooter.controller.root.main.ExitController;
import pro.artwave.spaceshooter.controller.root.main.MainMenuController;
import pro.artwave.spaceshooter.controller.root.main.OptionsController;
import pro.artwave.spaceshooter.controller.root.main.SelectEquipmentController;
import pro.artwave.spaceshooter.controller.root.main.SelectPlayerController;
import pro.artwave.spaceshooter.controller.root.main.SelectMissionController;
import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.XSmallAssetBitmapFont;
import pro.artwave.spaceshooter.view.Loader;
import pro.artwave.spaceshooter.view.root.MainView;

public class MainController extends Controller {
	//private IntroAssetAtlas _introAssetAtlas;
	//private StageAssetAtlas _stageAssetAtlas;
	private SelectAssetAtlas _selectAssetAtlas;
	
	private BigOutlineAssetBitmapFont _bigOutlineAssetBitmapFont;	
	private MediumOutlineAssetBitmapFont _mediumOutlineAssetBitmapFont;	
	private SmallOutlineAssetBitmapFont _smallOutlineAssetBitmapFont;		
	private XSmallAssetBitmapFont _xSmallOutlineAssetBitmapFont;		
	
	public static int INTRO_CONTROLLER;
	public static int MAIN_MENU_CONTROLLER;	
	public static int OPTIONS_CONTROLLER;
	public static int CREDITS_CONTROLLER;
	//public static int SELECT_CAMPAIGN_CONTROLLER;	
	public static int SELECT_PLAYER_CONTROLLER;	
	public static int SELECT_MISSION_CONTROLLER;	
	public static int SELECT_EQUIPMENT_CONTROLLER;	
	
	public static int END_GAME_CONTROLLER;	
	//public static int GAME_CONTROLLER;		
	public static int EXIT_CONTROLLER;		
	private MainView _view;
	@Override
	public void onInit() {
		//INTRO_CONTROLLER=this.addController(new IntroController());
		MAIN_MENU_CONTROLLER=this.addController(new MainMenuController());
		OPTIONS_CONTROLLER=this.addController(new OptionsController());
		CREDITS_CONTROLLER=this.addController(new CreditsController());
	//	SELECT_CAMPAIGN_CONTROLLER=this.addController(new SelectCampaignController());
		SELECT_PLAYER_CONTROLLER=this.addController(new SelectPlayerController());
		SELECT_MISSION_CONTROLLER=this.addController(new SelectMissionController());
		SELECT_EQUIPMENT_CONTROLLER=this.addController(new SelectEquipmentController());
		END_GAME_CONTROLLER=this.addController(new EndGameController());
	//	GAME_CONTROLLER=this.addController(new GameController());
		EXIT_CONTROLLER=this.addController(new ExitController());
	}

	@Override
	public void onLoad() {
		_selectAssetAtlas=new SelectAssetAtlas();
		this.addModelToControl(_selectAssetAtlas);	
	//	_stageAssetAtlas=new StageAssetAtlas();
//		this.getChildController(SELECT_CAMPAIGN_CONTROLLER).addModelToControl(_stageAssetAtlas);
//		IconAssetAtlas iconAssetAtlas=new IconAssetAtlas();
//		this.addModelToControl(iconAssetAtlas);		
		
		_bigOutlineAssetBitmapFont=new BigOutlineAssetBitmapFont();
		this.addModelToControl(_bigOutlineAssetBitmapFont);	
		_mediumOutlineAssetBitmapFont=new MediumOutlineAssetBitmapFont();
		this.addModelToControl(_mediumOutlineAssetBitmapFont);	
		_smallOutlineAssetBitmapFont=new SmallOutlineAssetBitmapFont();
		this.addModelToControl(_smallOutlineAssetBitmapFont);			
		
		_xSmallOutlineAssetBitmapFont=new XSmallAssetBitmapFont();
		this.addModelToControl(_xSmallOutlineAssetBitmapFont);			
	//	_introAssetAtlas=new IntroAssetAtlas();
	//	this.getChildController(INTRO_CONTROLLER).addModelToControll(_introAssetAtlas);
		
		this.setLoader(new Loader());
	}

	@Override
	public void onPrepare() {
		_bigOutlineAssetBitmapFont.setSmooth();
		_mediumOutlineAssetBitmapFont.setSmooth();
		_smallOutlineAssetBitmapFont.setSmooth();
		_xSmallOutlineAssetBitmapFont.setSmooth();
		
		_selectAssetAtlas.setSmooth();
		_view=new MainView();
		_view.init();
		this.setView(_view);
		this.getParent().getParent().getView().addContent(_view);
	}
	@Override
	public void onClear() {
	}
}

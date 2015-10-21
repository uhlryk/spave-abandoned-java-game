package pro.artwave.spaceshooter.controller;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.controller.root.GameController;
import pro.artwave.spaceshooter.controller.root.TransactionController;
import pro.artwave.spaceshooter.model.asset.RootAssetAtlas;

public class RootController extends Controller {
	RootAssetAtlas _loaderAssetAtlas;
	public static int GAME_CONTROLLER;
	public static int MAIN_CONTROLLER;
	public static int TRANSACTION_CONTROLLER;
	@Override
	public void onInit(){
		MAIN_CONTROLLER=addController(new MainController());
		GAME_CONTROLLER=addController(new GameController());
		TRANSACTION_CONTROLLER=addController(new TransactionController());
	}

	@Override
	public void onLoad() {
		_loaderAssetAtlas=new RootAssetAtlas();
		this.addModelToControl(_loaderAssetAtlas);
		
	}

	@Override
	public void onPrepare() {
		_loaderAssetAtlas.setSmooth();
	}

	@Override
	public void onClear() {
		_loaderAssetAtlas=null;
	}
}

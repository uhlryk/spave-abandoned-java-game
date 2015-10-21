package pro.artwave.spaceshooter.controller.root.main;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.ToggleView.FinishListener;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.model.asset.IntroAssetAtlas;
import pro.artwave.spaceshooter.view.root.main.IntroView;

public class IntroController extends Controller {
	private IntroAssetAtlas _introAssetAtlas;	
	private IntroView _view;
	public void onInit(){
	}

	@Override
	public void onLoad() {
		_introAssetAtlas=new IntroAssetAtlas();
		this.addModelToControl(_introAssetAtlas);
	}

	@Override
	public void onPrepare() {
		_view=new IntroView();
		_view.init();
		_view.onFinish(new FinishListener() {			
			@Override
			public void onFinish() {
				Intent intent=new Intent(MainController.MAIN_MENU_CONTROLLER);
				getParent().setDispatch(intent);
				System.out.println("aaaaaaa");
			}
		});
		this.setView(_view);
		this.getParent().getView().addContent(_view);
	}
	@Override
	public void onClear() {
		_introAssetAtlas=null;
	}
}

package pro.artwave.spaceshooter.controller.root.main;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button.ClickListener;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.view.root.main.CreditsView;

public class CreditsController extends Controller {
	private CreditsView _view;
	@Override
	public void onInit() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onPrepare() {
		_view=new CreditsView();
		_view.init();
		this.setView(_view);
		this.getParent().getView().addContent(_view);
		_view.onClick(CreditsView.BUTTON_OK,new ClickListener() {			
			@Override
			public void onClick() {
				Intent intent=new Intent(MainController.MAIN_MENU_CONTROLLER);
				getParent().setDispatch(intent);
			}
		});		
	}

}

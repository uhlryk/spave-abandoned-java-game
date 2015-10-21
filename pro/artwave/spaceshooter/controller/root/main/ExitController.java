package pro.artwave.spaceshooter.controller.root.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.spaceshooter.view.root.main.ExitView;

public class ExitController extends Controller {
	private ExitView _view;
	private float _timer;
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
		_timer=0;
		_view=new ExitView();		
		this.setView(_view);
		this.getParent().getView().addContent(_view);
		_view.init();
	}
	@Override
	public void onPlayLoop(float delta){
		_timer+=delta;
		if(_timer>10){
			Gdx.app.exit();
		}
	}
	@Override
	public boolean onKeyUp(int keycode){
		if(keycode==Input.Keys.ESCAPE||Input.Keys.BACK==keycode){
			Gdx.app.exit();
			return true;
		}
		return false;
	}
}

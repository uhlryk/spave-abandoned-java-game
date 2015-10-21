package pro.artwave.spaceshooter.view.root.main;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.ToggleView;
import pro.artwave.spaceshooter.model.asset.IntroAssetAtlas;

public class IntroView extends View {
	private IntroAssetAtlas _introAssetAtlas;	
	private ToggleView _toggleView;
	public void init(){
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		_introAssetAtlas=new IntroAssetAtlas();
		_toggleView=new ToggleView();
		_toggleView.init(2,2,2);
		_toggleView.setSize(this.getWidth(),this.getHeight());
		_toggleView.addSprite(_introAssetAtlas.createSprite("wzoreka"));
		_toggleView.addSprite(_introAssetAtlas.createSprite("wzorekb"));
		_toggleView.addSprite(_introAssetAtlas.createSprite("wzorekc"));
		this.addActor(_toggleView);
	}
	public void onFinish(ToggleView.FinishListener listener){
		_toggleView.addFinishListener(listener);
	}
}

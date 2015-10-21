package pro.artwave.spaceshooter.view.root;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Background;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;

public class MainView extends View {
	private SelectAssetAtlas _selectAssetAtlas;	
	public void init(){
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		_selectAssetAtlas=new SelectAssetAtlas();
		Background background=new Background();
		background.init(_selectAssetAtlas.createSpriteMenuBackground());
		background.setSize(this.getWidth(),this.getHeight());
		this.addActor(background);
	}
}

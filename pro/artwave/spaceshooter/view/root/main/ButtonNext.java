package pro.artwave.spaceshooter.view.root.main;

import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;

public class ButtonNext extends Button {
	private Effects.Play _clickSound;
	public void init(){
		setSize(130,130);
		Effects effects=Effects.getInstance();
		_clickSound=effects.getButtonClickPositive();		
		SelectAssetAtlas selectAssetAtlas=new SelectAssetAtlas();
		this.setBackground(selectAssetAtlas.createSpriteButtonNext());
	//	this.setBackgroundOn(selectAssetAtlas.createSpriteButtonX2On());
		init(1);
	}
	@Override
	protected void onDown(){
		_clickSound.play();
	}	
}

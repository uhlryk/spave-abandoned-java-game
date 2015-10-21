package pro.artwave.spaceshooter.view.root;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Background;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.view.root.main.ButtonPrev;

public class TransactionView extends View {
	public static final int BUTTON_EXIT=0;	
	private SelectAssetAtlas _selectAssetAtlas;	
	private ButtonPrev _buttonExit;	
	public void init(){
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		_selectAssetAtlas=new SelectAssetAtlas();
		Background background=new Background();
		background.init(_selectAssetAtlas.createSpriteMenuBackground());
		background.setSize(this.getWidth(),this.getHeight());
		this.addActor(background);
		_buttonExit=new ButtonPrev();
		_buttonExit.init();
		_buttonExit.setPosition((this.getWidth()-_buttonExit.getWidth())/2,0);
		this.addActor(_buttonExit);		
		System.out.println("TRANSACTION VIEW");
		
	}
	public void onClick(int buttonId,Button.ClickListener listener){
		switch(buttonId){
		case BUTTON_EXIT:
			_buttonExit.addClickListener(listener);
			break;	
		}
	}	
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		batch.setColor(1,1,1,1);
		super.draw(batch, parentAlpha);
	}
}

package pro.artwave.spaceshooter.view.root.main;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;
import pro.artwave.spaceshooter.view.helper.Checkbox;
import pro.artwave.spaceshooter.view.helper.LabeledCheckbox;

public class CreditsView extends View {
	public static final int BUTTON_OK=0;

	private Checkbox _okButton;
	private GlobalTranslation _translation;	
	private UpperLongLabel _upperLabel;	
	


	private Sprite _textBackground;
	private BitmapFont _mediumFont;

	
	public void init(){
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		SelectAssetAtlas selectAssetAtlas=new SelectAssetAtlas();
		_translation=new GlobalTranslation();
		_translation.init();	
		
		_upperLabel=new UpperLongLabel();
		_upperLabel.init(_translation._("Credits","CreditLabel"));
		addActor(_upperLabel);
		_upperLabel.setPosition((this.getWidth()-_upperLabel.getWidth())/2, this.getHeight()-_upperLabel.getHeight());
		
		_textBackground=selectAssetAtlas.createSpriteTextBackground();
		_textBackground.setScale(1.5f);
		
		_okButton=new Checkbox();
		_okButton.init(Checkbox.TYPE_NONE_OFF,true,Checkbox.TYPE_POSITIVE,false);
		_okButton.setPosition(this.getX()+(this.getWidth()-_okButton.getWidth())/2,this.getY()+30);
		this.addActor(_okButton);			

		
		MediumOutlineAssetBitmapFont _fontAssetBitmapFont=new MediumOutlineAssetBitmapFont();
		_mediumFont=_fontAssetBitmapFont.getFont();

	}
	public void onClick(int buttonId,Button.ClickListener listener){
		switch(buttonId){
			case BUTTON_OK:
				_okButton.addClickListener(listener);
				break;			
		}
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){		
		_textBackground.setPosition(this.getX()+(this.getWidth()-_textBackground.getWidth())/2,this.getY()+this.getHeight()-_upperLabel.getHeight()-_textBackground.getHeight()-50);
		_textBackground.draw(batch,parentAlpha*0.8f);
		super.draw(batch, parentAlpha);	
	}	
}

package pro.artwave.spaceshooter.view.root.game;

import pro.artwave.fgm.view.helper.Popup;
import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.model.resource.GameTranslation;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;
import pro.artwave.spaceshooter.view.helper.Checkbox;
import pro.artwave.spaceshooter.view.helper.LabeledCheckbox;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;


public class GameMenuPopup extends Popup {
	public static int BUTTON_ID_RETURN=1;
	public static int BUTTON_ID_EXIT=2;	
	public Sprite _bg;
	private BitmapFont _font;
	private GameTranslation _translation;		
	public void init(){
		super.init();
		_translation=new GameTranslation();
		_translation.init();	
		BigOutlineAssetBitmapFont font=new BigOutlineAssetBitmapFont();
		_font=font.getFont();

	//	GameAssetAtlas gameAssetAtlas=new GameAssetAtlas();
	//	_bg=gameAssetAtlas.createSpritePopupBgEmpty();
	//	_bg.setSize(500,14);
		LabeledCheckbox returnButton=new LabeledCheckbox();
		returnButton.init(_translation._("PopupMenu","button_continue"),true,Checkbox.TYPE_POSITIVE_OFF,false,Checkbox.TYPE_POSITIVE,false);
		
		LabeledCheckbox exitButton=new LabeledCheckbox();
		exitButton.init(_translation._("PopupMenu","button_menu"),false,Checkbox.TYPE_NEGATIVE_OFF,false,Checkbox.TYPE_NEGATIVE,false);
		
		this.setSize(600,229);
		this.addButton(BUTTON_ID_RETURN, returnButton,(int) (this.getWidth()-returnButton.getWidth()),0);
		this.addButton(BUTTON_ID_EXIT, exitButton,0,0);		
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
	//	_bg.setPosition(this.getX()+50,this.getY()+this.getHeight()-150);
		//_bg.draw(batch, parentAlpha);
		_font.setColor(1,1,1,parentAlpha*this.getAnimation());
		_font.setScale(1);
		_font.drawWrapped(batch,"PAUSE",this.getX()+50,this.getY()+220,500,HAlignment.CENTER);		
		super.draw(batch, parentAlpha);
	}
}

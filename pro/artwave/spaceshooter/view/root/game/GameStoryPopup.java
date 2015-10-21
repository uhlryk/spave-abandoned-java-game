package pro.artwave.spaceshooter.view.root.game;

import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.model.resource.GameTranslation;
import pro.artwave.spaceshooter.view.helper.Checkbox;
import pro.artwave.spaceshooter.view.helper.LabeledCheckbox;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class GameStoryPopup extends MessagePopup {
	public static int BUTTON_ID_LEFT=1;		
	public static int BUTTON_ID_RIGHT=2;	
	public static int BUTTON_ID_CENTER=3;	
	private boolean _isShow;
	private GameTranslation _translation;
	public Sprite _bg;
	public Sprite _image;
	private GameAssetAtlas _gameAssetAtlas;
	public void init(){
		super.init();
		_translation=new GameTranslation();
		_translation.init();			
		_gameAssetAtlas=new GameAssetAtlas();
		_bg=_gameAssetAtlas.createSpritePopupBgBig();
		_bg.setSize(570,296);	
		
		
		
		_isShow=false;
		this.setSize(650,330);
		this.setTitle("");
		this.setSmallText("");
		this.setBigText("");
		
	/*	this.setSmallTextYOffset(-20);
		this.setSmallTextXOffset(130);
		this.setSmallTextWidth(540);*/
		
		this.setBigTextOffset(-100);
		
		LabeledCheckbox leftButton=new LabeledCheckbox();
		leftButton.setVisible(false);
		leftButton.init("",false,Checkbox.TYPE_NEGATIVE_OFF,false,Checkbox.TYPE_NEGATIVE,false);
		this.addButton(BUTTON_ID_LEFT, leftButton,50,0);			
		
		LabeledCheckbox rightButton=new LabeledCheckbox();
		rightButton.setVisible(false);
		rightButton.init("",true,Checkbox.TYPE_POSITIVE_OFF,false,Checkbox.TYPE_POSITIVE,false);
		this.addButton(BUTTON_ID_RIGHT, rightButton,(int) (this.getWidth()-rightButton.getWidth())+100,0);
		
		LabeledCheckbox centerButton=new LabeledCheckbox();
		centerButton.setVisible(false);
		centerButton.init("",true,Checkbox.TYPE_POSITIVE_OFF,false,Checkbox.TYPE_POSITIVE,false);
		this.addButton(BUTTON_ID_CENTER, centerButton,(int) (this.getWidth()-centerButton.getWidth())+100,0);	
		

	}
	/**
	 * Sprawdzamy czy ten popup ma byæ widoczny. Mo¿e siê zda¿yæ ¿e gdy wdusimy pause to zniknie
	 * Po wy³¹czeniu pausy ma byæ dalej widoczny
	 * @return
	 */
	public boolean commandShow(){
		return _isShow;
	}
	/**
	 * okreœla czy mamy typ z obrazkiem czy bez
	 */
	private void setType(boolean isImageType){
		if(isImageType){
			
			this.setSmallTextYOffset(-15);
			this.setSmallTextXOffset(250);
			this.setSmallTextWidth(430);
		}else{
			
			this.setSmallTextYOffset(-20);
			this.setSmallTextXOffset(130);
			this.setSmallTextWidth(560);
		}
		
	}
	public void showMessage(String message,String button1,int imageId){
		this.setSmallText(_translation.getStoryMessage(message));
		this.getButton(BUTTON_ID_LEFT).setVisible(false);
		this.getButton(BUTTON_ID_RIGHT).setVisible(false);
		this.getButton(BUTTON_ID_CENTER).setVisible(true);
		this.getButton(BUTTON_ID_CENTER).setText(_translation.getStoryPopupButton(button1));
		_isShow=true;
		if(imageId==0){
			_image=null;
			this.setType(false);
		}else{
			_image=_gameAssetAtlas.createSpriteImage(imageId);
			_image.setScale(0.8f);
			this.setType(true);
		}
	//	System.out.println("START POPUP AAAA!!!!!!!!!!!!!");
		super.show();
	}
	public void showSelect(String message,String button1,String button2,int imageId){
		_isShow=true;
		this.setSmallText(_translation.getStoryMessage(message));
		this.getButton(BUTTON_ID_LEFT).setVisible(true);
		this.getButton(BUTTON_ID_RIGHT).setVisible(true);
		this.getButton(BUTTON_ID_CENTER).setVisible(false);
		this.getButton(BUTTON_ID_LEFT).setText(_translation.getStoryPopupButton(button1));
		this.getButton(BUTTON_ID_RIGHT).setText(_translation.getStoryPopupButton(button2));
	//	System.out.println("START POPUP BBBB!!!!!!!!!!!!!");
		if(imageId==0){
			_image=null;
			this.setType(false);
		}else{
			_image=_gameAssetAtlas.createSpriteImage(imageId);
			_image.setScale(0.8f);
			this.setType(true);
		}
		super.show();
	}
	public void hideMessage(){
		_isShow=false;
		super.hide();
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		_bg.setPosition(this.getX()+115,this.getY()+this.getHeight()-290);
		_bg.draw(batch, parentAlpha*this.getAnimation());	
		if(_image!=null){
			_image.setPosition(this.getX()+109,this.getY()+this.getHeight()-205);
			_image.draw(batch, parentAlpha*this.getAnimation());	
		}
		super.draw(batch, parentAlpha);
	}	
	
}

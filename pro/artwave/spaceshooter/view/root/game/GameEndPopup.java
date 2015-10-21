package pro.artwave.spaceshooter.view.root.game;

import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.model.resource.GameTranslation;
import pro.artwave.spaceshooter.view.helper.Checkbox;
import pro.artwave.spaceshooter.view.helper.LabeledCheckbox;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;


public class GameEndPopup extends MessagePopup {
	public static final String MESSAGE_SUCCESS_LOW="successNorecord";
	public static final String MESSAGE_SUCCESS_RECORD="successRecord";
	public static final String MESSAGE_FAILED_SHIP_DESTROY="failureDestruction";//statek gracza zosta³ zniszczony
	public static final String MESSAGE_FAILED_OTHER="failureOther";
	public static final int BUTTON_TYPE_LEFT=1;		
	public static final int BUTTON_TYPE_RIGHT=2;
	
	public static final String TITLE_SUCCESS="success";
	public static final String TITLE_FAILED="failure";
	
	public static final String LABEL_CONTINUE="continue";	
	public static final String LABEL_MENU="menu";
	public static final String LABEL_RESTART="restart";
	public static final String LABEL_FINISH="finish";
	
	public Sprite _bg;
	private BitmapFont _font;
	private String _title;
	private GameTranslation _translation;
	public void init(){
		super.init();
		_translation=new GameTranslation();
		_translation.init();
		BigOutlineAssetBitmapFont font=new BigOutlineAssetBitmapFont();
		_font=font.getFont();

	//	GameAssetAtlas gameAssetAtlas=new GameAssetAtlas();
	//	_bg=gameAssetAtlas.createSpritePopupBgEmpty();
	//	_bg.setSize(600,14);
		LabeledCheckbox leftButton=new LabeledCheckbox();
		leftButton.init("",false,Checkbox.TYPE_NEGATIVE_OFF,false,Checkbox.TYPE_NEGATIVE,false);
		
		LabeledCheckbox rightButton=new LabeledCheckbox();
		rightButton.init("",true,Checkbox.TYPE_POSITIVE_OFF,false,Checkbox.TYPE_POSITIVE,false);

		this.setSize(600,229);
		this.addButton(BUTTON_TYPE_LEFT, leftButton,0,0);	
		this.addButton(BUTTON_TYPE_RIGHT, rightButton,(int) (this.getWidth()-rightButton.getWidth()),0);
		
		this.setTitle("");
		this.setSmallText("");	
		this.setBigText("");
		this.setSmallTextYOffset(-40);
		_title="";
	}
	public void setButtonText(int buttonId,String buttonLabel){
		super.setButtonText(buttonId,_translation.getEndPopupButton(buttonLabel));
	}
	/**
	 * Podobne do
	 * @see setMessage(int titleId,int messageId)
	 * ale ma dodatkowy parametr pozwalaj¹cy dodaæ stringa, jest on niet³umaczony przez modu³ nale¿y wiêc
	 * obs³u¿yæ go w miejscu dodania. Przyk³adem moze byæ "1000 punktów" "przez dzia³ko naziemne" itp
	 * @param titleId
	 * @param messageId
	 * @param messageAddition
	 */
	public void setMessage(String title,String message,String messageAddition){
		setMessage(_translation.getEndPopupTitle(title),_translation.getEndPopupMessage(message)+messageAddition);
	}	
	/**
	 * Ustawia popup tak by mia³ zdefiniowany tytu³ i wiadomoœæ
	 * @param title bêdzie t³umaczony przez metodê
	 * @param message wiadomoœæ musi byæ przet³umaczona, metoda sama nie zrobi tego
	 */
	public void setMessage(String title,String message){
		_title=_translation.getEndPopupTitle(title);
		this.setSmallText(_translation.getEndPopupMessage(message));
	}
	/**
	 * Metoda ustawia dane popupa i go wyœwietla
	 * @see setMessage(int titleId,int messageId,String messageAddition)
	 * @param titleId
	 * @param messageId
	 * @param messageAddition
	 */
	public void showMessage(String title,String message,String messageAddition){
		setMessage(title,message,messageAddition);
		super.show();
	}	
	/**
	 * @see showMessage(int titleId,int messageId,String messageAddition)
	 * @param titleId
	 * @param messageId
	 */
	public void showMessage(String title,String message){
		setMessage(title,message);
		super.show();
	}	
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
	//	_bg.setPosition(this.getX()+50,this.getY()+this.getHeight()-150);
	//	_bg.draw(batch, parentAlpha);
		_font.setColor(1,1,1,1);
		_font.setScale(1);
		_font.drawWrapped(batch,_title,this.getX()-50,this.getY()+270,this.getWidth()+100,HAlignment.CENTER);		
		super.draw(batch, parentAlpha);
	}	
}

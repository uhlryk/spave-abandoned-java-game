package pro.artwave.spaceshooter.view.root.main.selectequipment;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.PulseSprite;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.helper.WeaponParams;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.model.asset.RootAssetAtlas;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.XSmallAssetBitmapFont;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;
import pro.artwave.spaceshooter.model.resource.WeaponResource;
import pro.artwave.spaceshooter.view.helper.Checkbox;
import pro.artwave.spaceshooter.view.root.main.ButtonNext;
import pro.artwave.spaceshooter.view.root.main.ButtonPrev;
import pro.artwave.spaceshooter.view.root.main.UpperLongLabel;

public class SelectWeaponView extends View {
	public static final int BUTTON_PREV=0;
	public static final int BUTTON_NEXT=1;	
	private ButtonPrev _buttonPrev;
	private ButtonNext _buttonNext;
	public static final int BUTTON_ACTIVE_WEAPON=1;

	
	private BitmapFont _mediumFont;
	private BitmapFont _requirementsFont;
	private BitmapFont _smallFont;
	private Sprite bigImage;
	private Table _table;
	private SelectAssetAtlas _selectAssetAtlas;	
	private RootAssetAtlas _rootAssetAtlas;
	private Sprite _smallBg;
	private Sprite _bigBg;
	private GlobalTranslation _translation;	
	private String _name;
	private ScrollPane _scroll;
	private Checkbox _selectButton;
	
	private UpperLongLabel _upperMenu;
	
	private int _points;
	private int _activeWeaponId;
	private int _secondaryWeaponId;
	private int _selectedWeaponId;
	private String _requirementsLabel;
//	private Sprite _bigShipActiveLabelBg;
	private ClickListener _listener;
	
	private float _moveScroll;
	private boolean _isScrollTarget;
	private float _lastScrollPos;
	
	private Map<Integer, SelectWeapon> _weaponList;
	
//	private Sprite _starSprite;
	private Sprite _iconDamage;
	private Sprite _iconReload;
	private Sprite _iconSpeed;
	private Sprite _iconTick;
	
	private StatBar _statBarDamage;
	private StatBar _statBarReload;
	private StatBar _statBarSpeed;
	
	private WeaponResource _resource;
	
	private Sprite _buttonPositive;
	private Sprite _buttonNegative;
	
	private PulseSprite _buttonSmallNeutral;
	private Sprite _buttonSmallNegative;
	private Sprite _buttonSmallPositive;
	
	private String _activeLabel;
	private String _scoreLabel;
	private Effects.Play _clickSound;
	public void init(WeaponResource resource,int points,int activeWeaponId,int secondaryActiveWeaponId,int weaponType){
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		Effects effects=Effects.getInstance();
		_clickSound=effects.getButtonClick1();
		_resource=resource;
		_points=points;
		_activeWeaponId=activeWeaponId;
		_secondaryWeaponId=secondaryActiveWeaponId;
		_translation=new GlobalTranslation();
		_translation.init();			
		_selectAssetAtlas=new SelectAssetAtlas();
		_rootAssetAtlas=new RootAssetAtlas();
		_smallBg=_selectAssetAtlas.createSpriteIconBg();
		_smallBg.setScale(1.5f);
		_smallBg.setColor(0.00f,0.00f,0.00f,0.8f);
		
		_bigBg=_selectAssetAtlas.createSpriteIconBg();
		_bigBg.setScale(2.5f);
		_bigBg.setColor(0.00f,0.00f,0.00f,0.8f);
		
		_iconDamage=_selectAssetAtlas.createSpriteGraphic(4);
		_iconReload=_selectAssetAtlas.createSpriteGraphic(5);
		_iconSpeed=_selectAssetAtlas.createSpriteGraphic(3);

		_iconTick=_selectAssetAtlas.createSpriteTick();
		
		_buttonPositive=_rootAssetAtlas.createSpriteButton3Positive();
		_buttonNegative=_rootAssetAtlas.createSpriteButton3Negative();
		
		_buttonSmallPositive=_rootAssetAtlas.createSpriteButton3Positive();
		_buttonSmallPositive.setScale(0.5f);
		
		_buttonSmallNegative=_rootAssetAtlas.createSpriteButton3Negative();
		_buttonSmallNegative.setScale(0.5f);
		
		_buttonSmallNeutral=new PulseSprite(0.6f,1f);
		_buttonSmallNeutral.set(_rootAssetAtlas.createSpriteButton3());
		_buttonSmallNeutral.startPulse();
		_buttonSmallNeutral.setScale(0.5f);
		
		MediumOutlineAssetBitmapFont smallAssetBitmapFont=new MediumOutlineAssetBitmapFont();
		_mediumFont=smallAssetBitmapFont.getFont();
		SmallOutlineAssetBitmapFont mediumAssetBitmapFont=new SmallOutlineAssetBitmapFont();
		_requirementsFont=mediumAssetBitmapFont.getFont();
		XSmallAssetBitmapFont xsmallAssetBitmapFont=new XSmallAssetBitmapFont();
		xsmallAssetBitmapFont.setSmooth();
		_smallFont=xsmallAssetBitmapFont.getFont();
		_weaponList=new HashMap<Integer,SelectWeapon>(20);
		_table=new Table();
		_moveScroll=0;
		_isScrollTarget=false;
		_lastScrollPos=-1;
		_scoreLabel=_translation._("SelectShip","ScorePoint")+"\n"+Integer.toString(points);
		boolean lockStartHeight=false;
		for(Entry<Integer,WeaponParams> entry:_resource.getWeaponMap().entrySet()){
			WeaponParams weaponParams=entry.getValue();
			if(weaponParams.playability==true&&weaponParams.type==weaponType){
				int weaponId=entry.getKey();
				if(weaponId==_secondaryWeaponId){
					continue;
				}
				SelectWeapon selectWeapon=new SelectWeapon(weaponId,weaponParams.points);
				selectWeapon.setImage(_selectAssetAtlas.createSpriteWeapon(weaponId));
				_table.add(selectWeapon);
				_table.row();
				_weaponList.put(weaponId, selectWeapon);
				if(lockStartHeight==false){
					if(_activeWeaponId==weaponId){
						lockStartHeight=true;
					}else{
						_moveScroll+=selectWeapon.getHeight();
					}
				}
			}
		}
		_table.padTop(150);
		_table.padBottom(150);
		_scroll=new ScrollPane(_table);
	//	scroll.setFadeScrollBars(true);
		_scroll.setSize(280, this.getHeight());
	//	_scroll.setScrollY(startHeight);
	//	_scroll.setScrollPercentY(0.5f);
		this.addActor(_scroll);
		
		_selectButton=new Checkbox();
		_selectButton.init(Checkbox.TYPE_NONE,true,Checkbox.TYPE_POSITIVE,false);
		_selectButton.setPosition((this.getWidth()-_selectButton.getWidth())/2-90,this.getY()+260);
		_selectButton.addClickListener(new Button.ClickListener() {	
			@Override
			public void onClick() {
				_clickSound.play();
				_activeWeaponId=_selectedWeaponId;
				selectBigWeapon(_selectedWeaponId);
				_listener.onClick(_activeWeaponId);
			}
		});
		this.addActor(_selectButton);
		
		_statBarDamage=new StatBar();
		_statBarDamage.init(100, 20);
		_statBarDamage.setPosition((this.getWidth())/2+80,350);
		this.addActor(_statBarDamage);
		_statBarReload=new StatBar();
		_statBarReload.init(1500,20);
		_statBarReload.setPosition((this.getWidth())/2+165,350);
		this.addActor(_statBarReload);
		_statBarSpeed=new StatBar();
		_statBarSpeed.init(250,20);
		_statBarSpeed.setPosition((this.getWidth())/2+250,350);
		this.addActor(_statBarSpeed);	
		_activeLabel=_translation._("SelectShip","ACTIVE_SHIP");

		selectBigWeapon(_activeWeaponId);
		
		_buttonPrev=new ButtonPrev();
		_buttonPrev.init();
		_buttonPrev.setPosition(20,5);
		this.addActor(_buttonPrev);
		_buttonNext=new ButtonNext();
		_buttonNext.init();
		_buttonNext.setPosition(this.getWidth()-_buttonNext.getWidth()-20,5);
		this.addActor(_buttonNext);	
		_upperMenu=new UpperLongLabel();
		if(weaponType==1){
			_upperMenu.init(_translation._("SelectEquipment","SelectWeaponTitle1"));
		}else{
			_upperMenu.init(_translation._("SelectEquipment","SelectWeaponTitle2"));
		}
		addActor(_upperMenu);
		_upperMenu.setPosition((this.getWidth()-_upperMenu.getWidth())/2, this.getHeight()-_upperMenu.getHeight());
				
	}
	public void onClick(int buttonId,Button.ClickListener listener){
		switch(buttonId){
		case BUTTON_PREV:
			_buttonPrev.addClickListener(listener);
			break;
		case BUTTON_NEXT:
			_buttonNext.addClickListener(listener);
			break;
		}
	}	
	public void onClick(int buttonId,final ClickListener listener){
		switch(buttonId){
		case BUTTON_ACTIVE_WEAPON:
			_listener=listener;
			break;
		}
	}	
	public static abstract class ClickListener{
		public abstract void onClick(int shipId);
	}
	@Override
	public void act(float delta){
		super.act(delta);
		_buttonSmallNeutral.act(delta);
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		
		if(_isScrollTarget==false){
			_scroll.setScrollY(_moveScroll);
			_scroll.setTouchable(Touchable.disabled);
			if(_scroll.getScrollY()==_moveScroll||_lastScrollPos==_scroll.getScrollY()){
				_isScrollTarget=true;
			}
			_lastScrollPos=_scroll.getScrollY();
		}else{
			_scroll.setTouchable(Touchable.enabled);
		}
		
		_requirementsFont.setColor(1,0.3f,0.3f,1);
		_bigBg.setPosition(this.getX()+(this.getWidth()-_bigBg.getWidth())/2-60,this.getY()+180);
		_bigBg.draw(batch,parentAlpha);
		bigImage.setPosition(this.getX()+(this.getWidth()-bigImage.getWidth())/2-90,this.getY()+180);
	//	_smallFont.setColor(1,1, 1,1);
		
		if(_activeWeaponId==_selectedWeaponId){

			bigImage.draw(batch,parentAlpha);	
			_buttonSmallPositive.setPosition(this.getX()+(this.getWidth()-_buttonSmallPositive.getWidth())/2-90,this.getY()+420);
			_buttonSmallPositive.draw(batch);
		}else if(_requirementsLabel!=null){
			bigImage.draw(batch,parentAlpha*0.4f);	
			_mediumFont.setColor(1,1,1,1);
			_mediumFont.drawWrapped(batch,_scoreLabel,this.getX()+(this.getWidth()-200)/2-90,this.getY()+370,200,HAlignment.CENTER);
			_requirementsFont.drawWrapped(batch,_requirementsLabel,this.getX()+(this.getWidth()-200)/2-90,this.getY()+270,200,HAlignment.CENTER);
		}else{
			bigImage.draw(batch,parentAlpha);	
	//		_mediumFont.setColor(1f,1f,1,1);
	//		_mediumFont.drawWrapped(batch,_labelSelectShip,this.getX()+(this.getWidth()-200)/2-60,this.getY()+509,180,HAlignment.CENTER);
		}
		
		
	//	_smallFont.drawWrapped(batch,_name,this.getX()+this.getWidth()/2+140,this.getY()+this.getHeight()/2-110,280,HAlignment.LEFT);
		_iconDamage.setPosition(this.getX()+(this.getWidth())/2+80,this.getY()+285);
		_iconDamage.draw(batch);
		_iconReload.setPosition(this.getX()+(this.getWidth())/2+165,this.getY()+285);
		_iconReload.draw(batch);
		_iconSpeed.setPosition(this.getX()+(this.getWidth())/2+250,this.getY()+285);
		_iconSpeed.draw(batch);
		
		super.draw(batch, parentAlpha);
		
	}	
	private void selectBigWeapon(int id){
		_requirementsLabel=null;
		bigImage=_selectAssetAtlas.createSpriteWeapon(id);	
		_selectedWeaponId=id;
		if(_activeWeaponId==_selectedWeaponId){
			_selectButton.setVisible(false);
			WeaponParams weaponParams=_resource.getWeapon(_activeWeaponId);
			_statBarDamage.setStat(weaponParams.damage);
			_statBarReload.setStat(weaponParams.reload);
			_statBarSpeed.setStat(weaponParams.speed);			
		}else{
			WeaponParams refWeaponParams=_resource.getWeapon(_activeWeaponId);
			WeaponParams weaponParams=_resource.getWeapon(id);
			
			_statBarDamage.setStat(weaponParams.damage, refWeaponParams.damage);
			_statBarReload.setStat(weaponParams.reload, refWeaponParams.reload);
			_statBarSpeed.setStat(weaponParams.speed, refWeaponParams.speed);
			
			SelectWeapon weapon=_weaponList.get(id);
			if(weapon.getMinPoints()>_points){
				_requirementsLabel="";//_translation._("SelectShip","Requirements_label")+"\n";
				if(weapon.getMinPoints()>_points){//nie spe³niamy kryteriów punktów
					_requirementsLabel+=_translation._("SelectShip","Requirements_point")+"\n"+weapon.getMinPoints()+"\n";
				}	
				_selectButton.setVisible(false);
			}else{
				_selectButton.setVisible(true);
			}
		}
		
	}
	private class SelectWeapon extends Actor{
		private int _weaponId;
		private int _minPoints;
		private int _offsetScoreLabel;
		public SelectWeapon(int id,int points){
			setSize(280, 200);
			_weaponId=id;
			_minPoints=points;

			this.addListener(new InputListener(){
				private float _dist_x=10;
				private float _dist_y=10;				
				private float _x;
				private float _y;	
				private boolean _dragged;
				
				private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
				private static final int DOUBLE_DISTANCE=30;
				long lastClickTime = 0;	
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			//		_isTouchDown=true;
					_dragged=false;
					_x=x;
					_y=y;				
					return true;
				}
				@Override
				public void touchDragged (InputEvent event, float x, float y, int pointer) {
					if(x-_x>_dist_x||x-_x<-_dist_x||y-_y>_dist_y||y-_y<-_dist_y){
						_dragged=true;
			//			_isTouchDown=false;
					}
				}	
				@Override
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//	_isTouchDown=false;
					if(_dragged==false){
						_clickSound.play();
						SelectWeaponView.this.selectBigWeapon(_weaponId);

						long clickTime = System.currentTimeMillis();
				        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA
				        		&&_x+DOUBLE_DISTANCE>x&&_x-DOUBLE_DISTANCE<x
				        		&&_y+DOUBLE_DISTANCE>y&&_y-DOUBLE_DISTANCE<y
				        ){//double click
				        	System.out.println("almost double click!");
				        	if(_selectButton.isVisible()){
				        		System.out.println("double click!");
				        		_selectButton.triggerButton();
				        	}
				        } else {//single click
				        }
				        lastClickTime = clickTime;
						
					}
				}
			});
		}
		public int getMinPoints(){
			return _minPoints;
		}		
		private Sprite _image;
		public void setImage(Sprite image){
			_image=image;
			_image.setSize(_image.getWidth()/2,_image.getHeight()/2);
		}
		@Override
		public void draw(SpriteBatch batch,float parentAlpha){
			_smallBg.setPosition(this.getX()+(this.getWidth()-_smallBg.getWidth())/2,this.getY()+(this.getHeight()-_smallBg.getHeight())/2);
			_smallBg.draw(batch,parentAlpha);	
			_image.setPosition(this.getX()+(this.getWidth()-_image.getWidth())/2,this.getY()+(this.getHeight()-_image.getHeight())/2);
			
			if(_activeWeaponId==_weaponId){
				_image.draw(batch,parentAlpha);		
				_buttonSmallPositive.setPosition(this.getX()+(this.getWidth()-_buttonSmallPositive.getWidth())/2,this.getY()+(this.getHeight()-_buttonSmallPositive.getHeight())/2);
				_buttonSmallPositive.draw(batch,parentAlpha);
			}else if(_minPoints>_points){
				_image.draw(batch,parentAlpha*0.3f);	
				_buttonSmallNegative.setPosition(this.getX()+(this.getWidth()-_buttonSmallNegative.getWidth())/2,this.getY()+(this.getHeight()-_buttonSmallNegative.getHeight())/2);
				_buttonSmallNegative.draw(batch,parentAlpha);
			}else{
				_image.draw(batch,parentAlpha);	
				_buttonSmallNeutral.setPosition(this.getX()+(this.getWidth()-_buttonSmallNeutral.getWidth())/2,this.getY()+(this.getHeight()-_buttonSmallNeutral.getHeight())/2);
				_buttonSmallNeutral.draw(batch,parentAlpha);
			}		
		}
	}
}

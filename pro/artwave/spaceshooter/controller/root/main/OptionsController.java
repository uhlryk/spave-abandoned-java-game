package pro.artwave.spaceshooter.controller.root.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button.ClickListener;
import pro.artwave.spaceshooter.controller.root.MainController;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.view.root.main.OptionsView;

public class OptionsController extends Controller {
	private OptionsView _view;
	private Preferences _prefs;
	private boolean _isMusicOn;
	private boolean _isEffectsOn;
	private float _musicVolume;
	private float _effectsVolume;
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
		_prefs=Gdx.app.getPreferences("Options");
		_isMusicOn=_prefs.getBoolean("musicOn",true);
		_isEffectsOn=_prefs.getBoolean("effectsOn",true);
		_musicVolume=_prefs.getFloat("musicVolume",100);
		_effectsVolume=_prefs.getFloat("effectsVolume",100);
		_view=new OptionsView();
		_view.init(_isMusicOn,_isEffectsOn,_musicVolume,_effectsVolume);
		this.setView(_view);
		this.getParent().getView().addContent(_view);
		_view.onClick(OptionsView.BUTTON_CANCEL,new ClickListener() {			
			@Override
			public void onClick() {
				Intent intent=new Intent(MainController.MAIN_MENU_CONTROLLER);
				getParent().setDispatch(intent);
			}
		});
		_view.onClick(OptionsView.BUTTON_ACCEPT,new ClickListener() {			
			@Override
			public void onClick() {
				_prefs.putBoolean("effectsOn",_isEffectsOn);
				_prefs.putBoolean("musicOn",_isMusicOn);
				_prefs.putFloat("musicVolume",_musicVolume);
				_prefs.putFloat("effectsVolume",_effectsVolume);
				_prefs.flush();
				Effects effects=Effects.getInstance();
				effects.setVolume();
				Intent intent=new Intent(MainController.MAIN_MENU_CONTROLLER);
				getParent().setDispatch(intent);
			}
		});
		_view.onClick(OptionsView.BUTTON_EFFECTS_OFF,new ClickListener() {
			@Override
			public void onClick() {
				_view.setButtonState(OptionsView.BUTTON_EFFECTS_ON,false);
				_view.setButtonState(OptionsView.BUTTON_EFFECTS_OFF,true);
				_isEffectsOn=false;
			}
		});
		_view.onClick(OptionsView.BUTTON_EFFECTS_ON,new ClickListener() {
			@Override
			public void onClick() {
				_view.setButtonState(OptionsView.BUTTON_EFFECTS_ON,true);
				_view.setButtonState(OptionsView.BUTTON_EFFECTS_OFF,false);
				_isEffectsOn=true;
			}
		});	
		_view.onClick(OptionsView.BUTTON_MUSIC_OFF,new ClickListener() {
			@Override
			public void onClick() {
				_view.setButtonState(OptionsView.BUTTON_MUSIC_ON,false);
				_view.setButtonState(OptionsView.BUTTON_MUSIC_OFF,true);
				_isMusicOn=false;			
			}
		});
		_view.onClick(OptionsView.BUTTON_MUSIC_ON,new ClickListener() {
			@Override
			public void onClick() {
				_view.setButtonState(OptionsView.BUTTON_MUSIC_ON,true);
				_view.setButtonState(OptionsView.BUTTON_MUSIC_OFF,false);
				_isMusicOn=true;	
			}
		});	
		_view.onChange(OptionsView.SLIDER_MUSIC,new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				float value = ( (Slider) actor ).getValue();
				_musicVolume=value;
				//System.out.println("music volume "+value);
			}
        });
		_view.onChange(OptionsView.SLIDER_EFFECTS,new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				float value = ( (Slider) actor ).getValue();
				_effectsVolume=value;
			//	System.out.println("effects volume "+value);
			}
        });		
	}

}

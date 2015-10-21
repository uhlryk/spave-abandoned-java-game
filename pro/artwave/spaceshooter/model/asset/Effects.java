package pro.artwave.spaceshooter.model.asset;

import java.util.HashMap;
import java.util.Map;

import pro.artwave.fgm.model.asset.LoaderManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;

public class Effects {
	private static Effects _instance;
	private boolean _isMusicOn;
	private boolean _isEffectsOn;
	private float _musicVolume;
	private float _effectsVolume;	
	public static Effects getInstance(){
		if(Effects._instance==null){
			Effects._instance=new Effects();
		}
		return Effects._instance;
	}
	private Effects(){
		this.setVolume();
		prepareSound("click1");
		prepareSound("click2");
		
		prepareSound("bonus2");
		prepareSound("laser1");
		prepareSound("laser2");
		prepareSound("laser3");
		
		prepareSound("explosion4");
		prepareSound("explosion2");
	}
	public void setVolume(){
		Preferences prefs=Gdx.app.getPreferences("Options");
		_isMusicOn=prefs.getBoolean("musicOn",true);
		_isEffectsOn=prefs.getBoolean("effectsOn",true);
		_musicVolume=prefs.getFloat("musicVolume",100)/100;
		_effectsVolume=prefs.getFloat("effectsVolume",100)/100;
	}
	private String path="effects/";

	public String getResourceName(String name){
		return path+name+".wav";
	}
	public void prepareSound(String name){
		LoaderManager.getAssetManager().load(getResourceName(name),Sound.class);
	}
	public Sound getSound(String name){
		Sound sound=null;
		if(LoaderManager.getAssetManager().isLoaded(getResourceName(name))) {
			sound =LoaderManager.getAssetManager().get(getResourceName(name), Sound.class);;
		}else {
			throw new RuntimeException("Nie uda³o siê za³adowaæ pliku "+getResourceName(name));
		}
		return sound;
	}
	public static void clear(){
		Effects._instance=null;
	}
	public Effects.Play getPlayObject(String name){
		Sound sound=this.getSound(name);
	//	sound.setVolume(id,100);
		return new Play(sound);
	}
	public Effects.Play getButtonClick1(){
		return getPlayObject("click2");
	} 
	public Effects.Play getButtonClickNegative(){
		return getPlayObject("click1");
	} 	
	public Effects.Play getButtonClickPositive(){
		return getPlayObject("click2");//zap1 ale za wysoki ton
	} 	
	public Effects.Play getBonusSound(){
		return getPlayObject("bonus2");
	} 	
	/**
	 * Modyfikator g³oœnoœci domyœlnie 1. jak damy 2 to bêdzie dŸwiêk 2x g³oœniejszy ni¿ 
	 * ustawiony. jak damy 0.5 to bêdzie 2x cichszy
	 * @param volume
	 */
	public Effects.Play getFire1(float volume){
		Effects.Play e=getPlayObject("laser2");
		e.setVolume(volume);
		return e;
	} 		
	/**
	 * Modyfikator g³oœnoœci domyœlnie 1. jak damy 2 to bêdzie dŸwiêk 2x g³oœniejszy ni¿ 
	 * ustawiony. jak damy 0.5 to bêdzie 2x cichszy
	 * @param volume
	 */	
	public Effects.Play getFire2(float volume){
		Effects.Play e=getPlayObject("laser3");
		e.setVolume(volume);
		return e;		
	} 	
	/**
	 * Modyfikator g³oœnoœci domyœlnie 1. jak damy 2 to bêdzie dŸwiêk 2x g³oœniejszy ni¿ 
	 * ustawiony. jak damy 0.5 to bêdzie 2x cichszy
	 * @param volume
	 */	
	public Effects.Play getFire3(float volume){
		Effects.Play e=getPlayObject("laser3");
		e.setVolume(volume);
		return e;		
	} 	
	public Effects.Play getExplosion(){
		return getPlayObject("explosion4");
	} 	
	public Effects.Play getHurt(){
		return getPlayObject("explosion2");
	} 		
	public class Play{
		private Sound _sound;
		private float _volume;
		public Play(Sound sound){
			_sound=sound;
			_volume=1;
		}
		/**
		 * Modyfikator g³oœnoœci domyœlnie 1. jak damy 2 to bêdzie dŸwiêk 2x g³oœniejszy ni¿ 
		 * ustawiony. jak damy 0.5 to bêdzie 2x cichszy
		 * @param volume
		 */
		public void setVolume(float volume){
			_volume=volume;
		}
		public void play(){
			long id =_sound.play();
		//	System.out.println("volume "+_effectsVolume);
			_sound.setVolume(id,_effectsVolume*_volume);
		//	_sound.setPitch(id,2f);
		}
	}
}

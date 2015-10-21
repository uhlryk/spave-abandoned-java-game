package pro.artwave.spaceshooter;

import pro.artwave.fgm.controller.ControllerInputProcessor;
import pro.artwave.fgm.controller.ControllerManager;
import pro.artwave.fgm.model.asset.LoaderManager;
import pro.artwave.fgm.model.resource.Translator;
import pro.artwave.fgm.utility.Setting;
import pro.artwave.spaceshooter.controller.RootController;
import pro.artwave.spaceshooter.model.asset.Effects;
import pro.artwave.spaceshooter.view.FullScreenViewManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class SpaceShooter implements ApplicationListener {
	private SpriteBatch _batch;
	private FPSLogger _fpsLogger;	
	private Stage _stage;
	private ControllerManager _controllerManager;
	private boolean _isResize;
	public SpaceShooter(int width,int height){
		Setting.setStage(width,height);

	}
	@Override
	public void create() {	
		_isResize=false;
	//	Gdx.graphics.setDisplayMode(1280, 800, true);
	//	Setting.setStage(1280, 800);
		this._fpsLogger = new FPSLogger();	
		this._batch=new SpriteBatch();
		System.out.println("prepare effects");
		Effects effects=Effects.getInstance();
		System.out.println("end prepare effects");
	//	_batch.disableBlending();
		
		this._stage=new Stage(Setting.getStage().width,Setting.getStage().height, true,this._batch);
		Setting.setScreen(Setting.getStage().width+this._stage.getGutterWidth()*2,Setting.getStage().height+this._stage.getGutterHeight()*2);
		
		Setting.setGutter(this._stage.getGutterWidth(),this._stage.getGutterHeight());
		this._controllerManager=new ControllerManager(this._stage,new RootController(),new FullScreenViewManager());
		ControllerInputProcessor controllerInputProcessor=new ControllerInputProcessor();
		controllerInputProcessor.init(this._controllerManager);
		InputMultiplexer multiplexer=new InputMultiplexer();
		multiplexer.addProcessor(controllerInputProcessor);
		multiplexer.addProcessor(this._stage);
		Gdx.input.setInputProcessor(multiplexer);	
		Translator.setLanguage("en");
	}
	@Override
	public void dispose() {
		Effects.clear();
		LoaderManager.clear();
	}
	@Override
	public void render() {	
		float delta=Gdx.graphics.getDeltaTime();
		
		Gdx.gl.glClearColor(0,0,0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this._controllerManager.onRender(delta);
		this._stage.act(delta);
		this._stage.draw();		
		//this._fpsLogger.log();
	}
	@Override
	public void resize(int width, int height) {
		if(_isResize==false){
			_isResize=true;
			this._stage.setViewport(Setting.getStage().width,Setting.getStage().height, true);
			this._stage.getCamera().translate(-this._stage.getGutterWidth(),-this._stage.getGutterHeight(),0f);	
		}
	//	this._stage.getCamera().translate(0f,0f,0f);
	}
	@Override
	public void pause() {	
		this._controllerManager.systemPause();
	}
	@Override
	public void resume() {
		this._controllerManager.systemResume();
	}
}

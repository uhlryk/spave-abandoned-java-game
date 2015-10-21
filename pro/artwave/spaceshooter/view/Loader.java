package pro.artwave.spaceshooter.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.helper.Background;
import pro.artwave.fgm.view.loader.AbstractLoader;
import pro.artwave.spaceshooter.model.asset.RootAssetAtlas;


public class Loader extends AbstractLoader {
	private float _alpha;
	
	RootAssetAtlas _loaderAssetAtlas;
	//private Sprite _title;
	private Sprite _loaderEmpty;
	private Sprite _loaderFull;
	private int _regionWidthLoadBar;
	
	private int _hideCounter=0;
	private int _showCounter=0;
	@Override
	public void init() {
		this._alpha=0;
		
		this.setPosition(0,0);
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		
		_loaderAssetAtlas=new RootAssetAtlas();
//		_background=_minimalAssetAtlas.createSpriteBackground();
	//	_title=_minimalAssetAtlas.createSpriteTitle();
		_loaderEmpty=_loaderAssetAtlas.createSpriteLoaderEmpty();
		_loaderFull=_loaderAssetAtlas.createSpriteLoaderFull();
	//	_title.setPosition(this.getX()+(this.getWidth()-_title.getWidth())/2,this.getY()+(this.getHeight()-_title.getHeight())/2+40);
		_loaderEmpty.setPosition(this.getX()+(this.getWidth()-_loaderEmpty.getWidth())/2,this.getY()+120);
		_loaderFull.setPosition(_loaderEmpty.getX(),_loaderEmpty.getY());
		_regionWidthLoadBar=_loaderFull.getRegionWidth();

		Background background=new Background();
		background.init(_loaderAssetAtlas.createSpriteLoadingBackground());
		background.setSize(this.getWidth(),this.getHeight());
		this.addActor(background);
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		parentAlpha=parentAlpha*this._alpha;
//		_background.draw(batch, parentAlpha);
		super.draw(batch, parentAlpha);
		this._loaderEmpty.draw(batch,parentAlpha);
		this._loaderFull.draw(batch,parentAlpha);
		
	//	this._title.draw(batch,parentAlpha);
	}

	@Override
	public void showLoop(float delta) {
		if(_showCounter<=4){
			_showCounter++;
			this._alpha=(float)_showCounter/5;
		}
	}

	@Override
	public boolean hideLoop(float delta) {
		_hideCounter++;
		this._alpha=1-(float)_hideCounter/3;//3
		if(_hideCounter>=3)return true;
		return false;
	}

	@Override
	public void showProgress(float progress) {
		_loaderFull.setRegionWidth((int)(_regionWidthLoadBar*progress));
		_loaderFull.setSize(_regionWidthLoadBar*progress,_loaderFull.getHeight());

	}
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

}

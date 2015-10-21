package pro.artwave.fgm.model.asset;

import com.badlogic.gdx.assets.AssetManager;

public class LoaderManager {
	private static AssetManager _assetManager;
	public static AssetManager getAssetManager(){
		if(_assetManager==null){
			_assetManager=new AssetManager();
		}
		return _assetManager;
	}	
	public static boolean isLoaderFinish(){
		return getLoadProgress()==1?true:false;
	}
	public static float getLoadProgress(){
		return getAssetManager().getProgress();
	}
	public static boolean updateLoad(){
		return getAssetManager().update();
	}
	public static void clear(){
		getAssetManager().dispose();
		_assetManager=null;
	}
}

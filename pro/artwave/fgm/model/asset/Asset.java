package pro.artwave.fgm.model.asset;


public abstract class Asset implements InterfaceControlAsset{

	public boolean isLoad(){
		return LoaderManager.getAssetManager().isLoaded(this.getResourceName());
	}
	public void load(){
		LoaderManager.getAssetManager().load(this.getResourceName(),this.getClassName());
	}	
	/**
	 * W metodzie nale¿y zwróciæ nazwê zasobu do wczytania
	 * @return
	 */
	public abstract String getResourceName();
	/**
	 * W metodzie nale¿y zwróciæ klasê zasobu
	 * @return
	 */
	public abstract Class<?> getClassName();	
	/**
	 * Metoda wywo³ywana przez controller, czyœci zasoby jesli dany kontroller za nie odpowiada
	 */
	public void clear(){
		LoaderManager.getAssetManager().unload(this.getResourceName());
	}
}

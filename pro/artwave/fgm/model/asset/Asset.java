package pro.artwave.fgm.model.asset;


public abstract class Asset implements InterfaceControlAsset{

	public boolean isLoad(){
		return LoaderManager.getAssetManager().isLoaded(this.getResourceName());
	}
	public void load(){
		LoaderManager.getAssetManager().load(this.getResourceName(),this.getClassName());
	}	
	/**
	 * W metodzie nale�y zwr�ci� nazw� zasobu do wczytania
	 * @return
	 */
	public abstract String getResourceName();
	/**
	 * W metodzie nale�y zwr�ci� klas� zasobu
	 * @return
	 */
	public abstract Class<?> getClassName();	
	/**
	 * Metoda wywo�ywana przez controller, czy�ci zasoby jesli dany kontroller za nie odpowiada
	 */
	public void clear(){
		LoaderManager.getAssetManager().unload(this.getResourceName());
	}
}

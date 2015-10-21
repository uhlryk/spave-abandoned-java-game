package pro.artwave.spaceshooter.model.asset;

import pro.artwave.fgm.model.asset.AssetImage;

public class GameBackgroundAssetImage extends AssetImage {
	private int _mapId;
	public void init(int mapId){
		_mapId=mapId;
	}
	@Override
	public String getResourceName() {
		return "map/b"+_mapId+".jpg";
	}

}

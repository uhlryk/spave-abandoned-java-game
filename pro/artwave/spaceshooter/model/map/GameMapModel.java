package pro.artwave.spaceshooter.model.map;

import com.badlogic.gdx.math.Vector2;

import pro.artwave.fgm.model.map.MapModel;

public class GameMapModel extends MapModel {
	public static final int TILE_SIZE=100;
	/**
	 * Plik bmp z g³êbi¹ 24bity
	 * wszystkie checkpointy musz¹ mieæ format
	 * 00..ffff
	 * 
	 */
	public static final int START=0x00ff00ff;
	private int _missionId;
	public void init(int missionId){
		_missionId=missionId;
		this.init();
	}
	@Override
	public String getResourceName() {
		return "map/m"+_missionId+".bmp";
	}
	public Vector2 getStartPosition(){
		for(int x=0;x<this.getMap().getWidth();x++){
			for(int y=0;y<this.getMap().getHeight();y++){
				if(getValue(x,y)==START){
					return new Vector2(x*TILE_SIZE,y*TILE_SIZE);
				}
			}
		}
		return new Vector2();
	}
}

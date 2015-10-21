package pro.artwave.fgm.model.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

public abstract class MapModel{
	private Pixmap _map;
	public void init(){
		_map=new Pixmap(getResourceFile());
	}
	public FileHandle getResourceFile(){
		return Gdx.files.internal(this.getResourceName());
	}	
	public abstract String getResourceName();

	public int getValue(int x,int y){
		return _map.getPixel(x,_map.getHeight()-y);
	}
	public Pixmap getMap(){
		return _map;
	}
}

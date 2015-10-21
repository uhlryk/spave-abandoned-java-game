package pro.artwave.fgm.view;

import java.util.ArrayList;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.loader.AbstractLoader;

import com.badlogic.gdx.scenes.scene2d.Group;

public class ViewManager extends View {
	private ArrayList<Group> _layerList;
	private int _idContentLayer;
	private int _idLoadLayer;
	public void init() {
		this._layerList=new ArrayList<Group>();
		this._idContentLayer=this.createLayer();
		this._idLoadLayer=this.createLayer();
		this.setTransform(false);
	}

	public Group getLayer(int idLayer){
		return this._layerList.get(idLayer);
	}
	public Group getContentLayer(){
		return this.getLayer(this._idContentLayer);
	}
	public Group getLoadLayer(){
		return this.getLayer(this._idLoadLayer);
	}	
	public int createLayer(){
		Group group=new Group();
		group.setTransform(false);
		this.addActor(group);
		this._layerList.add(group);
		return this._layerList.indexOf(group);
	}
	public void addContent(View child) {
		this.getLayer(this._idContentLayer).addActor(child);		
	}
	public void addLoader(AbstractLoader child) {
		this.getLayer(this._idLoadLayer).addActor(child);		
	}
}

package pro.artwave.spaceshooter.view.tile;

import pro.artwave.spaceshooter.model.map.GameMapModel;
import pro.artwave.spaceshooter.view.tile.TileBoardIsometric.PrepareRender;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Klasa odpowiada za rysowanie pod³ogi
 * Klasa otrzymuje dane z Klasy TileBoardIsometric
 * @see TileBoardIsometric
 * @author Krzysztof
 *
 */
public class BottomTileActor extends Actor {
	private TileBoardIsometric.PrepareRender[] _renderList;
	private int _newSize=0;
	public void init(PrepareRender[] renderList){
		_renderList=renderList;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		for(int i=0;i<_renderList.length;i++){		
			if(_renderList[i].isSet&&_renderList[i].mapTile!=null&&_renderList[i].mapTile.getType()==TileBoardIsometric.TYPE_WALL&&_renderList[i].mapTile.getRegionBottom()!=null){
		//		System.out.println("draw tile C");
				int h=-25;
				batch.draw(_renderList[i].mapTile.getRegionBottom(),
						   _renderList[i].x+this.getX(),
						   _renderList[i].y+this.getY()+h,
						   GameMapModel.TILE_SIZE/2,
						   GameMapModel.TILE_SIZE/2,
						   125,//widthOfThisTile*DeclaredTileSize/widthOfStandardTile  100*100/80
						   125,
						   1,1,0);
			}
		}	
	}
}

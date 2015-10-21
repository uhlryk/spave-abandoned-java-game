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
public class FloorTileActor extends Actor {
	private TileBoardIsometric.PrepareRender[] _renderList;
	public void init(PrepareRender[] renderList){
		_renderList=renderList;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		for(int i=0;i<_renderList.length;i++){		
			if(_renderList[i].isSet&&_renderList[i].mapTile!=null&&_renderList[i].mapTile.getType()==TileBoardIsometric.TYPE_PATH&&_renderList[i].mapTile.getRegionBottom()!=null){
				batch.draw(_renderList[i].mapTile.getRegionBottom(),
						_renderList[i].x+this.getX()+25,
						_renderList[i].y+this.getY()-25,
						GameMapModel.TILE_SIZE/2,
						GameMapModel.TILE_SIZE/2,
						GameMapModel.TILE_SIZE,
						GameMapModel.TILE_SIZE,1,1,0);
			}
		}	
	}
}

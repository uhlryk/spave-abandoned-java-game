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
public class TopTileActor extends Actor {
	private TileBoardIsometric.PrepareRender[] _renderList;
	public void init(PrepareRender[] renderList){
		_renderList=renderList;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		for(int i=0;i<_renderList.length;i++){		
			if(_renderList[i].isSet&&_renderList[i].mapTile!=null&&_renderList[i].mapTile.getType()==TileBoardIsometric.TYPE_WALL&&_renderList[i].mapTile.getRegionTop()!=null){
		//		System.out.println("draw tile C");
				
				int w=-25;//DeclaredTileSize-widthOfThisTile*DeclaredTileSize/widthOfStandardTile 100-125
				 
				batch.draw(_renderList[i].mapTile.getRegionTop(),
						   _renderList[i].x+this.getX()+w,
						   _renderList[i].y+this.getY(),
						   _renderList[i].mapTile.getRegionBottom().getRegionWidth()/2,
						   _renderList[i].mapTile.getRegionBottom().getRegionHeight()/2,
						   125,//widthOfThisTile*DeclaredTileSize/widthOfStandardTile  100*100/80
						   125,
						   1,1,0);
			}
		}	
	}
}

package pro.artwave.spaceshooter.view.root.main.selectequipment;

import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Wyœwietla w formie paska z ticków dan¹ statystykê
 * Wyœwietla j¹ na niebiesko, mo¿na podaæ referencyjny i jeœli jest od niego miejszy to ró¿nica czerwona
 * a jeœli wiêkszy to ró¿nica zielona
 * @author Krzysztof
 *
 */
public class StatBar extends Actor {
	private int _stat;
	private int _refStat;
	private int _maxVal;
	private int _maxTick;
	private Sprite _tick;
	public void init(int maxVal,int maxTick){
		SelectAssetAtlas selectAssetAtlas=new SelectAssetAtlas();
		_maxVal=maxVal;
		_maxTick=maxTick;
		_tick=selectAssetAtlas.createSpriteTick();
		setSize(70,25*_maxTick);
	}
	public void setStat(int stat,int refStat){
		float _f=(float)stat/(float)_maxVal;
		_stat=(int) (_f*_maxTick);
		if(_stat>_maxTick)_stat=_maxTick;
		if(refStat>-1){
			_f=(float)refStat/(float)_maxVal;
			_refStat=(int) (_f*_maxTick);	//jeœli -1 to nie bierzemy pod uwagê
			if(_refStat>_maxTick)_refStat=_maxTick;
		}else{
			_refStat=refStat;
		}
	}
	public void setStat(int stat){
		setStat(stat,-1);	
	}	
	@Override 
	public void draw(SpriteBatch batch,float parentAlpha){
		
		for(int i=0;i<_maxTick;i++){
			_tick.setColor(1,1,1,0.3f);
			_tick.setPosition(this.getX(),this.getY()+7*i);
			_tick.draw(batch,parentAlpha);
		}		
		for(int i=0;i<_stat;i++){
			_tick.setColor(0,0,1,0.7f);
			_tick.setPosition(this.getX(),this.getY()+7*i);
			_tick.draw(batch,parentAlpha);
		}
		if(_refStat>-1){
			if(_refStat>_stat){
				for(int i=_stat;i<_refStat;i++){
					_tick.setColor(1,0,0,0.7f);
					_tick.setPosition(this.getX(),this.getY()+7*i);
					_tick.draw(batch,parentAlpha);
				}
			}else if(_refStat<_stat){
				for(int i=_refStat;i<_stat;i++){
					_tick.setColor(0,1,0,0.7f);
					_tick.setPosition(this.getX(),this.getY()+7*i);
					_tick.draw(batch,parentAlpha);
				}
			}
		}
	}
}

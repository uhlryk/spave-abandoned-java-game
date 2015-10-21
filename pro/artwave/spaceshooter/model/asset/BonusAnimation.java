package pro.artwave.spaceshooter.model.asset;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pro.artwave.fgm.model.asset.AssetImage;

public class BonusAnimation extends AssetImage implements InterfaceEffectAnimation {
	public final static int SIZE=128;
	public final static int NUM_ROW=4;
	public final static int NUM_COL=4;
	public final static int MAX_NUM=64;		
	public final static int SPEED_FACTOR=60;	
	@Override
	public String getResourceName() {
		return "atlas/bonus.png";
	}
	public TextureRegion getFrame(int num){
		if(num>=MAX_NUM)return null;
		int col=num%NUM_COL;
		int row=num/NUM_ROW;
		return new TextureRegion(this.getTexture(),col*SIZE,row*SIZE, SIZE, SIZE);
	}
	public int getSize(){
		return SIZE;
	}
	public int getMaxNum(){
		return MAX_NUM;
	}	
	public int getSpeedFactor(){
		return SPEED_FACTOR;
	}			
}

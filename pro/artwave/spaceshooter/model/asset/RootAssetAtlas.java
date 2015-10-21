package pro.artwave.spaceshooter.model.asset;

import com.badlogic.gdx.graphics.g2d.Sprite;

import pro.artwave.fgm.model.asset.AssetAtlas;

public class RootAssetAtlas extends AssetAtlas {

	@Override
	public String getResourceName() {
		return "atlas/root.pack";
	}
	public Sprite createSpriteLoadingBackground(){
		return this.createSprite("loadingbackground");
	}		
	public Sprite createSpriteLoaderEmpty(){
		return this.createSprite("LoaderEmpty");
	}	
	public Sprite createSpriteLoaderFull(){
		return this.createSprite("LoaderFull");
	}
	public Sprite createSpriteLogo(){
		return this.createSprite("Logo");
	}	
	public Sprite createSpriteButton2(){ 
		return this.createSprite("Button2");
	}	
	public Sprite createSpriteButton2Neutral(){ 
		return this.createSprite("Button2Neutral");
	}	
	public Sprite createSpriteButton2Positive(){ 
		return this.createSprite("Button2Positive");
	}		
	public Sprite createSpriteButton2Negative(){ 
		return this.createSprite("Button2Negative");
	}	
	
	public Sprite createSpriteButton2Off(){ 
		return this.createSprite("Button2Off");
	}	
	public Sprite createSpriteButton2NeutralOff(){ 
		return this.createSprite("Button2NeutralOff");
	}	
	public Sprite createSpriteButton2PositiveOff(){ 
		return this.createSprite("Button2PositiveOff");
	}		
	public Sprite createSpriteButton2NegativeOff(){ 
		return this.createSprite("Button2NegativeOff");
	}		
	
	public Sprite createSpriteButton3(){ 
		return this.createSprite("Button3");
	}	
	public Sprite createSpriteButton3Neutral(){ 
		return this.createSprite("Button3Neutral");
	}		
	public Sprite createSpriteButton3Positive(){ 
		return this.createSprite("Button3Positive");
	}		
	public Sprite createSpriteButton3Negative(){ 
		return this.createSprite("Button3Negative");
	}		
	
	public Sprite createSpriteButton3Off(){ 
		return this.createSprite("Button3Off");
	}	
	public Sprite createSpriteButton3NeutralOff(){ 
		return this.createSprite("Button3NeutralOff");
	}	
	public Sprite createSpriteButton3PositiveOff(){ 
		return this.createSprite("Button3PositiveOff");
	}		
	public Sprite createSpriteButton3NegativeOff(){ 
		return this.createSprite("Button3NegativeOff");
	}
	
	public Sprite createSpriteSliderBar(){ 
		return this.createSprite("SliderBar");
	}	
	public Sprite createSpriteSliderKnob(){ 
		return this.createSprite("SliderKnob");
	}		
	public Sprite createSpriteButtonTabNeutral(){
		return this.createSprite("ButtonTabNeutral");
	}	
	public Sprite createSpriteButtonTabPositive(){
		return this.createSprite("ButtonTabPositive");
	}	
	public Sprite createSpriteButtonTabNegative(){
		return this.createSprite("ButtonTabNegative");
	}		
}

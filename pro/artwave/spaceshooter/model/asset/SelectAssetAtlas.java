package pro.artwave.spaceshooter.model.asset;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import pro.artwave.fgm.model.asset.AssetAtlas;

public class SelectAssetAtlas extends AssetAtlas {

	@Override
	public String getResourceName() {
		return "atlas/select.pack";
	}
	public Sprite createSpriteUpperMenu(){
		return this.createSprite("UpperLongMenu");
	}			
	public Sprite createSpriteButtonPrev(){
		return this.createSprite("ButtonPrev");
	}
	public Sprite createSpriteButtonNext(){
		return this.createSprite("ButtonNext");
	}	
	public Sprite createSpriteButtonPlayer(){
		return this.createSprite("ButtonSelectPlayer");
	}	
	public Sprite createSpriteButtonActivePlayer(){
		return this.createSprite("ButtonSelectActivePlayer");
	}		
	public Sprite createSpriteActivePlayer(){
		return this.createSprite("BtnPlayerActive");
	}		
	
	public Sprite createSpriteMenuBackground(){
		return this.createSprite("menubackground");
	}				
	public Sprite createSpriteButtonMenu(){ 
		return this.createSprite("ButtonMenu");
	}
	public Sprite createSpriteButtonMenuFlamesOn(){
		return this.createSprite("ButtonMenuFlamesOn");
	}	
	public Sprite createSpriteButtonMenuFlamesOff(){
		return this.createSprite("ButtonMenuFlamesOff");
	}	
	public Sprite createSpriteButtonEngineAnim(int index){
		return getAtlas().createSprite("ButtonEngineAnim",index);
	}
	/*public Sprite createSpriteSelectLabel(){
		return this.createSprite("SelectLabel");
	}	*/	
	public Sprite createSpriteDecorationFrame(){
		return this.createSprite("DecorationFrame");
	}	
	public Sprite createSpriteTextBackground(){
		return this.createSprite("TextBackground");
	}		
	public Sprite createSpriteMissionButton(){
		return this.createSprite("MissionButton");
	}
	public Sprite createSpriteActiveMission(){
		return this.createSprite("MissionAcv");
	}
	public Sprite createSpriteStopMission(){
		return this.createSprite("MissionStop");
	}	
/*	public Sprite createSpriteButtonTab(){
		return this.createSprite("ButtonTab");
	}	
	public Sprite createSpriteButtonTabOn(){
		return this.createSprite("ButtonTabOn");
	}	*/
/*	public Sprite createSpriteTabIconShip(){
		return this.createSprite("TabShipIcon");
	}	
	public Sprite createSpriteTabIconWeaponI(){
		return this.createSprite("TabIWeaponIcon");
	}*/	
	public Sprite createSpriteTabIconWeaponII(){
		return this.createSprite("TabIIWeaponIcon");
	}		

	public Sprite createSpriteImage(int campaignId){
		return this.createSprite("c"+campaignId);
	}
	public Sprite createSpriteShip(int shipId){
		return this.createSprite("s"+shipId);
	}	
	public Sprite createSpriteBullet(int bulletId){
		return this.createSprite("f"+bulletId);
	}
	public Sprite createSpriteWeapon(int weaponId){
		return this.createSprite("wB"+weaponId);
	}	
	public Sprite createSpriteGraphic(int id){
		return this.createSprite("Graph"+id);
	}	
	public Sprite createSpriteTick(int color){
		return createSpriteTick(new Color(color));
	}	
	public Sprite createSpriteTick(Color color){
		Sprite s=createSpriteTick();
		s.setColor(color);
		return s;
	}
	public Sprite createSpriteTick(){
		return this.createSprite("IcoTick");
	}	
	public Sprite createSpriteIconBg(){
		return this.createSprite("BgSmall2");
	}		
}

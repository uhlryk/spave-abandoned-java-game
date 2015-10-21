package pro.artwave.spaceshooter.model.asset;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import pro.artwave.fgm.model.asset.AssetAtlas;

public class GameAssetAtlas extends AssetAtlas {

	@Override
	public String getResourceName() {
		return "atlas/game.pack";
	}
/*	public Sprite createSpriteButtonPopupLeft(){
		return this.createSprite("ButtonPopupLeft");
	}	
	public Sprite createSpriteButtonPopupRight(){
		return this.createSprite("ButtonPopupRight");
	}	*/
	public Sprite createSpriteUpperMenu(){
		return this.createSprite("UpperShortMenu");
	}	
/*	public Sprite createSpriteUpperScore(){
		return this.createSprite("UpperScore");
	}*/
	public Sprite createSpritePopupBgBig(){
		return this.createSprite("PopupBackground");
	}
/*	public Sprite createSpritePopupBgSmall(){
		return this.createSprite("PopupBackgroundSmall");
	}	*/
	/*public Sprite createSpritePopupBgEmpty(){
		return this.createSprite("PopupBackgroundEmpty");
	}	*/	
/*	public Sprite createSpriteFire(){
		return this.createSprite("ButtonFire");
	}		
	public Sprite createSpriteFireOn(){
		return this.createSprite("ButtonFireOn");
	}	*/
	public Sprite createSpriteHudLeft(){
		return this.createSprite("ShipConditionHudLeft");
	}
	public Sprite createSpriteBarEmpty(){
		return this.createSprite("EmptyBar");
	}		
	public Sprite createSpriteShield(){
		return this.createSprite("shield");
	}		
	public Sprite createSpriteShadow(){
		return this.createSprite("shadow");
	}		
	public Sprite createSpriteBarShield(){
		return this.createSprite("ShieldBar");
	}		
	public Sprite createSpriteBarHealth(){
		return this.createSprite("HealthBar");
	}	
	public Sprite createSpritePathTarget(){
		return this.createSprite("PathTarget");
	}		
	public Sprite createSpriteEnemyTarget(){
		return this.createSprite("EnemyTarget");
	}		
	public Sprite createSpriteCheckpointArrow(){
		return this.createSprite("CheckpointArrow");
	}	
/*	public Sprite createSpriteWeaponI(){
		return this.createSprite("WeaponIconI");
	}	
	public Sprite createSpriteWeaponII(){
		return this.createSprite("WeaponIconII");
	}	*/
	public Sprite createSpriteWeapon(int weaponId){
		return this.createSprite("wS"+weaponId);
	}		
	public Sprite createSpriteMap(int typeId){
		return this.createSprite("Map"+typeId);
	}	
	public Sprite createSpriteShip(int shipId){
		return this.createSprite("s"+shipId);
	}	
	public Sprite createSpriteShipMask(int shipId){
		return this.createSprite("sm"+shipId);
	}		
	public Sprite createSpriteTurretTop(int turretId){
		return this.createSprite("tt"+turretId);
	}	
	public Sprite createSpriteTurretTopMask(int turretId){
		return this.createSprite("ttm"+turretId);
	}		
	public Sprite createSpriteTurretBottom(int turretId){
		return this.createSprite("tb"+turretId);
	}		
	public Sprite createSpriteConstruction(int constructionId){
		return this.createSprite("c"+constructionId);
	}	
	public Sprite createSpriteConstructionMask(int constructionId){
		return this.createSprite("cm"+constructionId);
	}		
	public Sprite createSpriteBackfire(int backfireId){
		return this.createSprite("b"+backfireId);
	}		
	public Sprite createSpriteBullet(int bulletId){
		return this.createSprite("f"+bulletId);
	}
	public static final int RESOURCE_HEALTH =1;
	public static final int RESOURCE_SHIELD =2;
	
	public Sprite createSpriteResource(int resourceId){
		return this.createSprite("r"+resourceId);
	}	
	public Sprite createSpriteDecoration(int decorationId){
		return this.createSprite("d"+decorationId);
	}
	public Sprite createSpriteImage(int imageId){
		return this.createSprite("image"+imageId);
	}	
	public AtlasRegion getTile(String id){
		return getAtlas().findRegion("w"+id);
	}	
	public static final int TILE_TYPE_FLAT=1;
	public static final int TILE_TYPE_BOTTOM=2;
	public static final int TILE_TYPE_TOP=3;
	public AtlasRegion getTile(int id,int type){
		switch(type){
			case TILE_TYPE_FLAT:
				return getAtlas().findRegion("iso"+id+"p");
			case TILE_TYPE_BOTTOM:
				return getAtlas().findRegion("iso"+id+"b");
			case TILE_TYPE_TOP:
				return getAtlas().findRegion("iso"+id+"t");			
		}
		return null;
	}		
}

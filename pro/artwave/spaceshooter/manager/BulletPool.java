package pro.artwave.spaceshooter.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import pro.artwave.spaceshooter.helper.BulletParams;
import pro.artwave.spaceshooter.helper.WeaponParams;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;
import pro.artwave.spaceshooter.view.tile.TileBoardIsometric;

public class BulletPool{
	private Bullet[] _bulletList;
	/**
	 * Stopniowo zape³nia siê grafikami pocisków
	 */
	private Map<Integer,Sprite> _imageMap;
	private GameAssetAtlas _gameObjectAssetAtlas;
	//private float _maxLife=1.6f;
	private ViewActor _view;
	public void init(int limit){
		_view=new ViewActor();
		_view.init();
		_gameObjectAssetAtlas=new GameAssetAtlas();
		_bulletList=new Bullet[limit];
		for(int i=0;i<limit;i++){
			Bullet bullet=new Bullet();
			_bulletList[i]=bullet;
			bullet.velocity=new Vector2();
			bullet.isActive=false;
		}
		_imageMap=new HashMap<Integer,Sprite>();
	}
	public void fireWeapon(int warGroupId,WeaponParams weaponParams,Vector2 shipPosition,float shipAngle){
		fireWeapon(warGroupId,weaponParams,shipPosition,shipAngle,1);
	}
	/**
	 * Broñ mo¿e mieæ kilka bulletów wiêc metoda ta wygneruje kilka
	 */
	public void fireWeapon(int warGroupId,WeaponParams weaponParams,Vector2 shipPosition,float shipAngle,float nerfFactor){
		Map<Integer, BulletParams> bulletParams=weaponParams.bullets;
		for(Entry<Integer, BulletParams> entry:bulletParams.entrySet()){
			for(int i=0;i<_bulletList.length;i++){
				Bullet bullet=_bulletList[i];
				if(bullet.isActive==false){
					bullet.warGroupId=warGroupId;
					bullet.isActive=true;
					int imageType=entry.getValue().type;
					if(!_imageMap.containsKey(imageType)){
						Sprite image=_gameObjectAssetAtlas.createSpriteBullet(imageType);
						_imageMap.put(imageType,image);
						image.setOrigin(image.getWidth()/2,image.getHeight()/2);
						
					}
					bullet.angle=-shipAngle-entry.getValue().angle;
					bullet.lifetime=entry.getValue().lifetime;
					bullet.imageType=imageType;
					bullet.position=new Vector2();
				//	bullet.position.x=shipPosition.x+30;
				//	bullet.position.y=shipPosition.y+30;
					bullet.position.x=shipPosition.x-(float) (Math.sin(Math.toRadians(+shipAngle+entry.getValue().rad))*entry.getValue().rel);
					bullet.position.y=shipPosition.y+(float) (Math.cos(Math.toRadians(+shipAngle+entry.getValue().rad))*entry.getValue().rel);					
					bullet.actLive=0;
					bullet.penetration=entry.getValue().penetration;
					bullet.velocity.x=(float)Math.sin(Math.toRadians(bullet.angle))*entry.getValue().speed;
					bullet.velocity.y=(float)Math.cos(Math.toRadians(bullet.angle))*entry.getValue().speed;
					bullet.damage=(int) (entry.getValue().damage*nerfFactor);
					bullet.shieldDamage=(int) (entry.getValue().shieldDamage*nerfFactor);
					bullet.armorDamage=(int) (entry.getValue().armorDamage*nerfFactor);
			//		System.out.println("create bullet index:"+i+" type:"+imageType+_imageMap.get(imageType));
					break;
				}
			}
		}
	}
	public void calculateTileCollision(TileBoardIsometric tileBoard){
		for(int i=0;i<_bulletList.length;i++){
			Bullet bullet=_bulletList[i];
			if(bullet.isActive==true){
				if(tileBoard.checkPosition(bullet.position.x,bullet.position.y,20)==false){
					explosion(i);
				}
			}
		}
	}
	public BulletDamage calculateCollision(int warGroup,Vector2 position,int radius){
		for(int i=0;i<_bulletList.length;i++){
			Bullet bullet=_bulletList[i];		
			if(bullet.isActive==true&&bullet.warGroupId!=warGroup){
				Sprite img=_imageMap.get(bullet.imageType);
				int offsetX=0;//(int) img.getWidth()/5;
				int offsetY=0;//(int) img.getHeight()/5;
				if(bullet.position.x+offsetX>position.x-radius&&bullet.position.x-offsetX<position.x+radius&&	
					bullet.position.y+offsetY>position.y-radius&&bullet.position.y-offsetY<position.y+radius){
					if(bullet.penetration==false){
						explosion(i);
					}
					BulletDamage bulletDamage=new BulletDamage();
					bulletDamage.damage=bullet.damage;
					bulletDamage.shieldDamage=bullet.shieldDamage;
					bulletDamage.armorDamage=bullet.armorDamage;
					return bulletDamage;
				}
			}
		}
		return null;
	}
	public class BulletDamage{
		public int damage;
		public int shieldDamage;
		public int armorDamage;
	}
	public void calculate(float delta){
		for(int i=0;i<_bulletList.length;i++){
			Bullet bullet=_bulletList[i];
			if(bullet.isActive==true){
				float lifetime=(float)(bullet.lifetime)/1000;
				
				if(bullet.actLive>lifetime){
		//			System.out.println(bullet.actLive+" "+lifetime);
					explosion(i);					
				}
				bullet.actLive+=delta;
				bullet.position.add(bullet.velocity.cpy().scl(delta));
			}
		}
	}
	public void explosion(int index){
		_bulletList[index].isActive=false;
	}
	public Actor getView(){
		return _view;
	}
	private class ViewActor extends Actor{
		public void init(){
			
		}
		@Override
		public void draw(SpriteBatch batch,float parentAlpha){
			for(int i=0;i<_bulletList.length;i++){
				Bullet bullet=_bulletList[i];
				if(bullet.isActive==true){	
					Sprite image=_imageMap.get(bullet.imageType);
					
					image.setPosition(this.getX()+bullet.position.x-image.getOriginX(),this.getY()+bullet.position.y-image.getOriginY());
					image.setRotation(-bullet.angle);
					image.draw(batch,parentAlpha);
				}
			}
		}		
	}
	public class Bullet{
		public boolean isActive=false;
		public int imageType;
		public int warGroupId;
		public float actLive;
		public Vector2 position;
		public Vector2 velocity;
		public float angle;
		public int damage;
		public int lifetime;
		
		public int shieldDamage;
		public int armorDamage;
		public boolean penetration;
	}
}

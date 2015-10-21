package pro.artwave.spaceshooter.view.manipulator;


import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
/**
 * Posiada zgrupowane wszystkie przyciski strzelania i nimi zarz¹dza.
 * Czyli gdy wdusimy pierwszy to pilnuje by pozosta³e by³y wy³¹czone.
 * Przekazuje jest wynik wy¿ej
 * @author Krzysztof
 *
 */
public class BlockButtonFire extends Group implements InterfaceFireController {
	private ButtonFire _button1;
	private ButtonFire _button2;
//	private ButtonFire _button3;
	private int _fireButton;
	
	private GameAssetAtlas _gameAssetAtlas;
	
	public void init(int weapon1Id,int weapon2Id){
		this.setTransform(false);		
		_gameAssetAtlas=new GameAssetAtlas();
	//	this.setSize(120,460);
		_button1=new ButtonFire();
		_button1.init(_gameAssetAtlas.createSpriteWeapon(weapon1Id));
		_button1.setPosition(5,310);
		addActor(_button1);
		_button2=new ButtonFire();
		_button2.init(_gameAssetAtlas.createSpriteWeapon(weapon2Id));
		_button2.setPosition(5,140);
		addActor(_button2);
		
	/*	_button3=new ButtonFire();
		_button3.init(ButtonFire.WEAPON_SLOT_II);
		_button3.setVisible(false);
		_button3.setPosition(5,30);
		addActor(_button3);	*/
		_button1.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				setFire(1);
				return true;
			}	
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//setOffFire();
				_isFireEnemy=true;
			}			
		});		
		_button2.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				setFire(2);
				return true;
			}	
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			//	setOffFire();
				_isFireEnemy=true;
			}
		});		
		_button1.setFire(true);
	}
	private void setOffButtonFire(){
		_button1.setFire(false);
		_button2.setFire(false);

	}
//	public void setOffFire(){
//		_fireButton=0;
//	}
	public void setFire(int slotId){
		setOffButtonFire();
		_fireButton=slotId;
		switch(slotId){
		case 1:
			_button1.setFire(true);
			break;
		case 2:
			_button2.setFire(true);
			break;		
		}
	}
	public void setReady(int slotId,float ready){
		switch(slotId){
		case 1:
			_button1.setReadyFire(ready);
			break;
		case 2:
			_button2.setReadyFire(ready);;
			break;	
		}
	}
	public int getFire(){
		return _fireButton;
	}
	/**
	 * Jeœli true to znaczy ¿e strzelaæ mamy w wroga
	 * pozosta³e przyciski prze³¹czaj¹ broñ i ustawiaj¹ gotowoœæ strza³u
	 * poni¿sze kontroluje ¿e ma nast¹piæ strza³
	 */
	private boolean _isFireEnemy;
	public boolean fireEnemy(){
		return _isFireEnemy;
	}
	public void resetFireEnemy(){
		_isFireEnemy=false;
	}	
}

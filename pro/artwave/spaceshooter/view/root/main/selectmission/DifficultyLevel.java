package pro.artwave.spaceshooter.view.root.main.selectmission;

import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.resource.GlobalTranslation;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Metoda pozwala okreœliæ poziom trudnoœci gry
 * Jest to czêœæ widoku SelectMissionView
 * @author Krzysztof
 *
 */
public class DifficultyLevel extends Group {
	public static final int DIFFICULTY_NORMAL=1;
	public static final int DIFFICULTY_HARD=2;
	public static final int DIFFICULTY_VERYHARD=3;
	private int _actDifficulty;
	private Button _normalButton;
	private Button _hardButton;
	private Button _vHardButton;
	private ClickListener _listener;
	public void init(int difficultyLevel){
		GlobalTranslation _translation=new GlobalTranslation();
		_translation.init();
		this.setSize(210, 200);
		SmallOutlineAssetBitmapFont fontAsset=new SmallOutlineAssetBitmapFont();
		
		SelectAssetAtlas selectAssetAtlas=new SelectAssetAtlas();
		_normalButton=new Button();
		_normalButton.init(5);
		_normalButton.setSize(210,60);
		_normalButton.setFontBitmap(fontAsset.getFont());
		_normalButton.setText(_translation._("Level_Normal"));
	//	_normalButton.setBackground(selectAssetAtlas.createSpriteButtonSmallDark());
	//	_normalButton.setBackgroundOn(selectAssetAtlas.createSpriteButtonSmallBlue());
		_normalButton.addClickListener(new Button.ClickListener() {			
			@Override
			public void onClick() {
				setDifficulty(DIFFICULTY_NORMAL);
			}
		});
		_normalButton.setPosition(0,140);
		this.addActor(_normalButton);
		_hardButton=new Button();
		_hardButton.init(5);
		_hardButton.setSize(210,60);
		_hardButton.setFontBitmap(fontAsset.getFont());
		_hardButton.setText(_translation._("Level_Hard"));
	//	_hardButton.setBackground(selectAssetAtlas.createSpriteButtonSmallDark());
	//	_hardButton.setBackgroundOn(selectAssetAtlas.createSpriteButtonSmallBlue());
		_hardButton.addClickListener(new Button.ClickListener() {			
			@Override
			public void onClick() {
				setDifficulty(DIFFICULTY_HARD);
			}
		});		
		_hardButton.setPosition(0,70);
		this.addActor(_hardButton);
		_vHardButton=new Button();
		_vHardButton.init(5);
		_vHardButton.setSize(210,60);
		_vHardButton.setFontBitmap(fontAsset.getFont());
		_vHardButton.setText(_translation._("Level_VHard"));
//		_vHardButton.setBackground(selectAssetAtlas.createSpriteButtonSmallDark());
//		_vHardButton.setBackgroundOn(selectAssetAtlas.createSpriteButtonSmallBlue());
		_vHardButton.addClickListener(new Button.ClickListener() {			
			@Override
			public void onClick() {
				setDifficulty(DIFFICULTY_VERYHARD);
			}
		});		
		_vHardButton.setPosition(0,0);
		this.addActor(_vHardButton);
		
		setDifficulty(difficultyLevel);
	}
	public void setDifficulty(int difficultyLevel){
		_actDifficulty=difficultyLevel;
		switch(_actDifficulty){
		case DIFFICULTY_NORMAL:
			_normalButton.setPushedDown();
			_hardButton.setPushedUp();
			_vHardButton.setPushedUp();
			break;
		case DIFFICULTY_HARD:
			_normalButton.setPushedUp();
			_hardButton.setPushedDown();
			_vHardButton.setPushedUp();
			break;
		case DIFFICULTY_VERYHARD:
			_normalButton.setPushedUp();
			_hardButton.setPushedUp();
			_vHardButton.setPushedDown();
			break;
		}		
		if(_listener!=null){
			_listener.onClick(difficultyLevel);
		}
	}
	public void addClickListener(ClickListener listener){
		_listener=listener;
	}
	public static abstract class ClickListener{
		public abstract void onClick(int difficultyLevel);
	}	
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		super.draw(batch, parentAlpha);
	}
}

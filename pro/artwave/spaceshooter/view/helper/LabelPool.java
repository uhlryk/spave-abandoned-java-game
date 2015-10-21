package pro.artwave.spaceshooter.view.helper;

import java.util.ArrayList;

import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Klasa tworzy obiekt pool administruj¹cy etykiety wyœwietlane w ró¿nych miescach ekranu
 * np przy pobranym resource czy zniszczonym statku z odpowiedni¹ informacjê
 * @author Krzysztof
 *
 */
public class LabelPool extends Actor {
	private ArrayList<Label> _labelList;
	private Color _redColor;
	private Color _blueColor;
	private Color _greenColor;
	private Color _whiteColor;
	private BitmapFont _font;
	public void init(){	
		int listSize=20;
		_redColor=new Color(1,0,0,1);
		_blueColor=new Color(0,0,1,1);
		_greenColor=new Color(0,1,0,1);
		_whiteColor=new Color(1,1,1,1);
		_labelList=new ArrayList<Label>(listSize);
		MediumOutlineAssetBitmapFont fontAsset=new MediumOutlineAssetBitmapFont();
		_font=fontAsset.getFont();
		_font.setScale(0.7f);
		for(int i=0;i<listSize;i++){
			Label label=new Label();
			_labelList.add(label);
		}
	}
	public void newRed(String text,int x, int y){
		newLabel(_redColor,text,x,y);
	}
	public void newBlue(String text,int x, int y){
		newLabel(_blueColor,text,x,y);
	}
	public void newGreen(String text,int x, int y){
		newLabel(_greenColor,text,x,y);
	}
	public void newWhite(String text,int x, int y){
		newLabel(_whiteColor,text,x,y);
	}	
	public void newLabel(Color color,String text,int x, int y){
		for(Label label:_labelList){
			if(label.isActive==false){
				label.set(color, text, x, y);
				break;
			}
		}
	}
	@Override
	public void act(float delta){
		for(Label label:_labelList){
			if(label.isActive){
				label.act(delta);
			}
		}
	}
	@Override
	public void draw(SpriteBatch batch,float parentAlpha){
		for(Label label:_labelList){
			if(label.isActive){
				label.draw(batch, parentAlpha);
			}
		}
	}
	public class Label{
		public boolean isActive;
		public float _alpha;
		public Color _color;
		public float _x;
		public float _y;
		public String _text;
		public void set(Color color,String text,int x,int y){
			isActive=true;
			_text=text;
			_color=color;
			TextBounds bounds=_font.getBounds(text);
			_x=x-bounds.width/2;
			_y=y;
			_alpha=2;
		}
		public void act(float delta){
			_y+=60f*delta;
			_alpha-=1*delta;
			if(_alpha<=0){
				isActive=false;
			}
		}
		public void draw(SpriteBatch batch,float parentAlpha){
			_font.setColor(_color.r,_color.g,_color.b,_color.a*parentAlpha*(_alpha<1?_alpha:1));
			_font.draw(batch,_text,_x,_y);
		}
	}
}

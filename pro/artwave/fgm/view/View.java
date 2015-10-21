package pro.artwave.fgm.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public abstract class View extends Group{
	public View(){
		this.setTransform(false);
		this.setTouchable(Touchable.childrenOnly);//dzi�ki czemu sama warstwa widoku nie przys�ania poprzednich. Je�li mamy kilka widok�w z przyciskami to bez tego tylko najwy�szy b�dzie aktywny
	}
	/**
	 * W metodzie nale�y wszystko przygotowa� do narysowania
	 * Metoda mo�e mie� r�ne parametry wi�c nie jest okre�lona
	 */
	//public abstract void init();
	/**
	 * W metodzie nale�y obs�u�y� dodanie childContent do tego widoku. Czyli ustawi� jego pozycj�, doda� go jako aktora w odpowiednie
	 * miejsce. Pozycja mo�e by� wcze�niej przygotowana.
	 * W tym miejscu Controller wie jaki child jest aktywny, czyli kto wywo�a� metod� 
	 * a wi�c mo�na dzia�anie uzale�ni� od childa
	 * @param child
	 */
	public void addContent(View child){
		this.addActor(child);
	}
}

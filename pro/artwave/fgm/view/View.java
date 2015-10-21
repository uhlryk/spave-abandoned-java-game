package pro.artwave.fgm.view;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public abstract class View extends Group{
	public View(){
		this.setTransform(false);
		this.setTouchable(Touchable.childrenOnly);//dziêki czemu sama warstwa widoku nie przys³ania poprzednich. Jeœli mamy kilka widoków z przyciskami to bez tego tylko najwy¿szy bêdzie aktywny
	}
	/**
	 * W metodzie nale¿y wszystko przygotowaæ do narysowania
	 * Metoda mo¿e mieæ ró¿ne parametry wiêc nie jest okreœlona
	 */
	//public abstract void init();
	/**
	 * W metodzie nale¿y obs³u¿yæ dodanie childContent do tego widoku. Czyli ustawiæ jego pozycjê, dodaæ go jako aktora w odpowiednie
	 * miejsce. Pozycja mo¿e byæ wczeœniej przygotowana.
	 * W tym miejscu Controller wie jaki child jest aktywny, czyli kto wywo³a³ metodê 
	 * a wiêc mo¿na dzia³anie uzale¿niæ od childa
	 * @param child
	 */
	public void addContent(View child){
		this.addActor(child);
	}
}

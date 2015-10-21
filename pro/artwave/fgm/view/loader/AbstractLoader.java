package pro.artwave.fgm.view.loader;

import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class AbstractLoader extends Group {
	/**
	 * W metodzie nale�y wywo�a� wszystko do dzia�ania loadera
	 */
	public abstract void init();
	
	/**
	 * Metoda pozwala wy�wietli� loader w spos�b animowany. Dzia�anie niezale�ne od loadera
	 * @return
	 */
	public abstract void showLoop(float delta);
	/**
	 * Metoda pozwala w spos�b animowany usun�� loader. Metoda odpala si� po zaloadowaniu ca�o�ci. Czyli teoretycznie
	 * generuje op�nienie. Proces tak d�ugo b�dzie czeka� a� metoda zwr�ci true;
	 * @return
	 */
	public abstract boolean hideLoop(float delta);
	/**
	 * Metoda powinna pokaza� zmiany w loaderze w zale�no�ci od rozwoju.
	 */
	public abstract void showProgress(float progress);
	/**
	 * Metoda wywo�ywana przez dispatcher jeden raz w momencie zako�czenia �adowania
	 */
	public abstract void onFinish();
}

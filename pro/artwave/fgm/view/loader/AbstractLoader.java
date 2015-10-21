package pro.artwave.fgm.view.loader;

import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class AbstractLoader extends Group {
	/**
	 * W metodzie nale¿y wywo³aæ wszystko do dzia³ania loadera
	 */
	public abstract void init();
	
	/**
	 * Metoda pozwala wyœwietliæ loader w sposób animowany. Dzia³anie niezale¿ne od loadera
	 * @return
	 */
	public abstract void showLoop(float delta);
	/**
	 * Metoda pozwala w sposób animowany usun¹æ loader. Metoda odpala siê po zaloadowaniu ca³oœci. Czyli teoretycznie
	 * generuje opóŸnienie. Proces tak d³ugo bêdzie czeka³ a¿ metoda zwróci true;
	 * @return
	 */
	public abstract boolean hideLoop(float delta);
	/**
	 * Metoda powinna pokazaæ zmiany w loaderze w zale¿noœci od rozwoju.
	 */
	public abstract void showProgress(float progress);
	/**
	 * Metoda wywo³ywana przez dispatcher jeden raz w momencie zakoñczenia ³adowania
	 */
	public abstract void onFinish();
}

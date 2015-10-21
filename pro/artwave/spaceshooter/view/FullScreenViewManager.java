package pro.artwave.spaceshooter.view;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.ViewManager;
/**
 * KLasa nadpisuje standardowy root view. Chcemy by ekran zaczyna³ siê w prawym rogu, czyli
 * ma byæ cofniêty o margines.
 * Czemu kamery nie ustawi³em na ten róg? Ponieewa¿ przy dalszym skalowaniu by caly czasu by³ z boku
 * a teraz bêdzie œrodkowa³ obszar na pocz¹tku ustanowiony. Ale skaluje tak by stage by³ zawsze pe³ny
 * @author Krzysztof
 *
 */
public class FullScreenViewManager extends ViewManager {
	@Override
	public void init() {
		this.setPosition(this.getX()-Setting.getGutter().width,this.getY()-Setting.getGutter().height);
		super.init();
	}
}

package pro.artwave.spaceshooter.view;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.fgm.view.ViewManager;
/**
 * KLasa nadpisuje standardowy root view. Chcemy by ekran zaczyna� si� w prawym rogu, czyli
 * ma by� cofni�ty o margines.
 * Czemu kamery nie ustawi�em na ten r�g? Ponieewa� przy dalszym skalowaniu by caly czasu by� z boku
 * a teraz b�dzie �rodkowa� obszar na pocz�tku ustanowiony. Ale skaluje tak by stage by� zawsze pe�ny
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

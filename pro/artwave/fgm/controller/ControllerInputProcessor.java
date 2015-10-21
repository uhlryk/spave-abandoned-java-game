package pro.artwave.fgm.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
/**
 * W init przyjmuje obiekt interfejsu InterfaceInputController
 * I w zale�no�ci od akcji wywo�uje na nim odpowiedni� metod�.
 * Zadaniem obiektu jest przez wzorzec �a�cuch zobowi�za� przekaza� info
 * wszystkim zainteresowanym dzieciom. Je�li dane dziecko ma obs�u�y� kontroler
 * to nie przekazuje ni�ej.
 * Klasa powinna by� dodana do Gdx.input.setInputProcessor
 * Ale poniewa� chcemy by stage r�wnie� mia� dzia�aj�ce wszystko, to
 * zastosowa� mo�emy multiplexer. Pierwsze�stwo powiny mie� controllers.
 * @author Krzysztof
 *
 */
public class ControllerInputProcessor implements InputProcessor {
	private InterfaceInputController _listener;
	public void init(InterfaceInputController listener){
		setListener(listener);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
	}
	public void setListener(InterfaceInputController listener){
		_listener=listener;
	}
	@Override
	public boolean keyDown(int keycode) {
		return _listener.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		return _listener.keyUp(keycode);
	}

	@Override
	public boolean keyTyped(char character) {
		return _listener.keyTyped(character);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}

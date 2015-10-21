package pro.artwave.fgm.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
/**
 * W init przyjmuje obiekt interfejsu InterfaceInputController
 * I w zale¿noœci od akcji wywo³uje na nim odpowiedni¹ metodê.
 * Zadaniem obiektu jest przez wzorzec ³añcuch zobowi¹zañ przekazaæ info
 * wszystkim zainteresowanym dzieciom. Jeœli dane dziecko ma obs³u¿yæ kontroler
 * to nie przekazuje ni¿ej.
 * Klasa powinna byæ dodana do Gdx.input.setInputProcessor
 * Ale poniewa¿ chcemy by stage równie¿ mia³ dzia³aj¹ce wszystko, to
 * zastosowañ mo¿emy multiplexer. Pierwszeñstwo powiny mieæ controllers.
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

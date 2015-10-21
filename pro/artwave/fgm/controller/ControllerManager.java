package pro.artwave.fgm.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;

import pro.artwave.fgm.controller.dispatcher.Dispatcher;
import pro.artwave.fgm.controller.dispatcher.InterfaceDispatcher;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.ViewManager;

public class ControllerManager implements InterfaceDispatchDirector,InterfaceParentController,InterfaceInputController{
	private final Controller _controller;
	private InterfaceDispatcher _dispatcher;
	private ViewManager _view;
	private Stage _stage;
	public ControllerManager(Stage stage,Controller controller){
		this(stage,controller,new Dispatcher(),new ViewManager());
	}
	public ControllerManager(Stage stage,Controller controller,InterfaceDispatcher dispatcher){
		this(stage,controller,dispatcher,new ViewManager());
	}
	public ControllerManager(Stage stage,Controller controller,ViewManager view){
		this(stage,controller,new Dispatcher(),view);
	}	
	public ControllerManager(Stage stage,Controller controller,InterfaceDispatcher dispatcher,ViewManager view){
		this._view=view;
		this._stage=stage;
		this._dispatcher=dispatcher;
		this._controller=controller;
		this._controller.init(this,this);
	//	this._controller.outputStringStructure("",true);
		this.prepare();
		this._dispatcher.setController(this);
		this._dispatcher.setDefaultDispatch();
	}
	public InterfaceDispatcher getDispatcher(){
		return this._dispatcher;
	}
	/**
	 * Metoda wywo³ywana w pêtli
	 */ 
	public void onRender(float delta){
		if(this._controller!=null){
			this._controller.onRender(delta);
		}
		this._dispatcher.render(delta);
	}
	@Override
	public Controller getChildController(int idController) {
		return this._controller;
	}
	public Intent getIntent(){
		return null;
	}
	@Override
	public int getDefaultChildIdController() {
		return 1;
	}
	@Override
	public View getView() {
		return this._view;
	}
	public ViewManager getViewManager(){
		return this._view;
	}
	@Override
	public InterfaceParentController getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Metoda dodaje do stage pierwszy g³ówny widok, który ma warstwy i pe³ni rolê przewodni¹
	 */
	public void prepare(){
		this._stage.addActor(this._view);
		this._view.init();
		this._stage.setKeyboardFocus(this._view.getContentLayer());
	}
	@Override
	public void setDispatch(Intent intent) {
		// TODO Auto-generated method stub
		
	}
	public void systemPause(){
		getChildController(1).systemPause();
	}
	public void systemResume(){
		getChildController(1).systemResume();
	}
	@Override
	public boolean keyDown(int keycode) {
		return getChildController(1).keyDown(keycode);
	}
	@Override
	public boolean keyUp(int keycode) {
		return getChildController(1).keyUp(keycode);
	}
	@Override
	public boolean keyTyped(char character) {
		return getChildController(1).keyTyped(character);
	}
}

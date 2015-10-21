package pro.artwave.fgm.controller.dispatcher;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.InterfaceDispatchDirector;

public abstract class TemplateDispatcher implements InterfaceDispatcher {
	public static class Step{
		public static final int NEVER_BEFORE=0;
		public static final int INIT=1;
		public static final int ON_BEFORE_CLEAR_LOOP=2;
		public static final int ON_CLEAR=3;
		public static final int SET_ACTUAL=4;
		public static final int ON_LOAD=5;
		public static final int LOAD_LOOP=6;
		public static final int ON_PREPARE=7;		
		public static final int ON_DISPATCH_CHILD=8;	
		public static final int ON_BEFORE_PLAY_LOOP=9;
		public static final int ON_PLAY_LOOP=10;
	}	
	private InterfaceDispatchDirector _controller;
	/**
	 * Okreœla który krok dispatcha jest w danej chwili
	 */
	private int _step;
	private boolean _isDispatch;
	private int _actIdController;
	private int _newIdController;	
	@Override
	public void setController(InterfaceDispatchDirector controller) {
		this._controller=controller;
	}
	
	@Override
	public InterfaceDispatchDirector getController() {
		return this._controller;
	}
	@Override
	public void setActualChildIdController(int idController){
		this._actIdController=idController;
	}
	@Override
	public int getActualChildIdController(){
		return this._actIdController;
	}
	/**
	 * Metoda ustawia id potomnego kontrollera który oczekuje na zmianê
	 * @param idController
	 */
	public void setNewChildIdController(int idController){
		this._newIdController=idController;
	}
	/**
	 * Metoda zwraca id potomnego controllera który oczekuje na dispatch
	 * @return
	 */
	public int getNewChildIdController(){
		return this._newIdController;
	}	
	/**
	 * Prawdza czy controller ma ustawionego potomnego Controllera
	 * @return
	 */
	public boolean isActualChildController(){
		if(this.getActualChildIdController()>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Metoda zwraca potomnego controllera
	 * @return
	 */
	public Controller getActualChildController(){
		if(this.isActualChildController()==true){
			return this.getController().getChildController(this._actIdController);
		}else{
			return null;
		}
	}
	/**
	 * Ustawia flagê która na true oznacza ¿e jest oczekuj¹ce rz¹danie wywo³ania dispatcha
	 * @param isDispatch
	 */
	public void setIsDispatchFlag(boolean isDispatch){
		this._isDispatch=isDispatch;
	}
	/**
	 * Pobiera flagê sprawdzaj¹c¹ czy mamy oczekuj¹ce rz¹danie dispatcha
	 * @return
	 */
	public boolean getIsDispatchFlag(){
		return this._isDispatch;
	}
	@Override
	public void render(float delta) {
		switch(this._step){
			case Step.NEVER_BEFORE:
				if(this._isDispatch==true){
					this.dispatchInit();
				}
				break;
			case Step.ON_BEFORE_CLEAR_LOOP:
				this.dispatchOnBeforeClearLoop(delta);
				break;
			case Step.LOAD_LOOP:
				this.dispatchLoadLoop(delta);
				break;
			case Step.ON_BEFORE_PLAY_LOOP:
				this.dispatchOnBeforePlayLoop(delta);
				break;
			case Step.ON_PLAY_LOOP:
				if(this.dispatchOnPlayLoop(delta)==false){
					this.dispatchInit();
				}
				break;		
		}
	}
	protected abstract void dispatchInit();
	protected abstract void dispatchOnBeforeClearLoop(float delta);
	protected abstract void dispatchOnClear();
	protected abstract void dispatchSetActual();
	protected abstract void dispatchOnLoad();
	protected abstract void dispatchLoadLoop(float delta);
	protected abstract void dispatchOnPrepare();
	protected abstract void dispatchOnDispatchChild();
	protected abstract void dispatchOnBeforePlayLoop(float delta);
	protected abstract boolean dispatchOnPlayLoop(float delta);	
	
	@Override
	public void setStep(int step) {
		this._step=step;	
	}
	
	@Override
	public int getStep() {
		return this._step;
	}

	@Override
	public void setDefaultDispatch() {
		if(this._controller!=null){
			int idDefaultController=this._controller.getDefaultChildIdController();
			if(idDefaultController>0){
				this.setDispatch(idDefaultController);
			}
		}
	}	
	
	@Override
	public void setDispatch(int idController) {
		this.setNewChildIdController(idController);
		this.setIsDispatchFlag(true);
	}

	@Override
	public boolean isDispatchReady() {
		if(this._step==Step.ON_PLAY_LOOP||this._step==Step.NEVER_BEFORE){
			if(this.isActualChildController()==true){
				return this.getActualChildController().getDispatcher().isDispatchReady();
			}else{
				return true;
			}
		}else{
			return false;
		}	
	}
	
	@Override
	public void clear() {
		this.setActualChildIdController(0);
		this.setNewChildIdController(0);
		this.setIsDispatchFlag(false);
		this.setStep(Step.NEVER_BEFORE);
		
	}
}

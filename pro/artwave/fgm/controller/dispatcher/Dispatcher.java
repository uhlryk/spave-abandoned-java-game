package pro.artwave.fgm.controller.dispatcher;

import pro.artwave.fgm.model.asset.LoaderManager;

public class Dispatcher extends TemplateDispatcher {

	@Override
	public void dispatchInit() {
		this.setStep(TemplateDispatcher.Step.INIT);
		this.setIsDispatchFlag(false);
		this.dispatchOnBeforeClearLoop(0);		
	}
	/**
	 * Drugi krok, w p�tli wywo�uje si� tak d�ugo a� aktywny potomny Screen
	 * w metodzie onBeforeDisposeLoop() zwr�ci true. W niekt�rych aplikacjach mo�e by� ustawiony od razu na true
	 */
	@Override
	protected void dispatchOnBeforeClearLoop(float delta) {
		this.setStep(TemplateDispatcher.Step.ON_BEFORE_CLEAR_LOOP);
		if(this.isActualChildController()){
			if(this.getActualChildController().onBeforeClearLoop(delta)==true){
				this.dispatchOnClear();
			}
		}else{
			this.dispatchOnClear();
		}
	}
	/**
	 * Trzeci krok wywo�uje u potomnego aktywnego screena dispose i u jego dzieci
	 */	
	@Override
	protected void dispatchOnClear(){
		this.setStep(TemplateDispatcher.Step.ON_CLEAR);
		if(this.isActualChildController()){
			this.getActualChildController().clear();
		}
		this.dispatchSetActual();
	}	
	/**
	 * Ustawia nowy screen jako aktualny
	 */
	@Override
	protected void dispatchSetActual(){
		this.setStep(TemplateDispatcher.Step.SET_ACTUAL);
		this.setActualChildIdController(this.getNewChildIdController());
		this.setNewChildIdController(0);
		this.dispatchOnLoad();
	}	
	/**
	 * wywo�uje o potomnego aktywnego screena metod� onLoad
	 */
	@Override
	protected void dispatchOnLoad(){
		this.setStep(TemplateDispatcher.Step.ON_LOAD);
		this.getActualChildController().onLoad();
		this.getActualChildController().checkLoaderSet();
		if(LoaderManager.isLoaderFinish()==false){
			this.getActualChildController().showLoader();
			this.dispatchLoadLoop(0);
		}else{
			this.getActualChildController().clearLoader();
			this.dispatchOnPrepare();
		}
	}	
	@Override
	protected void dispatchLoadLoop(float delta) {
		this.setStep(TemplateDispatcher.Step.LOAD_LOOP);
		if(LoaderManager.updateLoad()){
			this.getActualChildController().getLoader().showProgress(LoaderManager.getLoadProgress());
			if(this.getActualChildController().getLoader().hideLoop(delta)){
				this.getActualChildController().clearLoader();
				this.dispatchOnPrepare();
			}
		}else{
			this.getActualChildController().getLoader().showLoop(delta);
			this.getActualChildController().getLoader().showProgress(LoaderManager.getLoadProgress());
			if(LoaderManager.updateLoad()){
				this.getActualChildController().getLoader().onFinish();
			}
		}
	}
	/**
	 * Wywo�uje u potomnego aktywnego screena metod� onPrepare
	 */
	@Override
	protected void dispatchOnPrepare(){
		
		this.setStep(TemplateDispatcher.Step.ON_PREPARE);
		this.getActualChildController().onPrepare();
		this.dispatchOnDispatchChild();
	}
	/**
	 * Jest to etap w kt�rym dispatchowany Screen mo�e odpali� r�wnie� swojego screena domy�lnego je�li go ma.
	 */
	@Override
	protected void dispatchOnDispatchChild(){
		this.setStep(TemplateDispatcher.Step.ON_DISPATCH_CHILD);
		/**
		 * Warunek sprawdza czy intent aktualnego dziecka nie ma wewn�trznego intenta, Wtedy zamiast wywo�a�
		 * u potomnego childa (ten kt�ry wywo�any jest przez intenta) jego domy�lnego, wywo�ujemy jego intentowy
		 * Czyli np mamy struktur�
		 * rootController
		 * -MainController
		 * --MainMenuController
		 * --SelectGameController
		 * -GameController
		 * Gdym wywo�ujemy MainController to wywo�uje si� domy�lny controller czyli mainmenu
		 * Gdy w mainmenu damy by main wywo�a� selectgame to przekazujemy intenta
		 * gdy game ma wywo�a� main to dajemy inenta wtedy root wywo�a main a main domy�lny
		 * Gdy game wywo�a intent main ale da zagnie�dzony intent dla maina by wywo�a� selectgame
		 * to root wywo�a main main sprawdzi czy ma przypadkiem zagnie�dzonego i zamiast domy�lnego wywo�a
		 * selectgame
		 */
		if(this.getActualChildController().getIntent()!=null&&this.getActualChildController().getIntent().getChildIntent()!=null){
		//	System.out.println(this.getActualChildController().getIntent()+" "+this.getActualChildController().getIntent().getChildIntent());
			this.getActualChildController().setDispatch(this.getActualChildController().getIntent().getChildIntent());
		//	System.out.println("test");
		}else{
			this.getActualChildController().getDispatcher().setDefaultDispatch();
		}
		this.dispatchOnBeforePlayLoop(0);
	}
	@Override
	protected void dispatchOnBeforePlayLoop(float delta) {
		this.setStep(TemplateDispatcher.Step.ON_BEFORE_PLAY_LOOP);
		if(this.getActualChildController().onBeforePlayLoop(delta)==true){
			this.dispatchOnPlayLoop(0);
		}
	}

	@Override
	protected boolean dispatchOnPlayLoop(float delta) {
		this.setStep(TemplateDispatcher.Step.ON_PLAY_LOOP);
		/**
		 * tu sprawdzamy czy zosta�o wywo�ane rz�danie dispatcha
		 */
		if(this.getIsDispatchFlag()==true){
			if(this.getActualChildController().getDispatcher().isDispatchReady()==true){		
				return false;
			}else{
				this.getActualChildController().onPlayLoop(delta);
			}
		}else{
			this.getActualChildController().onPlayLoop(delta);
		}
		return true;
	}

}

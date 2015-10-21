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
	 * Drugi krok, w pêtli wywo³uje siê tak d³ugo a¿ aktywny potomny Screen
	 * w metodzie onBeforeDisposeLoop() zwróci true. W niektórych aplikacjach mo¿e byæ ustawiony od razu na true
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
	 * Trzeci krok wywo³uje u potomnego aktywnego screena dispose i u jego dzieci
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
	 * wywo³uje o potomnego aktywnego screena metodê onLoad
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
	 * Wywo³uje u potomnego aktywnego screena metodê onPrepare
	 */
	@Override
	protected void dispatchOnPrepare(){
		
		this.setStep(TemplateDispatcher.Step.ON_PREPARE);
		this.getActualChildController().onPrepare();
		this.dispatchOnDispatchChild();
	}
	/**
	 * Jest to etap w którym dispatchowany Screen mo¿e odpaliæ równie¿ swojego screena domyœlnego jeœli go ma.
	 */
	@Override
	protected void dispatchOnDispatchChild(){
		this.setStep(TemplateDispatcher.Step.ON_DISPATCH_CHILD);
		/**
		 * Warunek sprawdza czy intent aktualnego dziecka nie ma wewnêtrznego intenta, Wtedy zamiast wywo³aæ
		 * u potomnego childa (ten który wywo³any jest przez intenta) jego domyœlnego, wywo³ujemy jego intentowy
		 * Czyli np mamy strukturê
		 * rootController
		 * -MainController
		 * --MainMenuController
		 * --SelectGameController
		 * -GameController
		 * Gdym wywo³ujemy MainController to wywo³uje siê domyœlny controller czyli mainmenu
		 * Gdy w mainmenu damy by main wywo³a³ selectgame to przekazujemy intenta
		 * gdy game ma wywo³aæ main to dajemy inenta wtedy root wywo³a main a main domyœlny
		 * Gdy game wywo³a intent main ale da zagnie¿dzony intent dla maina by wywo³a³ selectgame
		 * to root wywo³a main main sprawdzi czy ma przypadkiem zagnie¿dzonego i zamiast domyœlnego wywo³a
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
		 * tu sprawdzamy czy zosta³o wywo³ane rz¹danie dispatcha
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

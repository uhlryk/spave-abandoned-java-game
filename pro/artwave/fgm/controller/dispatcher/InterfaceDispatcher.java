package pro.artwave.fgm.controller.dispatcher;

import pro.artwave.fgm.controller.InterfaceDispatchDirector;

public interface InterfaceDispatcher {
	
	/**
	 * Metoda przy konfiguracji Dispatchera, Przyjmuje zarz¹dzany Controller
	 * @param controller
	 */
	void setController(InterfaceDispatchDirector controller);
	/**
	 * Zwraca ustawiony controller
	 * @return
	 */
	InterfaceDispatchDirector getController();
	/**
	 * Metoda sprawdza gotowoœæ dispatchera do dzia³ania
	 * @return
	 */
	boolean getIsDispatchFlag();
	/**
	 * Metoda wywo³ywana w controllerze. Informuje dispatcher o przebiegu pêtli, dziêki czemu idzie w jednym procesie
	 * @param delta
	 */
	void render(float delta);
	/**
	 * Zwraca aktualny etap na którym jest dispatcher
	 * @return
	 */
	int getStep();
	/**
	 * Ustawia nowy aktualny etap dispatcha
	 * @param step
	 */
	void setStep(int step);
	/**
	 * Ustawia rz¹danie dla dispatchera. Zostanie  ono wywo³ane w okreœlonych warunkach. 
	 * Jeœli wszystkie dzieci s¹ na ostatnim etapie dispatchera lub jeœli dispatcher jest pierwszy raz w u¿yciu
	 * @param idController
	 */
	void setDispatch(int idController);
	/**
	 * Ustawia rz¹danie dispatcha domyœlnego controllera potomnego, wywo³ywany zwykle przez rodzica gdy ustawia dany controller 
	 * przez dispatcher.
	 */
	void setDefaultDispatch();
	/**
	 * Metoda udziela odpowiedzi czy dzieci s¹ gotowe na dispatcha
	 */
	boolean isDispatchReady();
	/**
	 * Metoda zmienia id aktualnego potomnego controllera
	 * @param idController
	 */
	void setActualChildIdController(int idController);
	/**
	 * Metoda zwraca id aktualnego potomnego controllera
	 * @param idController
	 */
	int getActualChildIdController();
	/**
	 * Czyœci dispatchera
	 */
	void clear();
}

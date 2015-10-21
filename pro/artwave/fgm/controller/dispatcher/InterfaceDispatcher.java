package pro.artwave.fgm.controller.dispatcher;

import pro.artwave.fgm.controller.InterfaceDispatchDirector;

public interface InterfaceDispatcher {
	
	/**
	 * Metoda przy konfiguracji Dispatchera, Przyjmuje zarz�dzany Controller
	 * @param controller
	 */
	void setController(InterfaceDispatchDirector controller);
	/**
	 * Zwraca ustawiony controller
	 * @return
	 */
	InterfaceDispatchDirector getController();
	/**
	 * Metoda sprawdza gotowo�� dispatchera do dzia�ania
	 * @return
	 */
	boolean getIsDispatchFlag();
	/**
	 * Metoda wywo�ywana w controllerze. Informuje dispatcher o przebiegu p�tli, dzi�ki czemu idzie w jednym procesie
	 * @param delta
	 */
	void render(float delta);
	/**
	 * Zwraca aktualny etap na kt�rym jest dispatcher
	 * @return
	 */
	int getStep();
	/**
	 * Ustawia nowy aktualny etap dispatcha
	 * @param step
	 */
	void setStep(int step);
	/**
	 * Ustawia rz�danie dla dispatchera. Zostanie  ono wywo�ane w okre�lonych warunkach. 
	 * Je�li wszystkie dzieci s� na ostatnim etapie dispatchera lub je�li dispatcher jest pierwszy raz w u�yciu
	 * @param idController
	 */
	void setDispatch(int idController);
	/**
	 * Ustawia rz�danie dispatcha domy�lnego controllera potomnego, wywo�ywany zwykle przez rodzica gdy ustawia dany controller 
	 * przez dispatcher.
	 */
	void setDefaultDispatch();
	/**
	 * Metoda udziela odpowiedzi czy dzieci s� gotowe na dispatcha
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
	 * Czy�ci dispatchera
	 */
	void clear();
}

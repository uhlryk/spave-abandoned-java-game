package pro.artwave.fgm.controller;

import pro.artwave.fgm.controller.dispatcher.InterfaceDispatcher;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.View;
/**
 * Interfejs jaki ma posiada� rodzi� controllera. Nie musi by� on kontrollerem. Musi posiada� tylko wymienione metody
 * @author Krzysztof
 *
 */
public interface InterfaceParentController {
	/**
	 * Zwraca widok g��wny
	 * @return
	 */
	View getView();
	/**
	 * Z poziomu potomnego kontrolera ustawiamy rz�danie dispatcha
	 * @param idController
	 */
	void setDispatch(Intent intent);
	/**
	 * Czasem istnieje potrzeba wywo�a� kontroler wy�szy od rodzica. Np by zrobi� na wy�szym poziomie dispatcha
	 * @return
	 */
	InterfaceParentController getParent();
	/**
	 * Metoda zwraca kontroler potomny rodzica.
	 * Dzi�ki czemu mo�emy da� intenta do innego potomnego kontrolera, a nast�pnie w parencie wywo�ac potomny kontroler
	 * przez co mamy kontrol� na dowolny poziom w d�
	 * @param idController
	 * @return
	 */
	Controller getChildController(int idController);
	/**
	 * Metoda wyswietla intent danego kontrolera, je�li pojecenie np przysz�o na otwarcie kontrolera
	 * otrzyma ono intent, ale potem ono otworzy swoje dziecko, na to mo�e nie mie� intenta ale zrobi to
	 * automatycznie, wtedy intent jest null. Co innego jak posz�o celowe otwarcie wtedy jest ok.
	 * @return
	 */
	Intent getIntent();
	
	InterfaceDispatcher getDispatcher();
}

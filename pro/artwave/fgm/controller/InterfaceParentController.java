package pro.artwave.fgm.controller;

import pro.artwave.fgm.controller.dispatcher.InterfaceDispatcher;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.View;
/**
 * Interfejs jaki ma posiadaæ rodziæ controllera. Nie musi byæ on kontrollerem. Musi posiadaæ tylko wymienione metody
 * @author Krzysztof
 *
 */
public interface InterfaceParentController {
	/**
	 * Zwraca widok g³ówny
	 * @return
	 */
	View getView();
	/**
	 * Z poziomu potomnego kontrolera ustawiamy rz¹danie dispatcha
	 * @param idController
	 */
	void setDispatch(Intent intent);
	/**
	 * Czasem istnieje potrzeba wywo³aæ kontroler wy¿szy od rodzica. Np by zrobiæ na wy¿szym poziomie dispatcha
	 * @return
	 */
	InterfaceParentController getParent();
	/**
	 * Metoda zwraca kontroler potomny rodzica.
	 * Dziêki czemu mo¿emy daæ intenta do innego potomnego kontrolera, a nastêpnie w parencie wywo³ac potomny kontroler
	 * przez co mamy kontrolê na dowolny poziom w dó³
	 * @param idController
	 * @return
	 */
	Controller getChildController(int idController);
	/**
	 * Metoda wyswietla intent danego kontrolera, jeœli pojecenie np przysz³o na otwarcie kontrolera
	 * otrzyma ono intent, ale potem ono otworzy swoje dziecko, na to mo¿e nie mieæ intenta ale zrobi to
	 * automatycznie, wtedy intent jest null. Co innego jak posz³o celowe otwarcie wtedy jest ok.
	 * @return
	 */
	Intent getIntent();
	
	InterfaceDispatcher getDispatcher();
}

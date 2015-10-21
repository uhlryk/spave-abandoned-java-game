package pro.artwave.fgm.controller;

/**
 * Interfejs którego Dispatcher u¿ywa wzglêdem obiektu który go u¿ywa
 * @author Krzysztof
 *
 */
public interface InterfaceDispatchDirector {
	Controller getChildController(int idController);
	int getDefaultChildIdController();
}

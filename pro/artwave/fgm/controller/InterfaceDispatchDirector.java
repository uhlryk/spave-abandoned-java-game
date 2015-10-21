package pro.artwave.fgm.controller;

/**
 * Interfejs kt�rego Dispatcher u�ywa wzgl�dem obiektu kt�ry go u�ywa
 * @author Krzysztof
 *
 */
public interface InterfaceDispatchDirector {
	Controller getChildController(int idController);
	int getDefaultChildIdController();
}

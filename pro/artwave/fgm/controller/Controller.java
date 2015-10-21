package pro.artwave.fgm.controller;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import pro.artwave.fgm.controller.dispatcher.Dispatcher;
import pro.artwave.fgm.controller.dispatcher.InterfaceDispatcher;
import pro.artwave.fgm.controller.dispatcher.TemplateDispatcher;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.model.asset.InterfaceControlAsset;
import pro.artwave.fgm.view.View;
import pro.artwave.fgm.view.loader.AbstractLoader;
import pro.artwave.fgm.view.loader.EmptyLoader;

public abstract class Controller implements InterfaceDispatchDirector,InterfaceParentController,InterfaceInputController{
	private InterfaceParentController _parent;
	private ArrayList<InterfaceControlAsset> _modelArrayList;
	private int _defaultIdController;
	private ArrayList<Controller> _childArrayList;
	private AbstractLoader _loader;
	private ControllerManager _controllerManager;
	/**
	 * Widok g��wny Screena
	 */
	private View _view;
	private InterfaceDispatcher _dispatcher;
	private Intent _intent;
	/**
	 * Metoda inicjuj�ca, wywo�ywana tylko raz, 
	 */
	public void init(InterfaceParentController parent,ControllerManager controllerManager) {
		this._parent=parent;	
		this._controllerManager=controllerManager;
		this.onInit();
		this.checkDispatcherSet();
	}
	/**
	 * Metoda sprawdza czy w onInit() lub wcze�niej zosta� ustawiony Dispatcher, Je�li nie to ustawia domy�lny dispatcher
	 */
	public void checkDispatcherSet(){
		if(this.getDispatcher()==null){
			this.setDispatcher(new Dispatcher());
		}
	}		
	public void setDispatcher(InterfaceDispatcher dispatcher){
		if(this.getDispatcher()!=null){
			throw new RuntimeException("There is dispatcher already");
		}
		dispatcher.setController(this);
		this._dispatcher=dispatcher;
	}
	public InterfaceDispatcher getDispatcher(){
		return this._dispatcher;
	}
	/**
	 * Metoda inicjuj�ca nadpisywana przez usera,Nale�y w niej stworzy� potomne screeny
	 * doda� je do tego screena. Nie nale�y ich inicjowa�. Odpowiada za to metoda addChildScreen.
	 * Nie nale�y jej wywo�ywa� samodzielnie
	 * Przy dodawaniu screen�w warto zapami�ta� ich idScreen kt�ry odpowiada indexowi+1 (kolejno�� dodania)
	 * Mo�na ustawia�:
	 * - inne controllery
	 * - dispacher
	 */
	public abstract void onInit();
	/**
	 * Metoda nadpisywana przez usera powinna zawiera� list� asset�w kt�e maj� by� za�adowane w kroku dispatcha
	 * dispatchLoadLoop
	 * W metodzie mo�na okre�li� nowy loader, Nale�y pami�ta� by loader ten mia� assety wczytane w Controllerach
	 * na wy�szej hierarchii
	 * -�adujemy assety
	 * -loader
	 */
	public abstract void onLoad();
	/**
	 * Metoda sprawdza czy w onLoad() lub wcze�niej zosta� ustawiony Loader, Je�li nie to ustawia domy�lny loader
	 */
	public void checkLoaderSet(){
		if(this.getLoader()==null){
			AbstractLoader loader=new EmptyLoader();			
			this.setLoader(loader);
		}
	}		
	/**
	 * Mamy mo�liwo�� zdefiniowa� customowy loader.
	 * W przeciwnym razie odpalany jest domy�lny
	 */
	public void setLoader(AbstractLoader loader) {
		if(this.getLoader()!=null){
//			throw new RuntimeException("There is loader already");
		}
		this._loader=loader;	
		loader.init();
	}
	/**
	 * wywo�uje loader i wykonuje jego zadania przez interfejs
	 * @return
	 */
	public AbstractLoader getLoader() {
		return this._loader;
	}
	/**
	 * Metoda odpowiada za grup� do kt�rej zostaje wrzucony loader.
	 * Mo�na nadpisa� by to zmieni�. Domy�lnie wrzucany jest do ViewManagera w warstw� load
	 */
	public void showLoader(){
		this._controllerManager.getViewManager().addLoader(this.getLoader());
	}
	/**
	 * Metoda usuwa loader od controllera przy dispatchu
	 */
	public void clearLoader(){
		this._loader.remove();
		this._loader.clear();
		this._loader=null;
	}	
	/**
	 * Wywo�ywana w dispatcher parenta. Powinna by� nadpisana przez usera.
	 * W niej nale�y skonstruowa� widok, ustawi� go i zainicjowa�
	 * Nast�pnie konfigurujemy komunikacj� z 
	 * widokiem, tworzymy modele. Wszystko co niezb�dne do dalszej pracy. Okre�lamy szczeg�y co do tego co ma si�
	 * znale�� w widoku 
	 * -view
	 * 1)konstruktor
	 * 2)setView
	 * 3)addtoPararent
	 * 4)Inne metody inicjuj�ce
	 */
	public abstract void onPrepare();
	
	/**
	 * Wywo�ywana w dispatcher parenta. Zaraz za onPrepare. Jest ona wywo�ywana wielokrotnie w p�tli do
	 * momentu gdy zwr�ci true. W niej mo�emy zaanimowa� wy�wietlenie si� widoku.
	 * W niekt�rych aplikacjach mo�e by� od razu ustawiony na true
	 */
	@SuppressWarnings("static-method")
	public boolean onBeforePlayLoop(float delta){
		return true;
	}
	/**
	 * Wywo�ywana w dispatcher parenta. Wywo�ywana wielokrotnie w p�tli a� nie pojawi si� rz�danie nowego Screena
	 * Tu ca�a dynamiczna logika aplikacji
	 */
	public void onPlayLoop(float delta){}
	/**
	 * Wywo�ywana w dispatcher parenta. Wywo�ywane gdy obecny screen ma zosta� usuni�ty. 
	 * W tym miejscu TYLKO mo�emy ukry� w jaki� dynamiczny spos�b widok
	 * W niekt�rych aplikacjach mo�e by� od razu ustawiony na true	 * 
	 */
	@SuppressWarnings("static-method")
	public boolean onBeforeClearLoop(float delta){
		return true;
	}
	/**
	 * Wywo�ywana w dispatcher parenta. Metoda ma za zadanie usun�� wszystkie dane z Screena, zostawi� tylko
	 * pust� struktur� init�w Screen�w.
	 * Problem jest z czyszczeniem intentu. Je�li b�dziemy chcieli ponownie otworzy� ten sam kontroler
	 * to ustawiamy intent, nast�pnie w procesie czyszczenia starego kontrolera kasujemy jego
	 * intent. ale jako �e jest to ta sama instancja wi�c kasowali�my tego samego intenta
	 * Testowo wi�c wy��czam kasowanie intentu.
	 */
	public void clear() {
		if(this._childArrayList!=null){
			for(Controller child:this._childArrayList){
					child.clear();
			}	
		}
		this.getDispatcher().clear();
		this.onClear();
		if(this._intent!=null){
	//		this._intent.clear();
		}
		this.clearView();
		this.clearModel();
	}
	public void setIntent(Intent intent){
		if(this._intent!=null){
			this._intent.clear();
		}
		this._intent=intent;
	}
	public Intent getIntent(){
		return this._intent;
	}	
	/**
	 * Metoda nadpisywana przez usera. Nale�y wyczy�ci� widok!
	 */
	public void onClear(){}
	/**
	 * Metoda pozwala doda� do hierarchii Screen kolejny element zale�ny od obecnego
	 * Nie nale�y realizowa� tego dynamicznie. Struktura powinna by� zdefiniowana na pocz�tku dzia�ania aplikacji
	 * Metoda ta wywo�uje init screen�w potomnych
	 */	
	public int addController(Controller child){
		return this.addController(child,false);
	}
	/**
	 * Metoda pozwala doda� do hierarchii Screen kolejny element zale�ny od obecnego
	 * Nie nale�y realizowa� tego dynamicznie. Struktura powinna by� zdefiniowana na pocz�tku dzia�ania aplikacji
	 * Metoda ta wywo�uje init screen�w potomnych
	 */
	public int addController(Controller child,boolean isDefault) {
		if(this._childArrayList==null){
			this._childArrayList=new ArrayList<Controller>();
		}
		this._childArrayList.add(child);
		int id=this._childArrayList.size();
		if(isDefault==true||this._defaultIdController==0){
			this._defaultIdController=id;
		}
		child.init(this,this._controllerManager);
		return id;
	}
	public int getDefaultChildIdController(){
		return this._defaultIdController;
	}
	/**
	 * Rz�danie wy�wietlenia nowego Controllera. Nast�pi to jednak gdy obecny screen osi�gnie etap start_loop
	 * Jak r�wnie� jego aktywne screeny. Automatycznie na odpowiednim etapie odpali si� dispatcher wywo�any
	 * t� metod�
	 */
	public void setDispatch(Intent intent) {
	//	System.out.println("Controller.setDispatch "+intent);
		this.getDispatcher().setDispatch(intent.getIntentIdClass());
		this.getChildController(intent.getIntentIdClass()).setIntent(intent);
	}

	/**
	 * Metoda zwraca Controller potomny na podstawie jego id
	 * id jest o jeden wi�kszy od indexu a wi�c w metodzie przy get odejmujemy od indexu 1
	 * Celowo id jest o 1 wi�ksze by warto�� 0 by�a nieokre�lona
	 */
	public Controller getChildController(int idController){
		int index=idController-1;
		if(index<0)return null;
		if(index>=this._childArrayList.size())return null;
		return this._childArrayList.get(index);
	}
	/**
	 * Metoda zwraca screen rodzica
	 */
	public InterfaceParentController getParent() {
		return this._parent;
	}
	/**
	 * Metoda wywo�ywana w p�tli. U�ywana do dispatcher�w. Nie ma potrzeby u�ywania jej przez usera
	 * Lepiej u�y� OnBeforeClearLoop,OnBeforePlayLoop,OnPlayLoop
	 */ 
	public void onRender(float delta){
		if(this._childArrayList!=null){
			for(Controller child:this._childArrayList){
				child.onRender(delta);
			}
		}
		this.getDispatcher().render(delta);
	}
	/**
	 * Metoda czy�ci widok
	 */
	public void clearView(){
		if(_view!=null){
			_view.clear();
			_view.remove();
		}		
		this._view=null;
	}		
	/**
	 * Pozwala okre�li� domy�lny view
	 * @param view
	 */
	public void setView(View view){
		if(this._view!=null){
			throw new RuntimeException("There is view already");
		}
		this._view=view;
	}
	/**
	 * Przekazuje view
	 * @return
	 */
	public View getView(){
		return this._view;
	}
	
	/**
	 * metoda dodaje asset do kontrolera. Oznacza to �e kontroller za�aduje asset je�li ten nie zosta� wcze�niej
	 * za�adowany. Je�li ten kontroler go za�adowa� to te� go usunie po zako�czeniu dzia�ania
	 * @param asset
	 */
	public void addModelToControl(InterfaceControlAsset model){
		if(model.isLoad()==false){//asset
			if(this._modelArrayList==null){
				this._modelArrayList=new ArrayList<InterfaceControlAsset>();
			}
			this._modelArrayList.add(model);
			model.load();
		}
	}
	/**
	 * Metoda czy�ci z kolejki wszystkie assety kt�re powinny by� usuni�te a nast�pnie ca�� kolejk�
	 */
	public void clearModel(){
		if(this._modelArrayList!=null){
			for(InterfaceControlAsset model:this._modelArrayList){
				model.clear();
			}
			this._modelArrayList.clear();
		}
		this._modelArrayList=null;
		System.gc();
	}
	
	
	/**
	 * Wy�wietla w konsoli utworzon� struktur� Screen�w. Czyli struktur� kompozytu Screen
	 * @param levelSeparator
	 */
	public void outputStringStructure(String levelSeparator,boolean isActive){
		String newlevelSeparator=levelSeparator+"..";
		System.out.println(levelSeparator+this.getClass().getSimpleName()+" isActive:"+isActive);
		if(this._childArrayList!=null){
			for(int i=0;i<this._childArrayList.size();i++){
				Controller child=this._childArrayList.get(i);
				if(isActive==true&&this.getDispatcher().getActualChildIdController()==i){
					child.outputStringStructure(newlevelSeparator,true);
				}else{
					child.outputStringStructure(newlevelSeparator,false);
				}
			}
		}
			
	}
	/**
	 * Wywo�ywane przez system gdy u��dzedzenie b�dzie mie� telefon lub user zminimalizuje
	 * aplikacj�. Wywo�ywane wzorcem �a�cuch zobowi�za� do najni�szego poziomu
	 * Wywo�uje metod� tylko u aktywnego kontrolera potomnego 
	 * User powinien nadpisywa� onSystemPause();
	 */
	public void systemPause(){
		Controller childController=this.getChildController(this.getDispatcher().getActualChildIdController());
		if(childController!=null){
			childController.systemPause();
		}		
		this.onSystemPause();
	}
	/**
	 * Wywo�ywane przez system gdy u��dzedzenie w��czy aplikacj� kt�ra by�a pause
	 * aplikacj�. Wywo�ywane wzorcem �a�cuch zobowi�za� do najni�szego poziomu
	 * Wywo�uje metod� tylko u aktywnego kontrolera potomnego
	 * User powinien nadpisywa� onSystemPResume();
	 */	
	public void systemResume(){
		Controller childController=this.getChildController(this.getDispatcher().getActualChildIdController());
		if(childController!=null){
			childController.systemResume();
		}
		this.onSystemResume();
	}
	/**
	 * Metoda s�u�y do nadpisywania. Pozwala okre�li� zachowanie na systemow� pause
	 * Mo�na zapisa� stan gry
	 */
	public void onSystemPause(){}
	/**
	 * Metoda s�u�y do nadpisywania. Pozwala okre�li� zachowanie na systemowy resume
	 */	
	public void onSystemResume(){}	
	
	public boolean keyDown(int keycode){
		Controller childController=this.getChildController(this.getDispatcher().getActualChildIdController());
		if(childController!=null){
			if(childController.keyDown(keycode)==true){
				return true;
			}
		}
		if(this.getParent().getDispatcher().getStep()==TemplateDispatcher.Step.ON_PLAY_LOOP)
			return onKeyDown(keycode);
		else return false;
	}
	public boolean onKeyDown(int keycode){
		return false;
	}
	public boolean keyUp(int keycode){
		Controller childController=this.getChildController(this.getDispatcher().getActualChildIdController());
		if(childController!=null){
			if(childController.keyUp(keycode)==true){
				return true;
			}
		}		
		if(this.getParent().getDispatcher().getStep()==TemplateDispatcher.Step.ON_PLAY_LOOP)
			return onKeyUp(keycode);
		else return false;
	}
	public boolean onKeyUp(int keycode){
		return false;
	}	
	public boolean keyTyped(char character){
		Controller childController=this.getChildController(this.getDispatcher().getActualChildIdController());
		if(childController!=null){
			if(childController.keyTyped(character)==true){
				return true;
			}
		}		
		if(this.getParent().getDispatcher().getStep()==TemplateDispatcher.Step.ON_PLAY_LOOP)
			return onKeyTyped(character);
		else return false;
	}
	public boolean onKeyTyped(char character){
		return false;
	}	
}

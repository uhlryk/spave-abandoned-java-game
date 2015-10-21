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
	 * Widok g³ówny Screena
	 */
	private View _view;
	private InterfaceDispatcher _dispatcher;
	private Intent _intent;
	/**
	 * Metoda inicjuj¹ca, wywo³ywana tylko raz, 
	 */
	public void init(InterfaceParentController parent,ControllerManager controllerManager) {
		this._parent=parent;	
		this._controllerManager=controllerManager;
		this.onInit();
		this.checkDispatcherSet();
	}
	/**
	 * Metoda sprawdza czy w onInit() lub wczeœniej zosta³ ustawiony Dispatcher, Jeœli nie to ustawia domyœlny dispatcher
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
	 * Metoda inicjuj¹ca nadpisywana przez usera,Nale¿y w niej stworzyæ potomne screeny
	 * dodaæ je do tego screena. Nie nale¿y ich inicjowaæ. Odpowiada za to metoda addChildScreen.
	 * Nie nale¿y jej wywo³ywaæ samodzielnie
	 * Przy dodawaniu screenów warto zapamiêtaæ ich idScreen który odpowiada indexowi+1 (kolejnoœæ dodania)
	 * Mo¿na ustawiaæ:
	 * - inne controllery
	 * - dispacher
	 */
	public abstract void onInit();
	/**
	 * Metoda nadpisywana przez usera powinna zawieraæ listê assetów któe maj¹ byæ za³adowane w kroku dispatcha
	 * dispatchLoadLoop
	 * W metodzie mo¿na okreœliæ nowy loader, Nale¿y pamiêtaæ by loader ten mia³ assety wczytane w Controllerach
	 * na wy¿szej hierarchii
	 * -³adujemy assety
	 * -loader
	 */
	public abstract void onLoad();
	/**
	 * Metoda sprawdza czy w onLoad() lub wczeœniej zosta³ ustawiony Loader, Jeœli nie to ustawia domyœlny loader
	 */
	public void checkLoaderSet(){
		if(this.getLoader()==null){
			AbstractLoader loader=new EmptyLoader();			
			this.setLoader(loader);
		}
	}		
	/**
	 * Mamy mo¿liwoœæ zdefiniowaæ customowy loader.
	 * W przeciwnym razie odpalany jest domyœlny
	 */
	public void setLoader(AbstractLoader loader) {
		if(this.getLoader()!=null){
//			throw new RuntimeException("There is loader already");
		}
		this._loader=loader;	
		loader.init();
	}
	/**
	 * wywo³uje loader i wykonuje jego zadania przez interfejs
	 * @return
	 */
	public AbstractLoader getLoader() {
		return this._loader;
	}
	/**
	 * Metoda odpowiada za grupê do której zostaje wrzucony loader.
	 * Mo¿na nadpisaæ by to zmieniæ. Domyœlnie wrzucany jest do ViewManagera w warstwê load
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
	 * Wywo³ywana w dispatcher parenta. Powinna byæ nadpisana przez usera.
	 * W niej nale¿y skonstruowaæ widok, ustawiæ go i zainicjowaæ
	 * Nastêpnie konfigurujemy komunikacjê z 
	 * widokiem, tworzymy modele. Wszystko co niezbêdne do dalszej pracy. Okreœlamy szczegó³y co do tego co ma siê
	 * znaleœæ w widoku 
	 * -view
	 * 1)konstruktor
	 * 2)setView
	 * 3)addtoPararent
	 * 4)Inne metody inicjuj¹ce
	 */
	public abstract void onPrepare();
	
	/**
	 * Wywo³ywana w dispatcher parenta. Zaraz za onPrepare. Jest ona wywo³ywana wielokrotnie w pêtli do
	 * momentu gdy zwróci true. W niej mo¿emy zaanimowaæ wyœwietlenie siê widoku.
	 * W niektórych aplikacjach mo¿e byæ od razu ustawiony na true
	 */
	@SuppressWarnings("static-method")
	public boolean onBeforePlayLoop(float delta){
		return true;
	}
	/**
	 * Wywo³ywana w dispatcher parenta. Wywo³ywana wielokrotnie w pêtli a¿ nie pojawi siê rz¹danie nowego Screena
	 * Tu ca³a dynamiczna logika aplikacji
	 */
	public void onPlayLoop(float delta){}
	/**
	 * Wywo³ywana w dispatcher parenta. Wywo³ywane gdy obecny screen ma zostaæ usuniêty. 
	 * W tym miejscu TYLKO mo¿emy ukryæ w jakiœ dynamiczny sposób widok
	 * W niektórych aplikacjach mo¿e byæ od razu ustawiony na true	 * 
	 */
	@SuppressWarnings("static-method")
	public boolean onBeforeClearLoop(float delta){
		return true;
	}
	/**
	 * Wywo³ywana w dispatcher parenta. Metoda ma za zadanie usun¹æ wszystkie dane z Screena, zostawiæ tylko
	 * pust¹ strukturê initów Screenów.
	 * Problem jest z czyszczeniem intentu. Jeœli bêdziemy chcieli ponownie otworzyæ ten sam kontroler
	 * to ustawiamy intent, nastêpnie w procesie czyszczenia starego kontrolera kasujemy jego
	 * intent. ale jako ¿e jest to ta sama instancja wiêc kasowaliœmy tego samego intenta
	 * Testowo wiêc wy³¹czam kasowanie intentu.
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
	 * Metoda nadpisywana przez usera. Nale¿y wyczyœciæ widok!
	 */
	public void onClear(){}
	/**
	 * Metoda pozwala dodaæ do hierarchii Screen kolejny element zale¿ny od obecnego
	 * Nie nale¿y realizowaæ tego dynamicznie. Struktura powinna byæ zdefiniowana na pocz¹tku dzia³ania aplikacji
	 * Metoda ta wywo³uje init screenów potomnych
	 */	
	public int addController(Controller child){
		return this.addController(child,false);
	}
	/**
	 * Metoda pozwala dodaæ do hierarchii Screen kolejny element zale¿ny od obecnego
	 * Nie nale¿y realizowaæ tego dynamicznie. Struktura powinna byæ zdefiniowana na pocz¹tku dzia³ania aplikacji
	 * Metoda ta wywo³uje init screenów potomnych
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
	 * Rz¹danie wyœwietlenia nowego Controllera. Nast¹pi to jednak gdy obecny screen osi¹gnie etap start_loop
	 * Jak równie¿ jego aktywne screeny. Automatycznie na odpowiednim etapie odpali siê dispatcher wywo³any
	 * t¹ metod¹
	 */
	public void setDispatch(Intent intent) {
	//	System.out.println("Controller.setDispatch "+intent);
		this.getDispatcher().setDispatch(intent.getIntentIdClass());
		this.getChildController(intent.getIntentIdClass()).setIntent(intent);
	}

	/**
	 * Metoda zwraca Controller potomny na podstawie jego id
	 * id jest o jeden wiêkszy od indexu a wiêc w metodzie przy get odejmujemy od indexu 1
	 * Celowo id jest o 1 wiêksze by wartoœæ 0 by³a nieokreœlona
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
	 * Metoda wywo³ywana w pêtli. U¿ywana do dispatcherów. Nie ma potrzeby u¿ywania jej przez usera
	 * Lepiej u¿yæ OnBeforeClearLoop,OnBeforePlayLoop,OnPlayLoop
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
	 * Metoda czyœci widok
	 */
	public void clearView(){
		if(_view!=null){
			_view.clear();
			_view.remove();
		}		
		this._view=null;
	}		
	/**
	 * Pozwala okreœliæ domyœlny view
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
	 * metoda dodaje asset do kontrolera. Oznacza to ¿e kontroller za³aduje asset jeœli ten nie zosta³ wczeœniej
	 * za³adowany. Jeœli ten kontroler go za³adowa³ to te¿ go usunie po zakoñczeniu dzia³ania
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
	 * Metoda czyœci z kolejki wszystkie assety które powinny byæ usuniête a nastêpnie ca³¹ kolejkê
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
	 * Wyœwietla w konsoli utworzon¹ strukturê Screenów. Czyli strukturê kompozytu Screen
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
	 * Wywo³ywane przez system gdy u¿¹dzedzenie bêdzie mieæ telefon lub user zminimalizuje
	 * aplikacjê. Wywo³ywane wzorcem ³añcuch zobowi¹zañ do najni¿szego poziomu
	 * Wywo³uje metodê tylko u aktywnego kontrolera potomnego 
	 * User powinien nadpisywaæ onSystemPause();
	 */
	public void systemPause(){
		Controller childController=this.getChildController(this.getDispatcher().getActualChildIdController());
		if(childController!=null){
			childController.systemPause();
		}		
		this.onSystemPause();
	}
	/**
	 * Wywo³ywane przez system gdy u¿¹dzedzenie w³¹czy aplikacjê która by³a pause
	 * aplikacjê. Wywo³ywane wzorcem ³añcuch zobowi¹zañ do najni¿szego poziomu
	 * Wywo³uje metodê tylko u aktywnego kontrolera potomnego
	 * User powinien nadpisywaæ onSystemPResume();
	 */	
	public void systemResume(){
		Controller childController=this.getChildController(this.getDispatcher().getActualChildIdController());
		if(childController!=null){
			childController.systemResume();
		}
		this.onSystemResume();
	}
	/**
	 * Metoda s³u¿y do nadpisywania. Pozwala okreœliæ zachowanie na systemow¹ pause
	 * Mo¿na zapisaæ stan gry
	 */
	public void onSystemPause(){}
	/**
	 * Metoda s³u¿y do nadpisywania. Pozwala okreœliæ zachowanie na systemowy resume
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

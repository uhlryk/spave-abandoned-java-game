package pro.artwave.fgm.controller.intent;

import java.util.HashMap;
import java.util.Map;

import pro.artwave.fgm.bundle.InterfaceBundle;

public class Intent {
	private int _intentIdClass;
	
	private Map<String,Integer> _mapInteger;
	private Map<String,String> _mapString;
	private Map<String,Boolean> _mapBoolean;
	private Map<String,InterfaceBundle> _mapBundle;	
	private Intent _childIntent;
	
	//private InterfaceParentController
	
	/**
	 * Przekazujemy id kontrolera kt�ry wywo�any jest w kontrolerze do kt�rego dispatchera przekazujemy potem intent 
	 * Controller.this.getParent().setDispatch(intent);
	 * @param intentIdClass
	 */
	public Intent(int intentIdClass){
		this._intentIdClass=intentIdClass;
		this._mapInteger=new HashMap<String,Integer>();
		this._mapString=new HashMap<String,String>();
		this._mapBoolean=new HashMap<String,Boolean>();
		this._mapBundle=new HashMap<String,InterfaceBundle>();		
	}
	/**
	 * Metoda pozwala doda� do danego intentu, intent do wywo�ania dziecka intentu
	 * Czyli wywo�ujemy jak�� g��wn� klas� przez konstruktor lub setIntentIdClass
	 * Nast�pnie tworzymy nowy intent do kontrolera kt�ry jest dzieckiem g��wnego kontrolera
	 * I dodajemy go do tej metody.
	 * Dispatcher wywo�a najpierw g��wny kontroler potem wywo�a nast�pny intent z setChildIntent czyli
	 * wywo�a pochodny kontroler.
	 * Pochodny kontroler mo�e mie� tak zdefiniowany sw�j pochodny
	 * Mo�emy wi�c robi� przej�cia z dowolnego intentu do dowolnego innego
	 * @param intent
	 */
	public void setChildIntent(Intent intent){
		 _childIntent=intent;
	}
	public Intent getChildIntent(){
		return _childIntent;
	}
	/**
	 * Mo�emy doda� nowy id kontrolera, by zmieni� ten z konstruktora
	 * @param intentIdClass
	 */
	public void setIntentIdClass(int intentIdClass){
		this._intentIdClass=intentIdClass;
	}
	public int getIntentIdClass(){
		return this._intentIdClass;
	}
	/**
	 * przekazujemy warto�� kt�ra b�dzie przekazana do nowego kontrolera
	 * @param key
	 * @param value
	 */
	public void addValue(String key,Integer value){
		this._mapInteger.put(key, value);
	}
	public void addValue(String key,String value){
		this._mapString.put(key, value);
	}	
	public void addValue(String key,Boolean value){
		this._mapBoolean.put(key, value);
	}	
	public void addValue(String key,InterfaceBundle value){
		this._mapBundle.put(key, value);
	}	
	public Integer getIntegerValue(String key){
		return this._mapInteger.get(key);
	}
	public String getStringValue(String key){
		return this._mapString.get(key);
	}
	public Boolean getBooleanValue(String key){
		return this._mapBoolean.get(key);
	}
	public InterfaceBundle getBundleValue(String key){
		return this._mapBundle.get(key);
	}		
	public Integer getIntegerValue(String key,Integer defValue){
		Integer val=this._mapInteger.get(key);
		return val!=null?val:defValue;
	}
	public String getStringValue(String key,String defValue){
		String val=this._mapString.get(key);
		return val!=null?val:defValue;		
	}
	public Boolean getBooleanValue(String key,Boolean defValue){
		Boolean val=this._mapBoolean.get(key);
		return val!=null?val:defValue;			
	}
	/**
	 * Na t� chwil� nie ma mo�liwo�ci przekazywa� bundle poniewa� nie istnieje jeszcze interfejs bundle
	 * Jak b�dzie taka potrzeba trzeba go dopisa�
	 * @param key
	 * @param defValue
	 * @return
	 */
	public InterfaceBundle getBundleValue(String key,InterfaceBundle defValue){
		InterfaceBundle val=this._mapBundle.get(key);
		return val!=null?val:defValue;			
	}	
	public void clear(){
		this._intentIdClass=0;
		if(this._mapInteger!=null){
			this._mapInteger.clear();
		}
		this._mapInteger=null;
		if(this._mapString!=null){
			this._mapString.clear();
		}
		this._mapString=null;
		if(this._mapBoolean!=null){
			this._mapBoolean.clear();
		}
		this._mapBoolean=null;
		if(this._mapBundle!=null){
			this._mapBundle.clear();
		}
		this._mapBundle=null;		
	}
	@Override
	public String toString(){
		String debugName="empty";
		if(this._mapString!=null){
			String s=this._mapString.get("debugName");
			if(s!=null){
				debugName=s;
			}
		}
		return "Intent: "+debugName;
	}
}

package pro.artwave.fgm.utility;

/**
 * Klasa ma zast�pi� HashMap u�ywan� do przechowywania niepowtarzalnych definicji obiekt�w typu Tile 
 * gdzie ka�da warto�� to jeden tile. Mechanizm hasmMap jest jak armata na komara. Mam nadziej� �e da
 * to troch� przy�pieszenia. Metoda nie jest thread safe
 * @author Krzysztof
 *
 * @param <T>
 */
public class SimpleStorage<T> {
	private Object[]_value;
	private int[]_key;
	private int _maxSize;
	private int _counter;
	/**
	 * Rzeczywista liczba element�w w bloku, pole tylko przedstawia rzeczywisty rozmiar, jego zmiania nie zmienia rzeczywistej liczby element�w
	 */
	public int length;
	public SimpleStorage(int maxSize){
		_maxSize=maxSize;
		_counter=0;
		length=0;
		_value=new Object[_maxSize];
		_key=new int[_maxSize];
	}
	/**
	 * Metoda zwraca true je�li mamy ju� jak�� warto�� pod danym kluczem
	 * @param key
	 * @return
	 */
	public boolean containsKey(int key){
		if(getContainsKey(key)>-1){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Metoda zwraca pozycj� value pod danym kluczem lub -1 je�li nie ma takiego klucza
	 * @param key
	 * @return
	 */
	private int getContainsKey(int key){
		for(int i=0;i<_counter;i++){
			int v=_key[i];
			if(v==key){
				return i;
			}
		}
		return -1;
	}
	/**
	 * Metoda zast�puje warto�� pod danym kluczem
	 * @param key
	 * @param val
	 */
	private void replace(int key,T val){
		_value[key]=val;
	}
	/**
	 * metoda dodaje klucz i warto��
	 * @param key
	 * @param val
	 */
	public void add(int key,T val){
		int pos=getContainsKey(key);
		if(pos>-1){
			replace(pos,val);
		}else{
			_value[_counter]=val;
			_key[_counter]=key;
			_counter++;
		}
		length=_counter;
	}
	//public 
	/**
	 * Zwraca warto�� pod danym kluczem
	 * @param key
	 * @return
	 * @see getValByIndex
	 */
	public T get(int key){
		for(int i=0;i<_counter;i++){
			int v=_key[i];
			if(v==key){
				return (T)_value[i];
			}
		}
		return null;
	}
	/**
	 * Zwraca warto�� zapisan� pod danym indexem
	 * Np jak dodamy pary (10,<costam1>),(16,<costam2>),(29,<costam3>)
	 * To metoda get(10) zwr�ci <costam1>, metoda getValByIndex(10) zwr�ci null
	 * je�li damy get(1) to zwr�ci null, a metoda getValByIndex(1) zwr�ci costam2
	 * metoda get zwraca warto�� po kluczu, a metoda getValByIndex po indexie (zwraca w kolejno�ci dodania)
	 * Metoda getValByIndex jest znacznie wydajniejsza
	 * @param index
	 * @return
	 */
	public T getValByIndex(int index){
		if(index<this.length){
			return (T)_value[index];
		}else{
			return null;
		}
	}
	@Override
	public String toString(){
		StringBuilder string=new StringBuilder();
		string.append("SimpleStorage:{\n");
		for(int i=0;i<_counter;i++){
			string.append("key:"+_key[i]+" val:"+_value[i]+"\n");
		}
		string.append("}");
		return string.toString();
	}
}

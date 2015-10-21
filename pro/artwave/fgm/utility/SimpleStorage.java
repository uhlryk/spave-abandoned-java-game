package pro.artwave.fgm.utility;

/**
 * Klasa ma zast¹piæ HashMap u¿ywan¹ do przechowywania niepowtarzalnych definicji obiektów typu Tile 
 * gdzie ka¿da wartoœæ to jeden tile. Mechanizm hasmMap jest jak armata na komara. Mam nadziejê ¿e da
 * to trochê przyœpieszenia. Metoda nie jest thread safe
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
	 * Rzeczywista liczba elementów w bloku, pole tylko przedstawia rzeczywisty rozmiar, jego zmiania nie zmienia rzeczywistej liczby elementów
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
	 * Metoda zwraca true jeœli mamy ju¿ jak¹œ wartoœæ pod danym kluczem
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
	 * Metoda zwraca pozycjê value pod danym kluczem lub -1 jeœli nie ma takiego klucza
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
	 * Metoda zastêpuje wartoœæ pod danym kluczem
	 * @param key
	 * @param val
	 */
	private void replace(int key,T val){
		_value[key]=val;
	}
	/**
	 * metoda dodaje klucz i wartoœæ
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
	 * Zwraca wartoœæ pod danym kluczem
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
	 * Zwraca wartoœæ zapisan¹ pod danym indexem
	 * Np jak dodamy pary (10,<costam1>),(16,<costam2>),(29,<costam3>)
	 * To metoda get(10) zwróci <costam1>, metoda getValByIndex(10) zwróci null
	 * jeœli damy get(1) to zwróci null, a metoda getValByIndex(1) zwróci costam2
	 * metoda get zwraca wartoœæ po kluczu, a metoda getValByIndex po indexie (zwraca w kolejnoœci dodania)
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

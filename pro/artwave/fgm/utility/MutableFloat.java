package pro.artwave.fgm.utility;

/**
 * Wrapper na float daj�cy przewag� nad zwyk�ym Fload �e jest mutable bo zwyk�y jest immutable
 * Dodatkowo klasa jest prosta ma minimum metod niezbednych.
 * Nie jest to thread safe. Ale jako �e jest to aplikacja jednow�tkowa to nie stanowi to wady
 * @author Krzysztof
 *
 */
public class MutableFloat {
	private float _value;
	public MutableFloat(){
		_value=0;
	}	
	public MutableFloat(float value){
		_value=value;
	}
	public float getValue(){
		return _value;
	}
	public void setValue(float value){
		_value=value;
	}
	public void addValue(float value){
		_value+=value;
	}
}

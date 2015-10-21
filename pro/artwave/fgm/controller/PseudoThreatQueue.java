package pro.artwave.fgm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pro.artwave.fgm.utility.MutableFloat;

/**
 * Metoda kolejkuje zadania do wykonania
 * Zastosowanie gdy mamy w danej klatce du�o metod
 * Mo�emy je podzieli� na wywo�anie co kt�r�� klatk�
 * @author Krzysztof
 *
 */
public class PseudoThreatQueue {
	
	private int _period;
	/**
	 * konkretny frame wg kolejki
	 */
	private int _actSchedule;
	/**
	 * Mapa zawieraj�ca wszystkie delty dla ca�ego periodu
	 */
	//private Map<Integer,Float> _deltaMap;
	/**
	 * Mapa z wszystkimi fazami i list� ich u�ycia w schedule
	 */
	private Map<Phase,ArrayList<Integer>> _phaseMap;
	/**
	 * Przekalkulowana mapa _phaseMap tak by by�a szybciej wywo�ywana
	 */
	private Map<Integer,ArrayList<Phase>> _scheduleMap;
	/**
	 * Przeliczane delty jako odst�p mi�dzy wywo�aniem danej fazy
	 */
	private Map<Phase,MutableFloat> _phaseDeltaMap;
	
	private boolean _isFirstTime;
	public void init(int period){
		_isFirstTime=true;
		_phaseMap=new HashMap<Phase,ArrayList<Integer>>();
		_phaseDeltaMap=new HashMap<Phase,MutableFloat>();
		setPeriod(period);
	}
	/**
	 * Metoda okre�la co ile fram�w liczy
	 * @param period
	 */
	public void setPeriod(int period){
		_period=period;
		_actSchedule=0;
	//	_deltaMap=new HashMap<Integer,Float>(_period);
	}
	public void recalculateSchedule(){
		_scheduleMap=new HashMap<Integer,ArrayList<Phase>>(60);
		for(int i=0;i<_period;i++){
			ArrayList<Phase> a=new ArrayList<Phase>(3);
			_scheduleMap.put(i,a);
		}
		for(Entry<Phase, ArrayList<Integer>> entry:_phaseMap.entrySet()){
			Phase p=entry.getKey();
			ArrayList<Integer> a=entry.getValue();
			for(Integer i:a){
				_scheduleMap.get(i).add(p);
			}
		}	
	}
	/**
	 * Metod� wywo�ujemy cyklicznie
	 * ona wywo�uje funkcje w�tk�w po kolei i po osiagni�ciu perioda od nowa
	 * Zalet� jest �e pami�ta delte z ca�ego cyklu, je�li wi�c jest w�tek wywo�ywany co
	 * 10 cykli to ka�dy cykl b�dzie zna� delte
	 * @param delta
	 */
	public void calculate(float delta){
		setSchedule();
	//	_deltaMap.put(getActSchedule(), delta);
		for(Entry<Phase,MutableFloat> entry:_phaseDeltaMap.entrySet()){
			MutableFloat d=entry.getValue();
			d.addValue(delta);
		}
	//	System.out.println("Threat: frame "+getActSchedule()+" delta "+delta);
		if(_isFirstTime){//je�li jest to pierwsze wywo�anie to odpalamy wszystkie fazy by by�y zainicjowane
			for(Entry<Phase, ArrayList<Integer>> entry:_phaseMap.entrySet()){
				Phase p=entry.getKey();
				p.calculate();
			}
			_isFirstTime=false;
		}else{
			ArrayList<Phase> phaseList=_scheduleMap.get(getActSchedule());
			for(Phase p:phaseList){
				MutableFloat d=_phaseDeltaMap.get(p);
				p.setDelta(d.getValue());
				d.setValue(0);
				long mili=System.currentTimeMillis();
				p.calculate();
	//			System.out.println(p.getName()+" t:"+(System.currentTimeMillis()-mili));
			}
		}
		
	}	
	/**
	 * Metoda spowoduje odpalenie jednorazowo wszystkich akcji
	 * Zastosowanie przy teleporcie gdzie trzeba od�wie�y� wiele element�w
	 */
	public void setRunAll(){
		_isFirstTime=true;
	}
	/**
	 * pobieramy aktualny frame
	 * @return
	 */
	public int getActSchedule(){
		return _actSchedule;
	}
	private void setSchedule(){
		_actSchedule++;
		if(_actSchedule>=_period){
			_actSchedule=0;
		}
	}
	/**
	 * Dodajemy now� faz�, czyli anonimow� funkcj� zawieraj�c� okre�lone metody,
	 * nast�pnie okre�lamy kiedy ma by� wywo�ywana
	 * @param phase
	 * @return
	 */
	public Phase addPhase(Phase phase){
		ArrayList<Integer> phaseSchedule=new ArrayList<Integer>(60);
		_phaseMap.put(phase,phaseSchedule);
		_phaseDeltaMap.put(phase,new MutableFloat());
		return phase;
	}
	/**
	 * Dla okre�lonej fazy okre�lany frame na kt�rym ma si� ona wywo�ywa�
	 * @param phase
	 * @param schedule
	 */
	public void addSchedule(Phase phase,int schedule){
		_phaseMap.get(phase).add(schedule);
	}
	/**
	 * Okre�lamy �e dana faza b�dzie co klatk�
	 * @param phase
	 */
	public void addAllSchedule(Phase phase){
		ArrayList<Integer> phaseSchedule=_phaseMap.get(phase);
		for(int i=0;i<_period;i++){
			phaseSchedule.add(i);
		}
	}
	/**
	 * Okre�lamy �e dana faza b�dzie co ile� klatek wywo�ywana mo�emy okre�lic offset
	 * @param phase
	 * @param offset
	 * @param period
	 */
	public void addSchedule(Phase phase,int offset,int period){
		ArrayList<Integer> phaseSchedule=_phaseMap.get(phase);
		for(int i=offset;i<_period;i+=period){
			phaseSchedule.add(i);
		}
	}
	public abstract static class Phase{
		private String _name="";
		public void setName(String name){
			_name=name;
		}
		public String getName(){
			return _name;
		}
		private float _delta;
		private void setDelta(float delta){
			_delta=delta;
		}
		public float getDelta(){
			return _delta;
		}
		public abstract void calculate();
	}
}

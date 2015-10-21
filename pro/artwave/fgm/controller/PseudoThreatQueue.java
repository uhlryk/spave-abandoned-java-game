package pro.artwave.fgm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pro.artwave.fgm.utility.MutableFloat;

/**
 * Metoda kolejkuje zadania do wykonania
 * Zastosowanie gdy mamy w danej klatce du¿o metod
 * Mo¿emy je podzieliæ na wywo³anie co któr¹œ klatkê
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
	 * Mapa zawieraj¹ca wszystkie delty dla ca³ego periodu
	 */
	//private Map<Integer,Float> _deltaMap;
	/**
	 * Mapa z wszystkimi fazami i list¹ ich u¿ycia w schedule
	 */
	private Map<Phase,ArrayList<Integer>> _phaseMap;
	/**
	 * Przekalkulowana mapa _phaseMap tak by by³a szybciej wywo³ywana
	 */
	private Map<Integer,ArrayList<Phase>> _scheduleMap;
	/**
	 * Przeliczane delty jako odstêp miêdzy wywo³aniem danej fazy
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
	 * Metoda okreœla co ile framów liczy
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
	 * Metod¹ wywo³ujemy cyklicznie
	 * ona wywo³uje funkcje w¹tków po kolei i po osiagniêciu perioda od nowa
	 * Zalet¹ jest ¿e pamiêta delte z ca³ego cyklu, jeœli wiêc jest w¹tek wywo³ywany co
	 * 10 cykli to ka¿dy cykl bêdzie zna³ delte
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
		if(_isFirstTime){//jeœli jest to pierwsze wywo³anie to odpalamy wszystkie fazy by by³y zainicjowane
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
	 * Zastosowanie przy teleporcie gdzie trzeba odœwie¿yæ wiele elementów
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
	 * Dodajemy now¹ fazê, czyli anonimow¹ funkcjê zawieraj¹c¹ okreœlone metody,
	 * nastêpnie okreœlamy kiedy ma byæ wywo³ywana
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
	 * Dla okreœlonej fazy okreœlany frame na którym ma siê ona wywo³ywaæ
	 * @param phase
	 * @param schedule
	 */
	public void addSchedule(Phase phase,int schedule){
		_phaseMap.get(phase).add(schedule);
	}
	/**
	 * Okreœlamy ¿e dana faza bêdzie co klatkê
	 * @param phase
	 */
	public void addAllSchedule(Phase phase){
		ArrayList<Integer> phaseSchedule=_phaseMap.get(phase);
		for(int i=0;i<_period;i++){
			phaseSchedule.add(i);
		}
	}
	/**
	 * Okreœlamy ¿e dana faza bêdzie co ileœ klatek wywo³ywana mo¿emy okreœlic offset
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

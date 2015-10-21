package pro.artwave.spaceshooter.model;

public class CountScore {
	private int _score;
	private float _multiply;
	public void init(int multiply){
		_multiply=multiply;
	}
	public void addScoreRaw(int score){
		_score+=score;
	}	
	public void addScore(int score){
		_score+=score*_multiply;
	}
	public int getScore(){
		return _score;
	}
}

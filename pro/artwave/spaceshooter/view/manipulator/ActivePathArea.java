package pro.artwave.spaceshooter.view.manipulator;

import java.util.ArrayList;

import pro.artwave.fgm.utility.Setting;
import pro.artwave.spaceshooter.model.asset.GameAssetAtlas;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
/**
 * Odpowiada za sterowanie Statkiem 
 * Miejsce wciœniêcia tworzy scie¿kê do której statek pod¹¿a. Mo¿na okreœliæ wiele punktów. Wtedy
 * statek bêdzie przemieszcza³ siê do nastêpnych po kolei.
 * Kontroler równie¿ sprawdza k¹ty i optymalizuje sposób dotarcia do celu
 * 
 * UWAGA!!!
 * U³atwienia sterowania powinny byæ na poziomie statku a nie kontrolera!
 * 
 * Bazowy Actor okreœla obszar ekranu który jest klikanlny
 * Ma dodatkowo drugiego aktora DrawingArea który rysuje kursor wduszenia
 * @author Krzysztof
 *
 */
public class ActivePathArea extends Actor{
	/**
	 * Jeœli gracz ma celowaæ poprzez dragg to musi byæ true
	 * oznacza to jeœli przeci¹ggniemy kursorem po obszarze gdzie jest wróg to bêdzie zaznaczony
	 * do celowania.
	 * w przeciwnym razie trzeba wroga klikn¹æ
	 */
	public final static boolean CAN_AIM_ON_DRAGG=false;
	
	/**
	 * jeœli true to nie dzia³a autofire i gracz musi klikaæ w przeciwnika by strzelaæ
	 */
	public final static boolean IS_AUTOFIRE=false;
/**
 * Dok³adnoœæ kiedy uznawane jest ¿e nale¿y wyczyœciæ targety bo gracz klikn¹³ w pobli¿u statku
 */
	public final static int ACCURACY_CLEAR=40;
	/**
	 * Dok³adnoœæ w pikselach kiedy jest zaliczane ¿e statek dotar³ do punktu im wartoœæ wy¿sza
	 * tym mo¿e byæ dalej od punktu
	 */	
	public final static int ACCURACY_HIT=25;
	private float _positionX;
	private float _positionY;
	private int _offsetX;
	private int _offsetY;

	private Vector2 _referencePosition;
//	private float _referenceAngle;
	private DrawingArea _drawingArea;
	private ArrayList<Target> _targetList;
	private Sprite _targetImage;
	private Sprite _enemyImage;
	private boolean _isDown;
	private boolean _isDrag;
	private boolean _isAiming;
	
	private boolean _isTarget;
	/**
	 * Ustawiamy czy w danym momencie kursor jest na poziomie celowania w wroga
	 * W zestawieniu z klikniêciem w activePathArea mamy czy klikamy na wroga
	 * @param isTarget
	 */
	public void setIsTarget(boolean isTarget){
		this._isTarget=isTarget;
		if(this._isTarget==true&&_isClickBeforeEnemyCheck==true){
			//znaczy ¿e mieliœmy klikniêcie w wroga
			System.out.println("strza³!!");
			_isFireEnemy=true;
		}
		_isClickBeforeEnemyCheck=false;
	}
	/**
	 * Jeœli true to znaczy ¿e strzelaæ mamy w wroga
	 */
	private boolean _isFireEnemy;
	public boolean fireEnemy(){
		return _isFireEnemy;
	}
	public void resetFireEnemy(){
		_isFireEnemy=false;
	}
	/**
	 * sprawdzamy czy w danym momencie kursor jest na poziomie celowania w wroga
	 * W zestawieniu z klikniêciem w activePathArea mamy czy klikamy na wroga
	 * @param isTarget
	 */	
	public boolean isTarget(){
		return this._isTarget;
	}
	/**
	 * Czy by³o klikniêcie przed metod¹ sprawdzaj¹c¹ czy by³o klikniêcie na wroga
	 */
	public boolean _isClickBeforeEnemyCheck;
	/**
	 * Metoda która musi byæ wywo³ana za pierwszym razem
	 * @param offsetX offset przesuniêcia wzglêdem œrodka ekrany wynikaj¹cy z tego
	 * ¿e statek nie jest równo na œrodku
	 * @param offsetY offset przesuniêcia wzglêdem œrodka ekrany wynikaj¹cy z tego
	 * ¿e statek nie jest równo na œrodku
	 * @param limitPath maksymalnie ile œcie¿ek jest dostêpnych
	 */
	public void init(int offsetX,int offsetY,int limitPath){
	//	this.rotate(-45);
		_isFireEnemy=false;
		_isClickBeforeEnemyCheck=false;
		_isAiming=false;
		_isDrag=false;
		GameAssetAtlas gameAssetAtlas=new GameAssetAtlas();
		_targetImage=gameAssetAtlas.createSpritePathTarget();
		_enemyImage=gameAssetAtlas.createSpriteEnemyTarget();
		_isDown=false;
		_drawingArea=new DrawingArea();
		/**
		 * W u¿yciu normalnie tylko jeden wskaŸnik, drugi gdy pierwszy namierza wroga
		 */
		_targetList=new ArrayList<Target>(2);
		_positionX=0;
		_positionY=0;
		_referencePosition=new Vector2();
		this.setSize(Setting.getScreen().width,Setting.getScreen().height);
		_offsetX=(int) (this.getWidth()/2+offsetX);
		_offsetY=(int) (this.getHeight()/2+offsetY);;		
		this.addListener(new InputListener(){
			/**
			 * Poni¿sze metody s³u¿¹ do okreœlenia czy mamy tylko touchDown czy te¿ drag
			 * chodzi o to by zmniejszyæ jego czu³oœæ
			 */
			private float _x;
			private float _y;	
			private float _isDraggedDistance=30;
			
			/**
			 * Odpowiedzialne za podwójne klikniêcie
			 */
			private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
			private static final int DOUBLE_DISTANCE=30;
		    long lastClickTime = 0;		
		    
		    public boolean onSingleClick(InputEvent event, float x, float y, int pointer, int button){
		    	_x=x;
				_y=y;
				_isDrag=false;
				_isDown=true;
				int count=_targetList.size();
				_positionX=x-_offsetX;
				_positionY=y-_offsetY;
				
				/*	if(_positionX>-ACCURACY_CLEAR&&_positionX<ACCURACY_CLEAR&&_positionY>-ACCURACY_CLEAR&&_positionY<ACCURACY_CLEAR){
					_targetList.clear();
					_isAiming=false;
					return true;
				}*/
				Target target;
				if(count<1||(_isAiming==true&&count<2)){//mamy 0 targetów lub jeden target bêd¹cy celowaniem
					target=new Target();
					_targetList.add(target);

				}else{//mamy jeden target który nie jest celowaniem, lub dwa targety, gdzie pierwszy jest celowaniem
					target=_targetList.get(count-1);
				}
				target.position=new Vector2(_positionX+_referencePosition.x,_positionY+_referencePosition.y);
				
				
				/*
				 * Warunki ¿e celujemy w wroga:
				 * target 1 lub 2
				 * isAiming==true
				 * target 1 musi byæ równy aktualnemu x i y
				 */
		//		if(count==1&&isTarget()){
					
			//		target=_targetList.get(0);
			//		System.out.println("strzelamy do wroga");
		//		}
				return true;
		    }
		    public boolean onDoubleClick(InputEvent event, float x, float y, int pointer, int button){
		    	if(isTarget()==false){
		    		clearTargets();
		    	}
		    	return onSingleClick(event,x,y,pointer,button);
		    }   		    
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				boolean result;
				long clickTime = System.currentTimeMillis();
		        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA
		        		&&_x+DOUBLE_DISTANCE>x&&_x-DOUBLE_DISTANCE<x
		        		&&_y+DOUBLE_DISTANCE>y&&_y-DOUBLE_DISTANCE<y
		        ){
		        	System.out.println("double click");
		        	result=onDoubleClick(event,x,y,pointer,button);
		        } else {
		        	result=onSingleClick(event,x,y,pointer,button);
		        }
		        lastClickTime = clickTime;
		        return result;
			}
			
			@Override
			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				/**
				 * warunek zmniejsza czu³oœæ, dziêki czemu klikaj¹c na statku wroga system
				 * nie wykryje od razu drag a wiêc bêdzie tylko strzela³, by by³ drag trzeba
				 * zrobiæ trochê wiêksze przesuniêcie
				 */
				if(x-_x>_isDraggedDistance||x-_x<-_isDraggedDistance||y-_y>_isDraggedDistance||y-_y<-_isDraggedDistance){
					_isDrag=true;
					if(_targetList.size()>0){
						_positionX=x-_offsetX;
						_positionY=y-_offsetY;
						
						Target target;
						if(_targetList.size()==1&&_isAiming==true){
							/**
							 * UWAGA ISTOTNE JEŒLI MA DZIA£AC NA DRAGGED
							 * jak to wy³¹cze to nie ustawia targetu na celu przy dragget jeœli
							 * nie ma ¿adnego celu, jeœli jest jakis cel to normalnie dragged prze³¹czy
							 */
							if(CAN_AIM_ON_DRAGG==true){
								target=new Target();
								_targetList.add(target);
								target.position=new Vector2(_positionX+_referencePosition.x,_positionY+_referencePosition.y);
							}
						}else{
				//			System.out.println("a1");
							/**
							 * SPRAWDZIÆ CO TO ROBI!!!
							 */
							target=_targetList.get(_targetList.size()-1);
							target.position.x=_positionX+_referencePosition.x;
							target.position.y=_positionY+_referencePosition.y;
						}
			//			System.out.println("drag");
					}
				}
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				_isDown=false;
				_isClickBeforeEnemyCheck=true;
			//	System.out.println("up");
				_positionX=0;
				_positionY=0;	
				_isDrag=false;
			}			
		});
	}
	/**
	 * Przekazujemy pozycjê okreœlonego targetu, W tej wersji jest tylko jeden
	 * @param index
	 * @return
	 */
	public Vector2 getTarget(int index){
		if(_targetList.size()>index){
			return _targetList.get(index).position;
		}else{
			return null;
		}
	}
	/**
	 * Pozycja pierwszego a w tej grze jedynego targetu
	 * @return
	 */
	public Vector2 getFirstTarget(){
		return getTarget(0);
	}
	/**
	 * Pozycja drugiego a w tej grze jedynego targetu
	 * @return
	 */
	public Vector2 getSecondTarget(){
		return getTarget(1);
	}	
	/**
	 * Mo¿emy zmieniæ dowolny istniej¹cy target
	 * @param index który target
	 * @param newPos nowa pozycja
	 */
	public void setTarget(int index,Vector2 newPos){
		Vector2 oldPos=getTarget(index);
		if(oldPos!=null){
			oldPos.x=newPos.x;
			oldPos.y=newPos.y;
		}
	}
	/**
	 * Mo¿emy zmieniæ pierwszy istniej¹cy target
	 * @param newPos nowa pozycja
	 */	
	public void setFirstTarget(Vector2 newPos){
		setTarget(0,newPos);
	}	
	/**
	 * Jeœli mamy tryb przesuwania paluchem po ekranie. mo¿e byæ bardziej trafne od _isDown
	 * @return
	 */
	public boolean isDragging(){
		return _isDrag;
	}
	/**
	 * Okreœlamy w kontrolerze pozycjê statku
	 * @param position
	 * @param angle
	 */
	public void setReferencePosition(Vector2 position){
		_referencePosition=position;
		if(_targetList.size()>0&&_isDown==true){
			Target target=_targetList.get(_targetList.size()-1);
			target.position.x=_positionX+_referencePosition.x;
			target.position.y=_positionY+_referencePosition.y;
		}
	}
	public Actor getDrawingArea(){
		return _drawingArea;
	}
	/**
	 * Zwraca iloœæ targetów kiedyœ true jeœli 2 w przeciwnym razie false
	 * Oznacza to ¿e wskazaliœmy 2 cele, pierwszy wróg a drugi ruch
	 */
	public int getTargetSize(){
		return _targetList.size();
	}
	public void clearTargets(){

		_targetList.clear();
	}
	public void clearPosition(){
		clearFirstTarget();
	}
	public void clearFirstTarget(){

		if(_targetList.size()>1||(_isDown==false&&_targetList.size()>0)){
			_targetList.remove(0);
		}
	}	
	public void clearSecondTarget(){

		if(_targetList.size()==2&&_isDown==false){
			_targetList.remove(1);
		}
	}		
	/**
	 * Metoda odpala siê w EnemyPool w momencie kiedy system wykryje ¿e user celuje w statek wroga
	 * EnemyPool.calculateCursorPosition()
	 * Jeœli znajdzie w danym miejscu wroga to isEnemy true
	 * W przeciwnym razie false;
	 * Metoda sprawdza czy w poprzednim odpaleniu EnemyPool.calculateCursorPosition()
	 * by³ dzia³aj¹cy cel a teraz nie. Jeœli w poprzedniej turze by³ cel a teraz go nie ma
	 * to metoda wy³¹cza zmienn¹ _blocade która odpowiada za blokadê i usuwa wszystkie targety
	 * chyba ¿e mamy wduszony przucisk celowania wtedy pierwszy target zamieni siê w ruch.
	 * @param isEnemy
	 */
	public void setIsBlocadeEnemy(boolean isEnemy){
		if(isEnemy){
		//	if(_isAiming==false){//niecelowaliœmy i teraz pojawi³ siê pierwszy cel
		//		System.out.println("pierwszy strza³");
		//		_isFireEnemy=true;
		//	}
			_isAiming=true;
		}
		else{ 
			if(_isAiming==true){//znaczy ¿e wczeœniej statek celowa³ w przeciwnika a teraz ju¿ nie
				clearFirstTarget();//czyœcimy wiêc target by statek siê nie uda³ w to miejsce, bo miejsce to by³o celem strza³ów
				clearFirstTarget();//jeœli by³ dwa targety to usuwamy oba
			}
			_isAiming=false;
		}
	}
	/**
	 * Metoda sprawdza czy w danej chwili celujemy
	 * jeœli true to celujemy
	 * mo¿na to sprawdziæ iloœci¹ targetów. Jeœli s¹ 2 to celujemy
	 * @return
	 */
	public boolean isAiming(){
		//System.out.println("ActivePathArea _isAiming: "+_isAiming);
		return _isAiming;
	}
	public class Target{
		public Vector2 position;
	}
	private class DrawingArea extends Actor{
		public void draw(SpriteBatch batch,float parentAlpha){
			int count=0;
			for(Target target:_targetList){
	//			if(_isDown==true&&count==_targetList.size()-1)break;;
				if(count==1||_isAiming==false){
					/**
					 * sprawdzam dystans miêdzy targetem ruchu a statkiem, jeœli jest mniejszy od 50, oznacza
					 * ¿e statek w praktyce dotar³ do celu, nie wyœwietlamy wtedy kursora
					 */
					float dist=target.position.dst(_referencePosition);
					if(dist>50){
						_targetImage.setPosition(this.getX()+target.position.x-_targetImage.getWidth()/2,this.getY()+target.position.y-_targetImage.getHeight()/2);
						_targetImage.draw(batch);
					}
				}else{
					_enemyImage.setPosition(this.getX()+target.position.x-_enemyImage.getWidth()/2,this.getY()+target.position.y-_enemyImage.getHeight()/2);
					_enemyImage.draw(batch);
				}
				count++;
			}
		}
	}
}

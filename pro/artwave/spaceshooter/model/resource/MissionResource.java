package pro.artwave.spaceshooter.model.resource;

import java.util.ArrayList;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import pro.artwave.fgm.model.resource.Resource;

/**
 * Klasa zaczytuje zasoby xml dotycz¹ce konkretnej misji
 * Otrzymujemy informacje o opisie obiektów na planszy
 * Otrzymujemy zadania i dialogi
 * Umownie wszystkie id s¹ id wyra¿onymi w kodzie szesnastkowym
 * Spowodowane to jest tym ¿e na planszy id jest to jedna z sk³adowych koloru który wyra¿ony jest 0xffffffff
 * @author Krzysztof
 *
 */
public class MissionResource extends Resource {
	private ArrayList<Decoration> _decorationList;
	private ArrayList<Task> _taskList;
	private ArrayList<Dialog> _dialogList;
	private ArrayList<Tile> _tileList;
	private ArrayList<Enemy> _enemyList;
	private ArrayList<Bonus> _bonusList;
	@Override
	public String getResourceName() {
		return "map/m"+_missionId+".xml";
	}
	private int _missionId;
	/**
	 * Bitmapa ma id w hexadecymalnym
	 * a w xml nale¿y u¿yæ przeliczonych dziesiêtnych
	 * czyli jeœli kolor id jest 10 to znaczy ¿e w xml musimy u¿ywaæ 16
	 * @param missionId
	 */
	public void init(int missionId){
		_missionId=missionId;
		super.init();
		_decorationList=new ArrayList<Decoration>(100);
		Array<XmlReader.Element> xmlDecorationList=this.getXmlObject().getChildrenByName("decoration");
		for(XmlReader.Element xmlDecoration:xmlDecorationList){
			Decoration decoration=new Decoration();
			decoration.id=parse(xmlDecoration.get("id","0"));;
			decoration.visible=xmlDecoration.getInt("visible",1);
			decoration.rotation=xmlDecoration.getInt("rotation",0);
			decoration.image=xmlDecoration.getIntAttribute("image");
			_decorationList.add(decoration);
		}
		_dialogList=new ArrayList<Dialog>(20);
		Array<XmlReader.Element> xmlDialogList=this.getXmlObject().getChildrenByName("dialog");
		for(XmlReader.Element xmlDialog:xmlDialogList){
			Dialog dialog=new Dialog();
			dialog.id=parse(xmlDialog.get("id","0"));;
			dialog.message=xmlDialog.getAttribute("message");
			dialog.buttonLeft=xmlDialog.getAttribute("left","S_LEFT");
			dialog.buttonRight=xmlDialog.getAttribute("right","S_RIGHT");
			dialog.buttonCenter=xmlDialog.getAttribute("center","S_CENTER");
			dialog.imageId=xmlDialog.getIntAttribute("image",0);
			_dialogList.add(dialog);
		}
		_tileList=new ArrayList<Tile>(20);
		Array<XmlReader.Element> xmlTileList=this.getXmlObject().getChildrenByName("tile");
		for(XmlReader.Element xmlTile:xmlTileList){
			Tile tile=new Tile();
			tile.id=parse(xmlTile.get("id","0"));
			tile.type=xmlTile.getAttribute("type");
			tile.shading=xmlTile.getIntAttribute("shading");
			tile.image=xmlTile.getIntAttribute("image");
			_tileList.add(tile);
		}	
		_bonusList=new ArrayList<Bonus>(20);
		Array<XmlReader.Element> xmlBonusList=this.getXmlObject().getChildrenByName("bonus");
		for(XmlReader.Element xmlBonus:xmlBonusList){
			Bonus bonus=new Bonus();
			bonus.id=parse(xmlBonus.get("id","0"));
			bonus.respawn=xmlBonus.getIntAttribute("respawn",0);
			Array<XmlReader.Element> xmlTypeList=xmlBonus.getChildrenByName("idtype");
			for(XmlReader.Element xmlType:xmlTypeList){
				Bonus.IdType type=new Bonus.IdType();
				type.id=xmlType.getIntAttribute("id");
				type.priority=xmlType.getIntAttribute("priority",1);
				bonus.idTypeList.add(type);
			}
			_bonusList.add(bonus);
		}		
		_enemyList=new ArrayList<Enemy>(20);
		Array<XmlReader.Element> xmlShipList=this.getXmlObject().getChildrenByName("enemy");
		for(XmlReader.Element xmlEnemy:xmlShipList){
			Enemy enemy=new Enemy();
			enemy.id=parse(xmlEnemy.get("id","0"));
			enemy.warGroup=xmlEnemy.getIntAttribute("wargroup",2);
			enemy.type=xmlEnemy.getAttribute("type");
			enemy.respawn=xmlEnemy.getIntAttribute("respawn",0);
			enemy.rotation=xmlEnemy.getIntAttribute("rotation",0);
			enemy.resistance=(xmlEnemy.getIntAttribute("resistance",0)==1?true:false);
			enemy.nerf=xmlEnemy.getIntAttribute("nerf",50);
			Array<XmlReader.Element> xmlTypeList=xmlEnemy.getChildrenByName("idtype");
			for(XmlReader.Element xmlType:xmlTypeList){
					Enemy.IdType type=new Enemy.IdType();
					type.id=xmlType.getIntAttribute("id");
					type.priority=xmlType.getIntAttribute("priority",1);
					type.weaponId=xmlType.getIntAttribute("weaponId",1);
					enemy.idTypeList.add(type);
			}
			_enemyList.add(enemy);
		}		
		_taskList=new ArrayList<Task>(20);
		Array<XmlReader.Element> xmlTaskList=this.getXmlObject().getChildrenByName("task");
		for(XmlReader.Element xmlTask:xmlTaskList){
			Task task=new Task();
			task.id=parse(xmlTask.getAttribute("id"));

			XmlReader.Element objectiveElement=xmlTask.getChildByName("objectives");
			if(objectiveElement!=null){
				Array<XmlReader.Element> xmlObjectiveList=objectiveElement.getChildrenByName("objective");
				for(XmlReader.Element xmlObjective:xmlObjectiveList){
					Task.Element objective=new Task.Element();
					Task.populateElement(objective, xmlObjective);
					task.objectiveList.add(objective);
				}
			}
			XmlReader.Element groupElement=xmlTask.getChildByName("onStart");
			if(groupElement!=null){
				Array<XmlReader.Element> xmlActionList=groupElement.getChildrenByName("action");
				for(XmlReader.Element xmlAction:xmlActionList){
					Task.Element action=new Task.Element();
					Task.populateElement(action, xmlAction);
					task.onStartList.add(action);
				}	
			}
			groupElement=xmlTask.getChildByName("onSuccess");
			if(groupElement!=null){			
				Array<XmlReader.Element> xmlActionList=groupElement.getChildrenByName("action");
				for(XmlReader.Element xmlAction:xmlActionList){
					Task.Element action=new Task.Element();
					Task.populateElement(action, xmlAction);
					task.onSuccessList.add(action);
				}	
			}
			groupElement=xmlTask.getChildByName("onFailure");
			if(groupElement!=null){				
				Array<XmlReader.Element> xmlActionList=groupElement.getChildrenByName("action");
				for(XmlReader.Element xmlAction:xmlActionList){
					Task.Element action=new Task.Element();
					Task.populateElement(action, xmlAction);
					task.onFailureList.add(action);
				}					
			}
			_taskList.add(task);
		}		
		
	}
	public static int parse(String text){
		if(text.startsWith("0x")){
			return (int)Long.parseLong(text.substring(2),16);
		}else{
			return Integer.parseInt(text);
		}
	}
	public static class Decoration{
		public int id;
		public int image;
		public int visible;
		public int rotation;
	}
	public ArrayList<Decoration> getDecorationList(){
		return _decorationList;
	}	
	public static class Task{
		public int id;
		public ArrayList<Element> objectiveList;
		public ArrayList<Element> onStartList;
		public ArrayList<Element> onSuccessList;
		public ArrayList<Element> onFailureList;
		
		public static class Element{

			public String type;
			public int val1;
			public int val2;
			public int val3;
			@Override
			public String toString(){
				return "Element type: "+type+" val1: "+val1+" val2: "+val2+" val3: "+val3;
			}
		}
		public Task(){
			objectiveList=new ArrayList<Element>(5);
			onStartList=new ArrayList<Element>(5);
			onSuccessList=new ArrayList<Element>(5);
			onFailureList=new ArrayList<Element>(5);
		}
		public static void populateElement(Task.Element element,XmlReader.Element xmlElement){
			element.type=xmlElement.getAttribute("type");
			element.val1=parse(xmlElement.get("val1","0"));
			element.val2=parse(xmlElement.get("val2","0"));
			element.val3=parse(xmlElement.get("val3","0"));
		}
		@Override
		public String toString(){
			StringBuilder stringBuilder=new StringBuilder();
			stringBuilder.append("MissionResource.Task "+id+"\n");
			stringBuilder.append("objectiveList: \n");
			int count=1;
			for(Element element:objectiveList){
				stringBuilder.append(count+") "+element+"  \n");
				count++;
			}
			stringBuilder.append("onStartList: \n");
			count=1;
			for(Element element:onStartList){
				stringBuilder.append(count+") "+element+"  \n");
				count++;
			}	
			stringBuilder.append("onSuccessList: \n");
			count=1;
			for(Element element:onSuccessList){
				stringBuilder.append(count+") "+element+"  \n");
				count++;
			}
			stringBuilder.append("onFailureList: \n");
			count=1;
			for(Element element:onFailureList){
				stringBuilder.append(count+") "+element+"  \n");
				count++;
			}				
			return stringBuilder.toString();
		}
	}
	public ArrayList<Task> getTaskList(){
		return _taskList;
	}
	public static class Dialog{
		public int id;
		public String message;
		public String buttonLeft;
		public String buttonRight;
		public String buttonCenter;
		public int imageId;
		@Override
		public String toString(){
			return "Dialog id: "+id+" message: "+message+" left: "+buttonLeft+" right: "+buttonRight+" center"+buttonCenter+" imageId"+imageId;
		}		
	}
	public ArrayList<Dialog> getDialogList(){
		return _dialogList;
	}	
	public static class Tile{
		public int id;//wyra¿ony hexadecymalnie
		public String type;
		public int shading;
		public int image;
		@Override
		public String toString(){
			return "Tile id: "+id+" type: "+type+" shading: "+shading+" image: "+image;
		}		
	}
	public ArrayList<Tile> getTileList(){
		return _tileList;
	}		
	public static class Enemy{
		public ArrayList<Element> scriptList; //lista zapêtlonych komend typu goto
		public ArrayList<IdType> idTypeList;//lista id typów obiektów
		public String type;//jaki typ mamy, czyli statek czy wie¿yczka, czy blokada 1 wie¿yczka; 2 blokada, 3 statek
		public int id;//wyra¿ony heksadecymalnie
		public int warGroup;//strona konfliktu, zwykle mamy dwie gracz i wrogowie 2 to wrogowie
		public int respawn;//jeœli wiecej ni¿ 0 to po tylu sekundach nast¹pi respawn
		public int nerf;//okreœla jak silny jest statek w porównaniu z analogicznym statkiem gracza 10 - oznacza 10% si³y gracza; 100% tak samo silny
		public boolean resistance;//mo¿na sprawiæ by dana grupa by³a nieœmiertelna
		public int rotation;//okreœlamy k¹t bazowy. Czasem mo¿e byæ istotne poniewa¿ mog¹ byæ nieruchome dzia³ka.
		@Override
		public String toString(){
			StringBuilder stringBuilder=new StringBuilder();
			stringBuilder.append("Ship id: "+id+" warGroup: "+warGroup+" respawn: "+respawn+" nerf: "+nerf);
			int count=1;
			for(Element element:scriptList){
				stringBuilder.append(count+") "+element+"  \n");
				count++;
			}
			return stringBuilder.toString();
		}	
		public static class IdType{
			public int id;
			public int priority;
			public int weaponId;
		}
		/**
		 * nA razie nie u¿ywane, docelowo ma byæ to lista komenda dla statku wywo³ywane cyklicznie
		 * @author Krzysztof
		 *
		 */
		public static class Element{
			public String type;
			public int val1;
			public int val2;
			public int val3;
			@Override
			public String toString(){
				return "Element type: "+type+" val1: "+val1+" val2: "+val2+" val3: "+val3;
			}
		}	
		public Enemy(){
			scriptList=new ArrayList<Element>(5);
			idTypeList=new ArrayList<IdType>(5);
		}
	}
	public ArrayList<Enemy> getEnemyList(){
		return _enemyList;
	}	
	public static class Bonus{
		public int id;//wyra¿ony heksadecymalnie
		public int respawn;//jeœli wiecej ni¿ 0 to po tylu sekundach nast¹pi respawn
		public ArrayList<IdType> idTypeList;
		public static class IdType{
			public int id;
			public int priority;
		}	
		@Override
		public String toString(){
			StringBuilder stringBuilder=new StringBuilder();
			stringBuilder.append("Bonus id:"+id+" respawn:"+respawn);
			return stringBuilder.toString();
		}	
		public Bonus(){
			idTypeList=new ArrayList<IdType>(5);
		}		
	}
	public ArrayList<Bonus> getBonusList(){
		return _bonusList;
	}	
}

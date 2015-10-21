package pro.artwave.spaceshooter.controller.root.game;

import java.util.ArrayList;

import pro.artwave.spaceshooter.model.resource.MissionResource;

/**
 * Klasa obs³uguje dialogi z zasobów misji, nanosi t³umaczenia
 * @author Krzysztof
 *
 */
public class DialogManager {
	private ArrayList<MissionResource.Dialog> _gameDialogList;
	private MissionResource.Dialog _lastDialog;
	private int _lastDialogId;
	public void init(ArrayList<MissionResource.Dialog> gameDialogList){
		_gameDialogList=gameDialogList;
		_lastDialogId=0;
	}
	/**
	 * metoda dostarcza do innych metod dialog. Zapisuje ostatni dialog i jeœli inne metody chc¹ go równie¿ to otrzymuj¹
	 * zapisany a nie brany ponownie z listy - taki cache
	 * @param dialogId
	 * @return
	 */
	private MissionResource.Dialog getDialog(int dialogId){
		if(_lastDialogId==dialogId){
			MissionResource.Dialog dialog=_lastDialog;
			return dialog;
		}else{
			for(MissionResource.Dialog d:_gameDialogList){
				if(d.id==dialogId){
					_lastDialogId=dialogId;
					_lastDialog=d;
					return d;
			//		System.out.println("getDialog id:"+dialogId+_gameDialogList);
				}
			}
		}
		return null;
	}
	/**
	 * Metoda zwraca treœæ danego dialogu na podstawie jego id
	 * @param dialogId
	 * @return
	 */
	public String getMessage(int dialogId){
		MissionResource.Dialog dialog=getDialog(dialogId);
		return dialog.message;
	}
	/**
	 * Zwraca treœæ przycisku left
	 * @param dialogId
	 * @return
	 */
	public String getButtonLeft(int dialogId){
		MissionResource.Dialog dialog=getDialog(dialogId);
		return dialog.buttonLeft;
	}	
	/**
	 * Zwraca treœæ przycisku right
	 * @param dialogId
	 * @return
	 */
	public String getButtonRight(int dialogId){
		MissionResource.Dialog dialog=getDialog(dialogId);
		return dialog.buttonRight;
	}	
	/**
	 * Metoda zwraca grafikê przypisan¹ do dialogu. jesli nie ma grafiki to zwraca 0
	 * Jeœli jest to zwraca jej identyfikator który ma odzwierciedlenie w nazwie grafiki
	 * @param dialogId
	 * @return
	 */
	public int getImageId(int dialogId){
		MissionResource.Dialog dialog=getDialog(dialogId);
		return dialog.imageId;
	}
	/**
	 * Zwraca treœæ przycisku right
	 * @param dialogId
	 * @return
	 */
	public String getButtonCenter(int dialogId){
		MissionResource.Dialog dialog=getDialog(dialogId);
		return dialog.buttonCenter;
	}		
	@Override
	public String toString(){
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("Dialoglist\n");
		for(MissionResource.Dialog dialog:_gameDialogList){
			stringBuilder.append(dialog+"\n");
		}
		return stringBuilder.toString();
	}
}

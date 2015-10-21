package pro.artwave.spaceshooter.model.resource;

import pro.artwave.fgm.model.resource.Translator;

public class GameTranslation extends Translator {

	@Override
	public String getResourceName() {
		return "translation/game/";
	}
	public String getEndPopupTitle(String titleId){
		return this._("popupEnd","title_"+titleId);
	}
	public String getEndPopupMessage(String messageId){
		return this._("popupEnd","message_"+messageId);
	}	
	public String getEndPopupButton(String buttonId){
		return this._("popupEnd","button_"+buttonId);
	}		
	public String getStoryPopupButton(String buttonId){
		return this._("popupStory","button_"+buttonId);
	}	
	public String getStoryMessage(String messageId){
		return this._("story",messageId);
	}	
	public String getBarTitle(String messageId){
		return this._("barGame","title_"+messageId);
	}		
}

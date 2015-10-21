package pro.artwave.spaceshooter.model.resource;

import pro.artwave.fgm.model.resource.Translator;

public class SelectGameTranslation extends Translator {

	@Override
	public String getResourceName() {
		return "translation/selectgame/";
	}
/*	public String getCampaignName(int campaignId){
		return this.translate("campaignName"+campaignId);
	}
	public String getCampaignDetails(int campaignId){
		return this.translate("campaignDetails"+campaignId);
	}*/
	public String getMissionName(int missionId){
		return this.translate("missionName"+missionId);
	}
	public String getMissionDetails(int missionId){
		return this.translate("missionDetails"+missionId);
	}	
}

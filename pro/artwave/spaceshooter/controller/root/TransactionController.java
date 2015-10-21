package pro.artwave.spaceshooter.controller.root;

import com.badlogic.gdx.Input;

import pro.artwave.fgm.controller.Controller;
import pro.artwave.fgm.controller.intent.Intent;
import pro.artwave.fgm.view.helper.Button;
import pro.artwave.spaceshooter.controller.RootController;
import pro.artwave.spaceshooter.controller.root.main.SelectEquipmentController;
import pro.artwave.spaceshooter.model.asset.BigOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.MediumOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.SelectAssetAtlas;
import pro.artwave.spaceshooter.model.asset.SmallOutlineAssetBitmapFont;
import pro.artwave.spaceshooter.model.asset.XSmallAssetBitmapFont;
import pro.artwave.spaceshooter.view.Loader;
import pro.artwave.spaceshooter.view.root.TransactionView;

public class TransactionController extends Controller {
	public final static int TRANS_TYPE_NOW_SHIP=1;
	public final static int TRANS_TYPE_NOW_WEAPON=2;
	public final static int TRANS_TYPE_NOW_CAMPAIGN=3;
	public final static int TRANS_TYPE_NOW_MISSION=4;
	public final static int TRANS_TYPE_UNLOCK_CAMPAIGN=5;
	public final static int TRANS_TYPE_BONUS=6;
	
	public final static int BACKLINK_SELECT_SHIP=1;
	public final static int BACKLINK_SELECT_WEAPON_I=2;
	public final static int BACKLINK_SELECT_WEAPON_II=3;
	public final static int BACKLINK_SELECT_CAMPAIGN=4;
	public final static int BACKLINK_SELECT_MISSION=5;
	
	private BigOutlineAssetBitmapFont _BigOutlineAssetBitmapFont;	
	private MediumOutlineAssetBitmapFont _mediumOutlineAssetBitmapFont;	
	private SmallOutlineAssetBitmapFont _smallOutlineAssetBitmapFont;		
	private XSmallAssetBitmapFont _xSmallOutlineAssetBitmapFont;		
	private SelectAssetAtlas _selectAssetAtlas;
	
	private TransactionView _view;
	@Override
	public void onInit() {}

	@Override
	public void onLoad() {
		_selectAssetAtlas=new SelectAssetAtlas();
		this.addModelToControl(_selectAssetAtlas);	
		_BigOutlineAssetBitmapFont=new BigOutlineAssetBitmapFont();
		this.addModelToControl(_BigOutlineAssetBitmapFont);	
		_mediumOutlineAssetBitmapFont=new MediumOutlineAssetBitmapFont();
		this.addModelToControl(_mediumOutlineAssetBitmapFont);	
		_smallOutlineAssetBitmapFont=new SmallOutlineAssetBitmapFont();
		this.addModelToControl(_smallOutlineAssetBitmapFont);			
		
		_xSmallOutlineAssetBitmapFont=new XSmallAssetBitmapFont();
		this.addModelToControl(_xSmallOutlineAssetBitmapFont);			
		this.setLoader(new Loader());
	}

	@Override
	public void onPrepare() {
		_view=new TransactionView();
		_view.init();
		this.setView(_view);
		this.getParent().getParent().getView().addContent(_view);
		_view.onClick(TransactionView.BUTTON_EXIT,new Button.ClickListener() {	
			@Override
			public void onClick() {
				goBackLink();
			}
		});		
		System.out.println("TRANSACTION CONTROLLER");
	}
	@Override
	public boolean onKeyUp(int keycode){
		if(keycode==Input.Keys.ESCAPE||Input.Keys.BACK==keycode){
			goBackLink();
			return true;
		}
		return false;
	}	
	private void goBackLink(){
		Intent intent;
		Intent subIntent;
		Intent subsubIntent;
		Integer backlink=this.getIntent().getIntegerValue("back_link");
		System.out.println("backlink"+backlink);
		switch(backlink){
		case BACKLINK_SELECT_SHIP:
			subsubIntent=new Intent(SelectEquipmentController.SHIP_CONTROLLER);
			subIntent=new Intent(MainController.SELECT_EQUIPMENT_CONTROLLER);
			subIntent.setChildIntent(subsubIntent);
			intent=new Intent(RootController.MAIN_CONTROLLER);
			intent.setChildIntent(subIntent);
			getParent().setDispatch(intent);
			break;
		case BACKLINK_SELECT_WEAPON_I:
			subsubIntent=new Intent(SelectEquipmentController.WEAPON_I_CONTROLLER);
			subsubIntent.addValue("debugName","FromTransactionToWeaponI");
			subsubIntent.addValue("slotId",1);
			subIntent=new Intent(MainController.SELECT_EQUIPMENT_CONTROLLER);
			subIntent.addValue("debugName","FromTransactionToEquipment");
			subIntent.setChildIntent(subsubIntent);
			intent=new Intent(RootController.MAIN_CONTROLLER);
			intent.setChildIntent(subIntent);
			getParent().setDispatch(intent);
			break;
		case BACKLINK_SELECT_WEAPON_II:
			subsubIntent=new Intent(SelectEquipmentController.WEAPON_II_CONTROLLER);
			subsubIntent.addValue("slotId",2);
			subIntent=new Intent(MainController.SELECT_EQUIPMENT_CONTROLLER);
			subIntent.setChildIntent(subsubIntent);
			intent=new Intent(RootController.MAIN_CONTROLLER);
			intent.setChildIntent(subIntent);
			getParent().setDispatch(intent);
			break;			
		case BACKLINK_SELECT_MISSION:
			subIntent=new Intent(MainController.SELECT_MISSION_CONTROLLER);
			int campaignId=this.getIntent().getIntegerValue("campaignId",1);
			subIntent.addValue("campaignId",campaignId);
			subIntent.addValue("debugName","FromTransactionToSelectMission c"+campaignId);
			intent=new Intent(RootController.MAIN_CONTROLLER);
			intent.setChildIntent(subIntent);
			intent.addValue("debugName","FromTransactionToMain");
			getParent().setDispatch(intent);
			break;			
		}		
	}
}

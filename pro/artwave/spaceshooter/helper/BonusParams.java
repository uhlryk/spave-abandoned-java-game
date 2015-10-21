package pro.artwave.spaceshooter.helper;

import java.util.ArrayList;

public class BonusParams {
	public int id;
	public String text;
	public ArrayList<Effect> effectList;	
	public BonusParams(){
		effectList=new ArrayList<Effect>(5);	
	}
	public static class Effect{
		public String type;
		public int val1;
		public int val2;
		public int val3;
	}
}

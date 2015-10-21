package pro.artwave.fgm.utility;

public class Setting {
	private static Size stageSize;
	public static void setStage(float width,float height){
		stageSize=new Size();
		stageSize.width=width;
		stageSize.height=height;
	}
	public static Size getStage(){
		return stageSize;
	}
	private static Size screenSize;
	public static void setScreen(float width,float height){
		screenSize=new Size();
		screenSize.width=width;
		screenSize.height=height;
	}
	public static Size getScreen(){
		return screenSize;
	}	
	private static Size gutterSize;
	public static void setGutter(float width,float height){
		gutterSize=new Size();
		gutterSize.width=width;
		gutterSize.height=height;
	}
	public static Size getGutter(){
		return gutterSize;
	}		
	public static class Size{
		public float width;
		public float height;
	}
	public static String toStaticString(){
		return "Setting: Stage("+stageSize.width+","+stageSize.height+")," +
				"Screen("+screenSize.width+","+screenSize.height+")," +
				"Gutter("+gutterSize.width+","+gutterSize.height+")";
	}
}

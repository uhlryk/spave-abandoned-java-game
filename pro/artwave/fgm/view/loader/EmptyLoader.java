package pro.artwave.fgm.view.loader;

public class EmptyLoader extends AbstractLoader {

	@Override
	public void init() {
	//	System.out.println("EmptyLoader::init()");

	}

	@Override
	public void showLoop(float delta) {
	//	System.out.println("EmptyLoader::showLoop()");

	}

	@Override
	public boolean hideLoop(float delta) {
	//	System.out.println("EmptyLoader::hideLoop()");
		return true;
	}

	@Override
	public void showProgress(float progress) {
	//	System.out.println("EmptyLoader::showProgress("+progress+")");

	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

}

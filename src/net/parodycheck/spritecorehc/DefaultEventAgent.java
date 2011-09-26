package net.parodycheck.spritecorehc;
import android.view.InputEvent;

public class DefaultEventAgent implements SpriteCoreEventAgent
{
    private static DefaultEventAgent _inst;
    public void handleEvent(InputEvent e)
    {

    }

    public static DefaultEventAgent instance()
    {
	if(_inst == null)
	    {
		_inst = new DefaultEventAgent();
	    }
	return _inst;
    }

}
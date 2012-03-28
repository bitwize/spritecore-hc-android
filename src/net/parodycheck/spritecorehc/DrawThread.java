package net.parodycheck.spritecorehc;
import java.lang.*;
import android.graphics.Canvas;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.util.Log;

class DrawThread extends Thread
{
    private SurfaceView _mysurfv;
    private DrawAgent _myagent;
    private boolean _running;
    private boolean _appRunning;
    public DrawThread(SurfaceView surfv,DrawAgent a)
    {
	_mysurfv = surfv;
	_myagent = a;
	_running = false;
	_appRunning = true;
    }
    public void run()
    {
	while(_running)
	    {
		try
		    {
			synchronized(_myagent)
			    {
				if(_appRunning) {
				    _myagent.step();
				    Canvas c = null;
				    try {
					c = _mysurfv.getHolder().lockCanvas(null);
					if(c != null) {
					    _myagent.drawOn(c);
					}
				    }
				    catch(Exception e)
					{
					    Log.e("DrawThread",e.getMessage());
					}
				    finally {
					if(c != null)
					    {
						_mysurfv.getHolder().unlockCanvasAndPost(c);
					    }
				    }
				}
			    }
		    }
		catch(Exception e)
		    {
		    }
		
		try {
		    Thread.sleep(20);
		}
		catch(InterruptedException e)
		    {}
	    }
    }
    
    public void setRunning(boolean r)
    {
	synchronized(this) {_running = r;}
    }
    void resumeDrawing()
    {
	synchronized(_myagent) {_appRunning = true;}
    }
    void pauseDrawing()
    {
	synchronized(_myagent) {_appRunning = false;}
    }

    public static class DrawTaskCallback implements android.view.SurfaceHolder.Callback
    {
	private DrawAgent _myagent;
	private SurfaceView _myview;
	private DrawThread _thr;
	public DrawTaskCallback(SurfaceView v,DrawAgent a)
	{
	    _myview = v;
	    _myagent = a;
	    _thr = null;
	}
	public void surfaceCreated(android.view.SurfaceHolder h)
	{
	    _thr = new DrawThread(_myview,_myagent);
	    _thr.setRunning(true);
	    _thr.start();
	    Log.i("DrawTaskCallback","thread started");
	}
	public void surfaceDestroyed(android.view.SurfaceHolder h)
	{
	    if(_thr != null) {
		_thr.setRunning(false);
		boolean retry = true;
		Log.i("DrawTaskCallback","stopping thread");
		while (retry) {
		    try {
			_thr.join();
			retry = false;
		    } catch (InterruptedException e) {
		    }
		}
		Log.i("DrawTaskCallback","thread stopped");
	    }
	    _thr = null;
	}
	public void surfaceChanged(android.view.SurfaceHolder h,
				   int format,int width,int height)
	{
	}
	public DrawThread thread()
	{
	    return _thr;
	}
    }
    public static DrawTaskCallback attachCallback(android.view.SurfaceView v,
				      DrawAgent a)
    {
	DrawTaskCallback cb = new DrawTaskCallback(v,a);
	v.getHolder().addCallback(cb);
	return cb;
    }
}
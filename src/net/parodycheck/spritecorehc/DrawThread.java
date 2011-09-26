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
    public DrawThread(SurfaceView surfv,DrawAgent a)
    {
	_mysurfv = surfv;
	_myagent = a;
	_running = true;
    }
    public void run()
    {
	while(_running)
	    {
		try
		    {
			synchronized(_myagent)
			    {
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
					Log.e("DrawTask",e.getMessage());
				    }
				finally {
				    if(c != null)
					{
					    _mysurfv.getHolder().unlockCanvasAndPost(c);
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

    public static class DrawTaskCallback implements android.view.SurfaceHolder.Callback
    {
	private DrawAgent _myagent;
	private DrawThread _thr;
	public DrawTaskCallback(SurfaceView v,DrawAgent a)
	{
	    _myagent = a;
	    _thr = new DrawThread(v,_myagent);
	}
	public void surfaceCreated(android.view.SurfaceHolder h)
	{
		_thr.start();
	    
	}
	public void surfaceDestroyed(android.view.SurfaceHolder h)
	{
	    _thr.setRunning(false);
	    boolean retry = true;
	    while (retry) {
		try {
		    _thr.join();
		    retry = false;
		} catch (InterruptedException e) {
		}
	    }
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
    public static DrawThread attachThread(android.view.SurfaceView v,
				      DrawAgent a)
    {
	DrawTaskCallback cb = new DrawTaskCallback(v,a);
	v.getHolder().addCallback(cb);
	return cb.thread();
    }
}
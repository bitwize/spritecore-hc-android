package net.parodycheck.spritecorehc;
import android.view.SurfaceView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap;
import java.util.*;
import java.lang.Thread;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.util.Log;

public class SpriteCoreView extends SurfaceView implements DrawAgent
{
    private Paint _p;
    private long _clock;
    private long _oldclock;
    private long _clockaccum;
    private android.graphics.Point _viewportSize;
    private DrawThread.DrawTaskCallback _cb;
    private Bitmap _background;
    private SpriteCoreEventAgent _eagent;
    private ArrayList<Sprite> _sprites;
    private java.util.Random _random;
    private void init()
    {
	_clock = System.currentTimeMillis();
	_oldclock = _clock;
	_clockaccum = 0;
	_cb = DrawThread.attachCallback(this,this);
	_p = new Paint();
	_sprites = new ArrayList<Sprite>();
	_eagent = DefaultEventAgent.instance();
	_random = new java.util.Random();
	_viewportSize = null;
    }
    public SpriteCoreView(Context ctx,AttributeSet attrs)
    {
	super(ctx,attrs);
	init();
    }
    public SpriteCoreView(Context ctx)
    {
	super(ctx);
	init();
    }
    public void drawOn(Canvas c)
    {
	c.drawColor(Color.BLACK);
	Iterator<Sprite> i= _sprites.iterator();
	while(i.hasNext())
	    {
		i.next().renderOn(c);
	    }
    }
    public void step()
    {
	synchronized(this)
	    {
		_oldclock = _clock;
		_clock = System.currentTimeMillis();
		_clockaccum += (_clock - _oldclock);
		while(_clockaccum >= 20) {
		    Iterator<Sprite> i = _sprites.iterator();
		    while(i.hasNext())
			{
			    i.next().step();
			}
		    _clockaccum -= 20;
		}
	    }

    }

    public Paint paint()
    {
	return _p;
    }

    public android.graphics.Point getViewportSize()
    {
	return _viewportSize;
    }

    public void setViewportSize(android.graphics.Point pt)
    {
	_viewportSize = pt;
    }

    public void setViewportSize(int x,int y)
    {
	_viewportSize = new android.graphics.Point(x,y);
    }

    
    public void add(Sprite s)
    {
	_sprites.add(s);
    }

    public void remove(Sprite s)
    {
	_sprites.remove(s);
    }
    public void placeBehind(Sprite s1,Sprite s2)
    {
	int i = _sprites.indexOf(s2);
	if(i != -1)
	    {
		remove(s1);
		_sprites.add(i,s1);
	    }
    }
    public long clock()
    {
	synchronized(this) {
	    return _clock;
	}
    }
    public long lastFrameTime() {
	synchronized(this) {
	    return _clock - _oldclock;
	}
    }

    public boolean onKeyDown(int code,KeyEvent e)
    {
	synchronized(this) {
	    _eagent.handleEvent(e);
	}
	return true;
    }

    public boolean onKeyUp(int code,KeyEvent e)
    {
	synchronized(this) {
	    _eagent.handleEvent(e);
	}
	return true;
    }

    public boolean onTrackballEvent(MotionEvent e)
    {
	synchronized(this) {
	    _eagent.handleEvent(e);
	}
	return true;
    }

    public boolean onTouchEvent(MotionEvent e)
    {
	Log.e("SpriteCoreView","calling handler");
	synchronized(this) {
	    _eagent.handleEvent(e);
	}
	return true;
    }

    protected void onMeasure(int widthSpec,int heightSpec) {
	if(_viewportSize == null) {
	    super.onMeasure(widthSpec,heightSpec);
	}
	else {
	    setMeasuredDimension(_viewportSize.x,_viewportSize.y);
	}
    }

    public DrawThread drawThread()
    {
	return _cb.thread();
    }

    public void pauseDrawing()
    {
	DrawThread dt = drawThread();
	if(dt != null)  {
	    dt.pauseDrawing();
	}
    }

    public void resumeDrawing()
    {
	DrawThread dt = drawThread();
	if(dt != null)  {
	    dt.resumeDrawing();
	}
    }

    public SpriteCoreEventAgent eventAgent() { return _eagent;}

    public void setEventAgent(SpriteCoreEventAgent ea) {_eagent = ea;}

    public java.util.Random randomSource()
    {
	return _random;
    }

}
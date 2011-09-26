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
    private Bitmap _background;
    private DrawThread _drawthread;
    private SpriteCoreEventAgent _eagent;
    private ArrayList<Sprite> _sprites;
    private void init()
    {
	_clock = System.currentTimeMillis();
	_oldclock = _clock;
	_clockaccum = 0;
	_drawthread = DrawThread.attachThread(this,
					      this);
	_p = new Paint();
	_sprites = new ArrayList<Sprite>();
	_eagent = DefaultEventAgent.instance();
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
	return super.onKeyDown(code,e);
    }

    public boolean onKeyUp(int code,KeyEvent e)
    {
	synchronized(this) {
	    _eagent.handleEvent(e);
	}
	return super.onKeyUp(code,e);
    }

    public boolean onTrackballEvent(MotionEvent e)
    {
	synchronized(this) {
	    _eagent.handleEvent(e);
	}
	return super.onTrackballEvent(e);
    }

    public boolean onTouchEvent(MotionEvent e)
    {
	Log.e("SpriteCoreView","calling handler");
	synchronized(this) {
	    _eagent.handleEvent(e);
	}
	return super.onTouchEvent(e);
    }

    public DrawThread drawThread()
    {
	return _drawthread;
    }

    public SpriteCoreEventAgent eventAgent() { return _eagent;}

    public void setEventAgent(SpriteCoreEventAgent ea) {_eagent = ea;}

}
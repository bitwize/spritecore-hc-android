package net.parodycheck.spritecorehc;
import android.graphics.PointF;
import android.graphics.Bitmap;
import java.util.*;
import android.util.Log;

public class Sprite
{
    PointF _pos;
    PointF _vel;
    PointF _hotspot;
    ArrayList<Bitmap> _frames;
    int _currentFrame;
    SpriteCoreView _host;
    SpriteAppearanceAgent _aAgent;
    SpriteBehaviorAgent _bAgent;


    private void initSprite(SpriteCoreView v,
			    List<Bitmap> flist)
    {
	_host = v;
	_frames = new ArrayList<Bitmap>();
	_currentFrame = 0;
	_pos = new PointF(0.0f,0.0f);
	_vel = new PointF(0.0f,0.0f);
	_hotspot = new PointF(0.0f,0.0f);
	for(Iterator<Bitmap> i = flist.iterator();
	    i.hasNext();)
	    {
		Bitmap b = i.next();
		_frames.add(b);
	    }
	_aAgent = DefaultAppearanceAgent.instance();
	_bAgent = DefaultBehaviorAgent.instance();
	v.add(this);
	
    }

    public Sprite(SpriteCoreView v,
	   List<Bitmap> flist)
    {
	initSprite(v,flist);
    }

    public Sprite(SpriteCoreView v,
	   Bitmap frame)
    {
	ArrayList<Bitmap> l = new ArrayList<Bitmap>();
	l.add(frame);
	initSprite(v,l);
    }

    public PointF pos()
    {
	return _pos;
    }

    public PointF vel()
    {
	return _vel;
    }

    public PointF hotspot()
    {
	return _hotspot;
    }

    public void moveTo(PointF newPos)
    {
	_pos.x = newPos.x;
	_pos.y = newPos.y;
    }

    public void setVel(PointF newVel)
    {
	_vel.x = newVel.x;
	_vel.y = newVel.y;
    }

    public void moveTo(float newx,float newy)
    {
	_pos.x = newx;
	_pos.y = newy;
    }

    public void setVel(float newx,float newy)
    {
	_vel.x = newx;
	_vel.y = newy;
    }


    public void setHotspot(float newx,float newy)
    {
	_hotspot.x = newx;
	_hotspot.y = newy;
    }

    public void setHotspot(PointF newhotspot)
    {
	_hotspot = newhotspot;
    }

    public int width()
    {
	return _frames.get(_currentFrame).getWidth();
    }
    public int height()
    {
	return _frames.get(_currentFrame).getHeight();
    }
    public Bitmap shape()
    {
	return _frames.get(_currentFrame);
    }
    public int currentFrame()
    {
	return _currentFrame;
    }
    public void setCurrentFrame(int n)
    {
	_currentFrame = n % _frames.size();
    }
    public SpriteCoreView host()
    {
	return _host;
    }
    public void nuke()
    {
	_host.remove(this);
    }
    public void goBehind(Sprite s)
    {
	_host.placeBehind(this,s);
    }
    public void step()
    {
	_bAgent.act(this);
    }
    public void renderOn(android.graphics.Canvas c)
    {
	_aAgent.renderSpriteOn(this,c);
    }
    public void setAppearanceAgent(SpriteAppearanceAgent a)
    {
	_aAgent = a;
    }

    public SpriteAppearanceAgent appearanceAgent()
    {
	return _aAgent;
    }

    public void setBehaviorAgent(SpriteBehaviorAgent a)
    {
	_bAgent = a;
    }

    public SpriteBehaviorAgent behaviorAgent()
    {
	return _bAgent;
    }
    
    public boolean isTouching(Sprite anotherSprite) {
	float left1,top1,right1,bottom1;
	float left2,top2,right2,bottom2;
	boolean step1,step2;
	left1 = pos().x - hotspot().x;
	top1 = pos().y - hotspot().y;
	right1 = left1 + shape().getWidth();
	bottom1 = top1 + shape().getHeight();
	left2 = anotherSprite.pos().x - anotherSprite.hotspot().x;
	top2 = anotherSprite.pos().y - anotherSprite.hotspot().y;
	right2 = left2 + anotherSprite.shape().getWidth();
	bottom2 = top2 + anotherSprite.shape().getHeight();
	if(left1 <= left2) {
		if(left2 < right1) step1 = true; else step1 = false;
	}
	else {
		if(right2 > left1) step1 = true; else step1 = false;
	}
	if(top1 <= top2) {
		if(top2 < bottom1) step2 = true; else step2 = false;
	}
	else {
		if(bottom2 > top1) step2 = true; else step2 = false;
	}
	return step1 && step2;
	
    }

}
package net.parodycheck.spritecorehc;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import java.io.*;

public class DefaultAppearanceAgent implements SpriteAppearanceAgent
{
    private static DefaultAppearanceAgent _inst = null;

    public void renderSpriteOn(Sprite aSprite,Canvas aCanvas)
    {
	Bitmap shape = aSprite.shape();
	PointF pos = aSprite.pos();
	PointF hs = aSprite.hotspot();
	SpriteCoreView v = aSprite.host();
	android.graphics.Paint p = v.paint();
	if(shape == null)
	    {
		Log.e("DefaultAppearanceAgent","shape is null");
	    }
	if(aCanvas == null)
	    {
		Log.e("DefaultAppearanceAgent","canvas is null");
	    }
	try {
	    aCanvas.drawBitmap(shape,pos.x - hs.x,pos.y - hs.y,null);
	}
	catch(Exception e)
	    {
		StringWriter wr = new StringWriter();
		e.printStackTrace(new PrintWriter(wr));
		Log.i("DefaultAppearanceAgent",wr.toString());
	    }
    }
    public static DefaultAppearanceAgent instance()
    {
	if(_inst == null)
	    {
		_inst = new DefaultAppearanceAgent();
	    }
	return _inst;
    }
}
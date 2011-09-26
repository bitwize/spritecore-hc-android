package net.parodycheck.spritecorehc;
import android.graphics.Canvas;

interface DrawAgent
{
    public void step();
    public void drawOn(Canvas aCanvas);
}
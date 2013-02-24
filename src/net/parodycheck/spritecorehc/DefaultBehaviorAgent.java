package net.parodycheck.spritecorehc;

public class DefaultBehaviorAgent implements SpriteBehaviorAgent
{
    private static DefaultBehaviorAgent _inst;
    public void act(Sprite aSprite)
    {
	aSprite.moveTo(aSprite.pos().x + aSprite.vel().x,
		       aSprite.pos().y + aSprite.vel().y);

    }

    public static DefaultBehaviorAgent instance()
    {
	if(_inst == null)
	    {
		_inst = new DefaultBehaviorAgent();
	    }
	return _inst;
    }

}
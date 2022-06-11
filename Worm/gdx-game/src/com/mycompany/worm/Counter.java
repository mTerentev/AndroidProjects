package com.mycompany.worm;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class Counter
{
	MyFontGenerator mfg;
	int score;
	float width;
	float height;
	Counter(float width,float height){
		this.width=width;
		this.height=height;
		mfg=new MyFontGenerator(Gdx.files.internal("MyFont3.png"));
		mfg.font=30;
		mfg.color=Color.GREEN;
		mfg.inter_leters=70;
		mfg.shadow_intense=10;
		mfg.shadow_direction=(float)(7*Math.PI/6);
		mfg.generate();
	}
	public void draw(SpriteBatch batch){
		mfg.write(batch,Integer.toString(score),(int)width-300,(int)height-300,300,300);
	}
}

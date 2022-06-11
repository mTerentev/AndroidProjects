package com.mycompany.worm;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import java.util.*;
import com.badlogic.gdx.graphics.g2d.*;

public class Apple
{
	Circle body;
	Texture texture;
	Vector2 position;
	int d;
	public Apple(Color c,int d){
		this.d=d;
		Pixmap p=new Pixmap(d,d,Pixmap.Format.RGBA8888);
		p.setColor(c);
		p.fillCircle(d/2,d/2,d/2);
		texture=new Texture(p);
		p.dispose();
	}
	public void draw(SpriteBatch batch){
		batch.draw(texture,body.x-body.radius,body.y-body.radius,body.radius*2,body.radius*2);
	}
	public void generate(Game game){
		boolean generated=false;
		while(!generated){
			Random r=new Random();
			position=new Vector2(r.nextInt(Gdx.graphics.getWidth()),r.nextInt(Gdx.graphics.getHeight()));
			body=new Circle(position.x,position.y,d/2);
			generated=true;
			for(Segment s:game.worm.segments){
				if(s.body.overlaps(body)){
					generated=false;
				}
			}
			/*for(Apple a:game.apples){
				if(a.body.overlaps(body)){
					generated=false;
				}
			}*/
			for(Wall w:game.walls){
				if(game.overlaps(w.body,body)){
					generated=false;
				}
			}
		}
	}
}

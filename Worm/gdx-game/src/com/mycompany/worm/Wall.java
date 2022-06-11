package com.mycompany.worm;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import java.util.*;

public class Wall
{
	Texture texture;
	int x;
	int y;
	float length;
	float width;
	float angle;
	Polygon body;
	
	Wall(int x, int y, float length, float width, float angle, Color color){
		this.x=x;
		this.y=y;
		this.length=length;
		this.width=width;
		this.angle=angle;
		generate_texture(color);
		body=new Polygon(new float[]{-width/2,-length/2,width/2,-length/2,width/2,length/2,-width/2,length/2});
		body.setOrigin(0,0);
		body.translate(x,y);
		body.setRotation(angle);
	}
	void draw(Batch batch){
		batch.draw(texture,x-width/2,y-length/2,width/2,length/2,width,length,1,1,angle,0,0,140,140,false,false);
	}
	void generate_texture(Color color){
		Pixmap texture_pixmap=new Pixmap(50,50,Pixmap.Format.RGBA4444);
		texture_pixmap.setColor(color);
		texture_pixmap.fillRectangle(0,0,50,50);
		texture=new Texture(texture_pixmap);
		texture_pixmap.dispose();
	}
	public static Wall generate(Game game){
		boolean generated=false;
		Wall w=null;
		while(!generated){
			Random r=new Random();
			w=new Wall(r.nextInt(game.width),r.nextInt(game.height),r.nextInt(1000-50)+50,50,r.nextInt(360),Color.ORANGE);
			generated=true;
			if(game.overlaps(w.body,game.worm.segments.get(0).body)){
				generated=false;
			}
			for(Wall wall:game.walls){
				if(Intersector.overlapConvexPolygons(wall.body,w.body)){
					generated=false;
				}
			}
		}
		return w;
	}
}

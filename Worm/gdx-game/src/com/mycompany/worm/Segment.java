package com.mycompany.worm;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.*;

public class Segment 
{
	Circle body;
	Texture texture;
	Vector2 position;
	Vector2 direction;
	float diameter;
	Segment(float d,Vector2 coordinstes,Texture texture){
		this.texture=texture;
		position=coordinstes;
		diameter=d;
		body=new Circle(coordinstes,d/2);
		direction=new Vector2(1,1).nor();
	}
	void draw(SpriteBatch batch){
		batch.draw(texture,body.x-body.radius,body.y-body.radius,35,35,body.radius*2,body.radius*2,1,1,direction.angle(),0,0,140,140,false,false);
	}
	void move(Vector2 position){
		this.position=position;
		body.setPosition(position);
	}
	void rotate(Vector2 direction){
		this.direction=direction;
	}
}

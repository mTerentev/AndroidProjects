package com.mycompany.worm;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import java.nio.file.attribute.*;
import java.util.*; 
public class Worm
{
	//segments
	float segments_interval;
	float segment_radius;
	Texture texture;
	
	//worm
	int length;
	float speed;
	Vector2 direction;
	Vector2 position;
	Array<Segment> segments;
	Array<Vector2> positions;
	Array<Vector2> directions;
	boolean controllable;
	
	Worm(Vector2 position,Vector2 direction,float speed){
		//segments
		segments_interval=5;
		segment_radius=70;
		texture_generate(Color.GREEN);
		//worm
		this.speed=speed;
		this.direction=direction;
		this.position=position;
		segments=new Array<Segment>();
		positions=new Array<Vector2>();
		directions=new Array<Vector2>();
		
		positions.add(position);
		directions.add(direction);
		controllable=true;
		grow();
	}
	
	void control(Vector2 inp_vector,float delta_time){
		Vector2 direction_not_nor=inp_vector.sub(position);
		if(direction_not_nor.len()<10){
			controllable=false;
		}
		direction=direction.mulAdd(direction_not_nor.nor(),speed*delta_time/40).nor();
	}
	
	void move(float delta_time){
		positions.add(new Vector2(position.mulAdd(direction,speed*delta_time)));
		directions.add(new Vector2(direction));
		int ind=0;
		for(Segment seg:segments){
			int index=positions.size-1-(int)(ind*segments_interval/speed/delta_time);
			seg.move(positions.get(index));
			seg.rotate(directions.get(index));
			ind++;
		}
		positions.removeIndex(0);
		directions.removeIndex(0);
	}
	
	void grow(){
		segments.add(new Segment(segment_radius,new Vector2(-100,-100),texture));
		length++;
	}
	
	void draw(SpriteBatch batch){
		Pixmap texture_pixmap=new Pixmap(10,10,Pixmap.Format.RGBA8888);
		texture_pixmap.setColor(Color.ORANGE);
		texture_pixmap.fillRectangle(0,0,10,5);
		Texture texture=new Texture(texture_pixmap);
		texture_pixmap.dispose();
		for(int x=segments.size-1;x>=0;x--){
			segments.get(x).draw(batch);
		}
		//for(int x=0;x<directions.size;x++){
			//batch.draw(texture,p.x,p.y);
			//batch.draw(texture,positions.get(x).x,positions.get(x).y,35,35,50,50,1,1,directions.get(x).angle(),0,0,140,140,false,false);
			
		//}
	}
	void texture_generate(Color color){
		Pixmap texture_pixmap=new Pixmap((int)segment_radius*2,(int)segment_radius*2,Pixmap.Format.RGBA4444);
		texture_pixmap.setColor(color);
		texture_pixmap.fillCircle((int)segment_radius,(int)segment_radius,(int)segment_radius);
		texture_pixmap.setColor(Color.ORANGE);
		texture_pixmap.fillRectangle((int)(0.3*segment_radius),0,(int)(segment_radius*0.4),(int)segment_radius*2);
		texture=new Texture(texture_pixmap);
		texture_pixmap.dispose();
	}
}

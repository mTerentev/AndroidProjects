package com.mycompany.worm;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import java.util.*;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Game implements ApplicationListener
{
	SpriteBatch batch;
	int width;
	int height;
	boolean gameOver;
	Timer action_timer;
	Timer controlling_timer;
	
	Worm worm;
	Array<Apple> apples;
	Array<Wall> walls;
	Counter counter;
	
	//Start_window sw;
	
	@Override
	public void create(){
		batch=new SpriteBatch();
		//sw=new Start_window();
		this.create_game();
	}
	
	public void create_game()
	{
		width=Gdx.graphics.getWidth();
		height=Gdx.graphics.getHeight();
		apples=new Array<Apple>();
		walls=new Array<Wall>();
		counter=new Counter(width,height);
		
		worm=new Worm(new Vector2(width/2,height/2),new Vector2(0,1),150);
		
		walls.add(new Wall(0,height/2,height,50,0,Color.ORANGE));
		walls.add(new Wall(width,height/2,height,50,0,Color.ORANGE));
		walls.add(new Wall(width/2,0,width,50,90,Color.ORANGE));
		walls.add(new Wall(width/2,height,width,50,90,Color.ORANGE));
		for(int x=0; x<5; x++){
			walls.add(Wall.generate(this));
		}
		for(int x=0;x<10;x++){
			apples.add(new Apple(Color.ORANGE,50));
			apples.get(x).generate(this);
		}
		
		Controlling ctrl=new Controlling(worm,0.001f);
		Actions act=new Actions(this,0.001f);
		
		controlling_timer=new Timer();
		action_timer=new Timer();
		
		controlling_timer.scheduleTask(ctrl,0f,ctrl.delta_time);
		action_timer.scheduleTask(act,0f,act.delta_time);
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
		batch.begin();
		for(Apple a:apples){
			a.draw(batch);
		}
		worm.draw(batch);
		for(Wall wall:walls){
			wall.draw(batch);
		}
		counter.draw(batch);
		batch.end();
		
		//counter.mfg.dispose();
	}

	@Override
	public void dispose()
	{
		action_timer.stop();
		controlling_timer.stop();
	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}
	public void eating(){
		for(Apple apple:apples){
			if(worm.segments.get(0).body.overlaps(apple.body)){
				for(int x=0;x<10;x++){
					worm.grow();
				}
				worm.speed+=5;
				counter.score+=1;
				apple.generate(this);
			}
		}
	}
	public void bumping(){
		for(int i=(int)(2*worm.segment_radius/worm.segments_interval);i<worm.segments.size;i++){
			if(worm.segments.get(0).body.overlaps(worm.segments.get(i).body)){
				this.dispose();
				this.create_game();
			}
		}
		for(Wall w:walls){
			if(overlaps(w.body,worm.segments.get(0).body)){
				this.dispose();
				this.create_game();
			}
		}
	}
	public static boolean overlaps(Polygon polygon, Circle circle) {
		float []vertices=polygon.getTransformedVertices();
		Vector2 center=new Vector2(circle.x, circle.y);
		float squareRadius=circle.radius*circle.radius;
		for (int i=0;i<vertices.length;i+=2){
			if (i==0){
				if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
					return true;
			} else {
				if (Intersector.intersectSegmentCircle(new Vector2(vertices[i-2], vertices[i-1]), new Vector2(vertices[i], vertices[i+1]), center, squareRadius))
					return true;
			}
		}
		return polygon.contains(circle.x, circle.y);
	}
}

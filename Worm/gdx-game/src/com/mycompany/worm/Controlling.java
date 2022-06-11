package com.mycompany.worm;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class Controlling extends Timer.Task
{
	Worm worm;
	float delta_time;
	Controlling(Worm worm,float delta_time){
		this.worm=worm;
		this.delta_time=delta_time;
	}
	public void run(){
		Vector2 inp_vector=null;
		if(Gdx.input.isTouched()&& worm.controllable){
			inp_vector=new Vector2(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY());
			worm.control(inp_vector,delta_time);
		}
		if(!Gdx.input.isTouched()){
			worm.controllable=true;
		}
	}
}

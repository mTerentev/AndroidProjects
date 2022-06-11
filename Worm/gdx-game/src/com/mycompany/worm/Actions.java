package com.mycompany.worm;

import java.sql.*;
import com.badlogic.gdx.utils.*;

public class Actions extends Timer.Task
{
	Game game;
	float delta_time;
	Actions(Game game,float delta_time){
		this.game=game;
		this.delta_time=delta_time;
	}
	public void run(){
		game.bumping();
		game.eating();
		game.worm.move(delta_time);
		//game.walls.get(0).angle+=10*delta_time;
	}
}

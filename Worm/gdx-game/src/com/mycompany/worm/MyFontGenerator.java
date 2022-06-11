package com.mycompany.worm;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import java.util.*;

public class MyFontGenerator{
	Map dict;
	String str;
	Texture res_text;
	Texture font_texture;
	Pixmap font_pixmap;
	Pixmap res;
	Pixmap p;
	boolean show_edges;
	boolean individual_width;
	int inter_lines;
	int inter_leters;
	float shadow_intense;
	float shadow_direction;
	Color shadow_color;
	Color color;
	int font;
	int start_font=20;
	float k;
	public MyFontGenerator(FileHandle font_file){
		individual_width=false;
		show_edges=false;
		inter_lines=100;
		inter_leters=50;
		color=Color.BLACK;
		shadow_intense=0;
		shadow_direction=0;
		shadow_color=Color.BLACK;
		font=12;
		font_texture = new Texture(font_file);
		dict=new HashMap<Character,Pixmap>();
		str="ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz 0123456789.,;:$#'!\"/?%&()@ ";
	}
	public void generate(){
		k=(float)font/(float)start_font;
		if (!font_texture.getTextureData().isPrepared()) {
			font_texture.getTextureData().prepare();
		}
		font_pixmap=font_texture.getTextureData().consumePixmap();
		for(int d=0;d<3;d++){
			int j=-1;
			for(int i=0;i<27;i++){
				if(i % 9==0){
					j++;
				}
				p=new Pixmap(Math.round(100f*(k+1)),Math.round(120f*(k+1)),Pixmap.Format.RGBA8888);
				if(shadow_intense!=0){
					for (int x = 20; x < 90; x++) {
						for (int y = 20; y < 120; y++) {
							if(font_pixmap.getPixel(x+110*(i-9*j),y+120*j+(120*3+24)*d)==Color.rgba8888(Color.BLACK)){
								p.setColor(shadow_color);
							}
							else{
								p.setColor(Color.CLEAR);
							}
							p.drawPixel((int)Math.round(x-Math.sin(shadow_direction)*shadow_intense),(int)Math.round(y-Math.cos(shadow_direction)*shadow_intense));
						}
					}
				}
				for (int x = 20; x < 90; x++) {
					for (int y = 20; y < 120; y++) {
						if(font_pixmap.getPixel(x+110*(i-9*j),y+120*j+(120*3+24)*d)==Color.rgba8888(Color.BLACK)){
							p.setColor(color);
						}
						else{
							p.setColor(Color.CLEAR);
						}
						p.drawPixel(x,y);
					}
				}
				dict.put(str.charAt(i+27*d),p);
			}
		}
		dispose_init();
	}
	public void write(SpriteBatch batch,String s,int write_x,int write_y,int width, int height){
		res=new Pixmap(width,height,Pixmap.Format.RGBA8888);
		int dist_x=0;
		int dist_y=0;
		for(char ch:s.toCharArray()){
			int d_dist=Math.round(inter_leters*k);
			if("rfijlt,.\"'/!:;()1".indexOf(ch)!=-1 && individual_width){
				d_dist=Math.round(inter_leters*k*0.5f);
			}
			if("abcdeghknopqsuvxyzIJ?&@$#23456789% ".indexOf(ch)!=-1 && individual_width){
				d_dist=Math.round(inter_leters*k*0.8f);
			}
			if("ABCDEFGHKLMNOPQRSTUVXYZ".indexOf(ch)!=-1 && individual_width){
				d_dist=Math.round(inter_leters*k*0.9f);
			}
			if("mw".indexOf(ch)!=-1 && individual_width){
				d_dist=Math.round(inter_leters*k*1.4f);
			}
			if("W@".indexOf(ch)!=-1 && individual_width){
				d_dist=Math.round(inter_leters*k*1.5f);
			}
			dist_x+=d_dist/2;
			if(ch!=' '){
				res.drawPixmap((Pixmap)dict.get(ch),0,0,100,120,dist_x,dist_y,Math.round(100*k),Math.round(120*k));

			}
			dist_x+=d_dist/2;
			if(dist_x>=width-50*k){
				dist_x=0;
				dist_y+=inter_lines*k;
			}
		}
		res_text=new Texture(res);
		batch.draw(res_text,write_x,write_y,width,height);
		if(show_edges){
			Pixmap edges=new Pixmap(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),Pixmap.Format.RGBA8888);
			edges.setColor(Color.BLACK);
			edges.drawRectangle(write_x,Gdx.graphics.getHeight()-write_y,width,-height);
			//batch.draw(new Texture(edges),0,0);
			edges.dispose();
		}
	}
	public void dispose(){
		res_text.dispose();
		res.dispose();
	}
	public void dispose_init(){
		p.dispose();
		font_pixmap.dispose();
		font_texture.dispose();
	}
}

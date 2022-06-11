package com.mycompany.myapp2;


import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import bsh.*;
import java.util.*;
import java.util.concurrent.*;
import android.opengl.*;

public class MainActivity extends Activity 
{
	int a=15;
	int w=5;
	int height;
	int width;
	int d;
	int my;
	int mx;
	int x;
	int y;
	int turn_numb;
	int[] last_dot;
	int[] first_dot;
	int[] second_dot;
	boolean gameOver=false;
	//int max_x=0,min_x=a,max_y=0,min_y=a;
	int max_x=a-1,min_x=0,max_y=a-1,min_y=0;
	Button button;
	TextView text;
	TextView debug;
	String debug_val;
	boolean onTurn=false;
	String b[][];
	AbsoluteLayout game_over;
	AbsoluteLayout screen;
	AbsoluteLayout win;
	TextView hint;
	Button show;
	public Graphics game;
	public myAI ai;
    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
	}
    public void global_start(View v)
    {
		setContentView(R.layout.main);
        game=new Graphics(this);
		ai=new myAI();
		screen=findViewById(R.id.screen);
		game_over=findViewById(R.id.gameOver);
		win=findViewById(R.id.win);
		screen.addView(game);
		button=findViewById(R.id.mainButton1);
		M();
	}
	public void tryAgain(View v){
		M();
	}
	public void Show(View v){
		screen.addView(win);
		screen.removeView(show);
	}
	public void Hide(View v){
		screen.removeView(win);
		screen.addView(show);
	}
	public void M()
	{
		screen.removeView(game_over);
		screen.removeView(win);
		screen.removeView(show);
		gameOver=false;
		max_x=a-1;min_x=0;max_y=a-1;min_y=0;
		turn_numb=0;
		last_dot=null;
		
		Start();
		game.invalidate();
		//Pl_turn();
		button.setClickable(false);
		new myAI().execute();
    }
	public void Start() 
	{
		text=findViewById(R.id.text);
		debug=findViewById(R.id.debug);
		text.setText("Мой ход");
		b=new String[a][a];
		for(int x=0;x<a;x++)
		{
			for(int y=0;y<a;y++)
			{
				b[x][y]=".";
			}
		}
	}
	public void Pl_turn(){
		x=-1;
		y=-1;
		game.setOnTouchListener(touch);
		onTurn=false;
	}
	public void click(View v){
		if(onTurn){
			button.setClickable(false);
			game.setOnTouchListener(null);
			if(turn_numb>=9){check("X");}
			if(!gameOver){
				new myAI().execute();
			}
		}
	}
	public void check(String lit){
		int dx=0,dy=0;
		for(int x=min_x;x<=max_x;x++){
			for(int y=min_y;y<=max_y;y++){
				for(int d=0;d<4;d++){
					switch(d){
						case 0:
							dx=1;dy=0;
							break;
						case 1:
							dx=0;dy=1;
							break;
						case 2:
							dx=1;dy=1;
							break;
						case 3:
							dx=1;dy=-1;
							break;
					}
					boolean checked=true;
					for(int l=0;l<w;l++){
						try{
							if(b[x+dx*l][y+dy*l]!=lit){checked=false;}
						}
						catch(ArrayIndexOutOfBoundsException a){checked=false;}
					}
					if(checked){
						gameOver=true;
						first_dot=new int[]{x,y};
						second_dot=new int[]{x+dx*(w-1),y+dy*(w-1)};
						GameOver(lit);
					}
				}
			}
		}
	}
	public void GameOver(String lit){
		game.invalidate();
		button.setClickable(false);
		game.setOnTouchListener(null);
		text.setText("Игра закончена");
		if(lit=="O"){
			screen.addView(game_over);
		}
		else{
			screen.addView(win);
		}
	}
	OnTouchListener touch= new OnTouchListener(){

		@Override
		public boolean onTouch(View p1, MotionEvent p2)
		{
			try{
				b[x][y]=".";
			}
			catch(ArrayIndexOutOfBoundsException a){}
			finally{}
			x=(int)(p2.getX()-mx)/d;
			y=(int)(p2.getY()-my)/d;
			onTurn=false;
			try{
				if(b[x][y]!="."){
					x=-1;
				}
				b[x][y]="X";
				onTurn=true;
			}
			catch(ArrayIndexOutOfBoundsException a){}
			finally{}
			game.invalidate();
			return false;
		}
	};
	public class Graphics extends View
	{
		public Graphics(Context context){
			super(context);
		}
		@Override
		protected void onDraw(Canvas canvas){
			super.onDraw(canvas);
			height=getHeight();
			width=getWidth();
			d=Math.min(height,width)/(a+1);
			my=(height-d*a)/2;
			mx=(width-d*a)/2;
			Paint p=new Paint();
			p.setColor(Color.GRAY);
			p.setStrokeWidth(5);
			for(int x=0;x<=a;x++){
				canvas.drawLine(x*d+mx,my,x*d+mx,d*a+my,p);
				canvas.drawLine(mx,x*d+my,d*a+mx,x*d+my,p);
			}
			for(int x=0;x<a;x++){
				for(int y=0;y<a;y++){
					if(b[x][y]!="."){
						if(b[x][y]=="X"){p.setColor(Color.RED);}
						else{p.setColor(Color.BLUE);}
						canvas.drawCircle((float)(d*(x+0.5))+mx,(float)(d*(y+0.5)+my),(float)d/2-5,p);
					}
				}
			}
			if(last_dot!=null&&!gameOver){
				p.setColor(Color.GREEN);
				p.setStrokeWidth(5);
				p.setStyle(Paint.Style.STROKE);
				canvas.drawRect(last_dot[0]*d+mx+1,last_dot[1]*d+my,(last_dot[0]+1)*d+mx+1,(last_dot[1]+1)*d+my,p);
			}
			if(gameOver){
				p.setColor(Color.GREEN);
				p.setStrokeWidth(8);
				canvas.drawLine((first_dot[0]+0.5f)*d+mx,(first_dot[1]+0.5f)*d+my,(second_dot[0]+0.5f)*d+mx,(second_dot[1]+0.5f)*d+my,p);
				
			}
		}
	}
	public class myAI extends AsyncTask<Void,Void,Void>
	{

		@Override
		protected void onPreExecute()
		{
			text.setText("Мой ход");
			debug.setText(debug_val);
			turn_numb++;
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void[] p1)
		{
			new myAI().AI_turn("O","X");
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			game.invalidate();
			button.setClickable(true);
			text.setText("Ваш ход");
			turn_numb++;
			Pl_turn();
			if(turn_numb>=9){check("O");}
			super.onPostExecute(result);
		}
		
    	public void AI_turn(String slf_lit,String opp_lit)
		{
			boolean k=true;
			int[] m=new int[a*a];
			ArrayList<Integer> m1=new ArrayList<Integer>();

			if(turn_numb>3){
				max_x=0;min_x=a-1;max_y=0;min_y=a-1;
				for(int x=0;x<a;x++){
					for(int y=0;y<a;y++){
						if(b[x][y]!="."){
							max_x=Math.max(max_x,x+1);
							min_x=Math.min(min_x,x-1);
							max_y=Math.max(max_y,y+1);
							min_y=Math.min(min_y,y-1);
						}
						if(max_x==a){max_x=a-1;}
						if(min_x==-1){min_x=0;}
						if(max_y==a){max_y=a-1;}
						if(min_y==-1){min_y=0;}
					}
				}
				//debug_val=max_x+" "+min_x+" "+max_y+" "+min_y;
				System.out.println(max_x+" "+min_x+" "+max_y+" "+min_y);
				int[] temp1=c2(b,slf_lit,w-1);
				int[] temp2=c2(b,opp_lit,w-1);
				int[] temp3=c2(b,slf_lit,w-2);
				if(temp1[0]>=1&& k){
					m[temp1[1]]=1;
					k=false;
				}
				if(temp2[0]>=1&& k){
					m[temp2[1]]=1;
					k=false;
				}
				if(k){
					for(int x=min_x;x<=max_x;x++){
						for(int y=min_y;y<=max_y;y++){
							if(b[x][y]=="."){
								b[x][y]=slf_lit;
								int[] temp=c2(b,slf_lit,w-1);
								if(temp[0]>=2){
									m[x*a+y]=1;
									k=false;
								}
								b[x][y]=".";
							}
						}
					}
				}
				if(temp3[0]>=1&& k){
					m[temp3[1]]=1;
					m[temp3[2]]=1;
					k=false;
				}
				if(k){
					for(int x=min_x;x<=max_x;x++){
						for(int y=min_y;y<=max_y;y++){
							if(b[x][y]=="."){
								b[x][y]=opp_lit;
								int[] temp=c2(b,opp_lit,w-1);
								if(temp[0]>=2){
									m[x*a+y]=1;
									k=false;
								}
								b[x][y]=".";
							}
						}
					}
				}
				if(k){
					for(int d=0;d<a*a;d++){
						if(b[d/a][d%a]=="."){
							m[d]=1;
						}
					}
				}
				for(int x=min_x;x<=max_x;x++){
					for(int y=min_y;y<=max_y;y++){
						if(b[x][y]=="."){
							b[x][y]=slf_lit;
							m[x*a+y]*=c1(b,slf_lit);
							b[x][y]=".";
						}
					}
				}
			}
			else{
				m[a/2*a+a/2]=1;
				m[(a/2-1)*a+a/2]=1;
				m[(a/2+1)*a+a/2]=1;
				m[a/2*a+a/2-1]=1;
				m[a/2*a+a/2+1]=1;
			}
			for(int x=0;x<a*a;x++){
				OptionalInt max=Arrays.stream(m).max();
				if(m[x]==max.getAsInt()&&b[x/a][x%a]=="."){
					m1.add(x);
				}
			}
			Random r=new Random();
			int rand=r.nextInt(m1.size());
			int coor= m1.get(rand);
			b[coor/a][coor%a]=slf_lit;
			last_dot=new int[]{coor/a,coor%a};
		}
		public int c1(String[][] b,String lit)
		{
			int k=0;
			for(int x=min_x;x<=max_x;x++)
			{
				for(int y=min_y;y<=max_y;y++)
				{
					for(int dx=-1;dx<=1;dx++)
					{
						for(int dy=-1;dy<=1;dy++)
						{
							try
							{
								if(b[x][y]==b[x+dx][y+dy]&&b[x][y]==lit)
								{
									k++;
								}
							}
							catch(ArrayIndexOutOfBoundsException a){}
							finally{}
						}
					}
				}
			}
			return k;
		}
		public int[] c2(String[][] b,String lit,int inp_len)
		{
			int result=0;
			ArrayList<Integer> ds=new ArrayList<Integer>();
			int dx=0,dy=0;
			for(int x=min_x;x<=max_x;x++)
			{
				for(int y=min_y;y<=max_y;y++)
				{
					for(int d=0;d<4;d++)
					{
						int[] ds1= new int[a*a];
						switch(d)
						{
							case 0:
								dx=1;dy=0;
								break;
							case 1:
								dx=0;dy=1;
								break;
							case 2:
								dx=1;dy=1;
								break;
							case 3:
								dx=1;dy=-1;
								break;
						}
						int k=0;
						int k2=0;
						for(int len=0;len<w;len++)
						{
							try
							{
								if(b[x+dx*len][y+dy*len]==lit)
								{
									k++;
								}
								if(b[x+dx*len][y+dy*len]==".")
								{
									ds1[k2]=(x+dx*len)*a+(y+dy*len);
									k2++;
								}
							}
							catch(ArrayIndexOutOfBoundsException a){}
							finally{}
						}
						if(k+k2==w && k2<=w-inp_len){
							result++;
							for(int dot=0;dot<k2;dot++){
								ds.add(ds1[dot]);
							}
						}
					}
				}
			}
			ds.add(0,result);
			int[] res_arr = new int[ds.size()];
			for(int x=0;x<ds.size();x++){
				res_arr[x]=ds.get(x);
			}
			return res_arr;
		}
	}
}


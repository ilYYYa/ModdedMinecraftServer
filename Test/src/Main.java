import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main
{

	public static void main(String[] args)
	{
		new FrameClassTest();
	}
}

class A
{
	byte b = 127;
	short b2 = 32500;
	int x;
	int y = 2100000000;
	long huge = 9000000000000000000L;
	double hp = 20.0D;
	float mp = 100.0F;
	boolean fall = false;
	boolean create = 40 < 5;
	String str = "str";
	B classB = new B();
	void func()
	{
		//some code
	}
}

class B
{
	int x = 1;
	public int y = 2;
	private int z = 3;
	static int t = 4;
	
	B()
	{
		System.out.println("This code will run, when you create B class");
	}
}

class FrameClassTest extends JFrame
{
	public FrameClassTest()
	{
		this.setTitle("Testing window");
		this.setBounds(100, 100, 1280, 720); // 100, 100 - x and y pos on screen, 1280 and 720 - width and height our window.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when exit all code stopped
		
		DoubleBuffer buff = new DoubleBuffer();
		
		this.add(buff);
		
		this.setVisible(true);
		
		while(true)
		{
			this.repaint();
		}
	}
}

class DoubleBuffer extends JPanel
{
	Listeners listeners = new Listeners();
	
	public DoubleBuffer()
	{
		this.addKeyListener(listeners);
		this.addMouseListener(listeners);
		this.addMouseMotionListener(listeners);
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.setColor(new Color(255,0,0));
		
		g.fillRect(listeners.mouseX - 50, listeners.mouseY - 50, 100, 100);
	}
}

class Listeners implements KeyListener, MouseListener, MouseMotionListener
{
	public int mouseX, mouseY = 0;
	
	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{

	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		
	}
	
}






























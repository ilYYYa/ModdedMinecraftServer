package ru.ilyyya.serverTestModification;

import java.util.ArrayList;

public class TickHandler
{
	public static ArrayList<EventHandler> handlers = new ArrayList<EventHandler>();
	
	public static void addHandler(EventHandler h)
	{
		handlers.add(h);
	}
	
	public static void removeHandler(EventHandler h)
	{
		handlers.remove(h);
	}
	
	public static void removeHandlerAt(int a)
	{
		handlers.remove(a);
	}
	
	public static void onTickUpdate()
	{
		for(int i = 0; i < handlers.size(); i++)
		{
			handlers.get(i).onTickUpdate();
		}
	}
	public static void onTickUpdateEnd()
	{
		for(int i = 0; i < handlers.size(); i++)
		{
			handlers.get(i).onTickUpdateEnd();
		}
	}
	
	public static void onEnemyUpdate()
	{
		for(int i = 0; i < handlers.size(); i++)
		{
			handlers.get(i).onEnemysUpdate();
		}
	}
	public static void onEnemyUpdateEnd()
	{
		for(int i = 0; i < handlers.size(); i++)
		{
			handlers.get(i).onEnemysUpdateEnd();
		}
	}
	
	public static void onLightsUpdate()
	{
		for(int i = 0; i < handlers.size(); i++)
		{
			handlers.get(i).onLightsUpdate();
		}
	}
	public static void onLightsUpdateEnd()
	{
		for(int i = 0; i < handlers.size(); i++)
		{
			handlers.get(i).onLightsUpdateEnd();
		}
	}
}

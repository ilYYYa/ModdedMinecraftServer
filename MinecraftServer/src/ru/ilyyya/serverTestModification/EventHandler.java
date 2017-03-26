package ru.ilyyya.serverTestModification;

public interface EventHandler
{
	public void onTickUpdate();
	public void onTickUpdateEnd();
	public void onEnemysUpdate();
	public void onEnemysUpdateEnd();
	public void onLightsUpdate();
	public void onLightsUpdateEnd();
}

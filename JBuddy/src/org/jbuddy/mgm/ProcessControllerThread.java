package org.jbuddy.mgm;

import java.util.*;

/**
 * By Leonardo Cecchi
 * leonardoce@interfree.it
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * Questo thread ha il compito di uccidere i processi che sono vivi
 * per più di un quanto di tempo deciso a priori.
 * 
 * Non ci deve essere piu' di una instanza di questo Thread e quindi
 * questa classe ha una struttura di un Singleton
 * 
 * @author Leonardo Cecchi
 */
public class ProcessControllerThread extends Thread 
{
	/**
	 * Configurazioni interne del thread
	 */
	
	// Intervallo di check del thread in millesimi di secondo
	private static int CHECK_INTERVAL=5000;
	
	// Durata massima di un processo in millesimi di secondo
	private static int MAX_PROCESS_TIME=10000;
	
	// Killing reason del processo
	private static String killing_reason="Too more time needed for this process....";
	
	/**
	 * Stato interno del thread
	 */
	private boolean shutdown;
	private static ProcessControllerThread my;
	
	/**
	 * Il costruttore non deve essere mai chiamato perche' la classe
	 * e' singleton. Per prendere l'unica instanza del thread e/o
	 * assicurarsi che il thread sia attivo chiamare il getInstance()
	 */
	private ProcessControllerThread()
	{
	}
	
	/**
	 * Controllo dei processi
	 */
	private void guardaAdesso()
	{
		Collection<BuddyProcess> processi=ProcessCollector.getInstance().getProcessVector();
		Iterator<BuddyProcess> itProcessi=processi.iterator();
		long controlTime=System.currentTimeMillis();
		
		while(itProcessi.hasNext())
		{
			BuddyProcess bp=(BuddyProcess)itProcessi.next();
			
			synchronized(bp)
			{
				bp.updateStatus();
				if(bp.getStatus()!=BuddyProcess.ST_RUNNING)
				{
					continue;
				}
				
				if((controlTime-bp.getStartingTime())>MAX_PROCESS_TIME)
				{
					BuddyLogger.getInstance().logWarning("Killed process for timeout. Command line: "+bp.getCommandLine());
					bp.kill(killing_reason);
				}
			}
		}
	}
	
	/**
	 * Vita del thread
	 */
	public void run()
	{
		while(!shutdown)
		{
			guardaAdesso();
			try
			{
				Thread.sleep(CHECK_INTERVAL);
			} catch(InterruptedException ie)
			{
				// Cosa fare qui???
				BuddyLogger.getInstance().logMsg("ProcessControllerThread interrupted!!");
			}
		}
		BuddyLogger.getInstance().logMsg("ProcessControllerThread down.");
	}
	
	/**
	 * Shutdown del thread
	 */
	public synchronized void callShutdown()
	{
		shutdown=true;
		BuddyLogger.getInstance().logMsg("ProcessControllerThread shutdown requested");
		my.interrupt();
	}
	
	/**
	 * Creazione, starting, instanze del thread
	 */
	public static ProcessControllerThread getInstance()
	{
		if(my==null)
		{
			my=new ProcessControllerThread();
			BuddyLogger.getInstance().logMsg("ProcessControllerThread started...");
			my.start();
		}
		
		return(my);
	}
}


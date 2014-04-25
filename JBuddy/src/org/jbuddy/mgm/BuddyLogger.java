package org.jbuddy.mgm;

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
 * Interfaccia logger generica per tutti i logger
 * 
 * @author Leonardo Cecchi
 */
public abstract class BuddyLogger 
{
	/**
	 * Priorita' per il logging, priorita' fra 0 e 9 estremi inclusi
	 */
	public static int WARNING_PRIORITY=5;
	public static int ERROR_PRIORITY=9;
	public static int MSG_PRIORITY=0;
	
	public abstract void logMessage(int priority, String msg);
	
	/**
	 * Stato interno
	 */
	private static BuddyLogger my=null;
	
	/**
	 * Logger con priorita' predeterminate
	 */
	public void logWarning(String msg)
	{
		logMessage(WARNING_PRIORITY, msg);
	}
	
	public void logError(String msg)
	{
		logMessage(ERROR_PRIORITY, msg);
	}
	
	public void logMsg(String msg)
	{
		logMessage(MSG_PRIORITY, msg);
	}
	
	/**
	 * Comodita' per avere un logger da usare
	 */
	public static BuddyLogger getInstance()
	{
		if(my==null)
		{
			my=new PrintLogger();
		}
		
		return(my);
	}
}


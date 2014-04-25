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
 * Gestione della collezione di processi che viene gestita dal programma
 * 
 * Una sessione puo' avere al massimo un processo.
 * 
 * Questa classe e' di template singleton
 * 
 * @author Leonardo Cecchi
 */
public class ProcessCollector 
{
	private static ProcessCollector my=null;
	private Hashtable<String, BuddyProcess> htProcessi;    // Mappatura Sessioni --> Processi
	
	/**
	 * Non usare questo costruttore. Per prendere l'unica instanza
	 * di questa classe prendere il metodo getInstance
	 */
	private ProcessCollector()
	{
		htProcessi=new Hashtable<String, BuddyProcess>();
	}
	
	/**
	 * L'unica instanza del programma
	 */
	public static ProcessCollector getInstance()
	{
		if(my==null)
		{
			my=new ProcessCollector();
		}
		
		return(my);
	}
	
	/**
	 * Ritorna un valore logicamente vero se la sessione ha un processo assegnato
	 */
	public boolean hasProcess(String sid)
	{
		return htProcessi.containsKey(sid);
	}
	
	/**
	 * Registra un nuovo processo per la sessione.
	 * Se la sessione ha gia' un processo questo metodo non fa niente e ritorna un valore logicamente falso.
	 * Se la sessione non ha un processo questo viene creato ed il metodo ritorna true
	 */
	public boolean registerProcess(String sid, BuddyProcess processo)
	{
		if(hasProcess(sid))
		{
			return false;
		}
		
		htProcessi.put(sid,processo);
		return true;
	}
	
	/**
	 * Pulisce l'entry del processo per la sessione in caso che questo
	 * sia terminato. Ritorna un valore true se l'operazione ha successo.
	 * Se no ritorna false;
	 */
	public boolean clearSessionEntry(String sid)
	{
		if(!hasProcess(sid))
		{
			return false;
		}
		
		BuddyProcess bp=(BuddyProcess) htProcessi.get(sid);
		
		bp.updateStatus();
		
		if(bp.getStatus()==BuddyProcess.ST_TERMINATED)
		{
			htProcessi.remove(sid);
			return true;
		} else
		{
			return false;
		}
	}
	
	/**
	 * Ritorna un vettore contenente tutti i processi registrati per le sessioni
	 */
	public Collection<BuddyProcess> getProcessVector()
	{
		Vector<BuddyProcess> vt=new Vector<BuddyProcess>();
		return htProcessi.values();
	}
	
	/**
	 * Prende un processo data una sessione. 
	 * Se per la sessione non esite un processo ritorna null
	 */
	public BuddyProcess getProcess(String sid)
	{
		if(hasProcess(sid))
		{
			return(htProcessi.get(sid));
		} else
		{
			return null;
		}
	}
}


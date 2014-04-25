package org.jbuddy;
import java.util.*;

// Per emacs lo stile di indent usato è ellemtel

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
 * Classe che monitora i processi.
 *
 * In maniera più semplice questa classe collezione reference ai processi
 * creati e permette la distruzione di questi tutti assieme.
 *
 * Questa classe risponde al template SingleInstance
 *
 * @author Leonardo Cecchi 
 */
public class ProcessMonitor
{
      private Vector<Process> vt;
      private static ProcessMonitor my=null;

      /**
       * Privato, ottenere una instanza di questa classe tramite
       * getInstance
       */
      private ProcessMonitor()
      {
	 super();
	 vt=new Vector<Process>();
      }

      /**
       * Da qui si ottiene l'instanza giusta di questa classe
       */
      public static ProcessMonitor getInstance()
      {
	 if(my==null)
	 {
	    my=new ProcessMonitor();
	 }

	 return(my);
      }

      /**
       * Aggiunge un processo al monitor
       */
      public void addProcess(Process x)
      {
	 if(!vt.contains(x))
	 {
	    vt.addElement(x);
	 }
      }

      /**
       * Distrugge tutti i processi nel monitor
       *
       * MOLTO PERICOLOSO: Puo' causare deadlock!!!
       */
      public void killProcesses()
      {
	 Enumeration enElements=vt.elements();

	 while(enElements.hasMoreElements())
	 {
	    Process x=(Process)enElements.nextElement();

	    x.destroy();
	    System.out.println("Process killed...");
	 }
      }

      /**
       * Elimina i processi da qui...
       *
       * Se il processo da eliminare non e' presente non fa niente
       */
      public void removeProcess(Process x)
      {
	 if(vt.contains(x))
	 {
	    vt.remove(x);
	 }
      }
}


package org.jbuddy.mgm;

import java.io.*;

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
 * Processo gestito da JBuddy. Questa classe è composta di una classe
 * processo originale, ci sono in più alcune gestioni
 * 
 * @author Leonardo Cecchi
 */
public class BuddyProcess 
{
	/**
	 * Stati dei processi
	 */
	public static int ST_INIT=1;
	public static int ST_RUNNING=2;
	public static int ST_TERMINATED=3;
	
	/**
	 * Cause di terminazione del processo
	 */
	public static int T_ND=-1;      // Non disponibile
	public static int T_NORMAL=1;
	public static int T_STOP=2;
	
	/**
	 * Causale di terminazione del processo
	 */
	public static String K_ND="ND";
	
	/**
	 * Stato interno del processo
	 */
	private int status, termination_cause, exit_value;
	private long startingTime;
	private Process myProcess;
	private String killReason;
	private String myCommandLine;
	
	/**
	 * Buffer per stdout, stderr
	 */
	private String stdout_buffer, stderr_buffer;
	private StreamGobbler stdout_gobbler, stderr_gobbler;
	
	
	
	/**
	 * Costruttore del processo, questo viene piazzato in stato ST_INIT ed in ST_RUNNING
	 */
	public BuddyProcess(String command_line) throws IOException
	{
		// Parametri essenziali del processo
		status=ST_INIT;
		termination_cause=T_ND;
		exit_value=T_ND;
		stdout_buffer="";
		stderr_buffer="";
		killReason=K_ND;
		myCommandLine=command_line;
		
		// Inizia il processo
		myProcess=Runtime.getRuntime().exec(command_line);
		stdout_gobbler=new StreamGobbler(myProcess.getInputStream());
		stderr_gobbler=new StreamGobbler(myProcess.getErrorStream());
		stdout_gobbler.start();
		stderr_gobbler.start();
		
		startingTime=System.currentTimeMillis();
		
		status=ST_RUNNING;
	}

	/**
	 * Ritorna lo stato interno del processo
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * Ritorna la decodifica in stringa dello stato interno del processo
	 */
	public String getStatusDecode()
	{
		if(status==ST_INIT)
		{
			return("Init");
		} else if(status==ST_RUNNING)
		{
			return("Running");
		} else if(status==ST_TERMINATED)
		{
			return("Terminated");
		} else
		{
			return("Unknown.");
		}
	}

	/**
	 * Ritorna la causa del termine del processo		
	 */
	public int getTerminationCause()
	{
		return termination_cause;
	}

	/**
	 * Ritorna la decodifica a stringa dalla causa di termine del processo
	 */
	public String getTerminationCauseDecode()
	{
		if(termination_cause==T_ND) 
		{
			return("ND");
		} else if(termination_cause==T_NORMAL) 
		{
			return("Normal");
		} else if(termination_cause==T_STOP)
		{
			return("Stopped");
		} else
		{
			return("Unknown.");
		}
	}
	
	/**
	 * Ritorna il valore di uscita del processo
	 */
	public int getExitValue()
	{
		return exit_value;
	}
	
	/**
	 * Aggiorna le variabili di stato della classe
	 */
	public synchronized void updateStatus()
	{
		
		stdout_buffer=stdout_gobbler.getString();
		stderr_buffer=stderr_gobbler.getString();

		if(status==ST_TERMINATED || status==ST_INIT)
		{
			return;
		}
		
		try 
		{
			exit_value=myProcess.exitValue();
			// Processo terminato...
			if(termination_cause==T_ND)
			{
				termination_cause=T_NORMAL;
			}
			status=ST_TERMINATED;
		} catch(IllegalThreadStateException e)
		{
			// Il processo non e' ancora terminato
			status=ST_RUNNING;
		}
	}
	
	/**
	 * Ritorna il buffer dell'stdout
	 */
	public String getStdoutBuffer()
	{
		return stdout_buffer;
	}	
	
	/**
	 * Ritorna il buffer dell'stderr
	 */
	public String getStderrBuffer()
	{
		return stderr_buffer;
	}
	
	/**
	 * Uccide il processo
	 */
	public synchronized void kill(String reason)
	{
		if(termination_cause==T_ND)
		{
			termination_cause=T_STOP;
			killReason=reason;
			myProcess.destroy();
		}
	}
	
	/**
	 * Ritorna l'ora di avvio del processo
	 */
	public long getStartingTime()
	{
		return(startingTime);
	}
	
	/**
	 * Ritorna il motivo del kill del processo. Se non 
	 * e' stato fatto un kill questo metodo ritorna la stringa
	 * "ND"
	 */
	public String getKillReason()
	{
		return killReason;
	}
	
	/**
	 * Ritorna la command line di invocazione del processo
	 */
	public String getCommandLine()
	{
		return myCommandLine;
	}
	
	/**
	 * Inserisce una stringa nell'stdin del processo se questo
	 * e' in stato running. Ritorna true se ci riesce
	 */
	public boolean sendString(String msg) throws IOException
	{
		if(getStatus()!=ST_RUNNING)
		{
			return(false);
		}
		
		OutputStreamWriter osw=new OutputStreamWriter(myProcess.getOutputStream());
		osw.write(msg,0,msg.length());
		osw.flush();
			
		return true;
	}
}


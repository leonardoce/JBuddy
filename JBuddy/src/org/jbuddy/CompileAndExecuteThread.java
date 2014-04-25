/**
 * JBuddy
 */

package org.jbuddy;

/**
 * Questa classe serve per compilare e eseguire un pezzo di codice Java.
 * 
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
 * Questa classe rappresenta un thread generico
 * che svolge i compiti di compilazione e esecuzione di una applicazione
 * java.
 * 
 * @author Leonardo Cecchi
 */
public class CompileAndExecuteThread extends Thread
{
	CompilerInfo jbcii;
	String txt;
	String trascode;
	int stat = 0;

	/**
	 * Questo metodo crea un nuovo thread
	 * 
	 * @param trascode Codice Java da compilare
	 */
	public CompileAndExecuteThread(String trascode)
	{
		this.trascode = trascode;
	}

	/**
	 * Sovrascrive la normale main
	 */
	public void run()
	{
		jbcii = Compilatore.compile(trascode);
		stat++;
		if (jbcii.getRetCode() == 0)
			txt = Compilatore.execute(jbcii);
	}

	/**
	 * Interrompe il/i processo/i corrente/i.......
	 */
	public void interrupt()
	{
		System.err.println("Interrotto!!!");
		ProcessMonitor.getInstance().killProcesses();
	}
	
	/**
	 * Ritorna lo stato di compilazione/esecuzione
	 * 
	 * @return (1 se siamo in fase di esecuzione) (0 se siamo in fase di compilazione)
	 */
	public int getStat()
	{
		return(stat);
	}
	
	/**
	 * Ritorna le informazioni che sono state ritornate dal
	 * compilatore
	 */
	public CompilerInfo getCompilerInfo()
	{
		return(jbcii);
	}
	
	/**
	 * Ritorna il testo che e' l'output dell'esecuzione del programma.
	 * Questo testo e' gia' preformattato per l'inserimento nella TextArea
	 */
	public String getTxt()
	{
		return(txt);
	}
}
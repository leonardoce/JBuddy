package org.jbuddy;
import java.io.*;
import java.util.*;

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
 * @author  Leonardo Cecchi
 */
public class Compilatore
{
	/**
	 * Compila un pezzo di codice rilasciando le informazioni di compilazione
	 *
	 * String --> JBuddyCompilerInfo
	 */
	public static CompilerInfo compile(String code)
	{
		String strFile = "";
		Properties config = Risorse.getInstance().getConfig();

		try
			{
			File fTemporaneo = File.createTempFile("JBuddy", ".java");
			strFile = fTemporaneo.getPath();
			String namefile = fTemporaneo.getName();
			String classname = namefile.substring(0, namefile.indexOf("."));
			int indexclass = code.indexOf("<<classname>>");
			String clearcode;
			if (indexclass != -1)
				{
				clearcode =
					code.substring(0, indexclass) + classname + code.substring(indexclass + 13);
			} else
				{
				clearcode = code;
			}
			FileWriter fw = new FileWriter(fTemporaneo);
			fw.write(clearcode);
			fw.close();
			String cmdary[] = new String[4];
			cmdary[0] = config.getProperty("javac.file");
			cmdary[1] = strFile;
			cmdary[2] = "-classpath";
			cmdary[3] = System.getProperty("java.class.path")+System.getProperty("path.separator")+
				Risorse.getInstance().getPrerequisitesClassPath();

			ProcessStatus vt = ProcessStatus.createFromCommand(cmdary);
			CompilerInfo jbci =
				new CompilerInfo(
					vt.getExitStatus(),
					"---------------\n" + vt.getProcessErrors(),
					classname,
					new File(fTemporaneo.getParentFile().getPath()));
			fTemporaneo.delete();
			return (jbci);
		} catch (IOException ex)
			{
			CompilerInfo jbci =
				new CompilerInfo(-1, "---------------\n" + ex.toString(), "", null);
			return (null);
		}
	}

	/**
	 * Esegue la classe e da il risultato
	 *
	 * JBuddyCompilerInfo --> String
	 */
	public static String execute(CompilerInfo jbci)
	{
		try
			{
			String ary[] = new String[4];
			ary[0] =
				System.getProperty("java.home")
					+ System.getProperty("file.separator")
					+ "bin"
					+ System.getProperty("file.separator")
					+ "java";
			ary[1] = "-cp";
			ary[2] =
				jbci.getTempDir().getCanonicalPath()
					+ System.getProperty("path.separator")
					+ System.getProperty("java.class.path")+System.getProperty("path.separator")+
					Risorse.getInstance().getPrerequisitesClassPath();
			ary[3] = jbci.getClassname();

			ProcessStatus vt = ProcessStatus.createFromCommand(ary);
			String message =
				"Standard Output\n"
					+ "---------------------------------------\n\n"
					+ vt.getProcessOutput()
					+ "\n\n"
					+ "Standard Errors\n"
					+ "---------------------------------------\n\n"
					+ vt.getProcessErrors()
					+ "\n\n";
			return (message);
		} catch (IOException ex)
			{
			return (ex.toString());
		}
	}

	/**
	 * Pulisce tutte le classi generate
	 */
	public static void cleanClasses(CompilerInfo jbci)
	{
		File[] files = jbci.getTempDir().listFiles();
		int i = 0;
		for (i = 0; i < files.length; i++)
		{
			File mio = files[i];
			if (mio.getName().startsWith(jbci.getClassname()))
			{
				mio.delete();
			}
		}
	}

	/**
	 * Funzioncina di test
	 */
	public static void main(String args[])
	{
		System.out.println(
			execute(new CompilerInfo(0, "", "JBuddy10519", new File("/tmp"))));
	}

}

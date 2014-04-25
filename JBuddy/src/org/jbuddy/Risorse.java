package org.jbuddy;

import java.util.*;
import java.io.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;

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
 * Tiene le risorse ed i file di configurazione di JBuddy
 */
public class Risorse 
{
	/**
	 * Stato interno della classe
	 */
	private static Risorse my;
	private ResourceBundle labels;
	private Properties config;

	/**
	 * L'unica instanza di questa classe
	 */
	public static Risorse getInstance()
	{
		if(my==null)
		{
			my=new Risorse();
		}
		
		return my;
	}
	
	/**
	 * Costruttore, legge le configurazioni e le risorse.
	 * Dato che questa classe e' singleton usare il getInstance per
	 * ottenere una instanza.
	 */
	private Risorse()
	{
		config=createConfig();
		labels=createResourceBundle(config);
	}
	
	/**
	 * Prende le labels
	 */
	public ResourceBundle getLabels()
	{
		return labels;
	}
	
	/**
	 * Prende la configurazione
	 */
	public Properties getConfig()
	{
		return config;
	}

	/**
	 * Crea la configurazione del programma
	 */
	private Properties createConfig()
	{
        Properties config=new Properties();
        
        config.setProperty("javac.file", System.getProperty("java.home")+
            System.getProperty("file.separator")+
            "bin"+
            System.getProperty("file.separator")+
            ".."+
            System.getProperty("file.separator")+
            ".."+
            System.getProperty("file.separator")+
            "bin"+
            System.getProperty("file.separator")+
            "javac");
        config.setProperty("default.locale", Locale.getDefault().getLanguage());
        config.setProperty("advanced.mode", "0");
        config.setProperty("saveonexecute","0");
        config.setProperty("default.dir", System.getProperty("user.home"));
        config.setProperty("prerequisites.count","0");        

        File configFile=new File(System.getProperty("user.home"),".jbuddysettings");
        if(configFile.exists())
        {
            try 
            {
                config.load(new FileInputStream(configFile));
            } catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        
        return(config);
	}

	/**
	 * Crea il resourceboundle per le etichette
	 */
	public ResourceBundle createResourceBundle(Properties config)
	{
		return(ResourceBundle.getBundle("JBuddy",new Locale(config.getProperty("default.locale"),"")));
	}

    /**
     * Scrive le proprieta
     */
    public void writeConfig()
    {
        File configFile=new File(System.getProperty("user.home"),".jbuddysettings");
        try
        {
            config.store(new FileOutputStream(configFile),"JBuddy Configuration");
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Carica una icona
     * @param imageName Nome dell'immagine da cercare (estensione compresa)
     * @return Instanza di Icon
     */
    public static Icon loadIcon(String imageName)
    {
        return new ImageIcon(
                Risorse.class.getClassLoader().getResource("org/jbuddy/gui/res/"+imageName));
    }
    
    /**
     * Calcola la classpath per i prerequisiti in configurazione
     * @return Stringa mai nulla
     */
    public String getPrerequisitesClassPath()
    {
        String cp="";
        int i;
        int prereqCount=Integer.parseInt(getConfig().getProperty("prerequisites.count"));
        
        for(i=0;i<prereqCount;i++)
        {
            if(cp.length()!=0)
            {
                cp+=System.getProperty("path.separator");
            }
            cp+=getConfig().getProperty("prerequisites."+i);
        }
        
        return cp;
    }
}


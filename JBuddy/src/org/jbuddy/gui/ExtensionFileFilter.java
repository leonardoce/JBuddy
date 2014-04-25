/*
 * Created on 29-mag-2005
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
 */
package org.jbuddy.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Generic extension file filter
 * @author leonardo
 */
public class ExtensionFileFilter extends FileFilter
{
    private String title, _extension;
    
    /**
     * Ritorna la descrizione
     */
    public String getDescription()
    {
		return(title);
    }

    /**
     * Filtra il contenuto
     */
    public boolean accept(File fl)
    {
		if(fl.isDirectory())
		{
		    return(true);
		}
	
		String ext=getExtension(fl);
	
		if(ext==null)
		{
		    return(false);
		}
	
		if(ext.equals(_extension))
		{
		    return(true);
		} else
		{
		    return(false);
		}
    }

    /*
     * Prende l'estensione del file
     */  
    public static String getExtension(File f) 
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) 
        {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    /**
     * Costruttore del filtro
     */
    public ExtensionFileFilter(String title, String extension)
    {
        this.title=title;
        this._extension=extension;
    }

}

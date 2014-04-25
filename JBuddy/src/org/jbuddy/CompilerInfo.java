package org.jbuddy;
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
 * Rappresenta le informazioni che vengono ritornate dall'operazione
 * di compilazione di un codice
 * 
 * @author Leonardo Cecchi
 */
public class CompilerInfo 
{
    private int retcode;
    private String message;
    private String classname;
    private File tempdir;
    
    /**
     * Costruisce una nuova classe di questo tipo
     */
    public CompilerInfo(int retcode, String message, String classname, File tempdir) 
    {
        this.retcode=retcode;
        this.message=message;
        this.classname=classname;
        this.tempdir=tempdir;
    }

    /**
     * Metodi getter
     */
    public int getRetCode() 
    {
        return(retcode);
    }
    
    public String getMessage()
    {
        return(message);
    }
    
    public String getClassname()
    {
        return(classname);
    }
    
    public File getTempDir()
    {
        return(tempdir);
    }
    
    /**
     * Metodi di pura utilita'
     */
    public String toString()
    {
        return("Exit code: "+retcode+"\n\nMessage : \n"+message);
    }
}

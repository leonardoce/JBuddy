package org.jbuddy;

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
 * Questa classe rappresenta una eccezione lanciata dal compilatore
 * Questi tipi di eccezione non vengono lanciati per segnalare errori
 * di compilazione (per questi vedi la classe CompilerInfo) ma per
 * segnalare errori di esecuzione del compilatore.
 * 
 * @author Leonardo Cecchi
 * @version 
 */
public class CompilerException extends Exception 
{
    String msg;
    
    /**
     * Crea una nuova eccezione
     */
    public CompilerException(String msg) {
        this.msg=msg;
    }
    
    /**
     * Ritorna una rappresentazione in stringa di questa
     * eccezione
     */
    public String toString()
    {
        return(msg);
    }
}



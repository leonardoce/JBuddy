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

/**
 * Filtro per i file jar
 * 
 * @author leonardo
 */
public class JarFileFilter extends ExtensionFileFilter
{
    private static final String USED_EXTENSION="jar";
    
    /**
     * Nuovo filtro per i file Jar
     * @param title Titolo da assegnare al filtro
     */
    public JarFileFilter(String title)
    {
        super(title, USED_EXTENSION);
    }
}

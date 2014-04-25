/*
 * Created on 29-mag-2005
 * By Leonardo Cecchi
 * 
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
 * @author Leonardo Cecchi
 */
package org.jbuddy.gui;

/**
 * File filter for JAVA files
 * @author leonardo
 */
public class JavaFileFilter extends ExtensionFileFilter
{
    private static final String USER_EXTENSION="java";
    
    /**
     * New Java file filter
     * @param title File filter title
     */
    public JavaFileFilter(String title)
    {
        super(title, USER_EXTENSION);
    }
}

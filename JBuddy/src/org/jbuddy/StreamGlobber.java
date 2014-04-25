/*
 * StreamGlobber.java
 *
 * Created on 17 agosto 2007, 16.46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jbuddy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

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
 * Thread che prende tutto il possibile da un InputStream
 */
class StreamGobbler extends Thread
{
	InputStream is;
	String msg = "";

	StreamGobbler(InputStream is)
	{
		this.is = is;
	}

	public void run()
	{
		try
			{
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				{
				msg += line + "\n";
			}
		} catch (IOException ioe)
			{
			ioe.printStackTrace();
		}
	}

	public String getString()
	{
		return (msg);
	}
}

/**
 * JBuddy
 */
package org.jbuddy.gui;

import org.jbuddy.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.ResourceBundle;

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
 * Classe che implementa una Status bar associata ad una JTextArea che dice la riga e la colonna
 * 
 * @author Leonardo Cecchi
 */
public class Statusbar extends JPanel implements CaretListener
{
	private JTextArea componentSource;
	private JLabel jlbMessaggio;
        private ResourceBundle labels;

	/**
	 * Costruttore della classe, richiede la JTextArea da monitorare
	 */
	public Statusbar(JTextArea jta)
	{
		super();
		
        this.labels=Risorse.getInstance().getLabels();
		componentSource=jta;
		jta.addCaretListener(this);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		jlbMessaggio=new JLabel(createMessage(0,0));
		add(jlbMessaggio);
	}

	/**
	 * Ascolta per i cambiamenti di posizione del cursore
	 */
	public void caretUpdate(CaretEvent e)
	{
		try
		{
			int posizione=e.getDot();
			int linea=componentSource.getLineOfOffset(posizione);
			int colonna=posizione-componentSource.getLineStartOffset(linea);
			jlbMessaggio.setText(createMessage(linea+1, colonna+1));
		} catch(BadLocationException ex)
		{
		}
	}

	/**
	 * Crea un messaggio per la statusbar
	 */
	public String createMessage(int line, int column)
	{
		return(labels.getString("eti_line")+
                " "+line+" -- "+labels.getString("eti_column")+" "+column);
	}
}
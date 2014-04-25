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
 * JBuddyCompilationDialog.java
 *
 * Created on March 20, 2002, 9:16 AM
 */

package org.jbuddy.gui;

import org.jbuddy.*;

import java.util.ResourceBundle;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 * Nonostante il nome questa classe rappresenta una Dialog che rende
 * visibile all'utente il processo di compilazione ed il processo di esecuzione
 * 
 * @author  Leonardo Cecchi
 */
public class CompilationDialog extends JDialog implements ActionListener
{
    private CompileAndExecuteThread cae;
    private JTextArea jta;
    private Timer taUpdate;
    private ResourceBundle labels;

    /**
     * Costruttore della Dialog....
     **/
    public CompilationDialog(JFrame padre, CompileAndExecuteThread cae) 
    {
        super(padre, Risorse.getInstance().getLabels().getString("compilation_dialog_title"), true);
        this.cae=cae;
        this.labels=Risorse.getInstance().getLabels();
        
        jta=new JTextArea(6, 40);
		jta.setFont(new Font("Monospaced",Font.PLAIN,12));
        jta.setEditable(false);
        jta.setBackground(Color.lightGray);
        jta.setText(labels.getString("compilation_starting"));
        getContentPane().setLayout(new BorderLayout());
        
        getContentPane().add("Center", new JScrollPane(jta));
        
        JPanel jpn=new JPanel();
        jpn.setLayout(new FlowLayout());
        JButton jbt=new JButton(labels.getString("compilation_dialog_interrupt"));
        jbt.setActionCommand("INTERRUPT!");
        jbt.addActionListener(this);
        jpn.add(jbt);
        
        getContentPane().add("South",jbt);
        
        taUpdate=new Timer(1000,new UpdateActionListener());
        
        pack();
    }

    /**
     * Fa partire il timer
     */
    public void startTimer()
    {
        taUpdate.start();
    }
    
    /**
     * Prende gli eventi di azione
     */
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("INTERRUPT!"))
        {
            cae.interrupt();
        }
    }

    /**
     * Costruisce una stringa di no pallino
     *
     * Integer --> String
     */
    public static String creaPallini(int no)
    {
        String buffer="";
        for(int i=0;i<no;i++)
        {
            buffer+=".";
        }
        return(buffer);
    }

    class UpdateActionListener implements ActionListener
    {
        int pallini=0;
        int prevstat=-1;
        
        public void actionPerformed(ActionEvent e)
        {
            if(cae.isAlive())
            {
                int stat=cae.getStat();
                if(prevstat!=stat)
                {
                    pallini=0;
                    prevstat=stat;
                }
                if(stat==0)
                {
                    jta.setText(labels.getString("compilation_compiling"));
                } else
                {
                    jta.setText(labels.getString("compilation_executing"));
                }
                jta.setText(jta.getText()+creaPallini(pallini));
                pallini++;
                if(pallini>10) 
                {
                    pallini=1;
                }
            } else
            {
                taUpdate.stop();
                setVisible(false);
            }
        }
    }
}

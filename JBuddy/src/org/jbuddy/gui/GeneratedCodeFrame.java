/*
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.fife.ui.RScrollPane;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxHighlightingColorScheme;
import org.jbuddy.Risorse;
import org.jbuddy.Translator;
import org.jbuddy.TranslatorException;

/**
 * Finestra per mostrare il codice generato 
 */
public class GeneratedCodeFrame extends JFrame implements ItemListener, ActionListener
{
    // Risorse
    private ResourceBundle labels;
    
    // Variabili instanza
    private String _sourceCode="";
    
    // Componenti
    private RSyntaxTextArea _rta;
    private JCheckBox _cbComments;
    private JButton _btSaveFile;
    private JFileChooser _jfc;
    
    /**
     * Costruttore di default
     */
    public GeneratedCodeFrame()
    {
        labels=Risorse.getInstance().getLabels();
        
        setTitle(labels.getString("title_gencode_window"));
        _rta=createGeneratedCodeArea();
        _cbComments=new JCheckBox(labels.getString("line_comments"));
        _cbComments.addItemListener(this);
        
        _btSaveFile=new JButton(labels.getString("bt_save_file"));
        _btSaveFile.addActionListener(this);
        
        _jfc=new JFileChooser();
        _jfc.setFileFilter(new JavaFileFilter(
                labels.getString("eti_java_file")));

        RScrollPane rsp=new RScrollPane(_rta);
        
        getContentPane().setLayout(new BorderLayout());
        JPanel pnNorthPanel=new JPanel();
        pnNorthPanel.setLayout(new BorderLayout());
        pnNorthPanel.add("Center",_cbComments);
        pnNorthPanel.add("East", _btSaveFile);
        
        getContentPane().add("North", pnNorthPanel);
        getContentPane().add("Center",rsp);

        setSize(400,400);
    }    
    
    /**
     * Imposta il testo dal quale generare il codice
     * 
     * @param txt Testo da impostare
     */
    public void setSourceCode(String txt)
    {
        _sourceCode=txt;
        refreshGeneratedCode();
    }

    /**
     * Costruisce una JTextArea per l'output del codice generato
     */
    private RSyntaxTextArea createGeneratedCodeArea()
    {
        RSyntaxTextArea jta = new RSyntaxTextArea();
        jta.setCurrentLineHighlightEnabled(false);
        jta.setSyntaxEditingStyle(RSyntaxTextArea.JAVA_SYNTAX_STYLE);
        jta.setSyntaxHighlightingColorScheme(new SyntaxHighlightingColorScheme(
                true));
        jta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        jta.setEditable(false);
        jta.setBackground(new Color(234, 234, 234));
        return (jta);
    }

    /**
     * Rigenera il codice
     */
    private void refreshGeneratedCode()
    {
        try
        {
            _rta.setText(Translator.translate(_sourceCode, 
                    _cbComments.isSelected()));
            _rta.setCaretPosition(0);
        } catch (TranslatorException ex)
        {
            _rta.setText("Error: "+ex.toString());
        }
    }
    
    /**
     * Cambiamento di stato di una checkbox
     */
    public void itemStateChanged(ItemEvent ie)
    {
        if(ie.getSource()==_cbComments)
        {
            refreshGeneratedCode();
        }
    }
    
    /**
     * ActionListener
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0)
    {
        if(arg0.getSource()==_btSaveFile)
        {
            if(_jfc.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)
            {
                return;
            }
            
            // Write file
            try
            {
                PrintWriter pw=new
                	PrintWriter(new FileOutputStream(_jfc.getSelectedFile()));
                pw.println(_rta.getText());
                pw.close();
            } catch(IOException ioe)
            {
                JOptionPane.showMessageDialog(this, 
                        "Error: "+ioe.toString());
            }
        }

    }
}

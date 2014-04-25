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
 */

package org.jbuddy.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import javax.swing.*;

import org.jbuddy.*;

/**
 * Questa classe rappresenta la dialog delle informazioni di compilazione
 * 
 * @author Leonardo Cecchi
 * @version 
 */
public class ConfigDialog extends JDialog implements ActionListener 
{
    JCheckBox jcbSaveOnExecute;
    JLFPane jlfCompiler;
    JDFPane jdfDefaultDir;
    JComboBox jcbLocale;
    
    ResourceBundle labels;
    Properties config;
    
    /**
     * Costruttore della classe
     */
    public ConfigDialog(JFrame padre) 
    {
        super(padre, Risorse.getInstance().getLabels().getString("config_title"), true);
        
        this.labels=Risorse.getInstance().getLabels();
        this.config=Risorse.getInstance().getConfig();
        
        getContentPane().setLayout(new BorderLayout());
        
        JPanel jpnBottoni=new JPanel();
        jpnBottoni.setLayout(new FlowLayout());
        jpnBottoni.add(createJButton(labels.getString("bt_ok")));
        jpnBottoni.add(createJButton(labels.getString("bt_cancel")));
        getContentPane().add("South",jpnBottoni);
        
        JPanel jpnConfig=new JPanel();
        jpnConfig.setLayout(new BoxLayout(jpnConfig, BoxLayout.Y_AXIS));
        jlfCompiler=new JLFPane(
            labels.getString("config_compiler"), 
            new File(config.getProperty("javac.file"))
            );
        jpnConfig.add(jlfCompiler);
        
        jdfDefaultDir=new JDFPane(
            labels.getString("config_directory"),
            new File(config.getProperty("default.dir"))
            );
        jpnConfig.add(jdfDefaultDir);

        JPanel jpnLocale=new JPanel();
        jpnLocale.setLayout(new FlowLayout());
        jpnLocale.add(new JLabel(labels.getString("config_locale")));
        String locales[]=
        {
            "it - Italiano",
            "en - English"
        };
        jcbLocale=new JComboBox(locales);
        jpnLocale.add(jcbLocale);
        if(config.getProperty("default.locale").equals("it"))
        {
            jcbLocale.setSelectedIndex(0);
        } else
        {
            jcbLocale.setSelectedIndex(1);
        }
        jpnConfig.add(jpnLocale);
        
        jcbSaveOnExecute=new JCheckBox(labels.getString("save_on_execute"));
        jcbSaveOnExecute.setSelected(config.getProperty("saveonexecute").equals("1"));
        jpnConfig.add(jcbSaveOnExecute);
                
        getContentPane().add("Center",jpnConfig);
        pack();
    }
    
    /**
     * Crea dei generici bottoni
     *
     * String --> JButton
     */
    private JButton createJButton(String txt)
    {
        JButton jbt=new JButton(txt);
        jbt.setActionCommand(txt);
        jbt.addActionListener(this);
        return(jbt);
    }

    /**
     * Capta gli eventi di azione generati dai bottoni
     */
    public void actionPerformed(ActionEvent ev)
    {
        if(ev.getActionCommand().equals(labels.getString("bt_ok")))
        {
            // Controllo inserimenti
            File compiler=jlfCompiler.getFile();
            if(!compiler.exists())
            {
                JOptionPane.showMessageDialog(this, labels.getString("msg_10"), 
                    "JBuddy",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            File defaultdir=jdfDefaultDir.getFile();
            if(!defaultdir.exists())
            {
                JOptionPane.showMessageDialog(this, labels.getString("msg_14"), 
                    "JBuddy",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String oldAdvanced=config.getProperty("advanced.mode");
            String oldDefaultLocale=config.getProperty("default.locale");
            
            config.setProperty("javac.file", compiler.getAbsolutePath());
            config.setProperty("default.locale", (jcbLocale.getSelectedIndex()==0?"it":"en"));
            config.setProperty("saveonexecute", (jcbSaveOnExecute.isSelected()?"1":"0"));
            config.setProperty("default.dir", defaultdir.getAbsolutePath());

            // In questi casi è necessario un riavvio dell'applicazione
            // per vedere le modifiche alla configurazione
            if(!oldAdvanced.equals(config.getProperty("advanced.mode")) ||
               !oldDefaultLocale.equals(config.getProperty("default.locale")))
            {
                JOptionPane.showMessageDialog(this, labels.getString("msg_11"), "JBuddy",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } 
        setVisible(false);
    }
    
    /**
     * Metodo getter per la configurazione
     */
    public Properties getConfiguration()
    {
        return(config);
    }
    
}

/**
 * Rappresenta un pannello contenente una label, una textfield ed un
 * pulsante per la ricerca di un file
 */
class JLFPane extends JPanel implements ActionListener
{
    File fl;
    JTextField jtf;

   /**
    * Costruttore
    */
    public JLFPane(String label, File fl)
    {
        super();
        setLayout(new FlowLayout());
        JLabel jlb=new JLabel(label);
        add(jlb);
        jtf=new JTextField(30);
        jtf.setText(fl.getAbsolutePath());
        add(jtf);
        JButton jbt=new JButton(">");
        jbt.addActionListener(this);
        add(jbt);
        this.fl=fl;
    }

    /**
     * Raccoglie gli eventi di azione
     */
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser jfc=new JFileChooser();
        if(jfc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
        {
            fl=jfc.getSelectedFile();
            jtf.setText(fl.getAbsolutePath());
        }
    }

    /**
     * Metodo getter del file
     */
    public File getFile()
    {
        return(new File(jtf.getText()));
    }
}

/**
 * Classe JFDPane 
 * Implementa una JTextField ed un pulsante che permette di scegliere una directory
 */
class JDFPane extends JPanel implements ActionListener
{
    File fl;
    JTextField jtf;

   /**
    * Costruttore
    */
    public JDFPane(String label, File fl)
    {
        super();
        setLayout(new FlowLayout());
        JLabel jlb=new JLabel(label);
        add(jlb);
        jtf=new JTextField(39);
        jtf.setText(fl.getAbsolutePath());
        add(jtf);
        JButton jbt=new JButton(">");
        jbt.addActionListener(this);
        add(jbt);
        this.fl=fl;
    }

    /**
     * Raccoglie gli eventi di azione
     */
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser jfc=new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setFileFilter(new DirFilter("Directories"));
        if(jfc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
        {
            fl=jfc.getSelectedFile();
            jtf.setText(fl.getAbsolutePath());
        }
    }

    /**
     * Metodo getter del file
     */
    public File getFile()
    {
        return(new File(jtf.getText()));
    }
}

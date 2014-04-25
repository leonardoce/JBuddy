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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.jbuddy.Risorse;

/**
 * Gestione dei prerequisiti per l'esecuzione.
 */
public class ManagePrerequisite extends JDialog implements ActionListener
{
    // Risorse
    private ResourceBundle labels;
    
    // Componenti
    private JList _lsPrerequisites;
    private JButton _btClose, _btAddJar, _btAddDir, _btDelete;
    private DefaultListModel _lsPrerequisitesModel;
    private JFileChooser _jfc;
    private Properties _config;
    
    /**
     * Nuova finestra di configurazione delle dipendenze.
     * La finestra in questione è modale
     * @param parent Finestra da bloccare
     */
    public ManagePrerequisite(Frame parent)
    {
        super(parent);
        
        _jfc=new JFileChooser();
        _config=Risorse.getInstance().getConfig();
        labels=Risorse.getInstance().getLabels();
        setTitle(labels.getString("title_prerequisite"));
        setModal(true);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("Center",createCenterPanel());
        getContentPane().add("South",createSouthPanel());
        
        pack();
    }
    
    /**
     * Creazione del pannello centrale alla finestra
     * @return Pannello centrale della finestra
     */
    private JPanel createCenterPanel()
    {
        JPanel jpn=new JPanel();
        jpn.setLayout(new BorderLayout());
        
        _lsPrerequisites=new JList();
        _lsPrerequisites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _lsPrerequisitesModel=new DefaultListModel();
        _lsPrerequisites.setModel(_lsPrerequisitesModel);
        jpn.add("Center", new JScrollPane(_lsPrerequisites));
        
        loadPrerequisites();
        return jpn;
    }
    
    /**
     * Creazione del pannello a sud
     * @return Pannello basso della finestra
     */
    private JPanel createSouthPanel()
    {
        JPanel jpn=new JPanel();
        jpn.setLayout(new FlowLayout());
        
        _btAddJar=new JButton(labels.getString("bt_add_jar"),
                Risorse.loadIcon("BeanAdd24.gif"));
        _btAddDir=new JButton(labels.getString("bt_add_dir"),
                Risorse.loadIcon("BeanAdd24.gif"));
        _btDelete=new JButton(labels.getString("bt_delete_prerequisite"),
                Risorse.loadIcon("Delete24.gif"));
        _btClose=new JButton(labels.getString("bt_close_save"),
                Risorse.loadIcon("Stop24.gif"));
        
        jpn.add(_btAddJar);
        jpn.add(_btAddDir);
        jpn.add(_btDelete);
        jpn.add(_btClose);
        
        _btAddDir.addActionListener(this);
        _btAddJar.addActionListener(this);
        _btDelete.addActionListener(this);
        _btClose.addActionListener(this);
        
        return jpn;
    }
    
    /**
     * Carica nel componente JList la lista dei prerequisiti
     */
    private void loadPrerequisites()
    {
        int prerequisitesCount=Integer.parseInt(_config.getProperty("prerequisites.count"));
        int i;

        _lsPrerequisitesModel.clear();
        
        for(i=0;i<prerequisitesCount;i++)
        {
            _lsPrerequisitesModel.addElement(_config.getProperty("prerequisites."+i));
        }
    }
    
    /**
     * ActionListener
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0)
    {
        Object source=arg0.getSource();
        
        if(source==_btClose)
        {
            saveSettings();
            setVisible(false);
        } else if(source==_btAddDir)
        {
            // Add directory
            _jfc.setFileFilter(new DirFilter(labels.getString("eti_prerequisite_directory")));
            _jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(_jfc.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)
            {
                return;
            }
            // Must exists and represent a directory
            if(!_jfc.getSelectedFile().isDirectory() || !_jfc.getSelectedFile().exists())
            {
                return;
            }
            _lsPrerequisitesModel.addElement(_jfc.getSelectedFile().getAbsolutePath());
        } else if(source==_btAddJar)
        {
            // Add jar
            _jfc.setFileFilter(new JarFileFilter(labels.getString("eti_prerequisite_jar")));
            _jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if(_jfc.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)
            {
                return;
            }
            // Must exists and must is a file
            if(!_jfc.getSelectedFile().isFile() || !_jfc.getSelectedFile().exists())
            {
                return;
            }
            _lsPrerequisitesModel.addElement(_jfc.getSelectedFile().getAbsolutePath());
        } else if(source==_btDelete)
        {
            // Delete
            if(_lsPrerequisites.getSelectedIndex()==-1)
            {
                JOptionPane.showMessageDialog(this, labels.getString("eti_select_prerequisite"));
                return;
            }
            _lsPrerequisitesModel.removeElementAt(_lsPrerequisites.getSelectedIndex());
        }
    }
    
    /**
     * Save settings
     */
    private void saveSettings()
    {
        // Delete old settings
        int prerequisiteCount=Integer.parseInt(_config.getProperty("prerequisites.count"));
        int i;
        
        for(i=0;i<prerequisiteCount;i++)
        {
            _config.remove("prerequisites."+i);
        }
        
        prerequisiteCount=_lsPrerequisitesModel.getSize();
        _config.setProperty("prerequisites.count",""+prerequisiteCount);
        for(i=0;i<prerequisiteCount;i++)
        {
            _config.setProperty("prerequisites."+i,_lsPrerequisitesModel.elementAt(i).toString());
        }
        Risorse.getInstance().writeConfig();
    }
}

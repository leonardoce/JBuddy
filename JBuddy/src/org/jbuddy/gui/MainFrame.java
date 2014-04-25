package org.jbuddy.gui;

import org.jbuddy.*;
import org.jbuddy.util.indent.VanCBJ;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxHighlightingColorScheme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.RTextArea;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.help.CSH;
import javax.help.HelpSet;
import javax.swing.*;
import javax.swing.event.*;

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
 * Questa classe rappresenta il Frame principale dell'applicazione JBuddy.
 * 
 * La classe main e' la Application, non usare questa classe come classe Main
 * 
 * @author Leonardo Cecchi
 */
public class MainFrame extends JFrame implements ActionListener
{
    // Parametri di funzionamento
    private static Color ERROR_AREA_BACK=new Color(255, 253, 150);
    private static Color OUTPUT_AREA_BACK=new Color(167, 187, 221);
    
    JTextArea jtaBottomArea;
    JSplitPane spSplitter;
    GeneratedCodeFrame _generatedCodeFrame;

    // Area del codice
    RSyntaxTextArea jtaCode;
    boolean isSaved = true;

    File fileOpened = null;
    ResourceBundle labels;
    Properties config;

    /**
     * Costruttore della classe, provvede ad inizializzare i componenti
     */
    public MainFrame()
    {
        super();
        
        _generatedCodeFrame=new GeneratedCodeFrame();

        labels=loadLabels();
        loadLabels();
        this.config = Risorse.getInstance().getConfig();

        setFrameTitle();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createJMenuBar());

        /**
         * Area centrale finestra
         */
        JPanel jpnCode = new JPanel();
        JPanel jpnErrors = new JPanel();
        JPanel jpnOutput = new JPanel();

        jpnCode.setLayout(new BorderLayout());
        jtaCode=createCodeArea();
        RTextScrollPane scrollJtaCode=new RTextScrollPane(100, 100, (RTextArea)jtaCode, true);
        jpnCode.add("Center", scrollJtaCode);

        /**
         * Area output e errori
         */
        jtaBottomArea=createBottomArea();
        JScrollPane spBottomArea=new JScrollPane(jtaBottomArea);

        getContentPane().setLayout(new BorderLayout());
        spSplitter=new JSplitPane();
        spSplitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spSplitter.setLeftComponent(jpnCode);
        spSplitter.setRightComponent(spBottomArea);
        spSplitter.setOneTouchExpandable(true);
        spSplitter.setResizeWeight(1);
        getContentPane().add("Center", spSplitter);

        
        /**
         * Toolbar
         */
        JToolBar jAlto = new JToolBar();
        createJButton(labels.getString("bt_gencode"));

        jAlto.add(createJButton(labels.getString("mi_new"), "New24.gif", false));
        jAlto.addSeparator();
        jAlto.add(createJButton(labels.getString("bt_open"), "Open24.gif", false));
        jAlto.add(createJButton(labels.getString("bt_save"), "Save24.gif", false));
        jAlto.addSeparator();
        jAlto.add(createJButton(labels.getString("bt_execute"), "Play24.gif", false));
        jAlto.addSeparator();
        jAlto.add(createJButton(labels.getString("mi_undo"), "Undo24.gif", false));
        jAlto.add(createJButton(labels.getString("mi_redo"), "Redo24.gif", false));
        jAlto.addSeparator();
        jAlto.add(createJButton(labels.getString("mi_cut"), "Cut24.gif", false));
        jAlto.add(createJButton(labels.getString("mi_copy"), "Copy24.gif", false));
        jAlto.add(createJButton(labels.getString("mi_paste"), "Paste24.gif", false));
        jAlto.addSeparator();
        jAlto.add(createJButton(labels.getString("mi_exit"), "Stop24.gif", false));

        getContentPane().add("North", jAlto);

        setSize(500, 400);

    }

    /**
     *  Load Labels
     */
    public ResourceBundle loadLabels()
    {
        return Risorse.getInstance().getLabels();
    }

    /**
     * Costruisce una JMenuItem e la registra
     * 
     * String -> JMenuItem
     */
    public JMenuItem generaJMenuItem(String testo)
    {
        JMenuItem jmi = new JMenuItem(testo);
        jmi.setActionCommand(testo);
        jmi.addActionListener(this);
        return (jmi);
    }

    /**
     * Gestisce gli eventi di azione di questa JFrame implementando
     * ActionListener
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if (command.equals(labels.getString("mi_exit")))
        {
            if (!isSaved)
            {
                JOptionPane jop = new JOptionPane();
                if (JOptionPane.showConfirmDialog(this, labels
                        .getString("msg_13"), "JBuddy",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                {
                    saveFile();
                }
            }
            System.exit(0);
        } else if (command.equals(labels.getString("mi_open")))
        {
            openFile();
        } else if (command.equals(labels.getString("mi_new")))
        {
            newFile();
        } else if (command.equals(labels.getString("mi_save")))
        {
            saveFile();
        } else if (command.equals(labels.getString("mi_saveas")))
        {
            saveAsFile();
        } else if (command.equals(labels.getString("mi_gencode")))
        {
            generateCodeWindow();
        } else if (command.equals(labels.getString("mi_about")))
        {
            aboutDialog();
        } else if (command.equals(labels.getString("mi_execute")))
        {
            if (config.getProperty("saveonexecute").equals("1"))
            {
                saveFile();
            }
            executeCode();
        } else if (command.equals(labels.getString("mi_config")))
        {
            configDialog();
        } else if (command.equals(labels.getString("mi_cut")))
        {
            jtaCode.cut();
        } else if (command.equals(labels.getString("mi_copy")))
        {
            jtaCode.copy();
        } else if (command.equals(labels.getString("mi_paste")))
        {
            jtaCode.paste();
        } else if (command.equals(labels.getString("mi_delete")))
        {
            int beginDelete;
            int lenDelete;

            if (jtaCode.getSelectedText() != null)
            {
                beginDelete = jtaCode.getSelectionStart();
                lenDelete = jtaCode.getSelectedText().length();
            } else
            {
                beginDelete = jtaCode.getCaretPosition();
                lenDelete = 1;
            }

            try
            {
                jtaCode.getDocument().remove(beginDelete, lenDelete);
            } catch (javax.swing.text.BadLocationException ble)
            {
            }
        } else if(command.equals(labels.getString("mi_undo")))
        {
            jtaCode.undoLastAction();
        } else if(command.equals(labels.getString("mi_redo")))
        {
            jtaCode.redoLastAction();
        } else if(command.equals(labels.getString("mi_indent")))
        {
            jtaCode.setText(VanCBJ.indentaCodice(jtaCode.getText()));
        } else if(command.equals(labels.getString("mi_prerequisite")))
        {
            // Prerequisite management
            ManagePrerequisite mp=new ManagePrerequisite(this);
            mp.setVisible(true);
            mp.dispose();
        }
    }

    /**
     * Costruisce una JMenuBar adatta per l'applicazione
     */
    public JMenuBar createJMenuBar()
    {
        JMenuBar jmb = new JMenuBar();
        JMenu file = new JMenu(labels.getString("mnu_file"));
        file.setMnemonic(KeyEvent.VK_F);
        JMenu edit = new JMenu(labels.getString("mnu_edit"));
        edit.setMnemonic(KeyEvent.VK_D);
        JMenu help = new JMenu(labels.getString("mnu_help"));

        JMenuItem jmiNew = generaJMenuItem(labels.getString("mi_new"));
        jmiNew.setMnemonic(KeyEvent.VK_N);
        jmiNew.setIcon(Risorse.loadIcon("New24.gif"));
        file.add(jmiNew);

        file.addSeparator();

        JMenuItem jmiOpen = generaJMenuItem(labels.getString("mi_open"));
        jmiOpen.setMnemonic(KeyEvent.VK_O);
        jmiOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                ActionEvent.CTRL_MASK));
        jmiOpen.setIcon(Risorse.loadIcon("Open24.gif"));
        file.add(jmiOpen);

        JMenuItem jmiSave = generaJMenuItem(labels.getString("mi_save"));
        jmiSave.setMnemonic(KeyEvent.VK_S);
        jmiSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.CTRL_MASK));
        jmiSave.setIcon(Risorse.loadIcon("Save24.gif"));
        file.add(jmiSave);

        JMenuItem jmiSaveAs = generaJMenuItem(labels.getString("mi_saveas"));
        jmiSaveAs.setMnemonic(KeyEvent.VK_A);
        jmiSaveAs.setIcon(Risorse.loadIcon("SaveAs24.gif"));
        file.add(jmiSaveAs);

        file.addSeparator();

        JMenuItem jmiExecute = generaJMenuItem(labels.getString("mi_execute"));
        jmiExecute.setMnemonic(KeyEvent.VK_E);
        jmiExecute.setIcon(Risorse.loadIcon("Play24.gif"));
        jmiExecute.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                ActionEvent.CTRL_MASK));
        file.add(jmiExecute);

        file.addSeparator();

        JMenuItem jmiExit=generaJMenuItem(labels.getString("mi_exit"));
        jmiExit.setIcon(Risorse.loadIcon("Stop24.gif"));
        file.add(jmiExit);

        JMenuItem jmiUndo = generaJMenuItem(labels.getString("mi_undo"));
        jmiUndo.setMnemonic(KeyEvent.VK_C);
        jmiUndo.setIcon(Risorse.loadIcon("Undo24.gif"));
        jmiUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                ActionEvent.CTRL_MASK));
        edit.add(jmiUndo);
        
        JMenuItem jmiRedo = generaJMenuItem(labels.getString("mi_redo"));
        jmiRedo.setMnemonic(KeyEvent.VK_Y);
        jmiRedo.setIcon(Risorse.loadIcon("Redo24.gif"));
        jmiRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                ActionEvent.CTRL_MASK));
        edit.add(jmiRedo);
        
        edit.addSeparator();
        
        JMenuItem jmiCut = generaJMenuItem(labels.getString("mi_cut"));
        jmiCut.setMnemonic(KeyEvent.VK_C);
        jmiCut.setIcon(Risorse.loadIcon("Cut24.gif"));
        jmiCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                ActionEvent.CTRL_MASK));
        edit.add(jmiCut);

        JMenuItem jmiCopy = generaJMenuItem(labels.getString("mi_copy"));
        jmiCopy.setMnemonic(KeyEvent.VK_C);
        jmiCopy.setIcon(Risorse.loadIcon("Copy24.gif"));
        jmiCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                ActionEvent.CTRL_MASK));

        edit.add(jmiCopy);

        JMenuItem jmiPaste = generaJMenuItem(labels.getString("mi_paste"));
        jmiPaste.setMnemonic(KeyEvent.VK_P);
        jmiPaste.setIcon(Risorse.loadIcon("Paste24.gif"));
        jmiPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                ActionEvent.CTRL_MASK));

        edit.add(jmiPaste);

        JMenuItem jmiDelete = generaJMenuItem(labels.getString("mi_delete"));
        jmiDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        jmiDelete.setIcon(Risorse.loadIcon("Delete24.gif"));
        edit.add(jmiDelete);

        edit.addSeparator();

        JMenuItem jmiConfig = generaJMenuItem(labels.getString("mi_config"));
        jmiConfig.setIcon(Risorse.loadIcon("Preferences24.gif"));
        edit.add(jmiConfig);

        JMenuItem jmiGuida = generaJMenuItem(labels.getString("mi_guide"));
        jmiGuida.setIcon(Risorse.loadIcon("Help24.gif"));
        jmiGuida.addActionListener(new CSH.DisplayHelpFromSource(createGuideHelpSet().createHelpBroker()));
        help.add(jmiGuida);
        
        help.addSeparator();
        
        JMenuItem jmiAbout = generaJMenuItem(labels.getString("mi_about"));
        jmiAbout.setIcon(Risorse.loadIcon("Information24.gif"));
        help.add(jmiAbout);

        JMenu tools=new JMenu(labels.getString("mnu_tools"));
        tools.setMnemonic(KeyEvent.VK_T);
        
        JMenuItem jmiViewCode = generaJMenuItem(labels.getString("mi_gencode"));
        jmiViewCode.setMnemonic(KeyEvent.VK_G);
        jmiViewCode.setIcon(Risorse.loadIcon("ZoomIn24.gif"));
        tools.add(jmiViewCode);

        JMenuItem jmiIndent=generaJMenuItem(labels.getString("mi_indent"));
        jmiIndent.setMnemonic(KeyEvent.VK_I);
        jmiIndent.setIcon(Risorse.loadIcon("AlignJustify24.gif"));
        tools.add(jmiIndent);
        
        JMenuItem jmiPrerequisite=generaJMenuItem(labels.getString("mi_prerequisite"));
        jmiPrerequisite.setMnemonic(KeyEvent.VK_P);
        jmiPrerequisite.setIcon(Risorse.loadIcon("Bean24.gif"));
        tools.add(jmiPrerequisite);
        
        jmb.add(file);
        jmb.add(edit);
        jmb.add(tools);
        jmb.add(help);

        return (jmb);
    }

    /**
     * Costruisce una JTextArea per il programma
     */
    public RSyntaxTextArea createCodeArea()
    {
        RSyntaxTextArea jta = new RSyntaxTextArea();
        jta.setAutoIndentEnabled(true);
        jta.setCurrentLineHighlightEnabled(false);
        jta.setSyntaxEditingStyle(RSyntaxTextArea.JAVA_SYNTAX_STYLE);
        jta.setSyntaxHighlightingColorScheme(new SyntaxHighlightingColorScheme(
                true));
        jta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        jta.getDocument().addDocumentListener(new JBuddyDocumentListener());
        return (jta);
    }

    /**
     * Costruisce una JTextArea per gli errori
     */
    public JTextArea createBottomArea()
    {
        JTextArea jta = new JTextArea();
        jta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        jta.setForeground(Color.black);
        jta.setBackground(new Color(255, 253, 150));
        jta.setEditable(false);
        return (jta);
    }

    /**
     * Costruisce un JButton poi lo registra String -> JButton
     */
    public JButton createJButton(String txt)
    {
        return createJButton(txt,null, true);
    }
    
    /**
     * Costruisce un JButton poi lo registra String -> JButton
     */
    public JButton createJButton(String txt, String img, boolean textVisible)
    {        
        JButton jbt = new JButton();
        if(textVisible)
        {
            jbt.setText(txt);
        } else
        {
            jbt.setText("");
            jbt.setToolTipText(txt);
        }
        if(img!=null)
        {
            jbt.setIcon(Risorse.loadIcon(img));
        }
        jbt.setActionCommand(txt);
        jbt.addActionListener(this);
        return (jbt);
    }


    /**
     * Set frame title
     */
    public void setFrameTitle()
    {
        setTitle(labels.getString("title_pre") + Application.version + " -- "
                + (isSaved ? "" : "**")
                + (fileOpened == null ? "noname.buddy" : fileOpened.getName()));
    }

    /**
     * File open
     */
    public void openFile()
    {
        if (!isSaved)
        {
            int st = JOptionPane
                    .showConfirmDialog(this, labels.getString("msg_1"),
                            "JBuddy", JOptionPane.OK_CANCEL_OPTION);

            if (st != JOptionPane.OK_OPTION)
            {
                return;
            }
        }
        JFileChooser jfc = new JFileChooser();
        jfc.addChoosableFileFilter(new JmmFileFilter(labels
                .getString("title_filter")));
        jfc.setCurrentDirectory(new File(config.getProperty("default.dir")));
        if (jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
        {
            return;
        }
        File fl = jfc.getSelectedFile();
        String buffer = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fl));
            String str;
            while ((str = br.readLine()) != null)
            {
                buffer += str + "\n";
            }
            br.close();
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, labels.getString("msg_2"),
                    "JBuddy", JOptionPane.ERROR_MESSAGE);
            return;
        }
        jtaCode.setText(buffer);
        jtaCode.setCaretPosition(0);
        isSaved = true;
        fileOpened = fl;
        setFrameTitle();
    }

    /**
     * Procedura di salvataggio di un file
     */
    private void saveFile()
    {
        if (fileOpened == null)
        {
            saveAsFile();
            return;
        }
        if (isSaved)
        {
            return;
        }

        try
        {
            FileWriter fw = new FileWriter(fileOpened);
            jtaCode.write(fw);
            fw.close();
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, labels.getString("msg_3"),
                    "JBuddy", JOptionPane.ERROR_MESSAGE);
            return;
        }
        isSaved = true;
        setFrameTitle();
    }

    /**
     * Salvataggio di un file con la possibilita di cambiare nome
     */
    public void saveAsFile()
    {
        if (fileOpened != null && isSaved)
        {
            return;
        }
        JFileChooser jfc = new JFileChooser();
        jfc.addChoosableFileFilter(new JmmFileFilter(labels
                .getString("title_filter")));
        jfc.setCurrentDirectory(new File(config.getProperty("default.dir")));
        if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
        {
            return;
        }
        File fl = jfc.getSelectedFile();

        try
        {
            fileOpened = fl;
            FileWriter fw = new FileWriter(fileOpened);
            jtaCode.write(fw);
            fw.close();
            isSaved = true;
            setFrameTitle();
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, labels.getString("msg_3"),
                    "JBuddy", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Classe interna che ascolta per le modifiche al documento
     */
    class JBuddyDocumentListener implements DocumentListener
    {
        public void insertUpdate(DocumentEvent e)
        {
            mandaInfo();
        }

        public void removeUpdate(DocumentEvent e)
        {
            mandaInfo();
        }

        public void changedUpdate(DocumentEvent e)
        {
            mandaInfo();
        }

        public void mandaInfo()
        {
            if(_generatedCodeFrame.isVisible())
            {
                _generatedCodeFrame.setSourceCode(jtaCode.getText());
            }
            if (isSaved)
            {
                isSaved = false;
                setFrameTitle();
            }
        }
    }

    /**
     * Genera il codice, lo mette nella TextArea relativa e sposta il JTab per
     * far vedere all'utente il codice
     */
    public void generateCodeWindow()
    {
        if(_generatedCodeFrame.isVisible())
        {
            _generatedCodeFrame.setVisible(false);
        } else
        {
            _generatedCodeFrame.setVisible(true);
            _generatedCodeFrame.setSourceCode(jtaCode.getText());
            _generatedCodeFrame.requestFocus();
        }
    }

    /**
     * Fa partire l'about dialog
     */
    private void aboutDialog()
    {
        JOptionPane.showMessageDialog(this, labels.getString("about_1") + " "
                + Application.version + "\n" + labels.getString("about_2")+"\n\n"+
                labels.getString("about_3"),
                "About JBuddy", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Genera il codice, e lo esegue
     */
    public void executeCode()
    {
        // Almeno 150 pixel servono per vedere gli errori
        if(Math.abs(spSplitter.getDividerLocation()-spSplitter.getSize().height)<150)
        {
            spSplitter.setDividerLocation(spSplitter.getSize().height-151);
        }

        jtaBottomArea.setBackground(ERROR_AREA_BACK);
        jtaBottomArea.setText("");
        final String trascode;
        try
        {
            trascode = Translator.translate(jtaCode.getText(), true);
        } catch (TranslatorException ex)
        {
            jtaBottomArea.setText(ex.toString());
            jtaBottomArea.setCaretPosition(0);
            return;
        }

        CompileAndExecuteThread thCompileAndExecute = new CompileAndExecuteThread(
                trascode);
        thCompileAndExecute.start();
        CompilationDialog jbcd = new CompilationDialog(this,
                thCompileAndExecute);
        jbcd.startTimer();
        jbcd.setVisible(true);

        CompilerInfo jbci = thCompileAndExecute.getCompilerInfo();

        if (jbci == null)
        {
            return;
        } else
        {
            if (jbci.getRetCode() != 0)
            {
                jtaBottomArea.setText(jbci.getMessage());
                jtaBottomArea.setCaretPosition(0);
                return;
            }

            jtaBottomArea.setBackground(OUTPUT_AREA_BACK);
            jtaBottomArea.setText(thCompileAndExecute.getTxt());
            jtaBottomArea.setCaretPosition(0);

            Compilatore.cleanClasses(jbci);
        }
    }

    /**
     * Azzeramento del contenuto del buffer di scrittura
     */
    public void newFile()
    {
        if (!isSaved)
        {
            int st = JOptionPane
                    .showConfirmDialog(this, labels.getString("msg_1"),
                            "JBuddy", JOptionPane.OK_CANCEL_OPTION);

            if (st != JOptionPane.OK_OPTION)
            {
                return;
            }
        }
        jtaCode.setText("");
        isSaved = true;
        fileOpened = null;
        setFrameTitle();
    }

    /**
     * Apre la config dialog
     */
    public void configDialog()
    {
        ConfigDialog jbcd = new ConfigDialog(this);
        jbcd.setVisible(true);
        config = jbcd.getConfiguration();
        Risorse.getInstance().writeConfig();
    }

    /**
     * Ritorna il set di help della guida
     */
    private HelpSet createGuideHelpSet()
    {
        HelpSet hs;
        
        String helpHS = "org/jbuddy/help/it/jbuddy.hs";
        ClassLoader cl = this.getClass().getClassLoader();
        try {
            URL hsURL = HelpSet.findHelpSet(cl, helpHS);
            hs = new HelpSet(cl, hsURL);
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "Errore nell\'aprire la guida: "+ee.getMessage());
            return null;
        }
        
        return hs;
    }
}
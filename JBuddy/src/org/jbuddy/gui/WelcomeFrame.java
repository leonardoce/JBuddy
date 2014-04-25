/*
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
import java.net.*;
import javax.swing.*;

import org.jbuddy.Risorse;

/**
 * Finestra di benvenuto
 * @author  Leonardo Cecchi
 */
public class WelcomeFrame extends javax.swing.JFrame 
{    
    private javax.swing.JLabel lbLicenza;
    private javax.swing.JLabel lbLogo;

    /**
     *  Nuova form
     */
    public WelcomeFrame() 
    {
        initComponents();
        lbLicenza.setText(Risorse.getInstance().getLabels().getString("welcome_info")+Application.version);
        
        int window_width;
        int window_height;
        
        // Loading del logo se presente
        URL logo_url=this.getClass().getClassLoader().getResource("org/jbuddy/gui/res/tommaxlogo.png");
        if(logo_url!=null)
        {
            ImageIcon ii=new ImageIcon(logo_url);
            lbLogo.setText("");
            lbLogo.setIcon(ii);
            lbLogo.setSize(ii.getIconWidth(), ii.getIconHeight());
            window_width=ii.getIconWidth();
            window_height=ii.getIconHeight()+lbLicenza.getHeight()+20;
        } else
        {
            window_width=400;
            window_height=200;
        }
        
        /**
         * Il codice che sta qui sotto serve per fare in modo che 
         * la finestra di Welcome appaia nel centro dello schermo.
         * Non sò se in Windows questa cosa succede di default... in
         * linux (con la mia configurazione) sicuramente no
         */
        int screen_width=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
        int screen_height=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
        setLocation((screen_width-window_width)/2, (screen_height-window_height)/2);
        setSize(window_width,window_height);
    }
    
    /** 
     * Inizializzazione componenti
     */
    private void initComponents() 
    {
        lbLogo = new javax.swing.JLabel();
        lbLicenza = new javax.swing.JLabel();
        lbLicenza.setHorizontalAlignment(JLabel.CENTER);

        setResizable(false);
        setUndecorated(true);

        lbLogo.setFont(new java.awt.Font("Default", 1, 36));
        lbLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbLogo.setText("JBuddy");
        lbLogo.setMaximumSize(new java.awt.Dimension(400, 200));
        lbLogo.setMinimumSize(new java.awt.Dimension(400, 200));
        lbLogo.setPreferredSize(new java.awt.Dimension(400, 200));
        getContentPane().add(lbLogo, java.awt.BorderLayout.CENTER);
        getContentPane().add(lbLicenza, java.awt.BorderLayout.SOUTH);
        this.getContentPane().setBackground(new Color(230,230,230));
    }
}

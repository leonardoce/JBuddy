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
 * 
 * Il vento che stasera suona attento
 * -ricorda un forte scotere di lame-
 * gli strumenti dei fitti alberi e spazza
 * l'orizzonte di rame
 * dove strisce di luce si protendono
 * come aquiloni al cielo che rimbomba
 * (Nuvole in viaggio, chiari
 * reami di lassu'! D'alti Eldoradi
 * malchiuse porte!)
 * e il mare che scaglia a scaglia,
 * livido, muta colore
 * lancia a terra una tromba
 * di schiume intorte;
 * il vento che nasce e muore
 * nell'ora che lenta s'annera
 * suonasse te pure stasera
 * scordato strumento,
 * cuore.
 * 
 * (Eugenio Montale - Corno Inglese (Ossi di Seppia))
 * 
 */

/**
 * Classe Main del progetto. 
 * Sezione GUI.
 */

package org.jbuddy.gui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe generale, gestione locale e parametri
 * 
 * @author Leonardo Cecchi
 */
public class Application
{
    public static String version="2.1";

    public static void main(String args[])
    {
        try
        {
            UIManager.setLookAndFeel(new org.jvnet.substance.SubstanceLookAndFeel());
        } catch (UnsupportedLookAndFeelException ulafe) {
            System.out.println("Substance failed to set");
        } catch (Exception cnfe) {
            System.out.println("Substance not found");
        }

        
        // Finestra Welcome
        WelcomeFrame wf=new WelcomeFrame();
        wf.setVisible(true);
        
        // Attesa....
        try
        {
            Thread.sleep(3000);
        } catch(InterruptedException ie)
        {
            // niente
        }
        
        // Via con la finestra principale
        wf.setVisible(false);
        wf.dispose();
        
        MainFrame jbf=new MainFrame();
        jbf.setVisible(true);
    }
}

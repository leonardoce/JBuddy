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
 * Per me si va ne la citta` dolente,
 *   per me si va ne l'etterno dolore,
 *   per me si va tra la perduta gente.
 * 
 * ...
 * 
 * Dinanzi a me non fuor cose create
 *   se non etterne, e io etterno duro.
 *   Lasciate ogne speranza, voi ch'intrate".
 * 
 * (Dante Alighieri / Divina Commedia / Inferno / Canto III)
 */

package org.jbuddy;

import java.io.*;
import java.util.*;

/**
 * Classe che trasforma il codice JBuddy in codice Java
 */
public class Translator
{
    private static String strIntestazione=
            "/**\n"+
            " * JBuddy Generated Code\n"+
            " */\n\n";
    
    private final static int ST_FREECODE=1;
    private final static int ST_FREEMETHOD=2;
    private final static int ST_FREECLASS=3;
    private final static int ST_COMMENT=4;
    private final static int ST_FREECLASS_COMMENT=5;
    private final static int ST_FREEMETHOD_COMMENT=6;
    
    /**
     * Converte una stringa rappresentante un codice JBuddy
     * nella corrispettiva stringa rappresentante codice Java
     * Se il parametro booleano è vero aggiunge ad ogni riga un commento con il
     * numero di riga originale
     *
     * String X boolean X ResourceBundle --> String
     */
    public static String translate(String strJBuddy, boolean linecomments) throws TranslatorException
    {
    	ResourceBundle labels=Risorse.getInstance().getLabels();
    	
            Vector<String> vtOrig=new Vector<String>();
            try
            {
                    BufferedReader br=new BufferedReader(new StringReader(strJBuddy));
                    String str;
                    while((str=br.readLine())!=null)
                    {
                            vtOrig.addElement(str);
                    }
                    br.close();
            } catch(IOException e)
            {
                    // Non deve accadere mai.....
                    throw new TranslatorException(labels.getString("msg_9"));
            }

            Vector<String> vtFreeCode=new Vector<String>();
            Vector<String> vtFreeMethods=new Vector<String>();
            Vector<String> vtClasses=new Vector<String>();
            Vector<String> vtImports=new Vector<String>();

            Enumeration enRighe=vtOrig.elements();
            int iLineNo=1;
            int iParentesisLevel=0;
            int iCircleLevel=0;
            int curr_state=ST_FREECODE;

            try {
                while(enRighe.hasMoreElements())
                {
                    String strCurrentLine=enRighe.nextElement().toString();
                    String trasLine=strCurrentLine;
                    if(linecomments)
                    {
                        trasLine+=" // JBUDDY LINE "+iLineNo;
                    }
                    trasLine+="\n";
                    
                    switch(curr_state)
                    {
                        case ST_FREECODE :
                            if(StringHandler.stringTrim(strCurrentLine).startsWith("/*"))
                            {
                                curr_state=ST_COMMENT;
                                break;
                            }
                            if(StringHandler.isStartMethod(strCurrentLine))
                            {
                                vtFreeMethods.addElement("static "+trasLine);
                                curr_state=ST_FREEMETHOD;
                                break;
                            }
                            if(StringHandler.isStartClass(strCurrentLine, labels))
                            {
                                vtClasses.addElement(trasLine);
                                curr_state=ST_FREECLASS;
                                break;
                            }
                            if(StringHandler.stringTrim(strCurrentLine).startsWith("import "))
                            {
                                vtImports.addElement(trasLine);
                                curr_state=ST_FREECODE;
                                break;
                            }
                            vtFreeCode.addElement(trasLine);
                            break;
                        case ST_FREEMETHOD :
                            if(StringHandler.stringTrim(strCurrentLine).startsWith("/*"))
                            {
                                vtFreeMethods.addElement(strCurrentLine+"\n");
                                curr_state=ST_FREEMETHOD_COMMENT;
                                break;
                            }
                            if(iParentesisLevel==1 && StringHandler.stringTrim(strCurrentLine).equals("}"))
                            {
                                vtFreeMethods.addElement(trasLine);
                                curr_state=ST_FREECODE;
                                break;
                            }
                            vtFreeMethods.addElement(trasLine);
                            break;
                        case ST_COMMENT :
                            if(StringHandler.stringTrim(strCurrentLine).endsWith("*/"))
                            {
                                curr_state=ST_FREECODE;
                                iLineNo++;
                                continue; // EVITA QUESTA RIGA
                            }
                            curr_state=ST_COMMENT;
                            break;
                        case ST_FREECLASS :
                            if(StringHandler.stringTrim(strCurrentLine).startsWith("/*"))
                            {
                                vtClasses.addElement(strCurrentLine+"\n");
                                curr_state=ST_FREECLASS_COMMENT;
                                break;
                            }
                            if(iParentesisLevel==1 && StringHandler.stringTrim(strCurrentLine).equals("}"))
                            {
                                vtClasses.addElement(trasLine);
                                curr_state=ST_FREECODE;
                                break;
                            }
                            vtClasses.addElement(trasLine);
                            curr_state=ST_FREECLASS;
                            break;
                        case ST_FREECLASS_COMMENT :
                            vtClasses.addElement(strCurrentLine+"\n");
                            if(StringHandler.stringTrim(strCurrentLine).endsWith("*/"))
                            {
                                curr_state=ST_FREECLASS;
                                iLineNo++;
                                continue; // EVITA QUESTA RIGA
                            }
                            curr_state=ST_FREECLASS_COMMENT;
                            break;
                        case ST_FREEMETHOD_COMMENT :
                            vtFreeMethods.addElement(strCurrentLine+"\n");
                            if(StringHandler.stringTrim(strCurrentLine).endsWith("*/"))
                            {
                                curr_state=ST_FREEMETHOD;
                                iLineNo++;
                                continue; // EVITA QUESTA RIGA
                            }
                            curr_state=ST_FREEMETHOD_COMMENT;
                            break;
                    }
                    
                    if(curr_state!=ST_COMMENT && 
                       curr_state!=ST_FREECLASS_COMMENT &&
                       curr_state!=ST_FREEMETHOD_COMMENT)
                    {
                        StringHandlerInfo jshi=StringHandler.getStringInfo(strCurrentLine);
                        if(jshi.graffaAperta)
                        {
                            iParentesisLevel++;
                        } 
                        if(jshi.graffaChiusa)
                        {
                            iParentesisLevel--;
                        }
                        iCircleLevel+=jshi.tondeAperte-jshi.tondeChiuse;
                    }
                    iLineNo++;
                }
            } catch(TranslatorException ex)
            {
                String message=ex.toString();
                throw new TranslatorException(labels.getString("eti_line")+" "+iLineNo+": "+message);
            }
            
            String translated=""+strIntestazione;
            
            Enumeration enImports=vtImports.elements();
            while(enImports.hasMoreElements())
            {
                translated+=enImports.nextElement().toString();
            }
            
            translated+="\n\npublic class <<classname>>\n";
            translated+="{\n";
            translated+="   public static void main(String args[])\n";
            translated+="   {\n";
            
            Enumeration enFreeCode=vtFreeCode.elements();
            while(enFreeCode.hasMoreElements())
            {
                translated+="       "+enFreeCode.nextElement().toString();
            }
            translated+="   }\n\n";
            
            Enumeration enFreeMethods=vtFreeMethods.elements();
            while(enFreeMethods.hasMoreElements())
            {
                translated+="   "+enFreeMethods.nextElement().toString();
            }
            
            translated+="}\n\n";
            
            Enumeration enClasses=vtClasses.elements();
            while(enClasses.hasMoreElements())
            {
                translated+=enClasses.nextElement().toString();
            }
            
            return(translated);
    }

    /**
     * Funzione di comodo, di default la precedente genera i numeri di riga
     */
    public static String translate(String strJBuddy) throws TranslatorException
    {
        return(translate(strJBuddy, true));
    }
}

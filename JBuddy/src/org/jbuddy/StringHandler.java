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
import java.util.*;


/**
 * Macchina a stati finiti per l'interpretazione delle stringhe.
 * 
 * @author Leonardo Cecchi
 */
public class StringHandler 
{
    /**
     * Stati della macchina
     */
    public final static int ST_CODE=1;
    public final static int ST_SINGLECHARSTRING=2;
    public final static int ST_FORCEDSINGLECHARSTRING=3;
    public final static int ST_MULTILINEENDWARN=4;
    public final static int ST_MULTILINECOMM=5;
    public final static int ST_MULTILINEWARNING=6;
    public final static int ST_EOLCOMMENT=7;
    public final static int ST_STRING=8;
    public final static int ST_FORCEDSTRING=9;
    
    /**
     * Ritorna una serie di informazioni sulla stringa con la quale è
     * stato eseguito.
     * 
     * String --> StringHandlerInfo
     *
     * Può emettere una eccezione di parsing.
     */
    public static StringHandlerInfo getStringInfo(String original) throws TranslatorException
    {
    	ResourceBundle labels=Risorse.getInstance().getLabels();
    	
        String transformedString=translate(original);
        if(transformedString.indexOf("\\")!=-1)
        {
            throw new TranslatorException(labels.getString("msg_12"));
        }
        int iGraffeAperte=getCharNoString(transformedString, '{');
        int iGraffeChiuse=getCharNoString(transformedString, '}');
        int iTondeAperte=getCharNoString(transformedString, '(');
        int iTondeChiuse=getCharNoString(transformedString, ')');
        if(iGraffeAperte>1)
        {
            throw new TranslatorException(labels.getString("msg_4"));
        }
        if(iGraffeChiuse>1)
        {
            throw new TranslatorException(labels.getString("msg_5"));
        }
        StringHandlerInfo jshi=new StringHandlerInfo(
            iGraffeAperte==1,
            iGraffeChiuse==1,
            iTondeAperte,
            iTondeChiuse
            );
        return(jshi);
    }
    
    /**
     * Trasforma una stringa togliendo i commenti come definiti sopra e le
     * stringhe
     */
    public static String translate(String original) throws TranslatorException
    {
    	ResourceBundle labels=Risorse.getInstance().getLabels();
    	
        char currChar;
        int currIndex=0;
        int curr_state=ST_CODE;
        String buffer="";
        while(currIndex<original.length())
        {
            currChar=original.charAt(currIndex);
            switch(curr_state)
            {
                case ST_CODE:
                    switch(currChar)
                    {
                        case '\'' :
                            curr_state=ST_SINGLECHARSTRING;
                            currIndex++;
                            break;
                        case '\"' :
                            curr_state=ST_STRING;
                            currIndex++;
                            break;
                        case '/' :
                            curr_state=ST_MULTILINEWARNING;
                            currIndex++;
                            break;
                        default :
                            buffer+=""+currChar;
                            currIndex++;
                            break;
                    }
                    break;
                case ST_SINGLECHARSTRING :
                    switch(currChar)
                    {
                        case '\\' :
                            curr_state=ST_FORCEDSINGLECHARSTRING;
                            currIndex++;
                            break;
                        case '\'' :
                            currIndex++;
                            curr_state=ST_CODE;
                            break;
                        default :
                            currIndex++;
                            break;
                    }
                    break;
                case ST_FORCEDSINGLECHARSTRING :
                    curr_state=ST_SINGLECHARSTRING;
                    currIndex++;
                    break;
                case ST_STRING :
                    switch(currChar)
                    {
                        case '\"' :
                            currIndex++;
                            curr_state=ST_CODE;
                            break;
                        case '\\' :
                            currIndex++;
                            curr_state=ST_FORCEDSTRING;
                            break;
                        default :
                            currIndex++;
                            break;
                    }
                    break;
                case ST_FORCEDSTRING :
                    currIndex++;
                    curr_state=ST_STRING;
                    break;
                case ST_MULTILINEWARNING :
                    switch(currChar)
                    {
                        case '/' :
                            currIndex++;
                            curr_state=ST_EOLCOMMENT;
                            break;
                        case '*' :
                            currIndex++;
                            curr_state=ST_MULTILINECOMM;
                            break;
                        default :
                            buffer+="/"+currChar;
                            currIndex++;
                            curr_state=ST_CODE;
                    }
                    break;
                case ST_EOLCOMMENT :
                    currIndex++;
                    break;
                case ST_MULTILINECOMM:
                    switch(currChar)
                    {
                        case '*' :
                            currIndex++;
                            curr_state=ST_MULTILINEENDWARN;
                            break;
                        default :
                            currIndex++;
                            break;
                    }
                    break;
                case ST_MULTILINEENDWARN :
                    switch(currChar)
                    {
                        case '/' :
                            currIndex++;
                            curr_state=ST_CODE;
                            break;
                        default :
                            buffer+="*"+currChar;
                            currIndex++;
                            break;
                    }
                    break;
            }
        }
        
        if(curr_state!=ST_CODE && curr_state!=ST_EOLCOMMENT)
        {
            if(curr_state==ST_STRING || curr_state==ST_SINGLECHARSTRING)
            {
                throw new TranslatorException(labels.getString("msg_6"));
            }
            if(curr_state==ST_MULTILINECOMM || curr_state==ST_MULTILINEENDWARN)
            {
                throw new TranslatorException(labels.getString("msg_7"));
            }
            throw new TranslatorException(labels.getString("msg_8")+curr_state);
        }
        
        return(buffer);
    }


    /**
     * Trasforma una stringa togliendo i commenti
     *
     * String --> String
     */
    public static String removeComments(String original, ResourceBundle labels) throws TranslatorException
    {
        char currChar;
        int currIndex=0;
        int curr_state=ST_CODE;
        String buffer="";
        while(currIndex<original.length())
        {
            currChar=original.charAt(currIndex);
            switch(curr_state)
            {
                case ST_CODE:
                    switch(currChar)
                    {
                        case '\'' :
                            curr_state=ST_SINGLECHARSTRING;
                            currIndex++;
                            buffer+=""+currChar;
                            break;
                        case '\"' :
                            curr_state=ST_STRING;
                            buffer+=""+currChar;
                            currIndex++;
                            break;
                        case '/' :
                            curr_state=ST_MULTILINEWARNING;
                            currIndex++;
                            break;
                        default :
                            buffer+=""+currChar;
                            currIndex++;
                            break;
                    }
                    break;
                case ST_SINGLECHARSTRING :
                    switch(currChar)
                    {
                        case '\\' :
                            curr_state=ST_FORCEDSINGLECHARSTRING;
                            buffer+=""+currChar;
                            currIndex++;
                            break;
                        case '\'' :
                            currIndex++;
                            curr_state=ST_CODE;
                            buffer+=""+currChar;
                            break;
                        default :
                            currIndex++;
                            buffer+=""+currChar;
                            break;
                    }
                    break;
                case ST_FORCEDSINGLECHARSTRING :
                    curr_state=ST_SINGLECHARSTRING;
                    currIndex++;
                    buffer+=""+currChar;
                    break;
                case ST_STRING :
                    switch(currChar)
                    {
                        case '\"' :
                            currIndex++;
                            curr_state=ST_CODE;
                            buffer+=""+currChar;
                            break;
                        case '\\' :
                            currIndex++;
                            curr_state=ST_FORCEDSTRING;
                            buffer+=""+currChar;
                            break;
                        default :
                            currIndex++;
                            buffer+=""+currChar;
                            break;
                    }
                    break;
                case ST_FORCEDSTRING :
                    currIndex++;
                    buffer+=""+currChar;
                    curr_state=ST_STRING;
                    break;
                case ST_MULTILINEWARNING :
                    switch(currChar)
                    {
                        case '/' :
                            currIndex++;
                            curr_state=ST_EOLCOMMENT;
                            break;
                        case '*' :
                            currIndex++;
                            curr_state=ST_MULTILINECOMM;
                            break;
                        default :
                            buffer+="/"+currChar;
                            currIndex++;
                            curr_state=ST_CODE;
                    }
                    break;
                case ST_EOLCOMMENT :
                    currIndex++;
                    break;
                case ST_MULTILINECOMM:
                    switch(currChar)
                    {
                        case '*' :
                            currIndex++;
                            curr_state=ST_MULTILINEENDWARN;
                            break;
                        default :
                            currIndex++;
                            break;
                    }
                    break;
                case ST_MULTILINEENDWARN :
                    switch(currChar)
                    {
                        case '/' :
                            currIndex++;
                            curr_state=ST_CODE;
                            break;
                        default :
                            buffer+="*"+currChar;
                            currIndex++;
                            break;
                    }
                    break;
            }
        }
        if(curr_state!=ST_CODE && curr_state!=ST_EOLCOMMENT)
        {
            if(curr_state==ST_STRING || curr_state==ST_SINGLECHARSTRING)
            {
                throw new TranslatorException(labels.getString("msg_6"));
            }
            if(curr_state==ST_MULTILINECOMM || curr_state==ST_MULTILINEENDWARN)
            {
                throw new TranslatorException(labels.getString("msg_7"));
            }
            throw new TranslatorException(labels.getString("msg_8")+" "+curr_state);
        }
        
        return(buffer);
    }


    /**
     * Ritorna un valore vero se considera un carattere "pulito"
     * in questa implementazione i caratteri sporchi sono lo spazio
     * e la tabulazione
     *
     * char --> boolean
     */
    private static boolean isClean(char x)
    {
        return(x!=' ' && x!='\t');
    }
    
    /**
     * Pulisce una stringa di codice Java, ovvero procede a togliere
     * i commenti e gli spazi all'inizio che non servono
     *
     * String --> String
     */
    private static String stringClean(String x, ResourceBundle labels) throws TranslatorException
    {
        String k=removeComments(x, labels);
        return(stringTrim(k));
    }
    
    /**
     * Pulesce una stringa di codice Java dagli spazi all'inizio ed alla fine
     */
    public static String stringTrim(String x)
    {   
        String k=x;
        while(!k.equals("") && !isClean(k.charAt(0)))
        {
            k=k.substring(1);
        }
        while(!k.equals("") && !isClean(k.charAt(k.length()-1)))
        {
            k=k.substring(0,k.length()-1);
        }
        return(k);
    }


    /**
     * Ritorna un valore booleano se quella riga rappresenta un inizio
     * di una classe, rimane da verificare che questa sia a "livello zero"
     */
    public static boolean isStartClass(String x, ResourceBundle labels) throws TranslatorException
    {
        return(stringClean(x, labels).startsWith("class "));
    }
    
    /**
     * Ritorna un valore boolean se quella riga di codice rappresenta un
     * inizio di una funzione (rimane da controllare se la riga di codice
     * suddetta è a "livello zero"
     */
    public static boolean isStartMethod(String x) throws TranslatorException
    {
        String strtrim=stringTrim(x);
        int idxGraffaAperta=x.indexOf('(');
        if(idxGraffaAperta<=0) return false;
        String strSettorePrimario=x.substring(0,idxGraffaAperta);
        if((new StringTokenizer(strSettorePrimario)).countTokens()!=2) return(false);
        if(strtrim.startsWith("new")) return(false);
        if(strtrim.startsWith("}") || strtrim.startsWith("{")) return(false);
        if(strSettorePrimario.indexOf('=')!=-1) return(false);
        return(true);
    }
    
    /**
     * Piccolo programma che serve per provare la classe sottostante
     */
    public static void main(String args[])
    {
        /*try
        {
            //System.out.println(JBuddyStringHandler.translate(""));
        } catch(JBuddyTranslatorException e)
        {
            System.out.println(e.toString());
        }*/
    }


    
    /**
     * Conta il numero di caratteri in una stringa
     *
     * String X char --> int
     */
    public static int getCharNoString(String str, char cc)
    {
        int no=0;
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i)==cc)
            {
                no++;
            }
        }
        return(no);
    }
}

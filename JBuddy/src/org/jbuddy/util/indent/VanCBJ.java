package org.jbuddy.util.indent;

/**
 * @(#)VanCBJ.java Version 4.2 1998/05/13
 *     VanCBJ.java Version 4.1 1998/05/06
 *     VanCBJ.java Version 4.0 1997/09/01
 *     Code Beautifier for C/C++
 * Copyright (c) 1997, 1998 Van Di-Han HO. All Rights Reserved.
 *
 * Email: starkville@geocities.com
 * @author Van Di-Han HO
 * @version 4.2 Date: 13 May 1998
 * 
 */
/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA. This General Public License
 * does not permit incorporating your program into proprietary programs. If your
 * program is a subroutine library, you may consider it more useful to permit
 * linking proprietary applications with the library. If this is what you want
 * to do, use the GNU Library General Public License instead of this License.
 */
import java.io.*;
import java.util.Date;

public class VanCBJ
{
    // Nuove cose
    String stringaInput;
    String currentStatus="";
    StringBuffer outputBuffer=new StringBuffer();
    
    public static String indentaCodice(String originale)
    {
        VanCBJ van=new VanCBJ();
        van.stringaInput=originale;
        van.VanCBJ_Start();
        String output=van.outputBuffer.toString();
        output=output.replaceAll("}[\t\n ]*else", "} else");
        output=output.replaceAll("}[\t\n ]*catch", "} catch");
        return output;
    }

    // Vecchie cose
    String Copyright_1997_Van_Di_Han_HO;
    String Copyright_1998_Van_Di_Han_HO;

    String Van_sinput;
    int    Van_indent=1;   
    String Van_cDotDot="\t";

    String Van_Version;
    Date   Van_StartDate;
    Date   Van_StopDate;
    int    Van_MonthAge;
    int    Van_EOF;

    BufferedInputStream bin = null;
    int nBytesRead;
    byte bArray[];
    String Van_sblock;
    int Van_idxblock;
    int Van_LineLen;
    int Van_LineNo;
    int Van_MaxLine;
    int s_level[];
    int c_level;
    int sp_flg[][];
    int s_ind[][];
    int s_if_lev[];
    int s_if_flg[];
    int if_lev;
    int if_flg;
    int level;
    int ind[];
    int e_flg;
    int paren; 
    static int p_flg[];
    char l_char;
    char p_char;
    int a_flg;
    int ct;
    int s_tabs[][];
    int q_flg;
    String w_if_;
    String w_else;
    String w_for;
    String w_ds;
    String w_case;
    String w_cpp_comment;
    String w_jdoc;
    int jdoc;
    int j;
    int BLOCK_MAXLEN;
    char string[];
    byte bstring[];
    byte bblank;
    char cc;
    int s_flg;
    int b_flg;
    int peek;    /* char */
    char peekc;    /* char */
    int tabs;
    char next_char, last_char;
    char lastc0, lastc1;    /* Van_: previous line */
    char c, c0;
    char w_kptr;

    String line_feed;
    /* temporary */
    static int outfil;

    private VanCBJ() 
    {
    }

    public void comment()
    {
        int save_s_flg;
        save_s_flg = s_flg;
        int done = 0;
rep:
        c = string[j++] = getchr();        /* get one extra char for * */
        while(done == 0)
        {

            c = string[j++] = getchr();
            while(c != '/')
            {
                if(c == '\n' || c == '\r') 
                {
                    Van_LineNo++;
                    putcoms();
                    s_flg = 1;                    
                }
                c = string[j++] = getchr();
            }
            String tmpstr = new String(string);
            if (j>1 && string[j-2] == '*') 
            {
                done = 1;
                jdoc = 0;
            }



        }
        /* s_flg = 1; /* Van_2.1d */
        putcoms();
        s_flg = save_s_flg;
        jdoc = 0;
        return;

    }

    private char get_string()
    {
        char ch;
        ch = '*';
        int beg_done = 0;
        while (true)        /* beg_done == 0) */
        {
            switch(ch)
            {
            default:
                ch = string[j++] = getchr();
                if(ch == '\\')
                {
                    string[j++] = getchr();
                    break ;
                }
                if(ch == '\'' || ch == '"')
                {
                    cc = string[j++] = getchr();
                    while(cc != ch)
                    {
                        if(cc == '\\')
                            string[j++] = getchr();
                        cc = string[j++] = getchr();
                    }
                    break ;
                }
                if(ch == '\n' || ch == '\r')
                {
                    indent_puts();
                    a_flg = 1;
                    break ;
                }
                else return(ch);
            }
        }
    }

    private void 
        indent_puts() 
    {
        string[j] = '\0';
        if(j > 0)
        {
            if(s_flg != 0)
            {
                if((tabs > 0) && (string[0] != '{') && (a_flg == 1)) 
                {
                    tabs++;
                }
                p_tabs();
                s_flg = 0;
                if((tabs > 0) && (string[0] != '{') && (a_flg == 1)) 
                {
                    tabs--;
                }
                a_flg = 0;
            }
            //        fprintf(outfil,"%s",string);
            String j_string = new String(string);
            outputBuffer.append(j_string.substring(0,j));
            for (int i=0; i<j; i++) string[i] = '\0';
            j = 0; 

        }
        else
        {
            if(s_flg != 0)
            {
                s_flg = 0;
                a_flg = 0;
            }
        }
    }
    
    private void fprintf(int outfil, String out_string) 
    {
        outputBuffer.append(out_string);
    }



    private void VanCBJ_Start() 
    {


        StringBuffer onechar;

        /* Initialization */
        Van_LineNo = 0;
        BLOCK_MAXLEN = 256;
        //Van_indent = 4;
        c_level = if_lev = level = e_flg = paren = 0;
        a_flg = q_flg = j = b_flg = tabs = 0;
        if_flg = peek = -1; 
        peekc = '`';
        s_flg = 1;
        bblank = ' ';
        jdoc = 0;

        s_level  = new int[10];
        sp_flg   = new int[20][10];
        s_ind    = new int[20][10];
        s_if_lev = new int[10];
        s_if_flg = new int[10];
        ind      = new int[10];
        p_flg    = new int[10];
        s_tabs   = new int[20][10];

        w_else = new String ("else");
        w_if_ = new String ("if");
        w_for = new String ("for");
        w_ds  = new String ("default");
        w_case  = new String ("case");
        w_cpp_comment = new String ("//");
        w_jdoc = new String ("/**");
        line_feed = new String ("\n");

        InputStream is = new StringBufferInputStream(stringaInput);
        bin = new BufferedInputStream(is); 

        // now read as long as there is something to read
        Van_EOF = 0;        /* =1 set in getchr when reach en-of-file */

        bArray = new byte[BLOCK_MAXLEN];
        string = new char[BLOCK_MAXLEN];
        try                         /* the whole process */
        {

            for (int ib=0; ib<BLOCK_MAXLEN; ib++) bArray[ib] = '\0';
            Van_LineLen = nBytesRead = 0;
            // read up a block - remember how many bytes read
            nBytesRead = bin.read(bArray);
            Van_sblock = new String(bArray,0);

            Van_LineLen = nBytesRead;
            Van_LineNo  = 1;
            Van_idxblock = -1;  
            j = 0;
            while(Van_EOF == 0)            /* bin.available() > 0) */
            {
                /*
                 * while ( Van_idxblock < Van_LineLen ) {
                 */
                c = getchr();
                switch(c)
                {
                default:
                    string[j++] = c;
                    if(c != ',')
                    {
                        l_char = c;
                    }
                    break;
                case ' ':
                case '\t':
                    if(lookup(w_else) == 1)
                    {
                        gotelse();
                        if(s_flg == 0 || j > 0)string[j++] = c;
                        indent_puts();
                        s_flg = 0;
                        break;
                    }
                    if(s_flg == 0 || j > 0)string[j++] = c;
                    break;
                case '\r':                    /* <CR> for MS Windows 95 */
                case '\n': 
                    /* 4.06Van_debug */
                    Van_LineNo++;
                    currentStatus="VanCBJ processing line: "+Van_LineNo;
                    if (Van_EOF==1) 
                    {
                        break;
                    }
                    String j_string = new String(string);

                    e_flg = lookup(w_else);
                    if(e_flg == 1) gotelse();
                    /* 4.06Van_debug */
                    if (lookup_com(w_cpp_comment) == 1) 
                    {

                        if (string[j] == '\n')
                        {
                            string[j] = '\0';
                            j--;
                        }
                    }

                    indent_puts();
                    /*
                     * System.out.println(""); if (Van_WriteFile == 1) {
                     * bstring[0] = (byte) '\r'; / * \n for Unix * /
                     * bout.write(bstring, 0, 1); }
                     */
                    fprintf(outfil, line_feed);
                    s_flg = 1;
                    if(e_flg == 1)
                    {
                        p_flg[level]++;
                        tabs++;
                    }
                    else
                        if(p_char == l_char)
                        {
                            a_flg = 1;
                        }
                    break;
                case '{':
                    if(lookup(w_else) == 1)gotelse();
                    s_if_lev[c_level] = if_lev;
                    s_if_flg[c_level] = if_flg;
                    if_lev = if_flg = 0;
                    c_level++;
                    if(s_flg == 1 && p_flg[level] != 0)
                    {
                        p_flg[level]--;
                        tabs--;
                    }
                    string[j++] = c;
                    indent_puts();
                    getnl() ;
                    /*
                     * 4.06 if (getnl() == 1) { peek = 1; peekc= '\n'; }
                     */
                    indent_puts();
                    fprintf(outfil,"\n");
                    tabs++;
                    s_flg = 1;
                    if(p_flg[level] > 0)
                    {
                        ind[level] = 1;
                        level++;
                        s_level[level] = c_level;
                    }
                    break;
                case '}':

                    c_level--;
                    if (c_level < 0)
                    {
                        Van_EOF = 1;
                        string[j++] = c;
                        indent_puts();
                        break;
                    }
                    if((if_lev = s_if_lev[c_level]-1) < 0)if_lev = 0;
                    if_flg = s_if_flg[c_level];
                    indent_puts();
                    tabs--;
                    p_tabs();
                    peekc = getchr();
                    if( peekc == ';')
                    {
                        //              fprintf(outfil,"%c;",c);
                        onechar = new StringBuffer();
                        onechar.append(c);                        /* } */
                        onechar.append(';');
                        fprintf(outfil, onechar.toString());
                        peek = -1;
                        peekc = '`';
                    }
                    else 
                    {
                        // fprintf(outfil,"%c",c);
                        onechar = new StringBuffer();
                        onechar.append(c);
                        fprintf(outfil, onechar.toString());
                        peek = 1;   /* 4.06V no! put back newline for getnl */
                    }
                    getnl();
                    /*
                     * if (getnl()==1) { peek = 1; peekc = '\n'; }
                     */
                    indent_puts();
                    
                    fprintf(outfil,"\n"); /* 4.06V */
                        
                    s_flg = 1;
                    if(c_level < s_level[level])
                        if(level > 0) level--;
                    if(ind[level] != 0)
                    {
                        tabs -= p_flg[level];
                        p_flg[level] = 0;
                        ind[level] = 0;
                    }
                    break;
                case '"':
                case '\'':
                    string[j++] = c;
                    cc = getchr();
                    while(cc != c)
                    {
                        /* Van_: max. length of line should be 256 */
                        /* if (ck_MAXLEN()) exit(90); */
                        string[j++] = cc;

                        if(cc == '\\')
                        {
                            cc = string[j++] = getchr();
                        }
                        if(cc == '\n')
                        {
                            Van_LineNo++;
                            indent_puts();
                            s_flg = 1;
                        }
                        cc = getchr();

                    }
                    string[j++] = cc;
                    if(getnl() == 1)
                    {
                        l_char = cc;
                        peek = 1;
                        peekc = '\n';
                    }
                    break;
                case ';':
                    string[j++] = c;
                    indent_puts(); 
                    if(p_flg[level] > 0 && ind[level] == 0)
                    {
                        tabs -= p_flg[level];
                        p_flg[level] = 0;
                    }
                    getnl();
                    indent_puts();
                    fprintf(outfil,"\n");  
                    s_flg = 1; 
                    if(if_lev > 0)
                        if(if_flg == 1)
                        {
                            if_lev--;
                            if_flg = 0;
                        }
                        else if_lev = 0;
                    break;
                case '\\':
                    string[j++] = c;
                    string[j++] = getchr();
                    break;
                case '?':
                    q_flg = 1;
                    string[j++] = c;
                    break;
                case ':':
                    string[j++] = c;
                    peekc = getchr();
                    if(peekc == ':')
                    {
                        indent_puts();
                        fprintf (outfil,":");
                        peek = -1;
                        peekc = '`';
                        break;
                    }
                    else 
                    {
                        int double_colon = 0;
                        peek = 1;
                    }

                    if(q_flg == 1)
                    {
                        q_flg = 0;
                        break;
                    }
                    if(lookup(w_ds) == 0 && lookup(w_case) == 0)
                    {
                        s_flg = 0;
                        indent_puts();
                    }
                    else
                    {
                        tabs--;
                        indent_puts();
                        tabs++;
                    }
                    peekc = getchr();
                    if(peekc == ';')
                    {
                        fprintf(outfil,";");
                        peek = -1;
                        peekc = '`';
                    }
                    else
                    {
                        peek = 1;
                    }
                    getnl();
                    indent_puts();
                    fprintf(outfil,"\n");
                    s_flg = 1;
                    break;

                case '/':
                    c0 = string[j];
                    string[j++] = c;
                    peekc = getchr();

                    if(peekc == '/')
                    {
                        string[j++] = peekc;
                        peekc = '`';
                        peek = -1;
                        cpp_comment();
                        fprintf(outfil,"\n");
                        break;
                    }
                    else
                    {
                        peek = 1;
                    }

                    //                       peekc = getchr();
                    if(peekc != '*') {
                        break;
                    }
                    else                                         
                    /* if (peekc == '*') */
                    {
                        if (j > 0) string[j--] = '\0';
                        if (j > 0) indent_puts(); 
                        string[j++] = '/';
                        string[j++] = '*';
                        peek = -1;
                        peekc = '`';
                        comment();
                        break;
                    }
                case '#':
                    string[j++] = c;
                    cc = getchr();
                    while(cc != '\n')
                    {
                        string[j++] = cc;
                        cc = getchr();
                    }
                    string[j++] = cc;
                    s_flg = 0;
                    indent_puts();
                    s_flg = 1;
                    break;
                case ')':
                    paren--;
                    if (paren < 0)
                    {
                        Van_EOF = 1;
                    }
                    string[j++] = c;
                    indent_puts();
                    if(getnl() == 1)
                    {
                        peekc = '\n';
                        peek = 1;
                        if(paren != 0)
                        {
                            a_flg = 1;
                        }
                        else if(tabs > 0)
                        {
                            p_flg[level]++;
                            tabs++;
                            ind[level] = 0;
                        }
                    }
                    break;
                case '(':
                    string[j++] = c;
                    paren++;
                    if ((lookup(w_for) == 1))
                    {
                        c = get_string();
                        while(c != ';') c = get_string();
                        ct=0;
                        int for_done = 0;
                        while (for_done==0)
                        {
cont:
                            c = get_string();
                            while(c != ')')
                            {
                                if(c == '(') ct++;
                                c = get_string();
                            }
                            if(ct != 0)
                            {
                                ct--;
                                //            goto cont;
                            }
                            else for_done = 1;
                        }                        /* endwhile for_done */
                        paren--;
                        if (paren < 0)
                        {
                            Van_EOF = 1;
                        }
                        indent_puts();
                        if(getnl() == 1)
                        {
                            peekc = '\n';
                            peek = 1;
                            p_flg[level]++;
                            tabs++;
                            ind[level] = 0;
                        }
                        break;
                    }

                    if(lookup(w_if_) == 1)
                    {
                        indent_puts();
                        /* tabs++; /*VDHPlay */
                        s_tabs[c_level][if_lev] = tabs;
                        sp_flg[c_level][if_lev] = p_flg[level];
                        s_ind[c_level][if_lev] = ind[level];
                        if_lev++;
                        if_flg = 1;
                    }
                }                /* end switch */
                //                    Van_idxblock++;
                /* } /* end while Van_idxblock < Van_LineLen */

                String j_string = new String(string);

            } // end while not Van_EOF
            bin.close( );
        }        // end try the whole process
        catch(IOException ioe)
        {
            System.out.println(ioe.toString());
            System.exit(0);

        }
    }    /* end VanCBJ */
    /* special edition of put string for comment processing */
    private void putcoms()
    {
        int i = 0;
        int sav_s_flg = s_flg;
        if(j > 0)
        {
            if(s_flg != 0)
            {
                p_tabs();
                s_flg = 0;
            }
            string[j] = '\0';
            i = 0;
            while (string[i] == ' ') i++; 
            if (lookup_com(w_jdoc) == 1) jdoc = 1;
            //        fprintf(outfil,"%s",string);
            String strBuffer = new String(string,0,j);
            if (string[i] == '/' && string[i+1]=='*')
            {
                if ((last_char != ';') && (sav_s_flg==1) )
                {
                    fprintf(outfil, strBuffer.substring(i,j));                   
                }
                else
                {
                    fprintf(outfil, strBuffer);                   
                }
            }
            else
                /*
                 * if (string[i] == '*' && string[i+1]=='/') fprintf(outfil,
                 * strBuffer.substring(i,j)); else { i = 0; while (string[i] == ' ')
                 * i++;
                 */
            {
                //     if (string[i]=='*')
                if (string[i]=='*' || jdoc == 0)
                    fprintf (outfil, " "+strBuffer.substring(i,j));
                else
                    fprintf (outfil, " * "+strBuffer.substring(i,j));
            }
            j = 0; 
            string[0] = '\0';
        }
    }
    private void 
        cpp_comment()
    {
rep: 
        c = getchr();
        while(c != '\n' && c != '\r' && j<133) 
        {
            string[j++] = c;
            c = getchr();
        }
        /* string[j++] = c; */
        Van_LineNo++;
        indent_puts();
        s_flg = 1;
    }

    /* expand Van_indent into tabs and spaces */
    private void
        p_tabs()
    {
        int i,j,k;

        if (tabs<0) tabs = 0;        /* Van_ 20b C++ inline gets next { */
        if (tabs==0) return;
        i = tabs * Van_indent;        /* calc number of spaces */
        j = i/8;        /* calc number of tab chars */
        /* i -= j*8; /* calc remaining spaces */

        /* for (k=0;k < j;k++) fprintf(outfil,"\t"); */
        // for (k=0;k < i;k++)
        // fprintf(outfil," ");
        for (k=0; k < i; k++) outputBuffer.append(Van_cDotDot);
    }
    private char
        getchr()
    {
        if((peek < 0) && (last_char != ' ') && (last_char != '\t'))
        {
            if((last_char != '\n') && (last_char != '\r'))
                p_char = last_char;
        }
        if(peek > 0)        /* char was read previously */
        {
            last_char = peekc;
            peek = -1;
        }
        else                    /* read next char in file */
        {
            Van_idxblock++;
            if (Van_idxblock >= Van_LineLen)
            {
                for (int ib=0; ib<nBytesRead; ib++) bArray[ib] = '\0';

                Van_LineLen = nBytesRead = 0;
                try /* to get the next block */
                {
                    if (bin.available() > 0)
                    {
                        currentStatus="VanCBJ processing line: "+Van_LineNo;
                        /*
                         * May 6, 1998 Release under GNU GPL if
                         * (Van_cDotDot.compareTo(" ")==0 && Van_LineNo >
                         * Van_MaxLine) Van_EOF = 1;
                         */
                        nBytesRead = bin.read(bArray);
                        Van_LineLen = nBytesRead;
                        Van_sblock = new String(bArray,0);
                        Van_idxblock = 0;
                        last_char = Van_sblock.charAt(Van_idxblock);
                        peek = -1;
                        peekc = '`';
                    }
                    else
                    {
                        Van_EOF = 1;
                        peekc  = '\0';
                    }
                }
                catch(IOException ioe)
                {
                    System.out.println(ioe.toString());
                }
            }
            else
            {
                last_char = Van_sblock.charAt(Van_idxblock);
                /*
                 * peek = -1; peekc = '`';
                 */
            }
        }
        peek = -1;
        if (last_char == '\r') 
        {
            last_char = getchr();
        }

        return last_char;
    }

    /* else processing */
    private void 
        gotelse()
    {
        tabs = s_tabs[c_level][if_lev];
        p_flg[level] = sp_flg[c_level][if_lev];
        ind[level] = s_ind[c_level][if_lev];
        if_flg = 1;
    }

    /* read to new_line */
    private int 
        getnl()
    {
        int save_s_flg;
        save_s_flg = tabs;
        peekc = getchr();
        while(peekc == '\t' || peekc == ' ')
        {
            string[j++] = peekc;
            peek = -1;
            peekc = '`';
            peekc = getchr();
            peek = 1;
        }
        peek = 1;

        if (peekc == '/')
        {
            peek = -1; 
            peekc = '`';
            peekc = getchr();
            if (peekc == '*')
            {
                string[j++] = '/';
                string[j++] = '*';
                peek = -1; 
                peekc = '`';
                comment();
            }
            else if (peekc == '/')
            {
                string[j++] = '/';
                string[j++] = '/';
                peek = -1; 
                peekc = '`';
                cpp_comment();
                return (1);
            }
            else 
            {
                string[j++] = '/';
                peek = 1; // VanCBJ004
            }
        }
        peekc = getchr();
        if(peekc == '\n')
        {
            Van_LineNo++;
            peek = -1;
            peekc = '`';
            tabs = save_s_flg;
            return(1);
        }
        else 
        {
            peek = 1;
        }
        return 0;
    }
    private int lookup (String keyword)
    {
        char r;
        int  l,kk,k,i;
        String j_string = new String(string);

        if (j<1) return (0);
        kk=0;
        while(string[kk] == ' ')kk++;
        l=0;
        l = j_string.indexOf(keyword);
        if (l<0 || l!=kk)
        {
            return 0;
        }
        r = string[kk+keyword.length()];
        if(r >= 'a' && r <= 'z') return(0);
        if(r >= 'A' && r <= 'Z') return(0);
        if(r >= '0' && r <= '9') return(0);
        if(r == '_' || r == '&') return(0);
        return (1);
    }

    private int lookup_com (String keyword)
    {
        char r;
        int  l,kk,k,i;
        String j_string = new String(string);

        if (j<1) return (0);
        kk=0;
        while(string[kk] == ' ')kk++;
        l=0;
        l = j_string.indexOf(keyword);
        if (l<0 || l!=kk)
        {
            return 0;
        }
        return (1);
    }

    //returns a random int between low and high inclusive
    private int 
        Random(int low, int high) 
    {
        int lucky;
        lucky = (int)(Math.floor(Math.random()*(high-low+1)) + low);
        return lucky;
    }
}

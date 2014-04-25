/*
 * ProcessStatus.java
 *
 * Created on 17 agosto 2007, 16.40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jbuddy;

import java.io.IOException;

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
 * @author leonardo
 */
public class ProcessStatus {
    private int _exitStatus;
    private String _processOutput, _processErrors;
    
    /**
     * This class represent a process exit status
     */
    public ProcessStatus(int exitStatus, String output, String error) {
        _exitStatus=exitStatus;
        _processOutput=output;
        _processErrors=error;
    }

    public int getExitStatus() {
        return _exitStatus;
    }

    public String getProcessOutput() {
        return _processOutput;
    }

    public String getProcessErrors() {
        return _processErrors;
    }
    
    static public ProcessStatus createFromCommand(String[] cmd) throws IOException
    {
        String list = "";

        Process proc = Runtime.getRuntime().exec(cmd);
        ProcessMonitor.getInstance().addProcess(proc);
        StreamGobbler sgOutput = new StreamGobbler(proc.getInputStream());
        StreamGobbler sgErrors = new StreamGobbler(proc.getErrorStream());
        sgOutput.start();
        sgErrors.start();

        try
                {
                proc.waitFor();
                ProcessMonitor.getInstance().removeProcess(proc);
        } catch (InterruptedException e)
                {
                System.err.println("process was interrupted");
        }

        int exitValue = proc.exitValue();
        return new ProcessStatus(exitValue, sgOutput.getString(), sgErrors.getString());
    }    
}

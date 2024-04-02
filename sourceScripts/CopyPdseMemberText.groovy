
//------------------------------------------------------------------------
// IDz EE - Groovy - Models for using in Pipeline, SSH command line
// -----------------------------------------------------------------------
// Identification:
// Program Name - CopyPdseMemberText.groovy
// Programmer   - Valter de Siqueira (Valtinho)
// Country      - Brazil 
// -----------------------------------------------------------------------
// Description:
// Copy PROGRAMS source Codes from a PDSE to other PDSE it is useful for 
// deployments and other processes where is necessary to get a copy from
// a PDSE.
// IMPORTANT: The member will be REPLACED if present in targe Dataset
//  
// Pre requirements: 
// -- TSO Interactive (ISPF) configurated.
// -- Path "/var/ispf/WORKAREA" defined in USS
//
// Command to be executed
// -- There is a list in /var/devops/sourceScripts/CommandsList.txt
// -- Look for CopyPdseMemberText in the list
// ------------------------------------------------------------------------

import com.ibm.dbb.dependency.*
import com.ibm.dbb.metadata.*
import com.ibm.dbb.build.*
import com.ibm.dbb.build.report.*
import com.ibm.dbb.build.html.*
import com.ibm.dbb.build.report.records.*
import groovy.util.*
import groovy.transform.*
import groovy.time.*
import groovy.xml.*
import groovy.cli.commons.*

// -- Receive parameters from command line (SSH connection for example)

def varParm = args

'Printing received parameters'
println 'varParm   =  ' + varParm

// -- parameters idstributed on Groovy variables

varProgram  = varParm[0]
varDsnIn    = varParm[1]
varDsnOut   = varParm[2]

println 'Printing the received and distributed variables'
println 'varProgram = ' + varProgram 
println 'varDsnIn   = ' + varDsnIn
println 'varDsnOut  = ' + varDsnOut

// Build variables using concatenation "+" to be used in DDStatement command 

varDDIn  = varDsnIn  + '('+varProgram + ')'
varDDOut = varDsnOut + '('+varProgram + ')'

println 'Printing dataset names build to be used in DDStamente command'
println 'varDDin    = ' + varDDIn
println 'varDDOut   = ' + varDDOut

def logDir = new File('/u/valter/dbbLog/Cep')

def logSysprint = new File(logDir, "copyLoadSysprint.log")

// -----------------------------------------------------------------------------------------
// Example of parameters for COPY ==> ("OCOPY INDD(IN) OUTDD(OUT) TEXT CONVERT(YES) TO1047")
// -----------------------------------------------------------------------------------------

// Using copy replace "(IN,R)" and "TEXT" for source code members

def memberCopy = new TSOExec().command("OCOPY INDD(IN) OUTDD(OUT) TEXT")

memberCopy.dd(new DDStatement().name("CMDSCP").dsn("VALTER.ISPFGWY.EXEC").options("shr"))
memberCopy.dd(new DDStatement().name("IN").dsn("${varDDIn}").options("shr"))
memberCopy.dd(new DDStatement().name("OUT").dsn("${varDDOut}").options("shr"))
memberCopy.logFile(new File("/u/valter/dbbLog/Cep/copyMemberSysprint.log"))
int rc = memberCopy.execute();

println "$rc"

// Comando passado 
// $DBB_HOME/bin/groovyz /u/builder/zappbuild/CopyPdseMemberText.groovy CEPCICS2 VALTER.BUILDER.CEP.COBOL VALTER.BUILDER.NEW.COBOL
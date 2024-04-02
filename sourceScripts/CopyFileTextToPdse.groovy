
//------------------------------------------------------------------------
// IDz EE - Groovy - Models for using in Pipeline, SSH command line
// -----------------------------------------------------------------------
// Identification:
// Program Name - CopyFileTestToPdse.groovy
// Programmer   - Valter de Siqueira (Valtinho)
// Country      - Brazil 
// -----------------------------------------------------------------------
// Description:
// Copy text files from USS file systems to PDSE datasets
// IMPORTANT: The member will be REPLACED if present in targe Dataset
//  
// Command to be executed
// -- There is a list in /var/devops/sourceScripts/CommandsList.txt 
// -- Look for CopyFileTestToPdse in the list
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

varFile     = varParm[0]
varPath     = varParm[1]
varMemb     = varParm[2]
varPdse     = varParm[3]

println 'Printing the received and distributed variables'
println 'varFile   = ' + varFile 
println 'varMemb   = ' + varMemb
println 'varPath   = ' + varPath
println 'varPdse   = ' + varPdse

// Build variables using concatenation "+" to be used in DDStatement command
// In this case, the path passed should not has a "/" as last character.

println 'Printing path + source file'
varFileIn  = varPath + "/" + varFile
println 'varFileIn  = ' + varFileIn

def copy = new CopyToPDS().file(new File("${varFileIn}")).dataset("${varPdse}").member("${varMemb}")
int rc = copy.execute()

println "$rc"

//endCode
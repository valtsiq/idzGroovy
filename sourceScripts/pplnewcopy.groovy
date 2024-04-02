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
import java.text.SimpleDateFormat
import java.time.LocalDateTime

// def varParm = args

// varLMod   = varParm[0]
// varIndd   = varParm[1]
// varOutdd  = varParm[2]

varLMod   = "CEPCICS3"
varIndd   = "VALTER.BUILDER.CEP.LOAD"
varOutdd  = "VALTER.BUILDER.NEW.LOAD"

timeStamp = LocalDateTime.now()
nowDate = timeStamp.format("YYYY-MM-dd")
nowTime = timeStamp.format("HH-mm-ss")

new File("/var/devops/pipeline/temp/jclFile_D${nowDate}_T${nowTime}.jcl").delete()

jclFile = new File("/var/devops/pipeline/temp/jclFile_D${nowDate}_T${nowTime}.jcl")
pipeLog = new File("/var/devops/pipeline/logs/pipeLog_D${nowDate}.log")

//                       1         2         3         4         5         6         7
//              123456789012345678901234567890123456789012345678901234567890123456789012
jclFile.append("//PPLLOADC  JOB (PIPELINE),IBMUSER,CLASS=A,MSGCLASS=H \n")
jclFile.append("//* ------------------------------------------------- \n")
jclFile.append("//ST1       EXEC PGM=IEBCOPY \n")
jclFile.append("//SYSPRINT  DD SYSOUT=* \n")
jclFile.append("//SYSUT3    DD UNIT=SYSDA,SPACE=(3120,(20,10)) \n")
jclFile.append("//SYSUT4    DD UNIT=SYSDA,SPACE=(3120,(20,10)) \n")
jclFile.append("//INDD      DD DISP=SHR,DSN=${varIndd} \n")
jclFile.append("//OUTDD     DD DISP=SHR,DSN=${varOutdd} \n")
// jclFile.append("//SYSIN     DD PATH='/u/valter/IEBSYSIN',PATHDISP=KEEP")
jclFile.append("//SYSIN     DD DISP=SHR,DSN=VALTER.BUILDER.JCL(IEBSYSIN) \n")
jclFile.append("//SYSIN     DD * \n")
jclFile.append("  COPY INDD=INDD,OUTDD=OUTDD \n")
jclFile.append("  SELECT MEMBER=((${varLMod},,R)) \n")
jclFile.append("/* \n")

def jobExec = new JobExec().file(new File("${jclFile}"))

def jobRc = jobExec.execute()
println jobRc
def jobID   = jobExec.getSubmittedJobId()
println jobID
def jobName = jobExec.getSubmittedJobName()
println jobName

jobExec.saveOutput(new File("/var/devops/pipeline/sysouts/${jobName}_${jobID}_D${nowDate}_T${nowTime}.sysout"))

if (jobRc < 1) {
   varPllMsg = "PPL0100I pgm=pplCopyload ${jobID} ${jobName} RC=${jobRc}"
   varPllTxt = "Module ${varLMod} copied from ${varIndd} to ${varOutdd}"
   }
else {
   println jobRc
   println "${jobRc}"
   varPllMsg = "PPL0101E pgm=pplCopyload ${jobID} ${jobName} RC=${jobRc}"
   varPllTxt = "Module ${varLMod} NOT copied from ${varIndd} to ${varOutdd}"
   }

println "${nowDate} ${nowTime} ${varPllMsg} ${varPllTxt}  \n"

pipeRecord = "${nowDate} ${nowTime} ${varPllMsg} ${varPllTxt}  \n"
pipeLog.append(pipeRecord)

// new File("/var/devops/pipeline/temp/jclFile_D${nowDate}_T${nowTime}.jcl").delete()

// finis, fine, fin, fim

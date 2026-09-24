@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@ECHO OFF
@SETLOCAL

SET ERROR_CODE=0

@REM To isolate internal variables from possible post scripts, we use LOCAL setvars.
SET MAVEN_PROJECTBASEDIR=%~dp0

IF NOT "%MAVEN_PROJECTBASEDIR%"=="" SET MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%

@REM Find execution directory
SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@REM Provide a default JAVA_HOME if not set
IF "%JAVA_HOME%"=="" (
  FOR /F "tokens=2*" %%A IN ('REG QUERY "HKLM\SOFTWARE\JavaSoft\JDK" /v CurrentVersion 2^>NUL') DO (
    FOR /F "tokens=2*" %%B IN ('REG QUERY "HKLM\SOFTWARE\JavaSoft\JDK\%%B" /v JavaHome 2^>NUL') DO (
      SET "JAVA_HOME=%%C"
    )
  )
)

IF "%JAVA_HOME%"=="" (
  SET JVM_COMMAND=java.exe
) ELSE (
  SET JVM_COMMAND="%JAVA_HOME%\bin\java.exe"
)

%JVM_COMMAND% -jar %WRAPPER_JAR% %*
IF %ERRORLEVEL% NEQ 0 SET ERROR_CODE=%ERRORLEVEL%

EXIT /B %ERROR_CODE%

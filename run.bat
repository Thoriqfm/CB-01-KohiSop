@echo off
REM Set JDK path
set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

REM Create bin folder if not exists
if not exist bin (
    mkdir bin
)

REM Compile semua file Java
echo Compiling Java files...
dir /s /b src\*.java > sources.txt
javac -d bin -sourcepath src @sources.txt
del sources.txt

if %ERRORLEVEL% neq 0 (
    echo Compile failed!
    exit /b 1
)

echo Compile success! Running application...
REM Run aplikasi
java -cp bin kohisop.Main

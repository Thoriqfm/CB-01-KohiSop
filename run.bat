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
javac -d bin -sourcepath src ^
    src\kohisop\Main.java ^
    src\kohisop\app\*.java ^
    src\kohisop\currency\*.java ^
    src\kohisop\model\*.java ^
    src\kohisop\payment\*.java ^
    src\kohisop\service\*.java

if %ERRORLEVEL% neq 0 (
    echo Compile failed!
    exit /b 1
)

echo Compile success! Running application...
REM Run aplikasi
java -cp bin kohisop.Main

@echo off
echo ===============================================
echo    IDA SignHub Desktop Application Builder
echo ===============================================
echo.

:: Set environment variables
set JAVA_HOME=C:\Program Files\Java\jdk-11
set MAVEN_HOME=C:\Program Files\Apache\maven
set INNO_SETUP="C:\Program Files (x86)\Inno Setup 6\ISCC.exe"

:: Check if required tools are available
echo [1/7] Checking build environment...
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java not found in PATH
    goto :error
)

where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven not found in PATH
    goto :error
)

if not exist %INNO_SETUP% (
    echo WARNING: Inno Setup not found, installer creation will be skipped
    set SKIP_INSTALLER=1
) else (
    set SKIP_INSTALLER=0
)

echo ✓ Build environment OK

:: Clean previous builds
echo.
echo [2/7] Cleaning previous builds...
if exist target rmdir /s /q target
if exist dist rmdir /s /q dist
mkdir target
mkdir dist
echo ✓ Clean completed

:: Compile and package with Maven
echo.
echo [3/7] Compiling and packaging application...
call mvn clean compile
if %errorlevel% neq 0 goto :error

call mvn package -DskipTests
if %errorlevel% neq 0 goto :error
echo ✓ Packaging completed

:: Create Windows executable with Launch4j
echo.
echo [4/7] Creating Windows executable...
call mvn package -P windows
if %errorlevel% neq 0 goto :error

if exist target\IDASignHub.exe (
    echo ✓ Windows executable created successfully
    copy target\IDASignHub.exe dist\
) else (
    echo WARNING: Windows executable not created
)

:: Create JLink runtime image
echo.
echo [5/7] Creating custom runtime image...
call mvn jlink:jlink
if %errorlevel% neq 0 (
    echo WARNING: JLink runtime creation failed, continuing...
) else (
    echo ✓ Custom runtime image created
)

:: Create JPackage installer (if JDK 14+ is available)
echo.
echo [6/7] Creating native installer with JPackage...
java -version 2>&1 | findstr "version" | findstr /C:"14\." /C:"15\." /C:"16\." /C:"17\." /C:"18\." /C:"19\." /C:"20\." /C:"21\." >nul
if %errorlevel% equ 0 (
    call mvn jpackage:jpackage
    if %errorlevel% equ 0 (
        echo ✓ JPackage installer created
        if exist target\installer\*.msi (
            copy target\installer\*.msi dist\
        )
    ) else (
        echo WARNING: JPackage installer creation failed
    )
) else (
    echo INFO: JPackage requires JDK 14+, skipping native installer
)

:: Create Inno Setup installer
echo.
echo [7/7] Creating Windows installer...
if %SKIP_INSTALLER% equ 0 (
    %INNO_SETUP% setup.iss
    if %errorlevel% equ 0 (
        echo ✓ Inno Setup installer created successfully
        if exist target\installer\*.exe (
            copy target\installer\*.exe dist\
        )
    ) else (
        echo ERROR: Inno Setup installer creation failed
        goto :error
    )
) else (
    echo INFO: Skipping installer creation (Inno Setup not found)
)

:: Copy additional files to distribution
echo.
echo Copying additional files to distribution...
if exist target\ida-signhub-desktop.jar copy target\ida-signhub-desktop.jar dist\
if exist README.md copy README.md dist\
if exist LICENSE copy LICENSE dist\

:: Create portable package
echo.
echo Creating portable package...
mkdir dist\portable
if exist target\IDASignHub.exe copy target\IDASignHub.exe dist\portable\
if exist target\ida-signhub-desktop.jar copy target\ida-signhub-desktop.jar dist\portable\
mkdir dist\portable\config
mkdir dist\portable\logs
mkdir dist\portable\temp

:: Copy resources
if exist src\main\resources xcopy src\main\resources dist\portable\resources\ /E /I /Q

:: Create run script for portable version
echo @echo off > dist\portable\run.bat
echo cd /d %%~dp0 >> dist\portable\run.bat
echo java -jar ida-signhub-desktop.jar >> dist\portable\run.bat

:: Create ZIP package
echo.
echo Creating ZIP distribution...
powershell -command "Compress-Archive -Path 'dist\portable\*' -DestinationPath 'dist\IDASignHub-Portable.zip'"

:: Summary
echo.
echo ===============================================
echo                 BUILD SUMMARY
echo ===============================================
echo.
if exist dist\IDASignHub.exe echo ✓ Windows Executable: dist\IDASignHub.exe
if exist dist\*.msi echo ✓ MSI Installer: dist\*.msi
if exist dist\*Setup.exe echo ✓ Inno Setup Installer: dist\*Setup.exe
if exist dist\ida-signhub-desktop.jar echo ✓ Executable JAR: dist\ida-signhub-desktop.jar
if exist dist\IDASignHub-Portable.zip echo ✓ Portable Package: dist\IDASignHub-Portable.zip
echo.
echo Build completed successfully!
echo Output files are available in the 'dist' directory.
echo.
pause
goto :end

:error
echo.
echo ===============================================
echo                 BUILD FAILED
echo ===============================================
echo.
echo Build process failed. Please check the errors above.
echo.
pause
exit /b 1

:end
echo Press any key to exit...
pause >nul
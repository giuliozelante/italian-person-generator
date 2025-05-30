@echo off
echo Building Italian Person Generator...
call gradlew.bat fatJar -q
echo.
echo Running Italian Person Generator:
echo ================================
java -jar build/libs/italian-person-generator-1.0.0-fat.jar
echo.
pause 
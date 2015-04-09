#!/bin/bash
echo -e "\n"
echo "++++++++++++++++++++++++++++++++++++Inicio generacion con JFlex++++++++++++++++++++++++++++++++++++";
java -jar JAR/JFlex.jar -d src/compilador/ especificacion_lexica/lexico.flex
echo "++++++++++++++++++++++++++++++++++++FIN generacion con JFlex++++++++++++++++++++++++++++++++++++";
echo -e "\n"
echo -e "\n"
echo "++++++++++++++++++++++++++++++++++++Inicio generacion con CUP++++++++++++++++++++++++++++++++++++";
java -jar JAR/java-cup-11a.jar -destdir src/compilador/ especificacion_sintactica/sintactico.cup
echo "++++++++++++++++++++++++++++++++++++FIN generacion con CUP++++++++++++++++++++++++++++++++++++";
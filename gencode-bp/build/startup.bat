title gencode
java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=256M -XX:MaxPermSize=256M -Dorg.apache.jasper.compiler.disablejsr199=true -jar bboss-rt.jar  %1
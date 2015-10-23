#!/usr/bin/env bash
mkdir -p outfile/
wget -P outfile/ http://192.168.65.11/deploy_dependencies/commons/tomcat.tar.gz
tar -xzf outfile/tomcat.tar.gz -C outfile/
rm -rf outfile/tomcat.tar.gz
mkdir -p release/tomcat/webapps/mercurius-webapi-@pack@api
unzip mercurius-dist/mercurius-webapi/mercurius-webapi-@pack@api/target/mercurius-webapi-@pack@api.war -d outfile/tomcat/webapps/mercuris-webapi-@pack@api
cp mercurius-core/mercurius-@pack@/start.sh outfile/
cp mercurius-core/mercurius-@pack@/appspec.yml outfile/
cp mercurius-core/mercurius-@pack@/validate.sh outfile/
chmod a+x outfile/validate.sh

cd outfile/
appid=me.ele.scm.@pack@
tomcat_jmx_port=10827
tomcat_shutdown_port=8006
tomcat_bind_port=9010
tomcat_redirect_port=8443
tomcat_ajp_port=8010
tomcat_ajp_redirect_port=8443
echo $appid
echo $tomcat_jmx_port
echo $tomcat_shutdown_port
echo $tomcat_bind_port
echo $tomcat_redirect_port
echo $tomcat_ajp_port
echo $tomcat_ajp_redirect_port

sed -i 's/appid/'"$appid"'/g' tomcat/bin/setenv.sh.etpl
sed -i 's/jmxport/'"$tomcat_jmx_port"'/g' tomcat/bin/setenv.sh.etpl

sed -i 's/appid/'"$appid"'/g' tomcat/conf/logging.properties

sed -i 's/tomcatport/'"$tomcat_shutdown_port"'/g' tomcat/conf/server.xml
sed -i 's/bindport/'"$tomcat_bind_port"'/g' tomcat/conf/server.xml
sed -i 's/bindredirect/'"$tomct_redirect_port"'/g' tomcat/conf/server.xml
sed -i 's/ajpport/'"$tomcat_ajp_port"'/g' tomcat/conf/server.xml
sed -i 's/ajpredirect/'"$tomcat_ajp_redirect_port"'/g' tomcat/conf/server.xml
sed -i 's/appid/'"$appid"'/g' tomcat/conf/server.xml
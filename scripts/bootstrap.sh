#
# bootstrapper to install gnuplot, Java8 and wget on CentOS VMs
#


mkdir bootstrap
cd bootstrap

#-------------------
#
# SETUP Java 8
#
#    Source: http://tecadmin.net/install-java-8-on-centos-rhel-and-fedora/
#
#-------------------

wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jdk-8u60-linux-x64.tar.gz"
tar xzf jdk-8u60-linux-x64.tar.gz
mv jdk1.8.0_60 /opt/

alternatives --install /usr/bin/java java /opt/java8/bin/java 2
alternatives --config java

alternatives --install /usr/bin/jar jar /opt/java8/bin/jar 2
alternatives --install /usr/bin/javac javac /opt/java8/bin/javac 2

alternatives --set jar /opt/java8/bin/jar
alternatives --set javac /opt/java8/bin/javac 

export JAVA_HOME=/opt/java8


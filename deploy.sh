zzinbros_pid=`sudo pgrep -f zzinbros-0.0.1-SNAPSHOT.*`

echo running ports of zzinbros:
echo $zzinbros_pid
sudo kill $zzinbros_pid

cd /home/ubuntu/miniprojects-2019
git pull

./gradlew clean build
sudo java -jar -Dserver.port=8000 build/libs/zzinbros-0.0.1-SNAPSHOT.jar &
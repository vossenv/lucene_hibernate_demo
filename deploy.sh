#!/usr/bin/env bash


./gradlew clean assemble

service_name="teamquery"
host="thenewcarag.com"
url="thenewcarag.com/teamquery/status"
username="deployment"

function check_remote(){
    echo -e "Checking API status remotely... \n"
    apiStatus="down"
    count=0
    while [ "$apiStatus" != "true" ]; do
        if [ "$count" -gt 50 ]; then break; fi
        apiStatus=$(curl $url -s)
        count=$((count + 1))
        sleep 1
        echo -n "$count "
    done
    echo -e "\n"
    [ "$apiStatus" = "true" ] && echo "Api is running, install complete!" || echo "Service not running, attention needed"
}

function deployBash() {
    script="sudo\ /usr/bin/java\ -jar\ teamquery-1.0.jar"
    dir="/home/$2/.$service_name"
    ssh $2@$1 "sudo rm -rf $dir; echo "Creating directory $dir";  sudo mkdir $dir; sudo chmod 777 $dir;"
    scp build/libs/teamquery-1.0.jar $2@$1:$dir
    ssh $2@$1 'bash -s' < ./install-microservice-bash.sh "$dir" "$script" "$service_name"
}

printf "\n===== Begin deploy: $username @ $host =====\n\n"
deployBash $host $username
check_remote
printf "===== End deploy: $username @ $host =====\n"
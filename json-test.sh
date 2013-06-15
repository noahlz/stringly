#! /bin/bash -e

f=$1
arg1=$2
arg2=$3

# defaults
HEADERS="-H Accept:application/json -H Content-Type:application/json"
DATA="-d {\"f\":\"${f}\",\"args\":"

if [ -z "$f" ]; then
  HEADERS="-H Accept:application/json"
elif [ -z "$arg1" ]; then
  DATA="-d {\"f\":\"${f}\"}"
elif [ -z "$arg2" ]; then
  DATA="${DATA}[\"${arg1}\"]}"
else
  DATA="${DATA}[\"${arg1}\",\"${arg2}\"]}"
fi

CMD="curl -i ${HEADERS} ${DATA} http://localhost:3000/stringly"

echo $CMD
$CMD
echo

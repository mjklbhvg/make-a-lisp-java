#!/usr/bin/env fish

test (count $argv) -eq 1
	or exit 1

#cd ../../out/production/make-a-lisp/
cd ~/mal/impls/mal

rm /tmp/fifo
mkfifo /tmp/fifo

fish -c '../java-again/run stepA_mal.mal < /tmp/fifo' &
#fish -c 'java mal/Main < /tmp/fifo' &

sleep 999999 > /tmp/fifo &
echo Starting

set lines (cat $argv[1])

for line in $lines
	if test -z $line
		continue
	end

	if string match -r '^;.*$' $line > /dev/null
		printf "\033[31m%s\033[m\n" $line
	else
		printf "\033[32m%s\033[m\n" $line
		echo $line > /tmp/fifo
	end
	read -p '' lol
end

killall sleep


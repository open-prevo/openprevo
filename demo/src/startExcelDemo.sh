doStart() {
    if hash start 2>/dev/null; then
		echo on windows please use the bat
    elif hash gnome-terminal 2>/dev/null; then
		cd $1
		gnome-terminal -t $1 -- ./start.sh
		cd ..
	else
		echo  termnial programm not found. exiting
    fi
}
for folder in NodeBaloiseExcel NodeHelvetiaExcel NodeZurichExcel hub  
do  
    doStart $folder
done  
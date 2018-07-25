FOR %%A IN (NodeBaloiseExcel,NodeHelvetiaExcel,NodeZurichExcel,hub) DO (
	pushd %%A
	call start.bat
	popd
)

FOR %%A IN (hub,NodeBaloiseExcel,NodeHelvetiaExcel,NodeZurichExcel) DO (
	pushd %%A
	call start.bat
	popd
)

FOR %%A IN (NodeBaloiseExcel,NodeHelvetiaExcel,NodeZurichExcel,hub) DO (
	pushd %%A
	del retirement-fund-out-data*.xlsx
	del openprevo.mv.db
	popd
)

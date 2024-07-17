{
#for(column : table.allColumns)
  "#(column.property)": "#(firstCharToLowerCase(column.property))"
#end
}
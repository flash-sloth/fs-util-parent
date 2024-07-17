{
#for(column : table.getSortedFormColumns())
  "#(column.property)": "#(firstCharToLowerCase(column.property))"
#end
}
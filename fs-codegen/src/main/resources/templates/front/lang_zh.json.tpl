{
#for(column : table.allColumns)
  "#(column.property)": "#(column.propertyConfig?.swaggerDescription)"
#end
}
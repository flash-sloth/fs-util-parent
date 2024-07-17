{
#for(column : table.getSortedFormColumns())
  "#(column.property)": "#(column.propertyConfig?.swaggerDescription)"
#end
}
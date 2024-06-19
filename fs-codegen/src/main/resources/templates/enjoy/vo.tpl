#set(withLombok = voConfig.isWithLombok())
#set(withSwagger = voConfig.isWithSwagger())
#set(swaggerVersion = voConfig.getSwaggerVersion())
#set(jdkVersion = voConfig.getJdkVersion())
package #(voPackageName);

#for(importClass : voConfig.buildImports(table))
import #(importClass);
#end

#if(jdkVersion >= 14)
import java.io.Serial;
#end

#if(withSwagger && swaggerVersion.getName() == "FOX")
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
#end
#if(withSwagger && swaggerVersion.getName() == "DOC")
import io.swagger.v3.oas.annotations.media.Schema;
#end
#if(withLombok)
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
#if(voConfig.getSuperClass())
import lombok.EqualsAndHashCode;
#end
#end

/**
 * #(table.getComment()) VO类（通常用作Controller出参）。
 *
 * @author #(javadocConfig.getAuthor())
 * @since #(javadocConfig.getSince())
 */
#if(withLombok)
@Accessors(chain = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
#if(voConfig.getSuperClass())
@EqualsAndHashCode(callSuper = true)
#end
#end
#if(withSwagger && swaggerVersion.getName() == "FOX")
@ApiModel("#(table.getComment())")
#end
#if(withSwagger && swaggerVersion.getName() == "DOC")
@Schema(description = "#(table.getComment())")
#end
#(table.buildTableAnnotation())
public class #(voClassName) #(voConfig.buildExtends(globalConfig))#(voConfig.buildImplements(globalConfig)) {

    #if(jdkVersion >= 14)
    @Serial
    #end
    private static final long serialVersionUID = 1L;

#for(column : table.allColumns)
    #set(comment = javadocConfig.formatColumnComment(column.comment))
    #if(isNotBlank(comment))
    /**
     * #(comment)
     */
    #end
    #set(annotations = column.buildAnnotations())
    #if(isNotBlank(annotations))
    #(annotations)
    #end
    #if(withSwagger && swaggerVersion.getName() == "FOX")
    @ApiModelProperty("#(column.comment)")
    #end
    #if(withSwagger && swaggerVersion.getName() == "DOC")
    @Schema(description = "#(column.comment)")
    #end
    private #(column.propertySimpleType) #(column.property)#if(isNotBlank(column.propertyDefaultValue)) = #(column.propertyDefaultValue)#end;

#end
#if(!withLombok)

    #for(column: table.allColumns)
    public #(column.propertySimpleType) #(column.getterMethod())() {
        return #(column.property);
    }

    public #(voClassName) #(column.setterMethod())(#(column.propertySimpleType) #(column.property)) {
        this.#(column.property) = #(column.property);
        return this;
    }

    #end
#end}

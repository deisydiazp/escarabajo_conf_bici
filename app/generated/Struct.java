//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacin de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perdern si se vuelve a compilar el esquema de origen. 
// Generado el: 2015.10.26 a las 05:59:30 PM COT 
//


package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}and"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "and"
})
@XmlRootElement(name = "struct")
public class Struct {

    @XmlElement(required = true)
    protected And and;

    /**
     * Obtiene el valor de la propiedad and.
     * 
     * @return
     *     possible object is
     *     {@link And }
     *     
     */
    public And getAnd() {
        return and;
    }

    /**
     * Define el valor de la propiedad and.
     * 
     * @param value
     *     allowed object is
     *     {@link And }
     *     
     */
    public void setAnd(And value) {
        this.and = value;
    }

}

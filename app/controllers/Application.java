package controllers;

import play.*;
import play.api.libs.json.Json;
import play.mvc.*;
import views.html.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import generated.Alt;
import generated.And;
import generated.Constraints;
import generated.Feature;
import generated.Imp;
import generated.Not;
import generated.Or;
import generated.FeatureModel;
import generated.ObjectFactory;
import generated.Rule;
import generated.Struct;

import javax.swing.text.html.HTML;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.gson.Gson;


public class Application extends Controller {

	public static Integer pantallaActual;
    public static List<Caracteristica> listCaracteristicas;
    public static List<Restriccion> listRestricciones;
    
    public Result index() throws JAXBException{
        
    	JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
	    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	    
	    File file = new File("modelo/model.xml");
	    FeatureModel featureModel = (FeatureModel)unmarshaller.unmarshal(file);
	    Struct struct = featureModel.getStruct();
	    And and = struct.getAnd();
	        
	    int i = 0;
	    Caracteristica caracteristica=new Caracteristica();
	    caracteristica.setNombre(and.getName());
	    listCaracteristicas = new ArrayList<Caracteristica>();
	    listRestricciones = new ArrayList<Restriccion>();
	    
	    agregarListaCaracteristicas(and.getAndOrAltOrOr(), caracteristica);
	    String cadRestricciones = agregarListaRestricciones(featureModel.getConstraints());
	    
	    //Json.toJson(listRestricciones);
	    return ok(index.render(caracteristica, cadRestricciones));
    }

    public static void agregarImagenes(Caracteristica caracteristica, String descripcion)
    {
    	if(descripcion.contains("tieneImagen")){

 			String sDirectorio = "public/images/" + descripcion.split("-")[0].trim();
 			File f = new File(sDirectorio);
 			if (f.exists()){  
 				File[] ficheros = f.listFiles();
 				for (int x = 0; x < ficheros.length; x++){
 				  caracteristica.getImagenes().add("/assets/images/" + descripcion.split("-")[0].trim() + "/" + ficheros[x].getName());
 				  //System.out.println("/assets/images/" + descripcion.split("-")[0].trim() + "/" + ficheros[x].getName());
 				}
 			}
 		}
 		caracteristica.titulo = descripcion.split("-")[1].trim();
    }
    
	public static String agregarListaRestricciones(Constraints constraints) {

		List<Rule> listRules = constraints.getRule();
		
		for (Rule rule : listRules) {
			List<Imp> listImp = rule.getImp();
			for (int i = 0; i < listImp.size(); i++) {
				Restriccion restriccion = new Restriccion();
				List<Object> listObj = listImp.get(i).getVarOrNot();
				for (Object object : listObj) {
					if(object instanceof String){
						restriccion.aplicaA = object.toString();
						listRestricciones.add(restriccion);
						//System.out.println("Aplica a --> " + object.toString());
					}
					else if(object instanceof Not){
		 	    		Not caracteristicaNot = (Not)object;
		 	    		List<Object> listObjectNot = caracteristicaNot.getVarOrNot();
		 	    		if(listObjectNot.size() > 0)
		 	    			agregarNot(restriccion, listObjectNot);
					}
				}
				
			}
		}
		return new Gson().toJson(listRestricciones);
	}
    
	public static void agregarNot(Restriccion restriccion, List<Object> listObjectNot)
	{
		for (Object object : listObjectNot) {
			if(object instanceof String){
				restriccion.getNoImplica().add(object.toString());
				//System.out.println(object.toString());
			}
			else
			{
				if(object instanceof Imp){
					agregarNot(restriccion, ((Imp) object).getVarOrNot());
				}
			}
		}
	}
	
    public void agregarListaCaracteristicas(List<Object> lstObj, Caracteristica padre)
	{
    	List<Object> lstObjChild = new ArrayList<Object>();
    	Caracteristica caracteristica;
 	    for (Object object : lstObj) {
 	    	
 	    	caracteristica = new Caracteristica();
 	    	caracteristica.setClase(object.getClass().getSimpleName());
 	    	
 	    	if(object instanceof Alt){
 	    		Alt caracteristicaAlt = (Alt)object;
 	    		caracteristica.setNombre(caracteristicaAlt.getName());
 	    		caracteristica.setOpcional(caracteristicaAlt.isMandatory());
 	    		padre.getCaracteristicas().add(caracteristica);
 	    		String descripcion = caracteristicaAlt.getDescription();
 	    		agregarImagenes(caracteristica, descripcion);
 	    		lstObjChild =  caracteristicaAlt.getAndOrAltOrOr();
 	    		if(lstObjChild.size() > 0)
 	    			agregarListaCaracteristicas(lstObjChild, caracteristica);
 	    	}
 	    	
 	    	if(object instanceof Or){
 	    		Or caracteristicaOr = (Or)object;
 	    		caracteristica.setNombre(caracteristicaOr.getName());
 	    		caracteristica.setOpcional(caracteristicaOr.isMandatory());
 	    		padre.getCaracteristicas().add(caracteristica);
 	    		String descripcion = caracteristicaOr.getDescription();
 	    		agregarImagenes(caracteristica, descripcion);
 	    		lstObjChild =  caracteristicaOr.getAndOrAltOrOr();
 	    		if(lstObjChild.size() > 0)
 	    			agregarListaCaracteristicas(lstObjChild, caracteristica);
 	    	}
 	    	
 	    	if(object instanceof Feature){
 	    		Feature caracteristicaFeature = (Feature)object;
 	    		caracteristica.setNombre(caracteristicaFeature.getName());
 	    		caracteristica.setOpcional(caracteristicaFeature.isMandatory());
 	    		padre.getCaracteristicas().add(caracteristica);
 	    		String descripcion = caracteristicaFeature.getDescription();
 	    		if(descripcion != null)
 	    			agregarImagenes(caracteristica, descripcion);
 	    	}
 	    }
	}
	
	public static class Restriccion{
		public String aplicaA;
		public List<String> noImplica;

		public Restriccion()
		{}
		
		public String getAplicaA() {
			return aplicaA;
		}

		public void setAplicaA(String aplicaA) {
			this.aplicaA = aplicaA;
		}

		public List<String> getNoImplica(){
			if(this.noImplica==null){
				this.noImplica=new ArrayList<String>();
			}
			return this.noImplica;
		}

		public void setNoImplica(List<String> noImplica) {
			this.noImplica = noImplica;
		}
	}
	
	public static class Caracteristica
	{
		public String nombre;
		public Boolean opcional;
		public String nombrePadre;
		public String clase;
		public List<String> imagenes;
		public List<Caracteristica> caracteristicas;
		public String titulo;
		
		public Caracteristica()
		{}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public Boolean getOpcional() {
			return opcional;
		}

		public void setOpcional(Boolean opcional) {
			this.opcional = opcional;
		}

		public String getNombrePadre() {
			return nombrePadre;
		}

		public void setNombrePadre(String nombrePadre) {
			this.nombrePadre = nombrePadre;
		}

		public String getClase() {
			return clase;
		}

		public void setClase(String clase) {
			this.clase = clase;
		}
		
		public List<Caracteristica> getCaracteristicas(){
			if(this.caracteristicas==null){
				this.caracteristicas=new ArrayList<Caracteristica>();
			}
			return this.caracteristicas;
		}
		
		public void setCaracteristicas(List<Caracteristica> caracteristicas){
			this.caracteristicas=caracteristicas;
		}
		
		public List<String> getImagenes(){
			if(this.imagenes==null){
				this.imagenes=new ArrayList<String>();
			}
			return this.imagenes;
		}
		
		public void setImagenes(List<String> imagenes){
			this.imagenes=imagenes;
		}
		
		public String getTitulo() {
			return titulo;
		}

		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}
	}

	
}

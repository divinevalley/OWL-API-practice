package pizzapractice.owlapi;


import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;

public class Functions {
	
	//declare a class
	public static OWLClass declareClass(OWLDataFactory df, IRI documentIRI, OWLOntology ontology, String tag){
		OWLClass newClass = df.getOWLClass(IRI.create(documentIRI+"#"+tag));
		OWLDeclarationAxiom declarationAxiom = df.getOWLDeclarationAxiom(newClass);
		ontology.add(declarationAxiom);
		return newClass;
	}
	
	public static void putAsSubclass(OWLDataFactory df, OWLOntology ontology, OWLClass subclass, OWLClass superclass){
		OWLAxiom subclassAxiom = df.getOWLSubClassOfAxiom(subclass, superclass); 
		ontology.add(subclassAxiom);
	}
	
	public static void putAllAsSubclass(OWLDataFactory df, OWLOntology ontology, List<OWLClass> subclasses, OWLClass superclass){
		for (OWLClass eachSubclass:subclasses){//for each item on list
			OWLAxiom subclassAxiom = df.getOWLSubClassOfAxiom(eachSubclass, superclass); //put as subclass of superclass
			ontology.add(subclassAxiom);
		}
	}
	
	public static void makeSubproperty(OWLDataFactory df, OWLOntology ontology, OWLOntologyManager manager, OWLObjectProperty subproperty, OWLObjectProperty superproperty){
		OWLSubObjectPropertyOfAxiom axiom = df.getOWLSubObjectPropertyOfAxiom(subproperty, superproperty);
		ontology.add(axiom);
	}
	
	public static void makeInverse(OWLDataFactory df, OWLOntology ontology, OWLOntologyManager manager, OWLObjectProperty forwardProperty, OWLObjectProperty inverseProperty){
		OWLInverseObjectPropertiesAxiom axiom = df.getOWLInverseObjectPropertiesAxiom(forwardProperty, inverseProperty);
		ontology.add(axiom);
	}
	
	public static void setDomain(OWLDataFactory df, OWLOntology ontology, OWLOntologyManager manager, OWLObjectProperty property, OWLClass domain){
		OWLObjectPropertyDomainAxiom axiom = df.getOWLObjectPropertyDomainAxiom(property, domain);
		ontology.add(axiom);
	}
	
	public static void setRange(OWLDataFactory df, OWLOntology ontology, OWLOntologyManager manager, OWLObjectProperty property, OWLClass range){
		OWLObjectPropertyRangeAxiom axiom = df.getOWLObjectPropertyRangeAxiom(property, range);
		ontology.add(axiom);
	}
	
	public static void makeDisjoint(OWLDataFactory df, IRI documentIRI, OWLOntology ontology, List<OWLClass> disjointClasses){
		OWLDisjointClassesAxiom disjointAxiom = df.getOWLDisjointClassesAxiom(disjointClasses);			
		ontology.add(disjointAxiom);
	}
}

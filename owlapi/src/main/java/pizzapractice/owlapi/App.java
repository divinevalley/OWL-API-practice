package pizzapractice.owlapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;
import org.semanticweb.owlapi.profiles.OWL2QLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLObjectTransformer;
import org.semanticweb.owlapi.vocab.OWLFacet;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		//manager pour manipuler les ontologies 
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		OWLDataFactory df = OWLManager.getOWLDataFactory();
		
		//enregistrer en local
		File OntologieAEnregistrer = new File("/Users/wung/Documents/Ontologies/pizzaOntology.owl");
		OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
		IRI documentIRI = IRI.create(OntologieAEnregistrer.toURI());

		
		try {
			//new ontology
			OWLOntology pizzaOntology = manager.createOntology();
			manager.saveOntology(pizzaOntology, owlxmlFormat, documentIRI);

		
			//prefixe manager 
			PrefixManager prefixe = new DefaultPrefixManager("http://owl.cs.manchester.ac.uk/co-ode-files/ontologies/ontology#");
			
			//Annotation
			OWLAnnotation commentAnno = df.getOWLAnnotation(df.getRDFSComment(), df.getOWLLiteral("A pizza ontology that describes various pizzas based on their toppings", "en"));
			df.getOWLAnnotationAssertionAxiom(documentIRI, commentAnno);
			

			//~~~~~~~~~~~~~~~~~~~~~~~create classes or axioms~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			//classes topping and base
			OWLClass pizza = Functions.declareClass(df, documentIRI, pizzaOntology, "Pizza");
			OWLClass topping = Functions.declareClass(df, documentIRI, pizzaOntology, "PizzaTopping");
			OWLClass base = Functions.declareClass(df, documentIRI, pizzaOntology, "PizzaBase");
			OWLClass deepPanBase = Functions.declareClass(df, documentIRI, pizzaOntology, "DeepPanBase");
			
			
			//make classes disjoint?
			List<OWLClass> disjointClasses = new ArrayList<OWLClass>();
			Collections.addAll(disjointClasses, topping, pizza, base);
			Functions.makeDisjoint(df, documentIRI, pizzaOntology, disjointClasses);
			
			Functions.putAsSubclass(df, pizzaOntology, deepPanBase, base); //deep pan base is under base
			
			
			
			//toppings
			OWLClass cheese = Functions.declareClass(df, documentIRI, pizzaOntology, "CheeseTopping");
				OWLClass mozzarella = Functions.declareClass(df, documentIRI, pizzaOntology, "MozzarellaTopping");
				OWLClass parmesan = Functions.declareClass(df, documentIRI, pizzaOntology, "ParmesanTopping");
			 
				//put as subclasses
				List<OWLClass> cheeseSubclasses = new ArrayList<OWLClass>();
				Collections.addAll(cheeseSubclasses, mozzarella, parmesan); //create list of all cheese subclasses
				Functions.putAllAsSubclass(df, pizzaOntology, cheeseSubclasses, cheese); //put all cheese subclasses under overall cheese class
			
			OWLClass meat = Functions.declareClass(df, documentIRI, pizzaOntology, "MeatTopping");
				OWLClass ham = Functions.declareClass(df, documentIRI, pizzaOntology, "HamTopping");
				OWLClass pepperoni = Functions.declareClass(df, documentIRI, pizzaOntology, "PepperoniTopping");
				OWLClass salami = Functions.declareClass(df, documentIRI, pizzaOntology, "SalamiTopping");
				OWLClass spicybeef = Functions.declareClass(df, documentIRI, pizzaOntology, "SpicyBeefTopping");
			
				//put as subclasses
				List<OWLClass> meatSubclasses = new ArrayList<OWLClass>();
				Collections.addAll(meatSubclasses, ham, pepperoni, salami, spicybeef); //create list of all meat subclasses
				Functions.putAllAsSubclass(df, pizzaOntology, meatSubclasses, meat); //put all meat subclasses under meat class
		
			OWLClass seafood = Functions.declareClass(df, documentIRI, pizzaOntology, "SeafoodTopping");
				OWLClass anchovy = Functions.declareClass(df, documentIRI, pizzaOntology, "AnchovyTopping");
				OWLClass prawn = Functions.declareClass(df, documentIRI, pizzaOntology, "PrawnTopping");
				OWLClass tuna = Functions.declareClass(df, documentIRI, pizzaOntology, "TunaTopping");
				
				//pub as subclasses
				List<OWLClass> seafoodSubclasses = new ArrayList<OWLClass>();
				Collections.addAll(seafoodSubclasses, anchovy, prawn, tuna); //create list of all seafood subclasses
				Functions.putAllAsSubclass(df, pizzaOntology, seafoodSubclasses, seafood); //put all seafood subclasses under seafood overall class
		
			
			OWLClass vegetable = Functions.declareClass(df, documentIRI, pizzaOntology, "VegetableTopping");
				OWLClass caper = Functions.declareClass(df, documentIRI, pizzaOntology, "CaperTopping");
				OWLClass mushroom = Functions.declareClass(df, documentIRI, pizzaOntology, "MushroomTopping");
				OWLClass olive = Functions.declareClass(df, documentIRI, pizzaOntology, "OliveTopping");
				OWLClass onion = Functions.declareClass(df, documentIRI, pizzaOntology, "OnionTopping");
				OWLClass tomato = Functions.declareClass(df, documentIRI, pizzaOntology, "TomatoTopping");
				OWLClass pepper = Functions.declareClass(df, documentIRI, pizzaOntology, "PepperTopping");
				
				List<OWLClass> vegSubclasses = new ArrayList<OWLClass>();
				Collections.addAll(vegSubclasses, caper, mushroom, olive, onion, tomato, pepper); //create list of all veg subclasses
				Functions.putAllAsSubclass(df, pizzaOntology, vegSubclasses, vegetable); //put all veg subclasses under veg class
		
					OWLClass redPepper = Functions.declareClass(df, documentIRI, pizzaOntology, "RedPepperTopping");
					OWLClass greenPepper = Functions.declareClass(df, documentIRI, pizzaOntology, "GreenPepperTopping");
					OWLClass jalapenoPepper = Functions.declareClass(df, documentIRI, pizzaOntology, "JalapenoPepperTopping");
					
					List<OWLClass> pepperSubclasses = new ArrayList<OWLClass>();
					Collections.addAll(pepperSubclasses, redPepper, greenPepper, jalapenoPepper); //create list of all pepper subclasses
					Functions.putAllAsSubclass(df, pizzaOntology, pepperSubclasses, pepper); //put all pepper subclasses under pepper class
			
			//put toppings subclasses all under pizzatopping class
			List<OWLClass> toppingSubclasses = new ArrayList<OWLClass>();
			Collections.addAll(toppingSubclasses, cheese, meat, seafood, vegetable);
			Functions.putAllAsSubclass(df, pizzaOntology, toppingSubclasses, topping);
			
				
			//make topping classes disjoint
			List<OWLClass> disjointClassesToppings = new ArrayList<OWLClass>();
			Collections.addAll(disjointClassesToppings, cheese, meat, seafood, vegetable);
			Functions.makeDisjoint(df, documentIRI, pizzaOntology, disjointClassesToppings);
			
			//make topping subclasses disjoint
			List<OWLClass> disjointCheeseToppings = new ArrayList<OWLClass>();
			Collections.addAll(disjointCheeseToppings, mozzarella, parmesan);
			Functions.makeDisjoint(df, documentIRI, pizzaOntology, disjointCheeseToppings);
			
			List<OWLClass> disjointMeatToppings = new ArrayList<OWLClass>();
			Collections.addAll(disjointMeatToppings,ham, pepperoni, salami, spicybeef);
			Functions.makeDisjoint(df, documentIRI, pizzaOntology, disjointMeatToppings);
			
			List<OWLClass> disjointSeafoodToppings = new ArrayList<OWLClass>();
			Collections.addAll(disjointSeafoodToppings, anchovy, prawn, tuna);
			Functions.makeDisjoint(df, documentIRI, pizzaOntology, disjointSeafoodToppings);
			
			List<OWLClass> disjointVegToppings = new ArrayList<OWLClass>();
			Collections.addAll(disjointVegToppings, caper, mushroom, olive, onion, pepper, tomato);
			Functions.makeDisjoint(df, documentIRI, pizzaOntology, disjointVegToppings);

			List<OWLClass> disjointPepperToppings = new ArrayList<OWLClass>();
			Collections.addAll(disjointPepperToppings, redPepper, greenPepper, jalapenoPepper);
			Functions.makeDisjoint(df, documentIRI, pizzaOntology, disjointPepperToppings);
			
			
			//Create some object properties
			OWLObjectProperty hasIngredient = df.getOWLObjectProperty(":hasIngredient", prefixe);
			OWLObjectProperty hasTopping = df.getOWLObjectProperty(":hasTopping", prefixe);
			OWLObjectProperty hasBase = df.getOWLObjectProperty(":hasBase", prefixe);
			Functions.makeSubproperty(df, pizzaOntology, manager, hasTopping, hasIngredient); //subproperties
			Functions.makeSubproperty(df, pizzaOntology, manager, hasBase, hasIngredient);
			
			//more object properties 
			OWLObjectProperty isToppingOf = df.getOWLObjectProperty(":isToppingOf", prefixe);
			OWLObjectProperty isBaseOf = df.getOWLObjectProperty(":isBaseOf", prefixe);
			//make them inverse properties 
			Functions.makeInverse(df, pizzaOntology, manager, hasTopping, isToppingOf);
			Functions.makeInverse(df, pizzaOntology, manager, hasBase, isBaseOf);
			

			Functions.setDomain(df, pizzaOntology, manager, hasTopping, pizza);
			
			
			Functions.setDomain(df, pizzaOntology, manager, isBaseOf, base);
			Functions.setRange(df, pizzaOntology, manager, isBaseOf, pizza);

			
			
//			//~~~~~~~~~~~~~~~~~~~~~~~remove~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//			//remove axiom & apply change
//			RemoveAxiom removeAxiom = new RemoveAxiom(pizzaOntology, axiom);
//			manager.applyChange(addAxiom);
//
//
//			//~~~~~~~~~~~~~~~~~~~create OWL literals~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//			OWLDatatype integerDatatype = df.getIntegerOWLDatatype();
//			OWLDatatype floatDatatype = df.getFloatOWLDatatype();
//			OWLDatatype doubleDatatype = df.getDoubleOWLDatatype();
//			OWLDatatype booleanDatatype = df.getBooleanOWLDatatype();
//			OWLDatatype StringDatatype = df.getStringOWLDatatype();
//
//			OWLLiteral literal = df.getOWLLiteral("51", integerDatatype);
//
//
//			//~~~~~~~~~~~~~~~~~~~~~~~~~Create object property~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//			OWLObjectProperty hasWife = df.getOWLObjectProperty(":hasWife", prefixe);
//
//
//
//			//~~~~~~~~~~~~~~~~~~~~~~~~~create instances~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//			OWLNamedIndividual john = df.getOWLNamedIndividual(":Jean", prefixe);
//			OWLNamedIndividual mary = df.getOWLNamedIndividual(":Marie", prefixe);
//
//
//
//			//~~~~~~~~~~~~~~~~~~~~~~~~axioms between instances~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//			OWLObjectPropertyAssertionAxiom propertyAssertion = df.getOWLObjectPropertyAssertionAxiom(hasWife, john, mary);
//			manager.addAxiom(pizzaOntology, propertyAssertion);
//
//			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~OWL Class Expression~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//			// use property to connect to class (ie. has part nose) 
//			OWLObjectProperty hasPart = df.getOWLObjectProperty(IRI.create(prefixe + "#hasPart"));
//			OWLClass nose = df.getOWLClass(IRI.create(prefixe + "#bruit"));
//			OWLClassExpression hasPartSomeNose = df.getOWLObjectSomeValuesFrom(hasPart,nose);
//
//			//restrict cardinality
//			OWLObjectExactCardinality hasGenderRestriction = df.getOWLObjectExactCardinality(1, hasWife);
//			OWLClass head = df.getOWLClass(IRI.create(prefixe + "#Tete"));
//
//			//another class, apply expression 
//			OWLSubClassOfAxiom axiome = df.getOWLSubClassOfAxiom(head, hasPartSomeNose);
//			manager.addAxiom(pizzaOntology, axiome);
//
//			//~~~~~~~~~~~~~~~~~~~~~~~~~~datatype restrictions (ie. adult >18yo)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//			OWLDataProperty hasAge = df.getOWLDataProperty(IRI.create(documentIRI + "#hasAge"));
//			OWLDataRange greaterThan18 = df.getOWLDatatypeRestriction(df.getIntegerOWLDatatype(), OWLFacet.MIN_INCLUSIVE, df.getOWLLiteral(18));
//			OWLClassExpression adultDefinition = df.getOWLDataSomeValuesFrom(hasAge, greaterThan18);
//
//			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~annotations~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//			OWLClass adult = df.getOWLClass(":adult", prefixe);
//			OWLAnnotation commentAnno = df.getOWLAnnotation(df.getRDFSComment(), df.getOWLLiteral("A person greater than 18", "en"));
//			OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(adult.getIRI(), commentAnno);
//
//			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~modify axioms (OWL API 5)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//			//delete
//			pizzaOntology.remove(ax); //(1 of 3 ways to do this)
//
//			//replace axioms (put this stuff in main??) 
//			Map<OWLClassExpression, OWLClassExpression> replacements = new HashMap<OWLClassExpression, OWLClassExpression>();
//
//			OWLClass A = df.getOWLClass(documentIRI + "#Viande");  //creating classes
//			OWLClass B = df.getOWLClass(documentIRI + "#Aliment");
//			OWLClass X = df.getOWLClass(documentIRI + "#Carnivore");
//
//			OWLObjectProperty R = df.getOWLObjectProperty(documentIRI + "#Manger"); //creating properties 
//			OWLObjectProperty S = df.getOWLObjectProperty(documentIRI + "#SeNourrir");
//
//			OWLSubClassOfAxiom subAx = df.getOWLSubClassOfAxiom(df.getOWLObjectSomeValuesFrom(R, A), df.getOWLObjectSomeValuesFrom(S, B));  //connecting them to create axioms?  
//			pizzaOntology.add(subAx);
//
//			pizzaOntology.logicalAxioms().forEach(System.out::println); 
//			replacements.put(df.getOWLObjectSomeValuesFrom(R, A), X);  //replace request in replacements Map 
//
//			OWLObjectTransformer<OWLClassExpression> replacer = new OWLObjectTransformer<OWLClassExpression>(  //then make sure you have replacer function
//					(x)->true,
//					(input)->{ OWLClassExpression l = replacements.get(input);
//						if(l==null) {
//							return input;
//							}
//						return l;
//						}, 
//					df, 
//					OWLClassExpression.class
//					);
//			List<OWLOntologyChange> results = replacer.change(pizzaOntology); //take replacer (from function) and make change official  
//			pizzaOntology.applyChanges(results);
//			pizzaOntology.logicalAxioms().forEach(System.out::println);
//
//
//			//go through ontology.... not sure what this stuff is
//			Stream<OWLClass> stream = pizzaOntology.classesInSignature();
//			Set<OWLClass> set = pizzaOntology.classesInSignature().collect(Collectors.toSet());
//			OWLClass[]array = pizzaOntology.classesInSignature().toArray(OWLClass[]::new);
//			
//			
//			//controler violations de profile 
//			OWL2DLProfile profile = new OWL2DLProfile();
//			OWL2ELProfile profile2 = new OWL2ELProfile();
//			OWL2QLProfile profile3 = new OWL2QLProfile();
//			System.out.println(profile2.getName());
//			OWLProfileReport report = profile2.checkOntology(pizzaOntology);
//			for(OWLProfileViolation v:report.getViolations()){
//				System.out.println(v);
//			}
//			
//			//parcourir axiomes
//			pizzaOntology.logicalAxioms().forEach(System.out::println);
//			
//			//get characteristics of an OWLEntity
//			//reasoner (need dependency)
//			//requetes (need dependency)
//			
//			
//			
			//save ontology
			manager.saveOntology(pizzaOntology, documentIRI);
			//manager.removeOntology(pizzaOntology);
			
			

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} 
		
		
		
		
		

	}
}

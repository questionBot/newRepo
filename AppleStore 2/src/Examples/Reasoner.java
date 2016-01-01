package Examples;

/****	IMPORT	****/
/* Import Packages / Libraries */
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/* Import Defined classes/files to work with */
import javax.xml.datatype.XMLGregorianCalendar;

import Examples.AppleStore;
import Examples.Product;
import Examples.Services;
import Examples.Discounts;
import Examples.GeniusAppt;
import Examples.SimpleGUI;

public class Reasoner {

	/* Main class Object that holds the domain knowledge
	 * Will generate classes automatically 
	 * 
	 **/
	
	/*	DECLARE VARIABLES, LISTS etc.	*/

	public AppleStore appleStoresLdn; //This is a candidate for a name change

	public SimpleGUI Myface;

	/*	THE LISTS HOLDING CLASS INSTANCES OF ALL DOMAIN ENTITIES	*/
	public List theAppleStoreList,theProductList,theServiceList,theDiscountList,theGeniusApptList,theRecentThing,theCustomerList = new ArrayList(); 

	/*	VECTORS - ARRAYS OF OBJECTS	*/
	public Vector<String> applestoresyn = new Vector<String>(); 
	public Vector<String> productsyn = new Vector<String>();    
	public Vector<String> servicesyn = new Vector<String>();   
	public Vector<String> discountsyn = new Vector<String>();  
	public Vector<String> geniusapptsyn = new Vector<String>();
	public Vector<String> recentobjectsyn = new Vector<String>();
	public Vector<String> customersyn = new Vector<String>();

	/*	MAIN QUESTION WORDS	*/
	public String list = "list";
	public String amount = "amount";
	public String checkfor = "checkfor";
	
	/*	QUESTIONTYPE SELECTS METHOD TO USE IN QUERY	*/
	public String questiontype = "";         
	/*	CLASSTYPE SELECTS WHICH CLASS LIST TO QUERY	*/
	public List classtype = new ArrayList(); 
	/*	ATTRIBUTE TYPE SELECTS THE ATTRIBUTE TO CHECK FOR IN THE QUERY	*/
	public String attributetype = "";   
	

	/*	LAST OBJECT DEAL WITH	*/
	public Object Currentitemofinterest; 
	/* LAST INDEX TO BE USED	*/
	public Integer Currentindex;        
	public String tooltipstring = "";
	
	/*	URL FOR WEBSITE */
	public String URL = "";       
	
	public Reasoner(SimpleGUI myface) {
		
		/*	REFERENCE TO GUI TO UPDATE TOOLTIP-TEXT*/
		Myface = myface;
		
	}

	/*	SET WEBSITE URL */
	public String setURL(){
		String websiteURL = "http://en.wikipedia.org/wiki/";
		return websiteURL;
	}
	
	/*	GET WEBSITE URL	*/
	public String getURL(){
		return setURL();
	}
	
	/* ACCEPTS STRING ARGUMENT TO BE DIPSPLAYED/PRINTED
	 * Each times when use this function we save 15 character for not using System.out.println().....
	 * */
	public void sop(String str){
		System.out.println(str);
	}

	/*
	 * Define specific Vector type i.e. booksyn
	 * Defines all specific words for book.
	 *  
	 * */
	public void addWordsToVector(Vector<String> vector, String... args) {
		
		String[] bookAsk=null;
	    for (String arg : args) {
	    	/* Define all words that regards to books */
			bookAsk = args;
	    }
	    vector.addAll(Arrays.asList(bookAsk));
	}
	
	/*Returns an item already wrapped up in <li> tag*/
	public String listItem(String listItem){
		return "<li>"+listItem+"</li>";
	}
	
	public void initknowledge() { // load all the library knowledge from XML 

		JAXB_XMLParser xmlhandler = new JAXB_XMLParser(); // we need an instance of our parser

		//This is a candidate for a name change
		File xmlfiletoload = new File("AppleStore.xml"); // we need a (CURRENT)  file (xml) to load  

		/*	Vector type + specific words to be listen	*/
		addWordsToVector(productsyn,"products","product","items","item");
		addWordsToVector(servicesyn,"services","apple care","insurance","camp","training","workshop");
		addWordsToVector(applestoresyn,"apple store","place","store","mac store","new apple products","apple shop");
		addWordsToVector(discountsyn,"discounts","money off","student");
		addWordsToVector(geniusapptsyn,"genius","appointment","broken","fix","help","genius appointment","geniuapt");
		/*	Spaces to prevent collision with "wHERe"*/
		addWordsToVector(recentobjectsyn," this"," that"," him"," her", "it");
		addWordsToVector(customersyn,"customer","my appointment");
		
		
		try {
			FileInputStream readthatfile = new FileInputStream(xmlfiletoload); // initiate input stream

			appleStoresLdn = xmlhandler.loadXML(readthatfile);

			// Fill the Lists with the objects data just generated from the xml

			theProductList = appleStoresLdn.getProduct();  		
			theServiceList = appleStoresLdn.getServices(); 	
			theDiscountList = appleStoresLdn.getDiscounts(); 	
			theGeniusApptList = appleStoresLdn.getGeniusAppt(); 
			theCustomerList = appleStoresLdn.getCustomer();
			theAppleStoreList.add(appleStoresLdn);             // force it to be a List
			
			
			sop("List reading");
		}

		catch (Exception e) {
			e.printStackTrace();
			sop("error in init");
		}
	}

	/*

	 * It accepts question type and an input + any number of arguments 
	 * and do something with it. Specified Strings will be displayed in bold.
	 *
	 *Example use: 
	 * 	acceptQuestionType(input, list, "list all","display all","show all");
	 * 
	 * */
	public String acceptQuestionType(String input, String two, String... args){
		
	    for (String arg : args) {	    	
	    	if (input.contains(arg)){questiontype = two; 
			input = input.replace(arg, "<b>"+arg+"</b>");} 		
	    }
		return input;
	}
	
	public void acceptQuestionTypeOr(String input,String two,String questionType,String message, String... args){		
		 for (String arg : args) {	    	
			 if (input.contains(two) 
				|| input.contains(arg));
		} 
		questiontype = questionType;
				sop(message);
	} 		
  
	/*  Foreach each answer get answer */
	public String getAnswer(String input, Vector<String> arg1, ArrayList agr2){
		
		/*returns the String, converted to lowercase.*/
		input = input.toLowerCase(); 
		Integer subjectcounter = 0;
		
		for (int x = 0; x < arg1.size(); x++) {   
			if (input.contains((CharSequence) arg1.get(x))) {    
				classtype = agr2;             
				
				input = input.replace((CharSequence) arg1.get(x), "<b>"+arg1.get(x)+"</b>");
				
				subjectcounter = 1;
				sop("Class type" + agr2 + "recognised.");
			}
		}
		return input;
	}
	
	public  Vector<String> generateAnswer(String input) { // Generate an answer (String Vector)

		Vector<String> out = new Vector<String>();
		out.clear();                 // just to make sure this is a new and clean vector
		
		questiontype = "none";

		Integer Answered = 0;        // check if answer was generated

		Integer subjectcounter = 0;  // Counter to keep track of # of identified subjects (classes)
		
		// Answer Generation Idea: content = Questiontype-method(classtype class) (+optional attribute)

		input = input.toLowerCase(); 
		
		// ___________________________________________________________________

		String answer = "";          // the answer we return

		
		/*CHECK FOR A 
		 * 
		 * Below Examples are the same initial code/example as:
		 * if (input.contains("how many")){questiontype = "amount"; input = input.replace("how many", "<b>how many</b>");} 
		 * */		
		acceptQuestionType(input, list, "list","list all","display","display all","show","show all");
		acceptQuestionType(input, amount, "how many","number of","amount of","count");
		acceptQuestionType(input, checkfor, "is there a","i am searching","i am looking for","do you have","i look for","is there");

		//acceptQuestionTypeOr(input,"where","location","Find Location","can't find","can i find","way to" );
		
		if (input.contains("where") 
				|| input.contains("can't find")
				|| input.contains("can i find") 
				|| input.contains("way to"))

		{
			questiontype = "location";
			sop("Find Location");
		}
		
		if (input.contains("can i buy") 
				|| input.contains("can i get")
				|| input.contains("can i find")
				|| input.contains("am i able to")
				|| input.contains("could i source") 
				|| input.contains("i want to buy")
				|| input.contains("i want to purchase"))

		{
			questiontype = "intent";
			sop("Find Product Availability");
		}
		
		if (input.contains("thank you") 
				|| input.contains("bye")
				|| input.contains("thanks")
				|| input.contains("cool thank")) 			

		{
			questiontype = "farewell";
			sop("farewell");
		}


		/*	Checking the Subject of the Question	*/

		/* Products */
		getAnswer(input, productsyn, (ArrayList) theProductList);
		/* Services */
		getAnswer(input, servicesyn, (ArrayList) theServiceList);
		/* Discounts */
		getAnswer(input, discountsyn, (ArrayList) theDiscountList);
		/* Genius Appointment */
		getAnswer(input, geniusapptsyn, (ArrayList) theGeniusApptList);
		
		
		if(subjectcounter == 0){
			for (int x = 0; x < recentobjectsyn.size(); x++) {  
				if (input.contains(recentobjectsyn.get(x))) {
					classtype = theRecentThing;
					
					input = input.replace(recentobjectsyn.get(x), "<b>"+recentobjectsyn.get(x)+"</b>");
					
					subjectcounter = 1;
					sop("Class type recognised as"+recentobjectsyn.get(x));
				}
			}
		}

		// More than one subject in question + Library ...
		// "Does the Library has .. Subject 2 ?"

		sop("subjectcounter = "+subjectcounter);

		for (int x = 0; x < applestoresyn.size(); x++) {  

			if (input.contains(applestoresyn.get(x))) {   

				// Problem: "How many Books does the Library have ?" -> classtype = Library
				// Solution:
				
				if (subjectcounter == 0) { // Library is the first subject in the question
					
					input = input.replace(applestoresyn.get(x), "<b>"+applestoresyn.get(x)+"</b>");
					
					classtype = theAppleStoreList;        //This is a candidate for a name change

					sop("class type Apple Store recognised");		

				}
			}
		}

		// Compose Method call and generate answerVector

		if (questiontype == "amount") { // Number of Subject

			Integer numberof = Count(classtype);

			answer=("The number of "
					+ classtype.get(0).getClass().getSimpleName() + "s is "
					+ numberof + ".");
					

			Answered = 1; // An answer was given

		}

		if (questiontype == "list") { // List all Subjects of a kind

			answer=("You asked for the listing of all "
					+ classtype.get(0).getClass().getSimpleName() + "s. <br>"
					+ "We have the following "
					+ classtype.get(0).getClass().getSimpleName() + "s:"
					// display/list items 
					+ ListAll(classtype));
			Answered = 1; // An answer was given

		}

		if (questiontype == "checkfor") { // test for a certain Subject instance

			Vector<String> check = CheckFor(classtype, input);
			answer=(check.get(0) + ListAll(classtype));
			Answered = 1; // An answer was given
			if (check.size() > 1) {
				Currentitemofinterest = classtype.get(Integer.valueOf(check
						.get(1)));
				sop("Classtype List = "
						+ classtype.getClass().getSimpleName());
				sop("Index in Liste = "
						+ Integer.valueOf(check.get(1)));
				Currentindex = Integer.valueOf(check.get(1));
				theRecentThing.clear(); // Clear it before adding (changing) the
				// now recent thing
				theRecentThing.add(classtype.get(Currentindex));
			}
		}

		// Location Question in Pronomial form "Where can i find it"

		if (questiontype == "location") {   // We always expect a pronomial question to refer to the last
											// object questioned for

			answer=("You can find the "
					+ classtype.get(0).getClass().getSimpleName() + " " + "at "
					+ Location(classtype, input));

			Answered = 1; // An answer was given
		}

		if ((questiontype == "intent" && classtype == theProductList) 
				||(questiontype == "intent" && classtype == theRecentThing)) {

			// Can I lend the book or not (Can I lent "it" or not)
			//answer=("You "+ BookAvailable(classtype, input));   <<<<< BOOKAVAILABLE METHOD UNDER CONSTRUCTION BELOW
			Answered = 1; // An answer was given
		}

		if (questiontype == "farewell") {       // Reply to a farewell
			
			answer=("You are welcome.");

			Answered = 1; // An answer was given
		}
		
		
		if (Answered == 0) { // No answer was given

			answer=("Sorry I didn't understand that.");
		}

		out.add(input);
		out.add(answer);
		
		return out;
	}

	// Methods to generate answers for the different kinds of Questions
	
	// Answer a question of the "Is a book or "it (meaning a book) available ?" kind
	/**
	 * This needs editing - our code can look for Appointment Slots - not Products - may need to create a 
	 * Products in Store Class? E.G. available products within the apple store class.
	 *    CHANGING TO APPOINTMENT AVAILABLE
	 */
	public String ApptAvailable(List thelist, String input) {

		boolean available =true;
		String answer ="";
		Product curbook = new Product();
		GeniusAppt curAppt = new GeniusAppt();
		String productname="";
		XMLGregorianCalendar appDate = null;

		if (thelist == theGeniusApptList) {                    

			int counter = 0;

			//Identify which book is asked for 

			for (int i = 0; i < thelist.size(); i++) {

				curAppt = (GeniusAppt) thelist.get(i);        

				if (input.equals(curAppt.getAppdate())         
						|| input.equals(curAppt.getApptime().toLowerCase())) {  

					counter = i;

					Currentindex = counter;
					theRecentThing.clear(); 									//Clear it before adding (changing) the
					classtype = theGeniusApptList;                                   
					theRecentThing.add(classtype.get(Currentindex));
					appDate=curAppt.getAppdate();
										
					if (input.contains(curAppt.getProblem().toLowerCase())){input = input.replace(curAppt.getProblem().toLowerCase(), "<b>"+curAppt.getProblem().toLowerCase()+"</b>");}          
					if (input.equals(curAppt.getAppdate())) {input = input.replace(curAppt.getAppdate().toString().toLowerCase(), "<b>"+curAppt.getAppdate().toString().toLowerCase()+"</b>");}     
					if (input.contains(curAppt.getApptime().toLowerCase())){input = input.replace(curAppt.getApptime().toLowerCase(), "<b>"+curAppt.getApptime().toLowerCase()+"</b>");}
										
					i = thelist.size() + 1; 									// force break
				}
			}
		}

		// maybe other way round or double 

		if (thelist == theRecentThing && theRecentThing.get(0) != null) {

			if (theRecentThing.get(0).getClass().getSimpleName()
					.toLowerCase().equals("book")) {                 

				curAppt = (GeniusAppt) theRecentThing.get(0);        		
				appDate=curAppt.getAppdate();
			}
		}

		// check all lendings if they contain the books ISBN
		
		for (int i = 0; i < theGeniusApptList.size(); i++) {

			GeniusAppt curlend = (GeniusAppt) theGeniusApptList.get(i);        

			// If there is a lending with the books ISBN, the book is not available

			if ( curAppt.getAppdate().equals(curlend.getAppdate()) &&
					curAppt.getApptime().contains(curlend.getApptime())) {          

				input = input.replace(curlend.getAppdate().toString(), "<b>"+curlend.getAppdate().toString()+"</b>");
				
				available=false;
				i = thelist.size() + 1; 									// force break
			}
		}

		if(available){
			answer="This appointment is available.";
		}
		else{ 
			answer="There are no slots available for this time.";
		}

		URL = getURL() + curAppt.getProblem();
		sop("URL = "+URL);
		tooltipstring = readwebsite(URL);
		String html = "<html>" + tooltipstring + "</html>";
		Myface.setmytooltip(html);
		Myface.setmyinfobox(URL);

		return(answer);

	}
	
	//end here <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NEEDS TO BE TESTED

	// Answer a question of the "How many ...." kind 
	
	public Integer Count(List thelist) { // List "thelist": List of Class Instances (e.g. theBookList)


		URL = getURL() + classtype.get(0).getClass().getSimpleName().toLowerCase();
		sop("URL = "+URL);
		tooltipstring = readwebsite(URL);
		String html = "<html>" + tooltipstring + "</html>";
		Myface.setmytooltip(html);
		Myface.setmyinfobox(URL);

		return thelist.size();
	}

	// Answer a question of the "What kind of..." kind
	
	public String ListAll(List thelist) {

		String listemall = "<ul>";

		if (thelist == theProductList) {              
			for (int i = 0; i < thelist.size(); i++) {
				Product curbook = (Product) thelist.get(i);                 
				listemall = listemall + listItem(curbook.getProductname());    
			}
		}

		if (thelist == theServiceList) {                             
			for (int i = 0; i < thelist.size(); i++) {
				Services curmem = (Services) thelist.get(i);         
				listemall = listemall +
						listItem(curmem.getNameofservice() + " " + curmem.getPriceRange());  
			}
		}

		if (thelist == theDiscountList) {                               
			for (int i = 0; i < thelist.size(); i++) {
				Discounts curcat = (Discounts) thelist.get(i);          
				listemall = listemall 
						+listItem(curcat.getDiscounttype());      
			}
		}
		
		if (thelist == theGeniusApptList) {                            
			for (int i = 0; i < thelist.size(); i++) {
				GeniusAppt curlend = (GeniusAppt) thelist.get(i);      
				listemall = listemall + "<li>" 
						+ listItem(curlend.getProblem());            
			}
		}
		
		listemall += "</ul>";
		
		URL = getURL() + classtype.get(0).getClass().getSimpleName().toLowerCase();
		sop("URL = "+URL);
		tooltipstring = readwebsite(URL);
		String html = "<html>" + tooltipstring + "</html>";
		Myface.setmytooltip(html);
		Myface.setmyinfobox(URL);
		
		return listemall;
	}

	// Answer a question of the "Do you have..." kind 
	
	public Vector<String> CheckFor(List thelist, String input) {

		Vector<String> yesorno = new Vector<String>();
		if (classtype.isEmpty()){
			yesorno.add("Class not recognised. Please specify if you are searching for a book, catalog, member, or lending?");
		} else {
			yesorno.add("No we don't have such a "
				+ classtype.get(0).getClass().getSimpleName());
		}

		Integer counter = 0;

		if (thelist == theProductList) {                         

			for (int i = 0; i < thelist.size(); i++) {

				Product curbook = (Product) thelist.get(i);                

				if (input.contains(curbook.getProductname().toLowerCase())         
						|| input.contains(curbook.getSerialnumber().toLowerCase()) 
						|| input.contains(curbook.getProducttype().toLowerCase())) {

					counter = i;
					yesorno.set(0, "Yes we have such a Product");              
					yesorno.add(counter.toString());
					i = thelist.size() + 1; // force break
				}
			}
		}

		if (thelist == theServiceList) {                                   
			for (int i = 0; i < thelist.size(); i++) {
				Services curmem = (Services) thelist.get(i);                    
				if (input.contains(curmem.getNameofservice().toLowerCase())     
						|| input.contains(curmem.getPriceRange().toLowerCase()) 
						|| input.equals(true)) {  

					counter = i;
					yesorno.set(0, "Yes we have such a Service");           
					yesorno.add(counter.toString());
					i = thelist.size() + 1;
				}
			}
		}

		if (thelist == theDiscountList) {                                   
			for (int i = 0; i < thelist.size(); i++) {
				Discounts curcat = (Discounts) thelist.get(i);                  
				if (input.contains(curcat.getDiscounttype().toLowerCase())      
						|| input.contains(curcat.getDiscountpercentage().toLowerCase())) { 

					counter = i;
					yesorno.set(0, "Yes we have such a Discount");           
					yesorno.add(counter.toString());
					i = thelist.size() + 1;
				}
			}
		}
		
		
		
		if (thelist == theGeniusApptList) {                                  
			for (int i = 0; i < thelist.size(); i++) {
				GeniusAppt curlend = (GeniusAppt) thelist.get(i);            
				if (input.contains(curlend.getProblem().toLowerCase())       
					|| input.equals(curlend.getAppdate())){ 

					counter = i;
					yesorno.set(0, "This appointment is taken.");        
					yesorno.add(counter.toString());
					i = thelist.size() + 1;
				}
			}
		}

		if (classtype.isEmpty()) {
			sop("Not class type given.");
		} else {
		
			URL = getURL() + classtype.get(0).getClass().getSimpleName().toLowerCase();
			sop("URL = "+URL);
			tooltipstring = readwebsite(URL);
			String html = "<html>" + tooltipstring + "</html>";
			Myface.setmytooltip(html);
			Myface.setmyinfobox(URL);
		}
	
		return yesorno;
	}

	//  Method to retrieve the location information from the object (Where is...) kind

	public String Location(List classtypelist, String input) {

		List thelist = classtypelist;
		String location = "";
		String accepted = "";

		// if a pronomial was used "it", "them" etc: Reference to the recent thing

		if (thelist == theRecentThing && theRecentThing.get(0) != null) {

			if (theRecentThing.get(0).getClass().getSimpleName()
					.toLowerCase().equals("product")) {                 

				Product curbook = (Product) theRecentThing.get(0);      
				location = (curbook.getLocation() + " ");             

			}
			
			/**
			 * 
			if (theRecentThing.get(0).getClass().getSimpleName()
					.toLowerCase().equals("member")) {               //This is a candidate for a name change

				Member curmem = (Member) theRecentThing.get(0);      //This is a candidate for a name change
				location = (curmem.getCity() + " " + curmem.getStreet() + " " + curmem  //This is a candidate for a name change
						.getHousenumber());                                    //This is a candidate for a name change

			}**/

			if (theRecentThing.get(0).getClass().getSimpleName()  
					.toLowerCase().equals("discounts")) {                 //This is a candidate for a name change

				Discounts curcat = (Discounts) theRecentThing.get(0);       //This is a candidate for a name change
				accepted = (curcat.isAccepted() + " ");                //This is a candidate for a name change

			}

			if (theRecentThing.get(0).getClass().getSimpleName()    
					.toLowerCase().equals("Apple Store")) {                  //This is a candidate for a name change

				location = (appleStoresLdn.getCity() + " " + appleStoresLdn.getStreet() + appleStoresLdn   //This is a candidate for a name change
						.getHousenumber());                                           //This is a candidate for a name change
			}

		}

		// if a direct noun was used (book, member, etc)

		else {

			if (thelist == theProductList) {                         //This is a candidate for a name change

				int counter = 0;

				for (int i = 0; i < thelist.size(); i++) {

					Product curproduct = (Product) thelist.get(i);         //This is a candidate for a name change

					if (input.contains(curproduct.getProductname().toLowerCase())            //This is a candidate for a name change
							|| input.contains(curproduct.getSerialnumber().toLowerCase())      //This is a candidate for a name change
							|| input.contains(curproduct.getProducttype().toLowerCase())) {  //This is a candidate for a name change

						counter = i;
						location = (curproduct.getLocation() + " ");
						Currentindex = counter;
						theRecentThing.clear(); 									// Clear it before adding (changing) theRecentThing
						classtype = theProductList;                                    //This is a candidate for a name change
						theRecentThing.add(classtype.get(Currentindex));
						i = thelist.size() + 1; 									// force break
					}
				}
			}

			
			////where members class is used we should add a customer class
			//service used for initial testing purposes
			if (thelist == theServiceList) {                                         

				int counter = 0;

				for (int i = 0; i < thelist.size(); i++) {

					Services curservice = (Services) thelist.get(i);         				  

					if (input.contains(curservice.getNameofservice().toLowerCase())            
							|| input.contains(curservice.getPriceRange().toLowerCase())    
							|| input.equals(curservice.isAvailable())) {  

						counter = i;
						//location = (curmember.getCity() + " ");    <<<<< needs modifying
						Currentindex = counter;
						theRecentThing.clear(); 										
						classtype = theServiceList;            	 						
						theRecentThing.add(classtype.get(Currentindex));
						i = thelist.size() + 1; 				             	        // force break
					}
				}
			}

			if (thelist == theDiscountList) {                                       	

				int counter = 0;

				for (int i = 0; i < thelist.size(); i++) {

					Discounts curcatalog = (Discounts) thelist.get(i);                  

					if (input.contains(curcatalog.getDiscounttype().toLowerCase())      						     
							|| input.contains(curcatalog.getDiscountpercentage().toLowerCase())) {   

						counter = i;
						//location = (curcatalog.getLocation() + " ");  <<< needs modifying
						Currentindex = counter;
						theRecentThing.clear();                                      // Clear it before adding (changing) the	
						classtype = theDiscountList;                                  
						theRecentThing.add(classtype.get(Currentindex));
						i = thelist.size() + 1;                                      // force break
					}
				}
			}

			if (thelist == theAppleStoreList) {   //This is a candidate for a name change
				
				
				int counter = 0;

				for (int i = 0; i < thelist.size(); i++) {

					AppleStore curstore = (AppleStore) thelist.get(i);         

					if (input.contains(curstore.getName().toLowerCase())          
							|| input.contains(curstore.getStreet().toLowerCase())      
							|| input.contains(curstore.getPostcode().toLowerCase())) { 

						counter = i;
						location = (curstore.getHousenumber()+" "+ curstore.getStreet()+" " +
								curstore.getCity()+" " +curstore.getPostcode() +" ");
						Currentindex = counter;
						theRecentThing.clear(); 									// Clear it before adding (changing) theRecentThing
						classtype = theAppleStoreList;                  
						theRecentThing.add(classtype.get(Currentindex));
						i = thelist.size() + 1; 									// force break
					}
				}
			

						//>>>>>>CHANGED - NOT TESTED<<<<<<
				location = (appleStoresLdn.getCity() + " " + appleStoresLdn.getStreet() + appleStoresLdn 
						.getHousenumber());                                                  
			}
		}

		URL = getURL() + classtype.get(0).getClass().getSimpleName().toLowerCase();
		sop("URL = "+URL);
		tooltipstring = readwebsite(URL);
		String html = "<html>" + tooltipstring + "</html>";
		Myface.setmytooltip(html);
		Myface.setmyinfobox(URL);

		return location;
	}

	public String testit() {   // test the loaded knowledge by querying for books written by dostoyjewski

		String answer = "";

		sop("Product List = " + theProductList.size());  

		for (int i = 0; i < theProductList.size(); i++) {   // check each book in the List, //This is a candidate for a name change

			Product curbook = (Product) theProductList.get(i);    // cast list element to Book Class //This is a candidate for a name change												
			sop("Testing Book" + curbook.getProducttype());

			if (curbook.getProducttype().equalsIgnoreCase("laptop")) {     // check for the author //This is a candidate for a name change

				answer = "An example of a " + curbook.getProducttype() + "\n"  
						+ " is for example a " + curbook.getProductname()      
						+ ".";
			}
		}
		return answer;
	}

	public String readwebsite(String url) {

		String webtext = "";
		try {
			BufferedReader readit = new BufferedReader(new InputStreamReader(
					new URL(url).openStream()));

			String lineread = readit.readLine();

			sop("Reader okay");

			while (lineread != null) {
				webtext = webtext + lineread;
				lineread = readit.readLine();				
			}

			// Hard coded cut out from "wordnet website source text": 
			//Check if website still has this structure   vvvv ...definitions...  vvvv 		
			
			webtext = webtext.substring(webtext.indexOf("<ul>"),webtext.indexOf("</ul>"));                                 //               ^^^^^^^^^^^^^^^^^              

			webtext = "<table width=\"700\"><tr><td>" + webtext
					+ "</ul></td></tr></table>";

		} catch (Exception e) {
			webtext = "Not yet";
			sop("Error connecting to "+getURL());
		}
		return webtext;
	}
}

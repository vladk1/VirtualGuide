SDK use example

/* Create a new member/user of your app */
User user = new User();
user.setUserName("johnDoe");
user.setPassword("1,618");

/* configure datastore with member credentials */
Datastore.configure(User.baseURL, User.apiKey, user.getUserName(), user.getPassword(), User.sdkVersion, User.system); //pure java
/* or with Android even easier: 
Datastore.configure( user );
*/
/* and save it */
user.save();
	
/* Now you can create objects of your class with this new member */
POI myPOI = new POI();
// you may set attributes here...
myPOI.save( ); 

/* 
* Later on you may want to load data using query arguments
* More query examples can be found in @see <a href="http://doc.apiomat.com">our wiki</a>
*/
// This statement  will return all objects with exact name \"XYZ\"
List<POI> results_name = POI.getPOIs( "name==\"XYZ\"" );
// This statement will return all objects near a point (location) with latitude 11.5 and longitude 50.0
List<POI> results_location = POI.getPOIs( "location near [11.5,50.0]" );

/* You can also load the element from a URL */
POI loadedObj = new POI();
loadedObj.load(myPOI.getHref());

/* At the end we delete the object */
myPOI.delete();

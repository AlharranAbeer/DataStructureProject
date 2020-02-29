import java.io.*;
public class LocNotManager {
	// Load notifications from file. Assume format is correct. The notifications are
	// indexed by latitude then by longitude. -----------------------------100%-----------------------
	@SuppressWarnings("resource")
	public static Map<Double, Map<Double, LocNot>> load(String fileName) {
		Map<Double, Map<Double, LocNot>> map = new  BST<Double, Map<Double, LocNot>>();
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader (new File(fileName)));
			String s;
		while (true) {
			 s = bf.readLine();
			String [] array = s.split("\t");
			Double LAT = Double.parseDouble(array[0]);
			Double LNG = Double.parseDouble(array[1]);
			Integer MAX = Integer.parseInt(array[2]);
			Integer REP = Integer.parseInt(array[3]);
	    LocNot m = new LocNot(array[4], LAT , LNG ,MAX , REP);
             addNot(map,m);
		}
	}
		catch(Exception e) {
			System.out.println(e);
		}
	return map;}

	// Save notifications to file.   -------------------------- 100% -----------------------
	@SuppressWarnings("resource")
	public static void save(String fileName, Map<Double, Map<Double, LocNot>> nots) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			PrintWriter pw = new PrintWriter(fos);
			 List<Pair<Double, Map<Double, LocNot>>> d = nots.getAll();
			if (!d.empty()) {
				d.findFirst();
			while(!d.last()) {
				 List<Pair<Double, LocNot>> j =d.retrieve().second.getAll();
				 j.findFirst();
				 while (!j.last()) {
				pw.print(j.retrieve().second.toString());
				j.findNext();}
				pw.print(j.retrieve().second.toString());
				
				d.findNext();	}
		
			 List<Pair<Double, LocNot>> j =d.retrieve().second.getAll();
			 j.findFirst();
			 while (!j.last()) {
			pw.print(j.retrieve().second.toString());
			j.findNext();}
			pw.print(j.retrieve().second.toString());}
		}
			catch(Exception e ) {
			e.printStackTrace();}
		}

	// Add a notification. Returns true if insert took place, false otherwise. ---------------100%-----------------
	public static boolean addNot(Map<Double, Map<Double, LocNot>> nots, LocNot not) {
		Map<Double,LocNot> l = new BST<Double,LocNot>();
		boolean flag , flag2;
		   if (nots.find(not.getLat())) {
			   l = nots.retrieve();
			   return l.insert(not.getLng(), not);  }
		  else {
			 flag= l.insert(not.getLng(), not);
			    flag2= nots.insert(not.getLat(), l);
			     
			   return flag&&flag2; 
			   }
		   }

	// Delete the notification at (lat, lng). Returns true if delete took place, false otherwise. ------ 50% ---------
	public static boolean delNot(Map<Double, Map<Double, LocNot>> nots, double lat, double lng) {
		boolean flag = false ;
		            if (nots.find(lat)) {
		            	flag = nots.retrieve().remove(lng);
		            	if (nots.find(lat))
		                if (nots.retrieve().empty())
		            	 nots.remove(lat);
		            return flag;}
		return false;
	}
	
	// Return all notifications sorted first by latitude then by longitude. --------- 50%--------------
	public static List<LocNot> getAllNots(Map<Double, Map<Double, LocNot>> nots) {
	     List<LocNot> l = new LinkedList<LocNot>();
        	List<Pair<Double,Map<Double,LocNot>>> l2 = nots.getAll();
        	 if (! l2.empty()) {
        	 l2.findFirst();
        	 while (!l2.last()) {
        		 List<Pair<Double , LocNot>> l3 = l2.retrieve().second.getAll();
        		 if (! l3.empty()) {
        		 l3.findFirst();
        		 while (!l3.last()) {
        			 l.insert(l3.retrieve().second);
        		 l3.findNext();
        		 } // end of while loop inner
            l.insert(l3.retrieve().second);
            }
        	l2.findNext();
        	}// end of while loop
             
        	 List<Pair<Double , LocNot>> l3 = l2.retrieve().second.getAll();
    		 if ( ! l3.empty()) {
    		 l3.findFirst();
    		 while (!l3.last()) {
    			 l.insert(l3.retrieve().second);
    		 l3.findNext();}
    		l.insert(l3.retrieve().second);}
    		 }
         
	return l;}
	
	// Return the list of notifications within a square of side dst (in meters) centered at the position (lat, lng) 
	//(it does not matter if the notification is active or not). 
	//Do not call Map.getAll().
	public static List<LocNot> getNotsAt(Map<Double, Map<Double, LocNot>> nots, double lat, double lng, double dst) {
		double angle = GPS.angle((dst/2));
		double lat1 = angle +lat;
		double lat2 = lat - angle;
		double lng1  = angle + lng;
		double lng2 = lng - angle;
		List<LocNot> myList = new LinkedList<LocNot>();
		List<Pair<Double , Map<Double , LocNot>>> listofmap = nots.getRange(lat2,lat1);
		if (! listofmap.empty()) {
			listofmap.findFirst();
			while (! listofmap.last()) {
				List<Pair<Double , LocNot>> listy = listofmap.retrieve().second.getRange(lng2, lng1);
				if (! listy.empty()) {
					listy.findFirst();
					while (! listy.last()) {
						myList.insert(listy.retrieve().second);
						listy.findNext();
					}myList.insert(listy.retrieve().second);
				} // end of listy not empty 
					
				listofmap.findNext();
			}// end of big while 
			List<Pair<Double , LocNot>> listy = listofmap.retrieve().second.getRange(lng2, lng1);
			if (! listy.empty()) {
				listy.findFirst();
				while (! listy.last()) {
					myList.insert(listy.retrieve().second);
					listy.findNext();
				}myList.insert(listy.retrieve().second);
			} // end of listy not empty 
       }
		
	return myList;}
	
	// Return the list of active notifications within a square of side dst (in meters) centered at the position (lat, lng).
	//Do not call Map.getAll().
	public static List<LocNot> getActiveNotsAt(Map<Double, Map<Double, LocNot>> nots, double lat, double lng, double dst) {
	        List<LocNot> FinalList = new LinkedList<LocNot>();
	        double angle = GPS.angle((dst/2));
			double lat1 = angle +lat;
			double lat2 = lat - angle;
			double lng1  = angle + lng;
			double lng2 = lng - angle;
			List<Pair<Double , Map<Double , LocNot>>> temp = nots.getRange(lat2, lat1);
			if (!temp.empty()) {
				temp.findFirst();
				while (! temp.last()){
					List<Pair<Double,LocNot>> temp2 = temp.retrieve().second.getRange(lng2, lng1);
					if (! temp2.empty()) {
						temp2.findFirst();
						while(!temp2.last()) {
							if (temp2.retrieve().second.isActive())
							FinalList.insert(temp2.retrieve().second);
							temp2.findNext();
						}
						if (temp2.retrieve().second.isActive())
							FinalList.insert(temp2.retrieve().second);
						
					}
					temp.findNext();
				}
				List<Pair<Double,LocNot>> temp2 = temp.retrieve().second.getRange(lng2, lng1);
				if (! temp2.empty()) {
					temp2.findFirst();
					while(!temp2.last()) {
						if (temp2.retrieve().second.isActive())
						FinalList.insert(temp2.retrieve().second);
						temp2.findNext();
					}
					if (temp2.retrieve().second.isActive())
						FinalList.insert(temp2.retrieve().second);
				}
	} // end of if list temp empty 
return FinalList;}

	// Perform task of any active notification within a square of side dst (in meters) centered at the position (lat, lng) 
	//(call method perform). Do not call Map.getAll().
	public static void perform(Map<Double, Map<Double, LocNot>> nots, double lat, double lng, double dst) {
		List <LocNot > tasks = getActiveNotsAt(nots,lat,lng,dst);
		if (tasks.empty()) return;
		tasks.findFirst();
		while (!tasks.last()) {
			tasks.retrieve().perform();
			tasks.findNext();
		}tasks.retrieve().perform();
	}

	// Return a map that maps every word to the list of notifications in which it appears. 
	//The list must have no duplicates.
	public static Map<String, List<LocNot>> index(Map<Double, Map<Double, LocNot>> nots) {
		
	Map<String , List<LocNot>> map = new BST<String , List<LocNot>>();	
	List<LocNot> l  = getAllNots(nots); 
	
	if (!l.empty()) {
		l.findFirst();
		while (!l.last()) {
			String stringArray [] = l.retrieve().getText().split(" ");
			for (int i = 0 ; i < stringArray.length ; i++) {
				if (map.find(stringArray[i])) { 
					if (!Exists(l.retrieve().getText(), map.retrieve()))
						map.retrieve().insert(l.retrieve());
					}
				else
				{
					List<LocNot> loc =  new LinkedList<LocNot>();
					loc.insert(l.retrieve());
						map.insert(stringArray[i], loc);
					}
				
				} // end of for loop
			l.findNext(); 
		}// end of while loop
// -------------------------------------------- LAST ELEMENT ------------------------------------------------	
		String stringArray [] = l.retrieve().getText().split(" ");
		for (int i = 0 ; i < stringArray.length ; i++) {
			if (map.find(stringArray[i])) { 
				if (!Exists(l.retrieve().getText(), map.retrieve()))
					map.retrieve().insert(l.retrieve());
				}
			else
			{
				List<LocNot> loc =  new LinkedList<LocNot>();
				loc.insert(l.retrieve());
					map.insert(stringArray[i], loc);
				}
			
			}
//----------------------------------------------LAST ELEMENT ----------------------------------------------
	}// end of if statment 
return map;}
	
	private static boolean Exists (String word, List<LocNot> myList) {
		if (myList.empty())
			return false;
		myList.findFirst();
	        while(!myList.last())
	        {
	        if(word.equals(myList.retrieve().getText()))
	            return true;
	        myList.findNext();
	        }
	         if(word.equals(myList.retrieve().getText()))
	            return true;
	         return false;
	}

	// Delete all notifications containing the word w.
	public static void delNots(Map<Double, Map<Double, LocNot>> nots, String w) {
	   Map <String  , List<LocNot>> map = index(nots);
		if (map.find(w))
		{List<LocNot> ls = map.retrieve();
		if (ls.empty()) return;
		ls.findFirst();
		while (!ls.last()) {
			delNot(nots, ls.retrieve().getLat() , ls.retrieve().getLng());
		ls.findNext();}
			
		 delNot(nots, ls.retrieve().getLat() , ls.retrieve().getLng());
		
		}
	}
	// --------------------------------------------------------------------------------------------------------------------------
	
	// ____________________________________________________________________________________________________________________
	// Print a list of notifications in the same format used in file. __________________________________________________
	public static void print(List<LocNot> l) {
		System.out.println("-------------------------------------------------------------------------------------");
		if (!l.empty()) {
			l.findFirst();
			while (!l.last()) {
				System.out.println(l.retrieve());
				l.findNext();
			}
			System.out.println(l.retrieve());
		} else {
			System.out.println("Empty");
		}
		System.out.println("------------------");
	}

	// Print an index.______________________________________________________________________________________________
	public static void print(Map<String, List<LocNot>> ind) {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		List<Pair<String, List<LocNot>>> l = ind.getAll();
		if (!l.empty()) {
			l.findFirst();
			while (!l.last()) {
				System.out.println(l.retrieve().first);
				print(l.retrieve().second);
				l.findNext();
			}
			System.out.println(l.retrieve().first);
			print(l.retrieve().second);
		} else {
			System.out.println("Empty");
		}
		System.out.println("++++++++++++++++++");
	}

}

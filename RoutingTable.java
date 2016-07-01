/**
 * Routing Table Simulation
 * TCSS 430 Summer 2016
 * Group 5 Adam Marr, David Humphreys, Gabriel Houle
 */

 import java.util.*;

 public class RoutingTable {
	private List<Entry> entries;

	public RoutingTable() {
		entries = new ArrayList<Entry>();
	}

	/**
	 * Just takes the incomming entry and updates the table
	 * @param theEntry this is the entry that is returned form routers giving us their info
	 * @return portNum returns the port number of the interface to send packet to
	 */
	public void update(Entry theEntry) {
		System.out.println("Entry From New Router: " + theEntry.getIP() + "/" + theEntry.getPrefix());
		entries.add(theEntry);
		int size = entries.size();
		int smallestDifference = Integer.MAX_VALUE;
		Entry entryA = null;
		Entry entryB = null;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(i != j) {
					boolean match = matchingIPLength(entries.get(i), entries.get(j));
					if(match && Math.abs(entries.get(i).getPrefix() - entries.get(j).getPrefix()) < smallestDifference &&
						!entries.get(i).getIP().equals("0.0.0.0") && !entries.get(j).getIP().equals("0.0.0.0")) {
						entryA = entries.get(i);
						entryB = entries.get(j);
						smallestDifference = Math.abs(entries.get(i).getPrefix() - entries.get(j).getPrefix());
					}
				}
			}
		}
		if(entryA != null || entryB != null) {
			System.out.println("Aggregating: " + entryA.getIP() + "/" + entryA.getPrefix() + " and " + entryB.getIP() + "/" + entryB.getPrefix());
			
			if(entryA.getPrefix() - entryB.getPrefix() > 0) {
				entries.remove(entryA);
			}
			else {
				entries.remove(entryB);
			}
		}
		else {
			entries.remove(10);
			System.out.println("Router Updated!");
		}
	}

	private boolean matchingIPLength(Entry entry, Entry entry2) {
		boolean match = true;
		int[] result = new int[4];
		String[] firstIP = entry.getIP().split("\\.");
		String[] secondIP = entry2.getIP().split("\\.");
		int mask = Math.min(entry.getPrefix(), entry.getPrefix());
		int maskOctets = mask/8;
		int maskRemainder = mask%8;
		int i;
		for(i = 0; i < maskOctets; i++) {
			if(!firstIP[i].equals(secondIP[i])) {
				match = false;
			}
		}
		if(maskRemainder != 0 && match) {
			int highestBit = 128;
			int maskDec = 0;
			while(maskRemainder > 0) {
				maskDec += highestBit;
				highestBit /= 2;
				maskRemainder--;
			}
			if((Integer.parseInt(firstIP[i], 10) & maskDec) != (Integer.parseInt(secondIP[i], 10) & maskDec)) {
				match = false;
			}
		}
		return match;
	}

	public List<Entry> getAllEntries() {
		return entries;
	}

	public void add(Entry entry) {
		entries.add(entry);
	}
	
	public int getSize() {
		return entries.size();
	}
	
	public Entry getEntry(int index) {
		return entries.get(0);
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < entries.size(); i++) {
			result.append(entries.get(i) + "\n");
		}
		return result.toString();
	}
	
	/**
	 * Returns the index of the best route from the routing table
	 * @return index  of the best route returns -1 if no route
	 */
	public String findBestRoute(String IP) {
		Collections.sort(entries);
		for(int i = 0; i < entries.size(); i++) {
			checkForMatch(IP, entries.get(i));
		}
		return "";
	}

	public boolean checkForMatch(String iP, Entry entry) {
		boolean match = true;
		int[] result = new int[4];
		String[] destIPArray = iP.split("\\.");
		String[] tableIPArray = entry.getIP().split("\\.");
		int mask = entry.getPrefix();
		int maskOctets = mask/8;
		int maskRemainder = mask%8;
		int i;
		for(i = 0; i < maskOctets; i++) {
			if(!destIPArray[i].equals(tableIPArray[i])) {
				match = false;
			}
		}
		if(maskRemainder != 0 && match) {
			int highestBit = 128;
			int maskDec = 0;
			while(maskRemainder > 0) {
				maskDec += highestBit;
				highestBit /= 2;
				maskRemainder--;
			}
			if((Integer.parseInt(destIPArray[i], 10) & maskDec) != (Integer.parseInt(tableIPArray[i], 10) & maskDec)) {
				match = false;
			}
		}
		return match;
	}
	
//	public String toBinary(String IP) {
//		System.out.println(IP);
//		String[] tempIP = IP.split("\\.");
//		System.out.println(tempIP.length);
//		String binaryRep = "";
//		for(int i = 0; i < tempIP.length; i++) {
//			binaryRep += Integer.par
//		}
//		System.out.println(binaryRep);
//		return binaryRep;
//	}
  


	public void setUpInitialTable(int size) {
		entries.add(new Entry());
		for(int i = 0; i < size - 1; i++) {
			entries.add(randomEntry());
		}
	}
	
	/**
	 * Just takes the incomming entry and updates the table
	 * @param theEntry this is the entry that is returned form routers giving us their info
	 * @return portNum returns the port number of the interface to send packet to
	 */
	public Entry randomEntry() {
		Random rand = new Random();
		String destIP = generateRandomIP();
		String nextRouterIP = generateRandomIP();
		int prefix = rand.nextInt(11) + 16;
		int hops = rand.nextInt(10);
		int portNum = rand.nextInt(10);
		Date time = new Date();
		boolean reachable = true;
		return new Entry(destIP, nextRouterIP, prefix, hops, portNum, time, reachable);
	}
	
	
	/**
	 * Generates a Random IP address
	 */
	public String generateRandomIP() {
		Random rand = new Random();
		String ip = "";
		for(int i = 0; i < 4; i++) {
			ip += "."  + (rand.nextInt(254) + 1);
		}
		return ip.substring(1);
	}
}
 
 
 
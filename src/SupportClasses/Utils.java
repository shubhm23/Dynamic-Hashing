package SupportClasses;

class Utils {
	
	protected static String generateBitString(int n) {
		int num = n;
		StringBuilder bitString = new StringBuilder();
		for(int i=0; i<10; i++) {
			bitString.insert(0,  num%2);
			num = num/2;
		}
		return bitString.toString();
	}
	
	protected static String trimToDepth(String bitString, int depth) {
		if(bitString.length() > depth) {
			return bitString.substring((bitString.length() - depth), bitString.length());
		}else if(bitString.length() == depth) {
			return bitString;
		}
		return null;
	}
	
}

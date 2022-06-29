package SupportClasses;

/**
* The DirectoryRecord represents the HashMap used to map the
* global hash value to it's corresponding Bucket.
*/

public class DirectoryRecord {
	StringBuilder globalHashValue;
	Bucket bucket;
	
	protected DirectoryRecord(boolean bit, Bucket bucket) {
		this.globalHashValue = new StringBuilder();
		if(bit) {
			this.globalHashValue.insert(0, 1);
		}else {
			this.globalHashValue.insert(0, 0);
		}
		this.bucket = bucket;
	}
	
	protected DirectoryRecord(String bitString, Bucket bucket) {
		this.globalHashValue = new StringBuilder(bitString);
		this.bucket = bucket;
	}
	
	protected DirectoryRecord(String bitString, boolean bit, Bucket bucket) {
		this.globalHashValue = new StringBuilder(bitString);
		if(bit) {
			this.globalHashValue.insert(0, 1);
		}else {
			this.globalHashValue.insert(0, 0);
		}
		this.bucket = bucket;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		int len = 10 - this.globalHashValue.length();
		s.append("| ");
		for(int i=0; i<len; i++) {
			s.append(" ");
		}
		s.append(this.globalHashValue.toString());
		s.append(" | ");
		s.append(this.bucket.toString());
		return s.toString();
	}
	
	public boolean equals(Object O){
		DirectoryRecord dR = (DirectoryRecord)O;
		return this.globalHashValue.toString().equals(dR.getGlobalHashValue());
	}
	
	protected String getGlobalHashValue(){
		return this.globalHashValue.toString();
	}
	
	protected Bucket getBucket(){
		return this.bucket;
	}
	
}

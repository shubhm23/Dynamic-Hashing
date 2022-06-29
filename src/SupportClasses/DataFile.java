package SupportClasses;
import java.util.*;

/**
* This class represents a file in the OS which is divided into many blocks. 
* Here we have chosen each block to be equal to a bucket.
*/

public class DataFile {
	int blockingFactor;
	ArrayList<Bucket> dataFileBuckets;
	
	protected DataFile(int blockingFactor, int localDepth) {
		int count = localDepth - 1;
		this.blockingFactor = blockingFactor;
		this.dataFileBuckets = new ArrayList<Bucket>();
		Bucket b0 = new Bucket(1, blockingFactor, false);
		Bucket b1 = new Bucket(1, blockingFactor, true);
		this.dataFileBuckets.add(b0);
		this.dataFileBuckets.add(b1);
		for(int i=0; i<count; i++){
			int len = this.dataFileBuckets.size();
			for(int j=0; j<len;j++){
				Bucket b = this.dataFileBuckets.get(j).splitBucket();
				this.dataFileBuckets.add(b);
			}
		}
		this.sortBuckets(this.dataFileBuckets);
	}
	
	protected void addBucket(Bucket newBucket){
		this.dataFileBuckets.add(newBucket);
		this.sortBuckets(this.dataFileBuckets);
	}
	
	protected void mergeBuckets(Bucket emptyBucket, Bucket mergeToBucket){
		mergeToBucket.merge();
		this.dataFileBuckets.remove(emptyBucket);
		this.sortBuckets(this.dataFileBuckets);
	}
	
	protected Bucket getBucket(String bitString){
		for(Bucket b : this.dataFileBuckets){
			String trimmedVal = Utils.trimToDepth(bitString, b.getLocalDepth());
			if(trimmedVal.equals(b.getLocalHashValue())){
				return b;
			}
		}
		return null;
	}
	
	protected Bucket getSisterBucket(Bucket bucket){
		String bucketName = bucket.getLocalHashValue();
		char bit = bucketName.charAt(0);
		StringBuilder s = new StringBuilder(bucketName.substring(1));
		if(bit == '0'){
			s.insert(0, 1);
		} else if(bit == '1'){
			s.insert(0, 0);
		} else {
			return null;
		}
		for(Bucket b : this.dataFileBuckets){
			if(b.getLocalHashValue().equals(s.toString())){
				return b;
			}
		}
		return null;
	}
	
	protected int getMaxLocalDepth(){
		int localDepth = this.dataFileBuckets.get(0).getLocalDepth();
		for(int i=1; i<this.dataFileBuckets.size(); i++){
			if(localDepth < this.dataFileBuckets.get(i).getLocalDepth()){
				localDepth = this.dataFileBuckets.get(i).getLocalDepth();
			}
		}
		return localDepth;
	}
	
	protected static void sortBuckets(ArrayList<Bucket> buckets){
		Collections.sort(buckets, new SortBitString());
	}
	
	protected int numberOfBuckets(){
		return this.dataFileBuckets.size();
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder();
		for(Bucket b : this.dataFileBuckets){
			s.append("|  ");
			int len = 11 - b.bucketName.length();
			for(int i=0; i<len; i++){
				s.append(" ");
			}
			s.append(b.bucketName + "  | ");
			int[] keys = b.getKeys();
			for(int i=0; i<b.getBlockingFactor(); i++){
				s.append(" " + keys[i] + " ");
			}
			s.append(" |\n");
		}
		return s.toString();
	}
}

class SortBitString implements Comparator<Bucket>{
	public int compare(Bucket a, Bucket b){
		return a.getLocalHashValue().compareTo(b.getLocalHashValue());
	}
}

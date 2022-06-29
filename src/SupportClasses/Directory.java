package SupportClasses;
import java.util.*;

/**
* This class represents the Directory Structure of the Extendible Hashing Scheme.
* This class has a list of records that maps the Global Hash Value to the Bucket.
* All the buckets of the Scheme are a part of the Data File.
*/

public class Directory {
	private int globalDepth;
	private int modGrouper;
	private DataFile dataFile;
	private ArrayList<DirectoryRecord> fileDirectory;
	private String currentBucket;
	private String hashvalue;
	
	public Directory(int globalDepth, int localDepth, int blockingFactor, int modGrouper) {
		this.globalDepth = globalDepth;
		this.modGrouper = modGrouper;
		this.dataFile = new DataFile(blockingFactor,  localDepth);
		this.fileDirectory = new ArrayList<DirectoryRecord>();
		this.generateFileDirectory();
		System.out.println(Math.pow(2, globalDepth) + " " + this.dataFile.numberOfBuckets());
	}
	
	public void insertKey(int key) {
		if(!this.searchKey(key)){
			int h = key % modGrouper;
			DirectoryRecord r = this.getDirectoryRecord(h);
			Bucket b = r.getBucket();
			this.hashvalue = Utils.trimToDepth(Utils.generateBitString(h), this.globalDepth);
			this.currentBucket = b.toString();
			if(b.canInsertKey()){
				b.insertKey(key);
			} else {
				if(b.getLocalDepth() >= globalDepth){
					++this.globalDepth;
					this.generateFileDirectory();
				}
				Bucket newBucket = b.splitBucket(modGrouper);
				if(b.getLocalHashValue().equals(Utils.trimToDepth(Utils.generateBitString(h), b.getLocalDepth()))){
					b.insertKey(key);
				} else {
					newBucket.insertKey(key);
				}
				this.dataFile.addBucket(newBucket);
				this.generateFileDirectory();
			}
		} else {
			System.out.println("Key is already present!!");
		}
	}
	
	public boolean searchKey(int key){
		int h = key % modGrouper;
		DirectoryRecord r = this.getDirectoryRecord(h);
		Bucket b = r.getBucket();
		this.hashvalue = Utils.trimToDepth(Utils.generateBitString(h), this.globalDepth);
		this.currentBucket = b.toString();
		if(b.isKeyPresent(key)){
			return true;
		}
		return false;
	}
	
	public void deleteKey(int key){
		int h = key % modGrouper;
		DirectoryRecord r = this.getDirectoryRecord(h);
		Bucket b = r.getBucket();
		this.hashvalue = Utils.trimToDepth(Utils.generateBitString(h), this.globalDepth);
		this.currentBucket = b.toString();
		if(b.isKeyPresent(key)){
			b.deleteKey(key);
			if(b.isEmpty()){
				Bucket sisterBucket = this.dataFile.getSisterBucket(b);
				if((sisterBucket != null) && (b.canMergeWith(sisterBucket))){
					this.dataFile.mergeBuckets(b, sisterBucket);
					this.generateFileDirectory();
				}
			}
		}
	}
	
	private DirectoryRecord getDirectoryRecord(int key){
		String bitString = Utils.trimToDepth(Utils.generateBitString(key), this.globalDepth);
		for(DirectoryRecord r : this.fileDirectory){
			if(bitString.equals(r.getGlobalHashValue())){
				return r;
			}
		}
		return null;
	}
	
	private void generateFileDirectory(){
		this.fileDirectory.clear();
		for(int i=0; i<Math.pow(2, this.globalDepth); i++){
			String bitString = Utils.generateBitString(i);
			this.fileDirectory.add(new DirectoryRecord(Utils.trimToDepth(bitString, globalDepth), dataFile.getBucket(bitString)));
		}
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder("-------------------------------------------\n");
		for(DirectoryRecord r : this.fileDirectory){
			s.append(r.toString() + "  \n");
		}
		s.append("-------------------------------------------");
		return s.toString();
	}
	
	public String guiOutput(){
		StringBuilder s = new StringBuilder("-------------------------------------------<br/>");
		for(DirectoryRecord r : this.fileDirectory){
			s.append(r.toString() + "  <br />");
		}
		s.append("-------------------------------------------");
		return "<html>" + s.toString() + "<html/>";
	}
	
	public String getCurrentBucket(){
		return this.currentBucket;
	}

	public String getHashValue(){
		return this.hashvalue;
	}
	
}

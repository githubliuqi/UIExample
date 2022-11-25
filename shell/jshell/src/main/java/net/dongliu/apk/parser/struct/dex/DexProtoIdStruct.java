package net.dongliu.apk.parser.struct.dex;

public class DexProtoIdStruct {
	
	public long shortyIdx;   // u4 /* index into stringIds for shorty descriptor */
	public long returnTypeIdx;// u4 /* index into typeIds list for return type */
	public long parametersOff; // u4 /* file offset to type_list for parameter types */
	
}

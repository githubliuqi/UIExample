package net.dongliu.apk.parser.struct.dex;

import java.util.Arrays;

public class DexProto {

	public String shortyInfo ;// shorty descriptor
	public String returnType ;// return type
	public String[] paramTypes;// param type
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		//return super.toString();
		StringBuilder sb = new StringBuilder();
		sb.append("\n[\n");
		sb.append(" shortyInfo:"+shortyInfo);
		sb.append(" returnType:"+returnType);
		if (null != paramTypes)
		{
			sb.append(Arrays.toString(paramTypes));
		}
		sb.append("\n]\n");
		return sb.toString();
	}
}

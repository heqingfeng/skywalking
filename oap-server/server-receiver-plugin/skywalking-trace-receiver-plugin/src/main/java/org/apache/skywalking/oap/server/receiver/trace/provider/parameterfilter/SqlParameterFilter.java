package org.apache.skywalking.oap.server.receiver.trace.provider.parameterfilter;


import java.util.List;
import org.apache.skywalking.apm.network.common.KeyStringValuePair;
import org.apache.skywalking.apm.network.language.agent.v2.SegmentObject;
import org.apache.skywalking.apm.network.language.agent.v2.SpanObjectV2;

public class SqlParameterFilter {

	private static final SqlParameterFilter sqlParameterFilter = new SqlParameterFilter();
	
	private static final String REPLACE_STR = " ?";
	
	private static final String FIND_TEXT = "=";
	
	private static final String START_FIND_TEXT = "where";
	
	private static final String SPANLAYER_NAME = "Database";
	
	private static final String BD_STATEMENT = "db.statement";
	
	private SqlParameterFilter() {}
	
	public static SqlParameterFilter getInstance() {
		return sqlParameterFilter;
	}
	
	public  SegmentObject parameterFilter(SegmentObject segmentObject) {
		
		List<SpanObjectV2> spanObjectV2List = segmentObject.getSpansList();
		int spanIndex = 0;
		for(SpanObjectV2 spanObjectV2 : spanObjectV2List) {
			String spanLayer = spanObjectV2.getSpanLayer().name();
			if(spanLayer!=null && SPANLAYER_NAME.equals(spanLayer)) {
				List<KeyStringValuePair> keyStringValuePairList = spanObjectV2.getTagsList();				
				for(int tagIndex =keyStringValuePairList.size()-1;tagIndex>=0;tagIndex--) {			
				  KeyStringValuePair keyStringValuePair = keyStringValuePairList.get(tagIndex);
				  String dbStatement = keyStringValuePair.getKey();
				  if(dbStatement!=null && BD_STATEMENT.equals(dbStatement)) {
					String sql = keyStringValuePair.getValue();
					sql = format(sql);						
					keyStringValuePair = keyStringValuePair.toBuilder().setValue(sql).buildPartial();			
					spanObjectV2 = spanObjectV2.toBuilder().setTags(tagIndex, keyStringValuePair).buildPartial();
					segmentObject = segmentObject.toBuilder().setSpans(spanIndex, spanObjectV2).buildPartial();
					break;
				  }				  
				}
				
			}
			spanIndex++;
		}
		
		return segmentObject;
		
	}
	/**
	 * 对sql进行？占位符替换
	 * @param srcText
	 * @param findText
	 * @return
	 */
	public  String format(String srcText) {     
	    StringBuffer sb = new StringBuffer(srcText);
	    int index = srcText.toLowerCase().indexOf(START_FIND_TEXT)+1; 
	    if(index<=0) {
	    	return srcText;
	    }
	    while ((index = sb.indexOf(FIND_TEXT, index)) != -1) {
	        index++;
	        boolean isSpace = false;
	        if(sb.charAt(index)== ' ') {//判断等号后面是否有空格
	        	isSpace =true;
	        }
	        for(int i = index; i<sb.length();i++) {//确定=号后面参数截止位置，并且进行替换	        	
	        	if(sb.charAt(i)== ' ' || i==sb.length()-1) {
	        		if(!isSpace) {
	        			if(!REPLACE_STR.trim().equals(sb.substring(index, i).trim())) {
	        				if(i==sb.length()-1) {//处理最后一位是明文参数值的情况
	        					sb.replace(index, i+1, REPLACE_STR);
	        				}else {
	        					sb.replace(index, i, REPLACE_STR);
	        				}	        				
	        			}
	        			break;
	        		}
	        	}else if(isSpace){
	        		isSpace = false;	        		
	        	}
	        }
	       
	    }
	   return sb.toString();
	}
	
}

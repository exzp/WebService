package com.github.webservice.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.ContentListFacade;
import org.dom4j.tree.DefaultAttribute;

public class ClientDemo {

	public static void main(String[] args) {
		MakeCallOnclick("7001", "018681471812", "122");
	}

	public static void MakeCallOnclick(String strExten, String str_tel_num, String strActionID) {
		try {
			String endpoint = "http://120.25.176.5/uncall_api/";
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(new QName("urn:Uncall", "OnClickCall")); // WSDL里面描述的接口名称如点击拨号
			call.addParameter("strExten", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// 接口的参数
			call.addParameter("strToTel", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// 接口的参数
			call.addParameter("strActionID", org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);// 接口的参数
			call.setReturnType(XMLType.XSD_STRING);
			call.setUseSOAPAction(true);
			String result = (String) call.invoke(new Object[] { strExten, str_tel_num, strActionID });
			System.out.println("response:" + result);
			
			Document doc = DocumentHelper.parseText(result);
			Map map = dom2Map(doc);
			System.out.println(map.toString());
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
	 public static Map<String, Object> dom2Map(Document doc) { 
	        Map<String, Object> map = new HashMap<String, Object>(); 
	        // 判断需要解析的文档是否为空 
	        if (doc == null) { 
	            return map; 
	        } 
	        // 获取根节点 
	        Element root = doc.getRootElement(); 
	 
	        // 获取根节点下的子节点迭代器 
	        Iterator iterator = root.elementIterator(); 
	 
	        // 循环子节点，开始向map中存值 
	        while (iterator.hasNext()) { 
	            Element e = (Element) iterator.next(); 
	            List list = e.elements(); 
	 
	            // 判断当前节点是否有子节点 
	            // 如果存在子节点调用element2Map(Element e)方法，不存在子节点直接存进map中 
	            if (list.size() > 0) { 
	                map.put(e.getName(), element2Map(e)); 
	            } else { 
	                saveAttribute2Map(map, e); 
	                map.put(e.getName(), e.getText()); 
	            } 
	        } 
	        return map; 
	    } 
	 
	 private static Map<String, Object> element2Map(Element e) { 
	        Map<String, Object> map = new HashMap<String, Object>(); 
	        List<?> list = e.elements(); 
	        saveAttribute2Map(map, e); 
	        if (list.size() > 0) { 
	            for (int i = 0, j = list.size(); i < j; i++) { 
	                Element iter = (Element) list.get(i); 
	                List<Object> mapList = new ArrayList<Object>(); 
	                // 存在子节点 
	                if (iter.elements().size() > 0) { 
	                    Map<?, ?> m = element2Map(iter); 
	                    if (map.get(iter.getName()) != null) { 
	                        Object obj = map.get(iter.getName()); 
	                        if (!obj.getClass().getName() 
	                                .equals("java.util.ArrayList")) { 
	                            mapList = new ArrayList<Object>(); 
	                            mapList.add(obj); 
	                            mapList.add(m); 
	                        } 
	                        if (obj.getClass().getName() 
	                                .equals("java.util.ArrayList")) { 
	                            mapList = (List) obj; 
	                            mapList.add(m); 
	                        } 
	                        map.put(iter.getName(), mapList); 
	                    } else 
	                        map.put(iter.getName(), m); 
	                } else { 
	                    if (map.get(iter.getName()) != null) { 
	                        Object obj = map.get(iter.getName()); 
	                        if (!obj.getClass().getName() 
	                                .equals("java.util.ArrayList")) { 
	                            mapList = new ArrayList(); 
	                            mapList.add(obj); 
	                            mapList.add(iter.getText()); 
	                        } 
	                        if (obj.getClass().getName() 
	                                .equals("java.util.ArrayList")) { 
	                            mapList = (List) obj; 
	                            mapList.add(iter.getText()); 
	                        } 
	                        map.put(iter.getName(), mapList); 
	                    } else 
	                        map.put(iter.getName(), iter.getText()); 
	                } 
	            } 
	        } else { 
	            saveAttribute2Map(map, e); 
	            map.put(e.getName(), e.getText()); 
	        } 
	 
	        return map; 
	    } 
	 
	    private static void saveAttribute2Map(Map<String, Object> map, Element e) { 
	        ContentListFacade attributes = (ContentListFacade) e.attributes(); 
	        if (attributes.size() > 0) { 
	            HashMap<String, String> attrMap = new HashMap<String, String>(); 
	            map.put("attribute", attrMap); 
	            DefaultAttribute attrTmp = null; 
	            for (int i = 0, j = attributes.size(); i < j; i++) { 
	                attrTmp = (DefaultAttribute) attributes.get(i); 
	                attrMap.put(attrTmp.getName(), attrTmp.getValue()); 
	            } 
	        } 
	    } 
	 

}

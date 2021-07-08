package com.yzj.ebs.util;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
/**
 * 
 *创建于:2012-9-25<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 用来转化参数表中的键值。
 * @author pengwenxuan
 * @version 1.0.0
 */
public class EbillUtil
{
  @SuppressWarnings({ "rawtypes", "unchecked" })
public static TreeMap<Integer, String> getSymmetryMap_SI2IS(TreeMap<String, Integer> srcMap)
  {
    TreeMap tarMap = new TreeMap();
    if (srcMap != null) {
      Set setOfKey = srcMap.keySet();
      Iterator iterator = setOfKey.iterator();
      while (iterator.hasNext()) {
        Integer value = (Integer)srcMap.get(iterator.next());
        tarMap.put(value, String.valueOf(iterator));
      }
    }
    return tarMap;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
public static TreeMap<String, String> getSymmetryMap_SS2SS(TreeMap<String, String> srcMap)
  {
    TreeMap tarMap = new TreeMap();
    if (srcMap != null) {
      Set setOfKey = srcMap.keySet();
      Iterator iterator = setOfKey.iterator();
      while (iterator.hasNext()) {
        String value = (String)srcMap.get(iterator.next());
        tarMap.put(value, String.valueOf(iterator));
      }
    }
    return tarMap;
  }
}
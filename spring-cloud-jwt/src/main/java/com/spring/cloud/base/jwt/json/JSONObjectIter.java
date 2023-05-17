package com.spring.cloud.base.jwt.json;

import com.spring.cloud.base.jwt.config.JSONObject;

import java.util.Iterator;

/**
 * @Author: ls
 * @Description: 此类用于在JSONAray中便于遍历JSONObject而封装的Iterable可以借助foreach语法遍历
 * @Date: 2023/4/25 11:29
 */
public class JSONObjectIter implements Iterable<JSONObject> {

	Iterator<Object> iterator;
	
	public JSONObjectIter(Iterator<Object> iterator) {
		this.iterator = iterator;
	}

	@Override
	public Iterator<JSONObject> iterator() {
		return new Iterator<JSONObject>() {
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}
			@Override
			public JSONObject next() {
				return (JSONObject) iterator.next();
			}
			@Override
			public void remove() {
				iterator.remove();
			}
		};
	}

}

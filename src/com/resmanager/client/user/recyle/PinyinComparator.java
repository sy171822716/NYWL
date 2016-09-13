package com.resmanager.client.user.recyle;

import java.util.Comparator;

import com.resmanager.client.model.CustomerModel;

/**
 * 
 * @author xiaanming
 * 
 */
public class PinyinComparator implements Comparator<CustomerModel> {

	public int compare(CustomerModel o1, CustomerModel o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}

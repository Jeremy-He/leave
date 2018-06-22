package com.jeremy.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		List<String> gradeList = new ArrayList<String>();
		
		String[] grades = new String[]{"1", "2", "3", "4"};
		Collections.addAll(gradeList, grades);
		int indexOf = gradeList.indexOf("2");
		if(gradeList.indexOf("2") != -1){
			String string = gradeList.get(indexOf-1);
			System.out.println(string);
		}
	}
}

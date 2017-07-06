package com.syz.zookeeper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
		Date date = dateFormat.parse("2017-7-02 17:14:00");
		System.out.println(date.getTime());
		date = dateFormat.parse("2017-6-22 17:14:05");
		System.out.println(date.getTime());
	}
}

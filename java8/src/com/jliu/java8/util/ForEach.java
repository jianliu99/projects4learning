package com.jliu.java8.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForEach {
	  public static void main(String[] args) {
		  List<String> items = new ArrayList<>();
		  items.add("A");
		  items.add("B");
		  items.add("C");
		  items.add("D");
		  items.add("E");
		  
		  /*
		  for (Map.Entry<String, Integer> entry : items.entrySet()) {
				System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
			}
			*/
		  for(String item : items){
				System.out.println(item);
			}
		  //lambda
		  //Output : A,B,C,D,E
		  items.forEach(item->System.out.println(item));

		  //Output : C
		  items.forEach(item->{
		  	if("C".equals(item)){
		  		System.out.println(item);
		  	}
		  });

		  //method reference
		  //Output : A,B,C,D,E
		  items.forEach(System.out::println);

		  //Stream and filter
		  //Output : B
		  items.stream()
		  	.filter(s->s.contains("B"))
		  	.forEach(System.out::println);
		  
		  
		  Map<String, Integer> Mitems = new HashMap<>();
		  Mitems.put("A", 10);
		  Mitems.put("B", 20);
		  Mitems.put("C", 30);
		  Mitems.put("D", 40);
		  Mitems.put("E", 50);
		  Mitems.put("F", 60);

		  Mitems.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));

		  Mitems.forEach((k,v)->{
		  	System.out.println("Item : " + k + " Count : " + v);
		  	if("E".equals(k)){
		  		System.out.println("Hello E");
		  	}
		  });
		  
		  Mitems.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));

		  Mitems.forEach((k,v)->{
		  	System.out.println("Item : " + k + " Count : " + v);
		  	if("E".equals(k)){
		  		System.out.println("Hello E");
		  	}
		  });
	  }
}

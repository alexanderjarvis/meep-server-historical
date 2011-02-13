package controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import play.mvc.Controller;

public class LongPoll extends Controller {
	
	static boolean locupdate = true;
	
	public static void index() throws InterruptedException {
		if (locupdate) {
			locupdate = false;
			String hello = "hello\n";
			response.setContentTypeIfNotSet("text/html");
			
			
			try {
				response.out.write(hello.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			locupdate = true;
			Thread.sleep(1000);
		}
		
	}
}

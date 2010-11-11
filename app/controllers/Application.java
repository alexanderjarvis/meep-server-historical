package controllers;

import controllers.oauth2.AccessTokenFilter;

public class Application extends AccessTokenFilter {

    public static void index() {
        render();
    }

}
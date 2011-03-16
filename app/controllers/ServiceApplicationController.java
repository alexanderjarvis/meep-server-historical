package controllers;

import play.mvc.Before;
import play.mvc.With;
import controllers.oauth2.AccessTokenFilter;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@With({JSONRequestTypeFilter.class, NoCookieFilter.class, LoggingFilter.class, SSLCheckFilter.class})
public class ServiceApplicationController extends AccessTokenFilter {

}

package controllers;

import play.mvc.Before;
import play.mvc.With;
import controllers.filters.JSONRequestTypeFilter;
import controllers.filters.LoggingFilter;
import controllers.filters.NoCookieFilter;
import controllers.filters.SSLCheckFilter;
import controllers.oauth2.AccessTokenFilter;

/**
 * The base controller class which defines the filters and behaviour required to
 * provide a service controller.
 * 
 * Declaring the behaviour in this base class reduces duplication and centralises
 * configuration for common filters/behaviours.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@With({JSONRequestTypeFilter.class, NoCookieFilter.class, LoggingFilter.class, SSLCheckFilter.class})
public class ServiceApplicationController extends AccessTokenFilter {

}

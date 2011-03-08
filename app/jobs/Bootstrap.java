package jobs;

import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@OnApplicationStart
public class Bootstrap extends Job {
    public void doJob() {
    	//TODO: create bootstrap data
        //Fixtures.load("initial-data.yml");
    }
}

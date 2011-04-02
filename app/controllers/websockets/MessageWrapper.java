package controllers.websockets;

/**
 * Wraps and unwraps messages to be sent/received by the WebSocket in a common format.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class MessageWrapper {
	
	/**
	 * Adds the custom frame prefix including the length of the message  after the prefix.
	 * 
	 *  e.g. ~m~5~m~hello
	 * 
	 * @param message
	 * @return
	 */
	public static String wrap(String message) {		
		return message.format("~m~%s~m~%s", Integer.toString(message.length()), message);
	}
	
	/**
	 * Removes the custom frame prefix from a message.
	 * 
	 * The format is ~m~[length of message]~m~ where the length of the message is
	 * measured in characters.
	 * 
	 * e.g. To send a message "hello" the following would be sent.
	 * 
	 * 		~m~5~m~hello
	 * 
	 * @param message
	 * @return
	 */
	public static String unwrap(String message) {
		return message.replaceFirst("~m~[0-9]+~m~", "");
	}

}

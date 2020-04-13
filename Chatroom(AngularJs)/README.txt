Steps to set up the environment and run the project:
1. Create an express project.
2. Copy all the files in this folder to the respective folders
3. Switch to the express project folder, run npm install
4. Set up a database, with name "assignment2"
5. Populate the database with users 
6. Use npm start to start the express project and goto localhost:3000 for testing.


Some implementation details:

In index.html:
	1.Login page:
		a.In index.html create a division with id='login_page', set ng-show='notLogin'.
		b. Create two input elements to obtain the user name and password. Use userInput.name and userInput.password to bind the value retrieved from the input boxes.
		c. Create a button element to invoke the handleLoginLogout() function.

	2.content
		a. Create a division with id='content', set ng-hide='notLogin'
		b. Create some key divisions in the content, 'header','nameList','selectfirendicon','ChatDisplay','ContactInfomat' and 'inputbox'

	3.header
		a. Create the icon of the current user.
		b. using ng-click='goToContactInfo()' to display the contact information of the current user when clicking the user's icon.
		c. Create the logout button.
	4.nameList
		a. Use ng-repeat to chreate the friend list
		b. Use ng-click='showUserMessages()'to display the chat messages when clicking the friends' name.
	5.ChatDisplay
		a. Use ng-repeat to display the messages between the current user and the friend the user has selected.
		b. Define handler for ng-dbclick as deleteMessage() to delete the current user's messages.
		c. Show the messages date and time as the handout requires.
	6. Inputbox
		a.Create a input box for sending messages to friends.
	7.ContactInfomat
		a. Create an img element to show the current user's icon.
		b. Create three input boxes to show the user's information.
		c. Create a button to submit the updated information.

In myscripts.js:

1. Page load. Define the pageLoad function and send an AJAX HTTP POST request for
http://localhost:3000/load; if receiving the empty string, show the login_page in the browser. Else, store the return data from the server and set the interval function (we will talk about it in detail later) to periodically get the new messages and new message numbers from the friends.

2. Log in. When the “Sign in” button is clicked, check if both the username and password input text boxes are non-empty: if so, send an AJAX HTTP POST request for http://localhost:3000/login, carrying the input username and password in the request body; otherwise, show an alert popup box stating “You must enter username and password”. If a “Login failure” response is received, display the login failure in the login page; otherwise, display the page initial page display the friend namelist and current user icon. Store all the response data sent from the server.

3. Log out. When the “log out” button is clicked, send an AJAX HTTP GET request for
http://localhost:3000/logout. Display the login_page division and disable the interval function enabled before.

4. Display user info. Define the goToContactInfo function. When the area containing the logged-in user’s icon and name is clicked, the function is invoked, and it sends an AJAX HTTP GET request for http://localhost:3000/getuserinfo. When the JSON string containing mobileNumber, homeNumber and address of the current user is received, the information is stored in the $scope.currentUser variable.

5. Update user info. Define saveContactInfo function. When the “Save” button on the page view is clicked, the function is invoked which sends an AJAX HTTP PUT request for http://localhost:3000/saveuserinfo, containing key-value pairs of information in the three input textboxes. If an error message is received from the server, display it in an alert box.

6. Load a conversation. Define the showMessages function. When a friend’s name in the left division is clicked, highlight the friend’s name, and remove the number and “()” following the name if there are. The function sends an AJAX HTTP GET request for http://localhost:3000/getconversation/:friendid. it displays the received friend icon, status and messages in the conversation, and the message input textbox. For displaying messages in the conversation, it shows the newest messages upon loading the conversation page view, while older messages become visible when scrolling up. The function stores _id of each message for future usage.

7. Post a new message. Define Postmessage function. When the user has typed a non-empty message in the message input textbox and pressed “Enter”, the function is invoked and it sends an AJAX HTTP POST request for http://localhost:3000/postmessage/:friendid, containing key-value pairs of the message content, date and time in request body. If a message _id is received from the server, it sets the placeholder of the inputbox to be  “Type a message here”, displays the just posted message in the conversation above, and stores _id of the message for future usage.

8. Delete a message. Define deleteMessage function. When a message sent by the current user on the conversation page has been double clicked, the function is invoked, and it shows a popup box asking the user to confirm deletion of the message. When “OK” is clicked, it sends an AJAX HTTP DELETE request for http://localhost:3000/deletemessage/:msgid. If an empty string is received from the server, it deletes the message from the conversation.

9. Periodical updates of conversations. 
(1) Define updateunreadmsg function. For the friend the user is chatting with, the function sends an AJAX HTTP GET request for http://localhost:3000/getnewmessages/:friendid. It adds the received new messages at the end of the conversation, as well as updates display of the friend’s status. Then it stores _id of each new message for future usage. In addition, it compares received _id values of all messages with those retained on the client side, to find out if any message(s) has/have been deleted by the friend; if so, it deletes it/them from the conversation display.

(2) Define updateunreadnum function. For each friend the user is not chatting with, the function sends an AJAX HTTP GET request for http://localhost:3000/getnewmsgnum/:friendid. It updates the number in “( )” following the friend’s name in the left division according to the number received in the response message.




In chats.js:

1. HTTP GET requests for http://localhost:3000/load. The middleware checks if the “userId” session variable has been set for the client. If not, send an empty string back to the client. Otherwise, retrieve name and icon of the current user, name and _id of friends of the current user, and the number of messages that the user has not loaded in the conversation with each friend from the database; send all retrieved information as a JSON string to the client if database operations are successful, and the error message if failure. The string sent to client follows the format:
{'user_name': ,'friend_list': ,'icon': ,'unreadmsgnum_list': }

2. HTTP POST requests for http://localhost:3000/login. The middleware parses the body of the HTTP POST request and extracts the username and password carried in request body. Then it checks whether the username and password match any record in the userList collection in the database. If no, it sends “Login failure” in the body of the response message. If yes, it creates a session variable “userId” and store this user’s _id in the variable, and updates “status” of the user in userList collection to “Online”. Then it sends name and icon of the current user, name and _id of friends of the current user and the number of messages that the user has not loaded in the conversation with each friend as a JSON string in the body of the response message if database operations are successful and the error message if failure.The string sent to client follows the format like this:
{'friend_list':,'name':,'icon':,'unreadmsgnum_list':}

3. HTTP GET requests for http://localhost:3000/logout. The middleware unsets the “userId” session variable, update status of the user in userList collection to “Offline”, and sends an empty string back to the user.

4. HTTP GET requests for http://localhost:3000/getuserinfo.  The middleware retrieves mobileNumber, homeNumber and address of the user from the userList collection based on the value of “userId” session variable. Then it sends retrieved information as a JSON string in the body of the response message if database operations are successful, and the error message if failure. The string sent to client follows the format like this:
{'mobileNumber':,'homeNumber':,'address':}

5. HTTP PUT requests for http://localhost:3000/saveuserinfo. The middleware updates the mobileNumber, homeNumber and address of the user in the userList collection based on the value of “userId” session variable and the data contained in the body of the request message. Then it returns an empty string to the client if success and the error message if failure.

6. HTTP GET requests for http://localhost:3000/getconversation/:friendid. The middleware retrieves icon and status of the friend from the userList collection based on the friendid in the URL. Then it retrieves all messages sent between the friend and the current user from the messageList collection, based on friendid in the URL and value of the “userId” session variable. it updates lastMsgId of the friend in the current user’s record in the userList collection according to _id of the latest message from the friend in the messageList collection and sends all retrieved information as a JSON string in the body of the response message if database operations are successful, and the error message if failure. The string sent to client follows the format like this: 
{'message_list':,'icon':,'status':}


7. HTTP POST requests for http://localhost:3000/postmessage/:friendid. The middleware parses the body of the HTTP POST request and extracts the message, date and time carried in the request body. Then it saves the message into the messageList collection and returns _id of the message record to the client if success and the error message if failure.



8. HTTP DELETE requests for http://localhost:3000/deletemessage/:msgid. The middleware deletes the message record from the messageList collection, corresponding to the msgid value in the URL. Then in the current user’s friends’ records in the userList collection, it updates LastMsgId from the current user, if the message deleted by the current user is the last message that the respective friend has loaded. Return an empty string to the client if success and the error message if failure.


9. HTTP GET requests for http://localhost:3000/getnewmessages/:friendid. The middleware retrieves new messages sent from the friend to the current user in the messageList collection, which have not been retrieved by the user before, based on friendid in the URL, value of the “userId” session variable, and lastMsgId of the friend in the user’s record in userList collection. Then it updates lastMsgId of the friend in the user’s record in userList collection accordingly. In addition, it retrieves the friend’s status from the userList collection, and _id of all the existing messages between the friend and the current user from the messageList collection. It sends all retrieved information as a JSON string in the body of the response message if database operations are successful, and the error message if failure. The string sent to client follows the format like this: 
{'message_list':,'status':,'existing_message_id_list':}


10. HTTP GET requests for http://localhost:3000/getnewmsgnum/:friendid. The middleware finds out the number of new messages sent by the friend to the current user in the messageList collection, which have not been retrieved by the user before, based on friendid in the URL, value of the “userId” session variable and lastMsgId of the friend in the user’s record in userList collection. Then it sends the number in the body of the response message if database operations are successful, and the error message if failure.



var chatterbox_app = angular.module('chatterbox', []);

chatterbox_app.directive('ngEnter', function () {
  return function (scope, element, attrs) {
    element.bind('keydown keypress', function (event) {
      if(event.which === 13) {
        scope.$apply(function (){
            scope.$eval(attrs.ngEnter);
          });
        event.preventDefault();
      }
    });
  };
});

chatterbox_app.directive('repeatFinish',function(){
  return function(scope, element, attrs) {
    if(scope.$last){
          scope.tobottom();
      }
  };
});

chatterbox_app.controller('ChatterBoxController', function($scope, $http, $interval){

  // loginErrorMsg is used to display both the header and the login error message
  $scope.showLoginErrorMsg = false;

  // a boolean flag indicating whether the user has logged in
  $scope.notLogin = true;

  // the string displayed in the button that is used to login/logout
  $scope.logButtonString = "log in";

  // this variable stores the name and contact info of the logged-in user
  $scope.currentUser = {'name':'', 'mobileNumber':'', 'homeNumber':'', 'address':''};

  // this variable stores the name, id and status of the user whose name is clicked in the left division
  $scope.selectedUser = {'name':'', '_id':'','status':''};

  // this array variable stores the friends of the logged-in user
  // each element in this array contains name and id of a friend 
  $scope.friends = [];

  $scope.CurrentuserIcon = '';

  $scope.SelecteduserIcon = '';

  // this boolean flag controls whether the webpage displays contact info
  $scope.showContactInfo = false;
  
  $scope.isOtherUser = false;

  // This variable stores all the messages of a user
  $scope.userMessages = [];

  var newnum_interval=undefined;
  var newmsg_interval=undefined;

  //this function is called when the web page is loaded
  $scope.pageLoad = function(){
    $http.get("/load").then(function(response){
      if(response.data === ""){
        // if the response data is empty, then
        // the user has not logged in. Restore all the
        // variables to their default value.
		$scope.showLoginErrorMsg = false;
        $scope.notLogin = true;
        $scope.logButtonString = "log in";
        $scope.currentUser = {'name':'','mobileNumber':'','homeNumber':'','address':''};
        $scope.selectedUser = {'name':'', '_id':'','status':''};
        $scope.friends = [];
        $scope.showContactInfo = false;
        $scope.userMessages = [];
        $scope.isOtherUser = false;
        $scope.CurrentuserIcon = '';
        $scope.SelecteduserIcon = '';
      }
      else{
  		  $scope.notLogin = false;

          // the string content in the login/logout button should be "log out"
	      $scope.logButtonString = "log out";
          $scope.userMessages = [];
          $scope.friends=response.data.friend_list;
	      $scope.currentUser.name = response.data.user_name;
          $scope.CurrentuserIcon = response.data.icon;
		  
          for(var index=0;index<response.data.unreadmsgnum_list;index++){
            $scope.friends[index].unreadnum=response.data.unreadmsgnum_list[index];
            if($scope.friends[index].unreadnum!=0){
              $scope.friends[index].unreadtext=" ("+$scope.friends[index].unreadnum+")";
            }            
          }
		  
          if (angular.isDefined(newnum_interval)==false){
            newnum_interval=$interval(updateunreadnum,1000);
          }
		  
          if (angular.isDefined(newmsg_interval)==false){
            newmsg_interval=$interval(updateunreadmsg,1000);
          }      
      }
    }, function(response){
      alert("Error getting initial page.");
    });
  };

  function updateunreadmsg(){
    
    $scope.friends.forEach(function(selected_friend){
      if(selected_friend._id==$scope.selectedUser._id){
        
        $http.get("/getnewmessages/"+selected_friend._id).then(function(response){
      
          $scope.selectedUser.status=response.data.status;
      
          for(var i=0;i<$scope.userMessages.length;i++){
            if(response.data.existing_message_id_list.indexOf($scope.userMessages[i]._id)==-1&&$scope.userMessages[i].receiverName==$scope.currentUser.name){
              $scope.userMessages.splice(i, 1);
            }
          }
      
          if (response.data.message_list){
              for(var i =0; i<response.data.message_list.length;i++){
                if($scope.userMessages.length==0){
                  $scope.userMessages.push({'_id':response.data.message_list[i]._id, 'senderId':response.data.message_list[i].senderId, 'senderName':$scope.selectedUser.name,'receiverId':response.data.message_list[i].receiverId,'receiverName':$scope.currentUser.name,'message':response.data.message_list[i].message,'date':response.data.message_list[i].date,'time':response.data.message_list[i].time,'show':true});
                }else{
                  if(response.data.message_list[i].date==$scope.userMessages[$scope.userMessages.length-1].date){
                    $scope.userMessages.push({'_id':response.data.message_list[i]._id, 'senderId':response.data.message_list[i].senderId, 'senderName':$scope.selectedUser.name,'receiverId':response.data.message_list[i].receiverId,'receiverName':$scope.currentUser.name,'message':response.data.message_list[i].message,'date':response.data.message_list[i].date,'time':response.data.message_list[i].time,'show':false});
                  }else{
                    $scope.userMessages.push({'_id':response.data.message_list[i]._id, 'senderId':response.data.message_list[i].senderId, 'senderName':$scope.selectedUser.name,'receiverId':response.data.message_list[i].receiverId,'receiverName':$scope.currentUser.name,'message':response.data.message_list[i].message,'date':response.data.message_list[i].date,'time':response.data.message_list[i].time,'show':true});
                  }
                }
              }
          }
          else{
            alert(response.data);
          }
        }, function(response){
          alert(response.data);
        });          
      

      }


    });
  }

  function updateunreadnum(){
	
    $scope.friends.forEach(function(selected_friend){
      if(selected_friend._id!=$scope.selectedUser._id){
     
        $http.get("/getnewmsgnum/"+selected_friend._id).then(function(response){
          if (response.data){
             
              if(response.data.newmsgnum!=0){
                selected_friend.unreadnum=response.data.newmsgnum;
                selected_friend.unreadtext=" ("+selected_friend.unreadnum+")";
                
              }else{
                selected_friend.unreadnum=response.data.newmsgnum;
                selected_friend.unreadtext=" ";
              }
          }
          else{
            alert(response.data);
          }
        }, function(response){
          alert(response.data);          
        });          


      }
    });
  }

  $scope.handleLoginLogout = function(){

    if($scope.notLogin == true){
 
      if(($scope.userInput.name=="")||($scope.userInput.password=="")){
        // generate alerts in case that the user doesn't fill in name or password
        alert("You must enter name and password");
      }
      else{
        $http.post("/login", {'name':$scope.userInput.name, 'password':$scope.userInput.password}).then(function(response){
          if(response.data === "Login failure"){
            // if the login fails, display the response message
            // underneath the "ChatterBox" header
			$scope.showLoginErrorMsg = true;
          }
		  else{			  
			  if(response.data.friend_list){
                // login succeeded
                // change the notLogin flag to false
	            $scope.notLogin = false;              

                // change the content of the login/logout button to "log out"
	            $scope.logButtonString = "log out";

                // save the login user name and id to currentUser, the name
                // of the login user could be obtained from the userInput model.
	            $scope.currentUser.name = $scope.userInput.name;

                $scope.CurrentuserIcon = response.data.icon;

                // save the friend list of the login user
	            $scope.friends = response.data.friend_list;
                
				for(var index=0;index<response.data.unreadmsgnum_list;index++){
                  $scope.friends[index].unreadnum=response.data.unreadmsgnum_list[index];
                  if($scope.friends[index].unreadnum!=0){
                    $scope.friends[index].unreadtext=" ("+$scope.friends[index].unreadnum+")";
                  }
			    }              

                if (angular.isDefined(newnum_interval)==false){
                  newnum_interval=$interval(updateunreadnum,1000);
                }
				
                if (angular.isDefined(newmsg_interval)==false){
                  newmsg_interval=$interval(updateunreadmsg,1000);
                }
		      }else{
                alert(response.data);
	          }
          }
        }, function(response){
          alert("Error login.");
        });
      }
    }
    else{
      //handle logout 
      $http.get("/logout").then(function(response){
	      if(response.data === ""){
            // logout succeeded, restore all the variables to
            // their default values
            $scope.showLoginErrorMsg = false;
            $scope.notLogin = true;
            $scope.logButtonString = "log in";
            $scope.currentUser = {'name':'','mobileNumber':'','homeNumber':'','address':''};
            $scope.selectedUser = {'name':'', '_id':'','status':''};
            $scope.friends = [];
            $scope.isOtherUser= false;
            $scope.showContactInfo = false;
            $scope.userMessages = [];
            $scope.CurrentuserIcon = '';
            $scope.SelecteduserIcon = '';
		  
            if (angular.isDefined(newnum_interval)){
              $interval.cancel(newnum_interval);
              newnum_interval=undefined;
            }
		  
            if (angular.isDefined(newmsg_interval)){
              $interval.cancel(newmsg_interval);
              newmsg_interval=undefined;
            }
		  
           // set userInput model to empty.
	        $scope.userInput.name = "";
	        $scope.userInput.password = "";
	      }else{
          alert(response.data);
        }
      }, function(response){
        alert("Error logout");
      });
    }
  }


  $scope.showUserMessages = function(user){    
    // the friend of the login user is clicked
    $scope.isOtherUser= true;    

    // we should not display contact, set this flag to false
    $scope.showContactInfo = false;

    // save the clicked user's information to selectedUser
    $scope.selectedUser = user;

    
    var i=$scope.friends.map(function(objectItem){return objectItem._id;}).indexOf(user._id);
    $scope.friends[i].unreadnum=0;
    $scope.friends[i].unreadtext='';

    $http.get("/getconversation/"+user._id).then(function(response){
		
      if (response.data){
  		$scope.userMessages = [];
        $scope.SelecteduserIcon = response.data.icon;
        $scope.selectedUser.status =response.data.status;
	      
		for(var index in response.data.message_list){
          // save the information of each message to the userMessages list
          var message = response.data.message_list[index];
		  
          if(message.senderId==user._id){	
			  		  
            if($scope.userMessages.length==0){
             $scope.userMessages.push({'_id':message._id, 'senderId':message.senderId, 'senderName':user.name,'receiverId':message.receiverId,'receiverName':$scope.currentUser.name,'message':message.message,'date':message.date,'time':message.time,'show':true});
            }else{
              if(message.date==$scope.userMessages[$scope.userMessages.length-1].date){
                $scope.userMessages.push({'_id':message._id, 'senderId':message.senderId, 'senderName':user.name,'receiverId':message.receiverId,'receiverName':$scope.currentUser.name,'message':message.message,'date':message.date,'time':message.time,'show':false});
              }else{
                $scope.userMessages.push({'_id':message._id, 'senderId':message.senderId, 'senderName':user.name,'receiverId':message.receiverId,'receiverName':$scope.currentUser.name,'message':message.message,'date':message.date,'time':message.time,'show':true});
              }
            }            
          }else{
			  
            if($scope.userMessages.length==0){
              $scope.userMessages.push({'_id':message._id, 'senderId':message.senderId, 'senderName':$scope.currentUser.name,'receiverId':message.receiverId,'receiverName':user.name,'message':message.message,'date':message.date,'time':message.time,'show':true});
            }else{
              if(message.date==$scope.userMessages[$scope.userMessages.length-1].date){
                $scope.userMessages.push({'_id':message._id, 'senderId':message.senderId, 'senderName':$scope.currentUser.name,'receiverId':message.receiverId,'receiverName':user.name,'message':message.message,'date':message.date,'time':message.time,'show':false});
              }else{
                $scope.userMessages.push({'_id':message._id, 'senderId':message.senderId, 'senderName':$scope.currentUser.name,'receiverId':message.receiverId,'receiverName':user.name,'message':message.message,'date':message.date,'time':message.time,'show':true});
              }
            }             
          }
        }
	  }
      else{
		  alert(response.data);
	  }
    }, function(response){
      alert("Error getting messages for current user.");
    });
  };


  $scope.tobottom= function(){
        var scroll=document.getElementById("chatDisplay");
        scroll.scrollTop=scroll.scrollHeight;
  }


  $scope.Postmessage = function(){
	  
	document.getElementById("messageinput").placeholder= "Type a message here";
    var message=$scope.MessageInput;
    $scope.MessageInput= "";

    if (message) {
      // post the message to the server
      d = new Date();
      datetext = d.toDateString();
      timetext = d.toTimeString();
      timetext = timetext.split(' ')[0];
    
	  $http.post("/postmessage/"+$scope.selectedUser._id, {'message':message,'date':datetext,'time':timetext}).then(function(response){
  			if (response.data._id){
            	if($scope.userMessages.length==0){
            		$scope.userMessages.push({'_id':response.data._id, 'senderName':$scope.currentUser.name,'receiverId':$scope.selectedUser._id,'receiverName':$scope.selectedUser.name,'message':message,'date':datetext,'time':timetext,'show':true});
            	}else{
              	  if(datetext==$scope.userMessages[$scope.userMessages.length-1].date){
                	  $scope.userMessages.push({'_id':response.data._id,'senderName':$scope.currentUser.name,'receiverId':$scope.selectedUser._id,'receiverName':$scope.selectedUser.name,'message':message,'date':datetext,'time':timetext,'show':false});
              	  }else{
                	$scope.userMessages.push({'_id':response.data._id, 'senderName':$scope.currentUser.name,'receiverId':$scope.selectedUser._id,'receiverName':$scope.selectedUser.name,'message':message,'date':datetext,'time':timetext,'show':true});
              	  }
            	}            
  			}
  			else{
  				alert(response.data);
  			}
	    }, function(response){
	      alert("Error posting message.")
	    });
	   }
  };

  $scope.deleteMessage = function(message){
    var confirmation = confirm('Are you sure you want to delete this message?');
    if(confirmation == true){
      // send the id of the deleted message to the server
      $http.delete('/deletemessage/'+message._id).then(function(response){
	      if (response.data === ""){
            // the deletion on the server side is successful
            // now find out the index of the deleted message within the usermessages list
	        var splice_index = 0;
	        for(var index in $scope.userMessages){
	          if($scope.userMessages[index]._id == message._id){
	            splice_index = index;
	          }
	        }

            // use the built-in splice function of JS array to
            // delete the message at position splice_index.
	        $scope.userMessages.splice(splice_index, 1);
         }
	     else {
         	alert(response.data);
		 }
      }, function(response){
        alert("Error deleting message.");
      });
    }
  };


  $scope.goToContactInfo = function(){

    $scope.showContactInfo = true;
    $scope.isOtherUser=false;
    $scope.selectedUser = {'name':'', '_id':'','status':''};
	
    $http.get("/getuserinfo").then(function(response){
      if (response.data){
        $scope.currentUser.mobileNumber=response.data.mobileNumber;
        $scope.currentUser.homeNumber=response.data.homeNumber;
        $scope.currentUser.address=response.data.address;
      }
      else{
        alert(response.data);
      }
    }, function(response){
      alert("Error getting userinfo.");
    });
  }


  $scope.saveContactinfo = function(){
  
	  if ($scope.ContactInput.mobileNumber == null)
	  	$scope.ContactInput.mobileNumber=$scope.currentUser.mobileNumber;
	  
	  if ($scope.ContactInput.homeNumber == null)
	  	$scope.ContactInput.homeNumber=$scope.currentUser.homeNumber;
	  
	  if ($scope.ContactInput.address == null)
	  	$scope.ContactInput.address=$scope.currentUser.address;
	  
	  $http.put('/saveuserinfo',{'mobileNumber':$scope.ContactInput.mobileNumber,'homeNumber':$scope.ContactInput.homeNumber,'address':$scope.ContactInput.address}).then(function(response){
    	  if(response.data === ""){
      		// update the friendListString
      		$scope.currentUser.mobileNumber = response.data.mobileNumber;
      	  	$scope.currentUser.homeNumber = response.data.homeNumber;
      		$scope.currentUser.address = response.data.address;
    	  }
    	  else{
       	    alert(response.data);
    	  }
  	    }, function(response){
      	  alert("Error update contact.");
  	  });
  }

});



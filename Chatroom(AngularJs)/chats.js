var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');


router.get('/load', function(req, res) {
  if(req.session.userID){
    
	var db = req.db;
    var user_list_collection = db.get("userList");
    var message_list_collection = db.get("messageList");
    var friend_list = [];
    var unreadmsgnum_list = [];

    // first find out the user with userID specified in the session variable
    user_list_collection.find({'_id':req.session.userID}, {}, function(error1, the_user){
      if(error1 === null){
        // the friend_name_list is an array containing all the friend names
        // of the current user. 
        var friend_name_list = the_user[0].friends.map(function(objectItem){return objectItem.name;});

        user_list_collection.find({}, {}, function(error2, users){
          if(error2 === null){
			  
            message_list_collection.find({},{},function(error3,messages){

              if(error3 === null){
                for(var index in users){
                  var i = friend_name_list.indexOf(users[index].name);
                  if(i != -1){
                    // the user.name is present in the friend_name_list,
                    // we should put user into friend_list
                    friend_list.push({'name':users[index].name, '_id':users[index]._id});
                    
                    var counter=0
                    for(var j=0;j<messages.length;j++){
                      if(messages[j].senderId==users[index]._id.toString()&&messages[j].receiverId==req.session.userID.toString()&&messages[j]._id.toString()>the_user[0].friends[i].lastMsgId.toString()){
                        counter++;
                      }
                    }
                    unreadmsgnum_list.push(counter);                    
                  }
                }
                res.json({'user_name':the_user[0].name,'friend_list':friend_list,'icon':the_user[0].icon,'unreadmsgnum_list':unreadmsgnum_list});
              }else{
                  res.send(error3);
              }
		  	});
          }
          else{
            res.send(error2);
          }
        });
      }
      else{
		  res.send(error1);
      }
    });
  }
  else{
    res.send("");
  }
});


router.post('/login', bodyParser.json(), function(req, res) {

  var db = req.db;
  var user_list_collection = db.get("userList");
  var message_list_collection = db.get("messageList");
  var unreadmsgnum_list = [];


  user_list_collection.find({'name':req.body.name}, {}, function(error1, login_user){
    if(error1 === null){
      if((login_user.length>0)&&(login_user[0].password==req.body.password)){

        req.session.userID=login_user[0]._id;
        login_user[0].status="online";

        user_list_collection.update({'_id':login_user[0]._id}, login_user[0], function(error2, result){
          if(error2 != null)
            res.send(error2);
        });

        // continue to get the friend list for this user
        var friend_list = [];

        user_list_collection.find({}, {}, function(error3, users){
          if(error3 === null){
             message_list_collection.find({},{},function(error4,messages){
              if(error4===null){
				  
				  for(var index in users){
                  var i=login_user[0].friends.map(function(objectItem){return objectItem.name;}).indexOf(users[index].name);
                  if(i!=-1){
                    // the user.name is present in the friends array,
                    // we should put user into friend_list
                    friend_list.push({'name':users[index].name, '_id':users[index]._id});
                    var counter=0
                    for(var j=0;j<messages.length;j++){
                      if(messages[j].senderId==users[index]._id.toString()&&messages[j].receiverId==req.session.userID.toString()&&messages[j]._id.toString()>login_user[0].friends[i].lastMsgId.toString()){
                        counter++;
                      }
                    }
                    unreadmsgnum_list.push(counter);
                  }
                }
                res.json({'friend_list':friend_list,'name':req.body.name,'icon':login_user[0].icon,'unreadmsgnum_list':unreadmsgnum_list});                
              }else{
                res.send(error4)
              }
             });
          }
          else{
            res.send(error3);
          }
        });
      }
      else{
        res.send("Login failure");
      }
    }
    else{
      res.send(error1);
    }
  });
});


router.get('/logout', function(req, res) {

  var db = req.db;
  var user_list_collection = db.get("userList");

  user_list_collection.update({'_id':req.session.userID}, {$set:{"status":"offline"}}, function(error, result){
    if(error === null){
	    req.session.userID = null;
	    res.send("");
    }
    else{
      res.send(error);
    }
  });
});


router.get('/getuserinfo', function(req, res) {

  var db = req.db;
  var user_list_collection = db.get("userList");

  user_list_collection.find({'_id':req.session.userID}, {}, function(error, result){
    if(error === null){
		res.json({'mobileNumber':result[0].mobileNumber,'homeNumber':result[0].homeNumber,'address':result[0].address});
    }
    else{
      res.send(error);
    }
  });
});


router.put('/saveuserinfo', bodyParser.json(), function(req, res){
  var db = req.db;
  var user_list_collection = db.get("userList");
  
  var mobileNumber=req.body.mobileNumber;
  var homeNumber=req.body.homeNumber;
  var address=req.body.address;
  
  user_list_collection.update({'_id':req.session.userID}, { $set: {'mobileNumber':mobileNumber,'homeNumber':homeNumber,'address':address}}, function(error, result){
    if(error === null){
		res.send("");
    }
    else{
      res.send(error);
    }
  });
});


router.get('/getconversation/:friendid', function(req, res) {
  var id = req.params.friendid;
  var db = req.db;
  var message_list_collection = db.get("messageList");
  var user_list_collection = db.get("userList");
  
  var Currentuserid = req.session.userID;
  
  // find out all the messages sent between id and Currentuserid
  message_list_collection.find({'senderId':{$in:[id,Currentuserid]},'receiverId':{$in:[id,Currentuserid]}}, {}, function(error1, docs){ 
    if(error1 === null){
      // put all the message in the message_list 
      var message_list=docs;
	  
      var last_rcv_msg_id='';
	  
      for(var index in docs){
      	//if((docs[index].senderId===Currentuserid&&docs[index].receiverId===id)||(docs[index].receiverId==Currentuserid&&docs[index].senderId==id)){
      	 //message_list.push({'_id':docs[index]._id, 'senderId':docs[index].senderId, 'receiverId':docs[index].receiverId, 'message':docs[index].message, 'date':docs[index].date,'time':docs[index].time});
         if(docs[index].receiverId===Currentuserid){
           last_rcv_msg_id=docs[index]._id;
         }
        //}
      }
      
	  user_list_collection.find({"_id":id}, {}, function(error2, the_friend){
      	if(error2 === null){
        	user_list_collection.find({'_id':Currentuserid}, {}, function(error3, login_user){
          	  if(error3 === null){
            	  var i=login_user[0].friends.map(function(objectItem){return objectItem.name;}).indexOf(the_friend[0].name);
            	  if(login_user[0].friends[i].lastMsgId!=last_rcv_msg_id){
              		login_user[0].friends[i].lastMsgId=last_rcv_msg_id;
					
              	  	user_list_collection.update({'_id':Currentuserid}, login_user[0],function(error4,response){
                	  if(error4!=null){
						  res.send(error4);
						  return;
					  }  
              		});
            	  } 
				  res.json({'message_list':message_list,'icon':the_friend[0].icon,'status':the_friend[0].status});
          	 }else{
            	res.send(error3);
          	 }
           });
      	}else{
        	res.send(error2);
      	}
     });
   }
   else{
   	res.send(error1);
   }
  });
});


router.post('/postmessage/:friendid',bodyParser.json(),function(req, res) {

  var db = req.db;
  var message_list_collection = db.get("messageList");
  
  var id = req.params.friendid;
  
  // update the database
  message_list_collection.insert({'senderId':req.session.userID, 'receiverId':id, 'message':req.body.message, 'date':req.body.date,'time':req.body.time}, function(error, result){
    if (error === null)
		  res.json({'_id':result._id});
	  else
		  res.send(error);
  })
});


router.delete('/deletemessage/:msgid', function(req, res) {

  var db = req.db;
  var message_list_collection = db.get("messageList");
  var user_list_collection = db.get("userList");
  var message_id = req.params.msgid;
  var Currentuserid = req.session.userID;
  message_list_collection.find({'_id':message_id},function(error1,msg){
    if(error===null){
      var receiverId=msg[0].receiverId;
      user_list_collection.find({'_id':receiverId},function(error2,receiver){
        if(error2==null){
          var receiver_name=receiver[0].name;
          var receiver_id=receiver[0]._id;
          user_list_collection.find({'_id':Currentuserid},{},function(error3,current_user){
            if(error3==null){
              var current_user_name=current_user[0].name;
              var index=receiver[0].friends.map(function(objectItem){return objectItem.name;}).indexOf(current_user_name); 
              var lastMsgId=receiver[0].friends[index].lastMsgId;
              if(lastMsgId.toString()==message_id){
                message_list_collection.find({'senderId':Currentuserid,'receiverId':receiver_id},{},function(error4,messages){
                  if(error4===null){
                    if(len(messages)<=1){
                      receiver[0].friends[index].lastMsgId='0';
                    }else{
                      receiver[0].friends[index].lastMsgId=messages[len(messages)-2]._id;
                    }
                    user_list_collection.update({'id':receiverId},receiver[0],function(error5,update_response){
                      if(error5!=null){
                        res.send(error5);
                      }
                    });
                  }
                  else{
                    res.send(error4);
                  }
                });
              }

            }else{
              res.send(error3);
            }
          });


        }else{
          res.send(error2);
        }
      });
    }else{
      res.send(error1);
    }

  });
  message_list_collection.remove({'_id':message_id}, function(error, result){
        if(error === null){
			res.send("");
        }
        else{
          res.send(error);
        }
  });
});


router.get('/getnewmessages/:friendid', function(req, res) {
	
 var id = req.params.friendid;
  var db = req.db;
  var message_list_collection = db.get("messageList");
  var user_list_collection = db.get("userList");
  var message_list=[]; 
  var existing_message_id_list=[];
  
  var Currentuserid = req.session.userID;
  
  user_list_collection.find({'_id':id}, {}, function(error2, the_friend){
    if(error2 === null){
		
      user_list_collection.find({'_id':Currentuserid}, {}, function(error3, login_user){
        if(error3 === null){
          
          var i=login_user[0].friends.map(function(objectItem){return objectItem.name;}).indexOf(the_friend[0].name); 

          message_list_collection.find({'_id':{$gt:login_user[0].friends[i].lastMsgId},'senderId':id,'receiverId':Currentuserid},{},function(error4,docs){
              if(error4===null){

                for(var index=0;index<docs.length;index++){
                  message_list.push({'_id':docs[index]._id, 'senderId':docs[index].senderId, 'receiverId':docs[index].receiverId, 'message':docs[index].message, 'date':docs[index].date,'time':docs[index].time});
                  login_user[0].friends[i].lastMsgId=docs[index]._id;
                }
                
                if(message_list.length>0){
                  user_list_collection.update({'_id':Currentuserid}, login_user[0],function(error5,response){
                	  if(error5!=null){
						  res.send(error5);
						  return;
					  }  
              		});
                }
                
				message_list_collection.find({'senderId':{$in:[id,Currentuserid]},'receiverId':{$in:[id,Currentuserid]}},{},function(error6,docs1){
                  if(error6===null){
                    for(var k=0;k<docs1.length;k++){
                      existing_message_id_list[k]=docs1[k]._id;
                    }
                    res.json({'message_list':message_list,'status':the_friend[0].status,'existing_message_id_list':existing_message_id_list})
                  }
				  else{
                	  res.send(error6);
                  }
                });
              }else{
                res.send(error4);
              }
          });           
         }else{
          res.send(error3);
        }
      });
    }else{
      res.send(error2);
    }
  });
});


router.get('/getnewmsgnum/:friendid', function(req, res) {
  var id = req.params.friendid;
  var db = req.db;
  var message_list_collection = db.get("messageList");
  var user_list_collection = db.get("userList");

  var Currentuserid = req.session.userID;
  
  user_list_collection.find({'_id':id}, {}, function(error2, the_friend){
    if(error2 === null){
      user_list_collection.find({'_id':Currentuserid}, {}, function(error3, login_user){
        if(error3 === null){
          
          var i=login_user[0].friends.map(function(objectItem){return objectItem.name;}).indexOf(the_friend[0].name); 

          message_list_collection.find({'_id':{$gt:login_user[0].friends[i].lastMsgId},'senderId':id,'receiverId':Currentuserid},{},function(error4,docs){
              if(error4===null){
                
                res.json({'newmsgnum':docs.length})

              }else{
                res.send(error4);
              }
          });    
        }else{
          res.send(error3);
        }
      });
    }else{
      res.send(error2);
    }
  });
});


module.exports = router;

# web


About src/ChatLan:
First tag created! ChatLan Source contains the necessary classes to run the Chat Lan application on multiple virtual machines.
You will need to run the Server on one virtual machine as an initial step and then the Client class, as many times as you want, on different machines.
ALERT. You need to modify the IP in the Client class manually:

Socket miSocket = new Socket("IP's Machine HERE",9999);


Pending improvement: change of ip for nicks with a HasMap, conversation history 

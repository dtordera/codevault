/*
// access.js : here are some previous operations before real CRUD work. 
// - Check API status
// - Retrieve resultcode list, to be cached
// - Validate user/pass for first time (although it is validated by DB in each API call)
// - Retrieve permisison list of a validated login.
// 
// As told, all petitions include user, salt, saltedhash and originadress as params. Validation
// is done on inner DB procedures through API. As salt gots a caducity of just 5s, there's
// then no real possibility to use hash-as-login. Anyway, the most correct way, doing as here calls
// directly from client, must be use of HTTPS/SSL transport against API, but although correct
// SSL means a trusted certificate on server, and so, a registered domain and a SSL validator company,
// I had prefered to left it this way and just comment about it: please read .PDF documentation attached with source, 
// where are explained possible alternatives to pure http/javascript (https, angular, java 
// web (vaadin, JSD), .aspx web, etc...)
*/

    // API requester. 
    // Each of the operations will be sent to API. As well, each operation must
    // be secured with a salted hash (salt validity 4 seconds). Please read documentation
    // about security. 
    
    function doAPICall(uri, payload, verb, OKFunction, KOFunction)
    {
        var r = new XMLHttpRequest();
        
        r.open(verb,_BASEURI + uri,true);
        r.onreadystatechange = 
            function() {
                if (r.readyState == 4)
                    if (r.status == 200)
                        OKFunction(r.response);
                    else
                        KOFunction(r.response);
            }
        
        // payloaded verbs...
        if (verb == "POST" || verb == "PUT")
            r.setRequestHeader("Content-Type","application/json");
        
        r.send(payload);                                        
    }     

    // INIT. bootstrap. Check API status & load resultcodes list (by API calls)
    function bootstrap()
    {
        disableCRUD();
        getIp();
        
        log("<br>Checking API (" + _BASEURI + "ping) :<br>ping... (" + Date() + ")");
        
        doAPICall("ping", null, "GET", 
                function(response) // got ping...
                {
                    var m = JSON.parse(response);
                    var e = m["echo"];
                    log("<br>..." + e["echo"] + " (" + new Date(e["timestamp"]) + ")");                        
                    show("initial");
                }, 
                function (response) { // fail
                    log("<br>PING FAILED. " + _API_IS_DOWN);
                    throw new Error();
                 });
        
        doAPICall("resultcodes",null,"GET",
                function(response)
                {
                    resultcodes = JSON.parse(response);
                    resultcodes = resultcodes["items"];
                    console.log("Retrieving resultcode list");
                    for (var i=0;i<resultcodes.length;i++)
                    {
                        console.log(resultcodes[i]);
                    }                            
            
                    log("<br>Resultcode list cached.");
                },
                function(response)
                {
                    log("<br>GET RESULTCODE LIST FAILED. " + _API_IS_DOWN);
                    throw new Error();
                }
        );
    }                

    // Call for permission list is used as login validity. As every call needs it's
    // own salted hash (remember salt caducity is <5s), in fact, there's no real 'login'. 
    // realized at client side. Please again, check documentation about limitations of 
    // (pure) Javascript related on security.
    
    function validateaccess()
    {
        logout();
        
        login = document.getElementById("iuser").value;
        pass = document.getElementById("ipass").value;        
        show("log2");  
        
        document.getElementById("log2").innerHTML = "";            
        doAPICall("permissions" + getQueryParams(),null,"GET", nextFromPermissions, getpermissionsKO);       
    }

    // retrieve permisison list: Permission check & operation validation is realized 
    // in inner DB procedures, so there's no real possibility of hack them from here.
    
    function nextFromPermissions(response)
    {
        permissions = JSON.parse(response);
        log2("<br>Validated access<br>Permissions for user " + login + " : ");
        
        var t = permissions["items"];
        
        for (i=0;i<t.length;i++)
            log2(" " + t[i].description);

        // All ok, show table & tools...        
        enableCRUD();
        initCRUDwork();
    }
    
    // Any kind of error means failed access or server down. 
    function getpermissionsKO(response)
    {
        var e = JSON.parse(response);            
        console.log(e["rc"]);
        
        log2("<br>Failed access<br>Failed retrieving permissions. Error : " + getResultMessage(e.rc));
    }
    
    // logout in this scenario is then just nothing that hide operations & clean all. 
    function logout()
    {
        disableCRUD();
        login = "";
        pass = "";
        
        document.getElementById("log2").innerHTML = "";
    }
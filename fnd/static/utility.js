/*
//  utility.js : utility functions
//
//
*/
    function getIp()
    {
        var r = new XMLHttpRequest();
        
        r.open("GET","https://freegeoip.net/json/?callback=",true);
        r.onreadystatechange = 
            function() {
                if (r.readyState == 4)
                    if (r.status == 200)
                    {
                        var p = JSON.parse(r.response)
                        console.log(p["ip"]);
                        _ORIGINADDRESS = p["ip"];                        
                        log("<br>Origin address: " + _ORIGINADDRESS + "<br>");            
                    }
                    else
                        _ORIGINADDRESS = "127.0.0.1";
            }                
        r.send();    
    }

  // logging and controlling visibility
    function log(message)
    {
        document.getElementById("log").innerHTML += message;
    }
    
    function log2(message)
    {
        document.getElementById("log2").innerHTML += message;                
    }

    function show(element)
    {
        document.getElementById(element).style.visibility = "visible";
    }
    
    function hide(element)
    {
        document.getElementById(element).style.visibility = "hidden";
    }
            
    // returns unix timestamp. WARNING: salt caducity is 5s. Depending on date
    // on unsynchronized servers this can provoque systematic fails. Please in 
    // case of continuos INVALID_SALT error check client machine settings
    function getsalt()
    {
        return Math.round(Date.now()/1000);
    }
 
    // calculate salted hash (sha2(sha2(salt) + sha2(pwd))
    function calculateHash(pass, salt)
    {
        var hsalt = Sha256.hash(salt ,'string');
        var hpass = Sha256.hash(pass, 'string');                
        return Sha256.hash(hsalt+hpass, 'string');
    }

    // returns validation parameters as queryparams            
    function getQueryParams()
    {
        var salt = getsalt();
        var saltedhash = calculateHash(pass, salt);
        
        return "?u=" + login + "&h=" + saltedhash + "&s=" + salt + "&o=" + _ORIGINADDRESS;
    }   
  
    // gets result descripton from cache and API returned resultcode for each request                      
    function getResultMessage(rc)
    {
        if (resultcodes !== null)
        for (var i=0;i<resultcodes.length;i++)
        {
            var r = resultcodes[i];
            if (r.id == rc) return r.id + " - " + r.description;
        }
        
        return "Unknown error";                        
    }

/*
// crud.js : crud operations and visualizations
//
*/

    // Shows & hide crud panel
    function enableCRUD()  
    {
        show("crud");   
        log2("<br>Getting current product list...")
        refreshlist();
        
    }
    function disableCRUD()
    {
        hide("crud");
    }

    // all CRUD api calls will use refresh list as ok callback, and generalerror as failed one
    function refreshlist()
    {
        doAPICall("products" + getQueryParams(),null,"GET", refreshtable, generalerror);       
    }
    function generalerror(response)
    {               
        
        document.getElementById("lastresult").innerHTML = new Date(Date.now()) + ": GENERAL SERVER ERROR (please check arguments)";
        
        var e = JSON.parse(response);          
        document.getElementById("lastresult").innerHTML = new Date(Date.now()) + ": " +getResultMessage(e["rc"]);
    }

    // generates table dynamically from response
    function refreshtable(response)
    {
        var t = document.getElementById("producttable");
        
        response = JSON.parse(response);
        
        t.innerHTML = '<table id="producttable">';            
        t.innerHTML += '<tr id="headrow"><th>skuid</th><th>name</th><th>price</th><th>avail.</th>'+
            '<th>description</th></tr>'            
        
        var items = response["items"];            
        for (var i=0;i<items.length;i++)
        {
            var r = items[i];
            console.log(r);                
            t.innerHTML += '<tr onclick="rowclick(' + r.id + ')"><td>' + r.id + "</td><td>" + r.name + "</td><td>" + r.price + "</td>" + 
                "<td>" + r.available + "</td><td>" + r.description + "</td></tr>"
        }
        
        t.innerHTML += '</table>';
        
        document.getElementById("lastresult").innerHTML = "";
    }
    
    // a click function on each row, to update edit boxes and to show detail
    function rowclick(id)
    {
        // (get just one product info by. This can be achieved but getting data from loaded table)
        doAPICall("products" + getQueryParams() + "&id=" + id,null,"GET", productdetail, generalerror);       
    }
    function productdetail(response)
    {
        var r = JSON.parse(response);
        r = r["items"][0];
        
        console.log(r);
                            
        document.getElementById("lid").innerHTML = r.id;
        document.getElementById("ename").value = r.name;
        document.getElementById("epric").value = r.price;
        document.getElementById("eavai").checked = r.available;
        document.getElementById("edesc").value = r.description;            
        
        var s = 'skuid:        ' + r.id + '\n'+ 
                'name:         ' + r.name + '\n' + 
                'price:        ' + r.price + '\n' + 
                'available:    ' + r.available + '\n' +
                'date created: ' + new Date(r.datecreated) + '\n' + 
                'description : ' + r.description + '\n';

        alert(s);
    }

    // tool button action to clear edits      
    function clearedits()
    {
        document.getElementById("lid").innerHTML = "skuid";
        document.getElementById("ename").value = "";
        document.getElementById("epric").value = "";
        document.getElementById("eavai").checked = false;
        document.getElementById("edesc").value = "";            
    }

    // tool button action to post a new product, with values on edits                            
    function newproduct()
    {
        var s = getsalt();            
        var params = {            
            user: login,               
            salt: s,
            hash: calculateHash(pass,s),
            originaddress:_ORIGINADDRESS,
            name:document.getElementById("ename").value,
            price:document.getElementById("epric").value,
            available:document.getElementById("eavai").checked,
            description:document.getElementById("edesc").value
        }
        console.log(params);            
        
        if (confirm("sure to enter new product?") === true)            
            doAPICall("products",JSON.stringify(params),"POST",refreshlist, generalerror);
    }

    // tool button action to update (PUT)
    function updateproduct()
    {
        var s = getsalt();            
        var params = {
            id: document.getElementById("lid").innerHTML,
            user: login,               
            salt: s,
            hash: calculateHash(pass,s),
            originaddress:_ORIGINADDRESS,
            name:document.getElementById("ename").value,
            price:document.getElementById("epric").value,
            available:document.getElementById("eavai").checked,
            description:document.getElementById("edesc").value
        }
        console.log(params);            
        
        if (confirm("sure to enter update product?") == true)            
            doAPICall("products",JSON.stringify(params),"PUT",refreshlist, generalerror);            
    }

    // tool button action to delete product      
    function deleteproduct()
    {
        var id = document.getElementById("lid").innerHTML;
        if (confirm("sure to delete product " + id + "?") === true)
            doAPICall("products/" + getQueryParams() + "&id=" + id,null,"DELETE",refreshlist,generalerror);
    }

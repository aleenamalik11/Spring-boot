const API_URL = 'http://localhost:8080/';

 async function post(api, token, bodyData){
    var headerData;
    if(token!=null){
        headerData ={
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    }
    else{
        headerData ={
            'Content-Type': 'application/json',
        }
    }
   
    try{
        let response = await fetch(API_URL + api, {
            method: 'POST',
            headers: headerData,
            body: JSON.stringify(bodyData)
            });
    
            let data = await response.json();
            return data;
    }
    catch (error) {
    console.error(error);
    throw new Error('Unable to fetch data');
  }
    
}


async function get(api, token){
    var headerData;
    if(token!=null){
        headerData ={
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    }
    else{
        headerData ={
            'Content-Type': 'application/json',
        }
    }
    try{
        let response = await fetch(API_URL + api, {
            method: 'GET',
            headers:headerData
            });
    
            let data = await response.json();
            return data;
    }
    catch (error) {
    console.error(error);
    throw new Error('Unable to fetch data');
  }
}